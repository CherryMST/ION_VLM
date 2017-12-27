package com.nokia.vlm.update;

import android.content.Context;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.os.SystemClock;
import android.text.TextUtils;

import com.qx.framelib.net.NetWorkUtil;
import com.qx.framelib.utlis.FileUtils;
import com.nokia.vlm.ui.QXApp;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.HttpHostConnectException;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.ConnectException;
import java.net.MalformedURLException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;


/**
 * 
 * 真正执行下载的类
 * 
 * 
 * @author lhb
 *
 */
public class DownloadRunnable {
	
	public final static int HTTP_CONNECTIONTIMEOUT = 30 * 1000; // 30 seconds

	public final static int HTTP_SOTIMEOUT = 30 * 1000; // 30 seconds

	private int resultCode = DownloadResult.CODE_UNDEFINED;
	
	private Throwable exception = null;

    //用于定位错误信息，暂时未用到有错误码足矣
	private byte[] extMsg = new byte[0];
	
	private long savedLength = 0;//单次连接请求正常下载的大小
	
	private DownloadRunnableConfig mConfig;

    //用于抛出下载过程中的各种回调通知
	private DownloadRunnableListener mListener;

    //任务执行状态，默认为true,如果取消或者写文件出错就为false
	private boolean runFlag = true;
	
	// 保存位置
	public int UNKNOWN = 0;
	public int PHONE = 1;
	public int SDCARD = 2;
	
	/**
	 * 判断剩余空间大于的安全系统，在文件大小*此系数，做为可以下载的判断
	 */
	public static final float SAVE_FATOR = 1.5f;
	
	public DownloadRunnable(DownloadRunnableConfig config,DownloadRunnableListener listener) {
		this.mConfig = config;
		this.mListener = listener;
	}
	
	public void cancel(){
		runFlag = false;
		resultCode = DownloadResult.CODE_CANCELED;
	}
	
	/**
	 * 获取当前可用空间
	 * @return
	 */
	private long getAvaiableCap(){
		long cap = -1;
		int location = getStorePosition(mConfig.dest);
		if (location == PHONE) {
			cap = FileUtils.getPhoneStorageAvailableCapacity();
		} else if (location == SDCARD) {
			cap = FileUtils.getExternalStorageAvailableCapacity();
		}
		return cap;
	}
	
	/**
	 * 检查空间是否足够
	 * @param savePath
	 * @param length
	 * @return
	 */
	private boolean isSpaceEnough(long length,String savePath){
		final long safeLength = (long) (length * SAVE_FATOR);
		return FileUtils.getAvailableSize(DownloadConfig.getDefaultSaveDir()) >= safeLength;
	}
	
	/**
	 * 获取保存位置,
	 * 
	 * @return PHONE,SDCARD,UNKOWN
	 */
	public int getStorePosition(String savePath) {
		if (savePath != null && savePath.contains("/data/data")) {
			return PHONE;
		} else if (FileUtils.isSDCardExistAndCanWrite()) {
			return SDCARD;
		}
		return UNKNOWN;
	}
	
