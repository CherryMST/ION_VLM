package com.nokia.vlm.update;

import android.text.TextUtils;

import com.qx.framelib.utlis.SecureUtils;
import com.qx.framelib.utlis.ZLog;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 由线程池来调度执行
 *
 * @author kevinlhbluo
 */
public class DownloadTask implements Runnable, DownloadRunnableListener {

    public static enum DOWNLOAD_STATUS {
        PENDING, INITIALIZE, STARTED, COMPLETE, FAILED, CANCELLED, CANCELLING
    }

    public DOWNLOAD_STATUS status = DOWNLOAD_STATUS.PENDING;

    //可能有多个观察者，都要照顾到位
    private List<DownloadTaskListener> mListeners = new ArrayList<DownloadTaskListener>();

    private String srcUrl;

    private String saveDir = null;

    private String saveName = null;

    private DownloadRunnable mDownloadRunable;

    // 已经接收到的文件大小
    public long receivedLength = 0;

    // 已保存的文件大小
    public long savedLength = 0;

    // 文件总长度
    public long totalLength = -1;

    private static final boolean DEBUG = true;

    public void log(String msg) {
        if (DEBUG) {
            ZLog.d("DownloadTask", msg);
        }
    }

    public DownloadTask(String url) {
        this.srcUrl = url;
    }

    public String getUKey() {
        return getUniqueKey(srcUrl);
    }

    public static String getUniqueKey(String url) {
        return SecureUtils.md5Encode(url);
    }

    public void addListener(DownloadTaskListener listener) {
        if (this.mListeners.contains(listener)) {
            return;
        }
        mListeners.add(listener);
    }

    public String getSavePath() {
        return saveDir + "/" + saveName;
    }

    @Override
    public void run() {
        // TODO Auto-generated method stub

        this.status = DOWNLOAD_STATUS.INITIALIZE;

        if (TextUtils.isEmpty(saveDir)) {
            saveDir = DownloadConfig.getDefaultSaveDir();
        }

        if (TextUtils.isEmpty(saveName)) {
            saveName = getUniqueKey(srcUrl);
        }
        ZLog.d("path:" + getSavePath());
        //没有断点续传的逻辑
        deleteFile();

        // 开始下载
        markTaskStarted();

        DownloadRunnable.DownloadRunnableConfig config = new DownloadRunnable.DownloadRunnableConfig();
        config.srcUrl = srcUrl;
        download(config);
    }


    /**
     * 删除文件。文件是否删除成功，删除成功返回true，如果没有文件存在或者删除失败，返回false
     *
     * @return
     */
    public boolean deleteFile() {
        File file = new File(getSavePath());
        if (file.exists()) {
            return file.delete();
        }
        return false;
    }


    public void cancel() {
        if (status == DOWNLOAD_STATUS.CANCELLING || status == DOWNLOAD_STATUS.CANCELLED) {
            return;
        }

        if (mDownloadRunable != null) {
            status = DOWNLOAD_STATUS.CANCELLING;
            mDownloadRunable.cancel();
        } else {
            status = DOWNLOAD_STATUS.CANCELLED;
        }
    }

    private void download(DownloadRunnable.DownloadRunnableConfig config) {
        // TODO Auto-generated method stub

        if (status != DOWNLOAD_STATUS.STARTED) {
            markTaskCanceled();
            return;
        }

        config.dest = getSavePath();
        mDownloadRunable = new DownloadRunnable(config, this);
        mDownloadRunable.exec();
    }

    // implements start

    @Override
    public void onStarted(long id, String url, String finalUrl, long totalLength, long begin, long length) {
        // TODO Auto-generated method stub
        log("[onStarted] url: " + url + " finalUrl: " + finalUrl);
        markTaskUrlStarted(id, url, finalUrl, totalLength, begin, length);
    }

    @Override
    public void onFileLengthDetermined(long id, String url, long length, boolean rangeable) {
        // TODO Auto-generated method stub
        log("[onFileLengthDetermined] length: " + length + " rangeable: " + rangeable);
        totalLength = length;
        markTaskTotalLength(length);
    }

    @Override
    public void onReceived(long id, String url, long size, double speed) {
        // TODO Auto-generated method stub
        receivedLength += size;
        notifyReceivedLength(speed);
    }

    @Override
    public void onSaveReceived(long id, String url, long length) {
        // TODO Auto-generated method stub
        savedLength += length;
        saveTaskReceivedLength();
    }

    @Override
    public void onRedirect(long id, String url, String prevUrl, String nextUrl) {
        // TODO Auto-generated method stub
        log("[onRedirect] url: " + url + " prevUrl: " + prevUrl + " nextUrl: " + nextUrl);
        markTaskUrlRedirect(id, url, nextUrl);
    }

