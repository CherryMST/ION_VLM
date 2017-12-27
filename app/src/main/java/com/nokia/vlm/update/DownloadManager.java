package com.nokia.vlm.update;

import android.text.TextUtils;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 
 * 取消下载
 * 目前仅仅用于更新升级使用
 * 这个是无限制往后加任务的明显不适合批量下载，其它下载慎用
 * 
 *
 * @author kevinlhbluo
 *
 */
public class DownloadManager implements DownloadTaskListener{
	
	private static DownloadManager instance;
	
	//<key,value> key: url ->md5
	private Map<String,DownloadTask> runningTask = new LinkedHashMap<String, DownloadTask>();
	private Object runningLock = new Object();
    private ExecutorService mDownloaderTaskExecutor;

	private DownloadManager()
	{
        mDownloaderTaskExecutor = Executors.newCachedThreadPool();
    }
	
	public static synchronized DownloadManager getInstance()
	{
		if(instance == null){
			instance = new DownloadManager();
		}
		return instance;
	}
	
	public void start(DownloadTask task){
		if(task == null){
			return;
		}
		DownloadTask tmpTask = null;
		synchronized (runningLock) {
			tmpTask = runningTask.get(task.getUKey());
			if(tmpTask != null){//已经有相同的任务在运行了
				return;
			}
			
			task.addListener(this);
			
			synchronized (runningLock) {
				runningTask.put(task.getUKey(), task);
			}
            mDownloaderTaskExecutor.submit(task);
		}
	}
	
	//取消下载
	public void cancel(String url){
		if(TextUtils.isEmpty(url)){
			return;
		}
		
		DownloadTask task = null;
		synchronized (runningLock) {
			task = runningTask.get(DownloadTask.getUniqueKey(url));
			if(task != null){
				task.cancel();
			}
		}
	}
	
	//查询一个任务是否正在执行
	public boolean isDownloading(String url)
	{
		synchronized (runningLock) {
			return runningTask.get(DownloadTask.getUniqueKey(url)) != null;
		}
	}
	
	public DownloadTask getDownloadingTask(String url)
	{
		synchronized (runningLock) {
			return runningTask.get(DownloadTask.getUniqueKey(url));
		}
	}

	@Override
	public void onTaskAlreadyCompleted(String url, String savePath) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTaskStarted(String url) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTaskUrlStarted(long chunkId, String url, String finalUrl, long totalLength, long begin, long size) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTaskUrlRedirect(long chunkId, String url, String nextUrl) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTaskUrlChunkSizeAdjusted(long chunkId, String url, long newSize) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTaskUrlCompleted(long chunkId, String url, long receivedLength) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTaskUrlFailed(long chunkId, String url, long receivedLength, int code, Throwable e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTaskUrlCanceled(long chunkId, String url, long receivedLength) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTaskSizeDetermined(String url, long length) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTaskReceived(String url, long totalLength, long length, double speed) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTaskCompleted(String url, String savePath) {
		// TODO Auto-generated method stub
		synchronized (runningLock) {
			runningTask.remove(DownloadTask.getUniqueKey(url));
		}
		
	}

	@Override
	public void onTaskFailed(String url, int errorCode, byte[] extMsg, String savePath) {
		// TODO Auto-generated method stub
		synchronized (runningLock) {
			runningTask.remove(DownloadTask.getUniqueKey(url));
		}
	}

	@Override
	public void onTaskCancel(String url) {
		// TODO Auto-generated method stub
		synchronized (runningLock) {
			runningTask.remove(DownloadTask.getUniqueKey(url));
		}
	}

}