	public void exec() {
		final int retry = DownloadConfig.MAX_RETRY;
		for (int i = 0; i < retry; i++) {
			long id = System.currentTimeMillis();
			resultCode = DownloadResult.CODE_UNDEFINED;
			exception = null;
			extMsg = new byte[0];
			boolean result = execDownload(id);
			if (result) { // 下载完成或者被用户暂停或者其他原因触发了cancel动作
				if (resultCode == DownloadResult.CODE_OK) {
					this.mListener.onCompleted(id, this.mConfig.srcUrl, mConfig.begin, this.savedLength, this.mConfig);
				} else if (resultCode == DownloadResult.CODE_CANCELED) {
					this.mListener.onCanceled(id, this.mConfig.srcUrl, this.savedLength);
				} else {
					this.mListener.onFailed(id, mConfig.srcUrl, this.savedLength, resultCode, exception);
				}
				return;
			} else {			// 下载失败，那么有可能需要做重试，这个时候UI上还没有收到失败通知
				this.mListener.onFailed(id, mConfig.srcUrl, this.savedLength, resultCode, exception);
				if(i == retry -1 || resultCode == DownloadResult.CODE_SPACE_NOT_ENOUGH
								 || resultCode == DownloadResult.CODE_CREATE_FILE_FAILED
								 || resultCode == DownloadResult.CODE_WRITE_FILE_FAILED
								 || resultCode == DownloadResult.CODE_FILE_NOT_EXIST
								 || resultCode == DownloadResult.CODE_NETWORK_UNAVAIABLE
								 || resultCode == DownloadResult.CODE_URL_EMPTY){		// 这几种原因的话就不用做失败重试，直接 通知UI这次下载失败
					this.mListener.onFinallyFailed(this.mConfig.srcUrl, resultCode, extMsg);
					return;
				}
				for (int j = 0; j < 10; j++) { 	// 如果有网络切换导致下载失败，那么用5s的时间等网络稳定下来后再继续下载
					SystemClock.sleep(500l); 	// 500ms检查一次
					if (resultCode == DownloadResult.CODE_CANCELED) {				// 如果在准备重试的过程中，用户取消这次下载行为，那么也不用重试了
						this.mListener.onCanceled(id, this.mConfig.srcUrl, this.savedLength);
						return;
					}
				}
				mConfig.begin += this.savedLength;
				this.savedLength = 0;
			}
		}
	}
	