    @Override
    public void onFinalUrl(long id, String url, String finalUrl) {
        // TODO Auto-generated method stub
        // do nothing...
        log("[onFinalUrl] url: " + url + " finalUrl: " + finalUrl);
    }

    @Override
    public void onCompleted(long id, String url, long begin, long receivedLength, DownloadRunnable.DownloadRunnableConfig config) {
        //下载完成
        log("[onCompleted] url: " + url + " savedLength: " + savedLength + " totalLength: " + totalLength);
        if (this.savedLength < totalLength) {
            download(config);
        } else {
            markTaskCompleted();
        }
    }

    @Override
    public void onFailed(long id, String url, long receivedLength, int code, Throwable exception) {
        // TODO Auto-generated method stub
        log("[onFailed] url: " + url + " receivedLength: " + receivedLength + " code: " + code);
        markTaskUrlFailed(id, url, receivedLength, code, exception);
    }

    @Override
    public void onFinallyFailed(String url, int code, byte[] extMsg) {
        // TODO Auto-generated method stub
        log("[onFinallyFailed] url: " + url + " code: " + code);
        markTaskFailed(code, extMsg);
    }

    @Override
    public void onCanceled(long id, String url, long receivedLength) {
        // TODO Auto-generated method stub
        log("[onCanceled] url: " + url + " savedLength: " + savedLength + " totalLength: " + totalLength);
        markTaskCanceled();
    }

    @Override
    public void onChunkSizeAdjusted(long id, String url, long oldSize, long newSize) {
        // TODO Auto-generated method stub
        log("[onChunkSizeAdjusted] url: " + url + " oldSize: " + oldSize + " newSize: " + newSize);
    }

    private void markTaskCanceled() {
        // TODO Auto-generated method stub
        status = DOWNLOAD_STATUS.CANCELLED;
        // TODO Auto-generated method stub
        for (DownloadTaskListener listener : mListeners) {
            try {
                listener.onTaskCancel(srcUrl);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void markTaskStarted() {
        // TODO Auto-generated method stub
        this.status = DOWNLOAD_STATUS.STARTED;
        // TODO Auto-generated method stub
        for (DownloadTaskListener listener : mListeners) {
            try {
                listener.onTaskStarted(srcUrl);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    private void markTaskUrlStarted(final long runnableId, final String url, final String finalUrl,
                                    final long totalLength, final long begin, final long size) {

        for (DownloadTaskListener listener : mListeners) {
            try {
                listener.onTaskUrlStarted(runnableId, url, finalUrl, totalLength, begin, size);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

    }

    private void markTaskTotalLength(final long length) {
        // TODO Auto-generated method stub
        for (DownloadTaskListener listener : mListeners) {
            try {
                listener.onTaskSizeDetermined(srcUrl, length);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


    }

    //进度回调，接收到的字节数
    private void notifyReceivedLength(final double speed) {
        if (status != DOWNLOAD_STATUS.STARTED) {
            return;
        }

        for (DownloadTaskListener listener : mListeners) {
            try {
                listener.onTaskReceived(srcUrl, DownloadTask.this.totalLength,
                        DownloadTask.this.receivedLength, speed);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

    }


    private void saveTaskReceivedLength() {
        // TODO Auto-generated method stub
        // 这里用于断点续传到本地db或者文件，方便下次恢复
    }

    private void markTaskUrlRedirect(final long id, final String url, final String nextUrl) {
        // TODO Auto-generated method stub
        for (DownloadTaskListener listener : mListeners) {
            try {
                listener.onTaskUrlRedirect(id, url, nextUrl);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    private void markTaskCompleted() {
        // TODO Auto-generated method stub
        status = DOWNLOAD_STATUS.COMPLETE;
        // TODO Auto-generated method stub
        for (DownloadTaskListener listener : mListeners) {
            try {
                listener.onTaskCompleted(srcUrl, getSavePath());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    //单步失败
    private void markTaskUrlFailed(final long id, final String url, final long receivedLength2, final int code, final Throwable exception) {
        // TODO Auto-generated method stub
        for (DownloadTaskListener listener : mListeners) {
            try {
                listener.onTaskUrlFailed(id, url, receivedLength, code, exception);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    //这是真的失败了
    private void markTaskFailed(final int code, final byte[] extMsg) {
        // TODO Auto-generated method stub
        status = DOWNLOAD_STATUS.FAILED;
        // TODO Auto-generated method stub
        for (DownloadTaskListener listener : mListeners) {
            try {
                listener.onTaskFailed(srcUrl, code, extMsg, getSavePath());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