	private boolean execDownload(final long id){
		
		initDownloadCofig();
		
		mListener.onStarted(id, mConfig.srcUrl, mConfig.finalUrl,mConfig.totalLength, mConfig.begin, mConfig.chunkSize);
		
		if(!NetWorkUtil.isNetworkActive()){
			resultCode = DownloadResult.CODE_NETWORK_UNAVAIABLE;
			return false;
		}
		
		if(TextUtils.isEmpty(mConfig.srcUrl) && TextUtils.isEmpty(mConfig.finalUrl)){
			resultCode = DownloadResult.CODE_URL_EMPTY;
			return false;
		}
		
		String range = getRangeHeader(mConfig.totalLength, mConfig.begin, mConfig.chunkSize);
		String nextUrl = TextUtils.isEmpty(mConfig.finalUrl) ? mConfig.srcUrl : mConfig.finalUrl;
		boolean continueRedirect = true;
		DefaultHttpClient httpClient = null;
		
		int redirectCount = 0;
		while (continueRedirect) {
			if(resultCode == DownloadResult.CODE_CANCELED){
				return true;
			}
			if(redirectCount > DownloadConfig.MAX_HTTP_REDIRECT){
				resultCode = DownloadResult.CODE_OVERLOAD_MAX_REDIRECT;
				return false;
			}
			
			InputStream in = null;
			CacheFileWriter cfw = null;
			WakeLock wakeLock = null;
			
			try {
				
				PowerManager pm = (PowerManager) QXApp.getAppSelf().getSystemService(Context.POWER_SERVICE);
				wakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, getClass().getSimpleName());
				wakeLock.setReferenceCounted(false);
				wakeLock.acquire();
				
				httpClient = createHttpClient(mConfig.socketBuffer);
				HttpGet httpget = new HttpGet(wrapUrl(nextUrl));
				
				httpget.addHeader("User-Agent", "Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.1; Trident/5.0)");

				if(range != null && range.length() > 0){
					httpget.addHeader("Range", range);
				}
				HttpResponse response = httpClient.execute(httpget);
				int statusCode = response.getStatusLine().getStatusCode();
				if (statusCode == HttpStatus.SC_OK || statusCode == HttpStatus.SC_PARTIAL_CONTENT) {		// 200, 206
					continueRedirect = false;
					if (mConfig.totalLength <= 0) {
						boolean rangeable = true;
						String contentRange = HttpClientUtils.getHeader(response, "Content-Range");
						if (contentRange == null || contentRange.length() == 0) { // 不支持分片下载的，取content-length作为文件长度
							String contentLength = HttpClientUtils.getHeader(response, "Content-Length");
							mConfig.totalLength = getLengthFromContentLength(contentLength);
							rangeable = false;
						} else { // 支持分片下载的，处理一下content-range，找出文件长度
							mConfig.totalLength = getLengthFromContentRange(contentRange);
							rangeable = true;
						}
						mConfig.rangeable = rangeable;//是否支持断点续传标示
						mListener.onFileLengthDetermined(id, mConfig.srcUrl, mConfig.totalLength, rangeable);
					}

					mConfig.finalUrl = nextUrl;
					mListener.onFinalUrl(id, mConfig.srcUrl, mConfig.finalUrl);
					
					final boolean isSpaceEnough = isSpaceEnough(mConfig.totalLength,mConfig.dest);
                    if(!isSpaceEnough)
                    {
                        resultCode = DownloadResult.CODE_SPACE_NOT_ENOUGH; // 空间不足
                        return false;
                    }

					// 开始下载资源
					File file = new File(mConfig.dest);
					if(!mConfig.rangeable){
						//服务器不支持断点续传，要删掉原来下载的文件
						if(file.exists()){
							file.delete();
							//起始位置置0
							mConfig.begin = 0;
						}
					}
					
					
					if (!file.exists()) {
						boolean createFile = FileUtils.createFileWithSpecialSize(mConfig.dest, mConfig.totalLength);
						if (!createFile) {
							resultCode = DownloadResult.CODE_CREATE_FILE_FAILED; // 文件创建失败
							return false;
						}
					}

					cfw = new SyncCacheWriter(mConfig.dest, mConfig.notifyLength, mConfig.saveLength, new OnSaveListener() {

						@Override
						public void onSave2Cache(long length) {
						}

						@Override
						public void onNotify(long length, double speed) {
							mListener.onReceived(id, mConfig.srcUrl, length, speed);
						}

						@Override
						public void onSave2File(long length) {
							savedLength += length;
							mListener.onSaveReceived(id, mConfig.srcUrl, length);
						}
						
						@Override
						public void onSave2FileFailed(Throwable e) {
							runFlag = false;
							exception = e;
						}

					});
					cfw.seek(mConfig.begin);
					in = response.getEntity().getContent();

					byte[] buffer = new byte[mConfig.socketBuffer];
					int length = 0;
					while ((length = in.read(buffer)) != -1 && runFlag) {
						cfw.write(buffer, length);
					}
					if(resultCode == DownloadResult.CODE_CANCELED){		// 下载过程中取消
						return true;
					}else{
						if(runFlag){					// 下载过程中一切正常
							resultCode = DownloadResult.CODE_OK;
							return true;
						}else{
							long cap = getAvaiableCap();
							if(cap < 100 * 1024){		// 剩余空间小于100k的话，那么认为是空间不足
								resultCode = DownloadResult.CODE_SPACE_NOT_ENOUGH; // 空间不足
							}else{
								resultCode = DownloadResult.CODE_WRITE_FILE_FAILED;	// 写文件失败
							}
							return false;
						}
					}
				} else if (statusCode == HttpStatus.SC_MOVED_PERMANENTLY
						|| statusCode == HttpStatus.SC_MOVED_TEMPORARILY
						|| statusCode == HttpStatus.SC_SEE_OTHER
						|| statusCode == HttpStatus.SC_TEMPORARY_REDIRECT)
				{	// 301, 302, 303, 307
					redirectCount++;
					nextUrl = HttpClientUtils.getHeader(response, "location");
					
					mListener.onRedirect(id, mConfig.srcUrl, httpget.getURI().toString(), nextUrl);
				} else if(statusCode == HttpStatus.SC_REQUEST_TOO_LONG
						|| statusCode == HttpStatus.SC_REQUESTED_RANGE_NOT_SATISFIABLE
						|| statusCode == HttpStatus.SC_INTERNAL_SERVER_ERROR){		// 413, 416 ，500 当前分片太大，调整range大小，继续使用此url
					long newChunkSize = 0;
					if(this.mConfig.chunkSize > 0){
						if(this.mConfig.chunkSize <= 50 * 1024){			// 如果分片大小比50还小了还是出现413，416或者500，那么就终止吧
							continueRedirect = false;
							resultCode = statusCode;
							return false;
						}else{
							newChunkSize = this.mConfig.chunkSize * 2 / 3;		// 调整分片大小为原大小的2/3
						}
					}else{
						int netType = NetWorkUtil.getGroupNetType();
						if (netType == NetWorkUtil.GROUP_NETTYPE_WIFI) { 					// wifi网络的分片大小
							newChunkSize = DownloadConfig.MAX_CHUNK_SIZE_WIFI;
						} else if (netType == NetWorkUtil.GROUP_NETTYPE_2g) { 				// 2G网络的分片大小
							newChunkSize = DownloadConfig.MAX_CHUNK_SIZE_2G;
						} else { 										// 未能成功判断网络类型的话，那么就用3g网络的分片大小来适应
							newChunkSize = DownloadConfig.MAX_CHUNK_SIZE_3G;
						}
					}
					mListener.onChunkSizeAdjusted(id, mConfig.srcUrl, this.mConfig.chunkSize, newChunkSize);
					this.mConfig.chunkSize = newChunkSize;
					range = getRangeHeader(mConfig.begin, this.mConfig.chunkSize);
				} else {
					continueRedirect = false;
					resultCode = statusCode;
					return false;
				}
			} catch (MalformedURLException e) {
				e.printStackTrace();
				resultCode = DownloadResult.CODE_URL_ERROR;
				exception = e;
				return false;
			} catch (ClientProtocolException e) {
				e.printStackTrace();
				resultCode = DownloadResult.CODE_CLIENT_PROTOCOL_EXCEPTION;
				exception = e;
				return false;
			} catch(UnknownHostException e){
				e.printStackTrace();
				resultCode = DownloadResult.CODE_UNKNOWN_HOST_EXCEPTION;
				exception = e;
				return false;
			} catch(HttpHostConnectException e){
				e.printStackTrace();
				resultCode = DownloadResult.CODE_HOST_CONNECTION_EXCEPTION;
				exception = e;
				return false;
			}catch (ConnectTimeoutException e) {
				e.printStackTrace();
				resultCode = DownloadResult.CODE_CONNECTION_TIMEOUT_EXCEPTION;
				exception = e;
				return false;
			} catch (ConnectException e) {
				e.printStackTrace();
				resultCode = DownloadResult.CODE_CONNECTION_EXCEPTION;
				exception = e;
				return false;
			} catch (SocketTimeoutException e) {
				e.printStackTrace();
				resultCode = DownloadResult.CODE_SOCKET_TIMEOUT_EXCEPTION;
				exception = e;
				return false;
			} catch (SocketException e) {
				e.printStackTrace();
				resultCode = DownloadResult.CODE_SOCKET_EXCEPTION;
				exception = e;
				return false;
			} catch (IOException e) {
				e.printStackTrace();
				resultCode = DownloadResult.CODE_IO_EXCEPTION;
				exception = e;
				return false;
			} catch (Exception e) {
				resultCode = DownloadResult.CODE_EXCEPTION;
				exception = e;
				return false;
			} finally {
				if(cfw != null){
					cfw.recycle();
				}
				if (httpClient != null) {
					httpClient.getConnectionManager().shutdown();
					httpClient = null;
				}
				if(in != null){
					try {
						in.close();
					} catch (IOException e) {
					}
				}
				if (wakeLock != null && wakeLock.isHeld()) {
					wakeLock.release();
					wakeLock = null;
				}
			}
		}
		return false;
	}
	
	/**
	 * 配置下载的参数。socket buffer, notify length, save length
	 */
	private void initDownloadCofig(){
		final int nettype = NetWorkUtil.getGroupNetType();
		if(nettype == NetWorkUtil.GROUP_NETTYPE_WIFI){	// wifi环境
			mConfig.socketBuffer = DownloadConfig.SOCKET_BUFFER_WIFI;
			mConfig.notifyLength = DownloadConfig.NOTIFY_UI_CHANGE_3G_WIFI;
			mConfig.saveLength = DownloadConfig.CAHCE_LENGTH_TO_SAVE;
		}else if(nettype == NetWorkUtil.GROUP_NETTYPE_2g){	// 2g
			mConfig.socketBuffer = DownloadConfig.SOCKET_BUFFER_2G;
			mConfig.notifyLength = DownloadConfig.NOTIFY_UI_CHANGE_2G;
			mConfig.saveLength = DownloadConfig.CAHCE_2G_LENGTH_TO_SAVE;
		}else{ // 3g
			mConfig.socketBuffer = DownloadConfig.SOCKET_BUFFER_3G;
			mConfig.notifyLength = DownloadConfig.NOTIFY_UI_CHANGE_3G_WIFI;
			mConfig.saveLength = DownloadConfig.CAHCE_3G_LENGTH_TO_SAVE;
		}
	}
	
	private String getRangeHeader(long beginIndex, long chunkSize){
		StringBuffer sb = new StringBuffer();
		sb.append("bytes=");
		sb.append(beginIndex);
		sb.append("-");
		sb.append(beginIndex + chunkSize - 1);
		return sb.toString();
	}
	
	private String getRangeHeader(long totalLength, long beginIndex, long chunkSize) {
		StringBuffer sb = new StringBuffer();
		sb.append("bytes=");
		sb.append(beginIndex);
		sb.append("-");
		if (totalLength > 0 && chunkSize > 0 && beginIndex + chunkSize < totalLength) {
			sb.append(beginIndex + chunkSize - 1);
		}
		return sb.toString();
	}
	
	private long getLengthFromContentRange(String contentRange){
		long result = 0;
		String[] s = contentRange.split("/");
		if (s != null && s.length == 2) {
			try {
				result = Long.valueOf(s[1]);
			} catch (Exception e) {
				result = 0;
			}
		}
		return result;
	}
	
	private long getLengthFromContentLength(String contentLength){
		long result = 0;
		try {
			result = Long.valueOf(contentLength);
		} catch (Exception e) {
			result = 0;
		}
		return result;
	}
	
	private String wrapUrl(String url) {
		String s = url.replace("\r", "").replace("\n", "").replace(" ", "%20").trim();
		return s;
	}
	
	private DefaultHttpClient createHttpClient(int socketBuffer) {
		DefaultHttpClient httpClient = null;

		HttpParams params = new BasicHttpParams();

		HttpConnectionParams.setConnectionTimeout(params, HTTP_CONNECTIONTIMEOUT);
		HttpConnectionParams.setSoTimeout(params, HTTP_SOTIMEOUT);
		HttpConnectionParams.setSocketBufferSize(params, socketBuffer);
		HttpClientParams.setRedirecting(params, false);

		httpClient = new DefaultHttpClient(params);
//		HttpClientUtils.proxyHttpClient(httpClient);
		HttpClientUtils.timeoutHttpClient(httpClient);

		return httpClient;
	}

	public void printHeader(HttpResponse response){
		Header[] headers = response.getAllHeaders();
		for(Header header : headers){
			System.out.println(header.toString());
		}
	}

	static class DownloadRunnableConfig{
		
		public boolean rangeable;//是否支持断点续传，注意是由服务器返回决定
		
		public String srcUrl;
		
		public String finalUrl;
		
		public long totalLength;
		
		public long begin;//断点续传会用到
		
		public long chunkSize;//range分片用到
		
		public int socketBuffer = DownloadConfig.SOCKET_BUFFER_3G;
		
		public String dest;//文件需要下载到的位置完整路径带文件名
		
		public long notifyLength = -1;
		
		public long saveLength = -1; 
	}

}
