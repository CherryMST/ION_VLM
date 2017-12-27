package com.nokia.vlm.update;

public interface DownloadTaskListener {

	void onTaskAlreadyCompleted(String url, String savePath);

	void onTaskStarted(String url);

	void onTaskUrlStarted(long chunkId, String url, String finalUrl, long totalLength, long begin,
						  long size);

	void onTaskUrlRedirect(long chunkId, String url, String nextUrl);

	void onTaskUrlChunkSizeAdjusted(long chunkId, String url, long newSize);

	void onTaskUrlCompleted(long chunkId, String url, long receivedLength);

	void onTaskUrlFailed(long chunkId, String url, long receivedLength, int code, Throwable e);

	void onTaskUrlCanceled(long chunkId, String url, long receivedLength);

	void onTaskSizeDetermined(String url, long length);

	void onTaskReceived(String url, long totalLength, long length, double speed);

	void onTaskCompleted(String url, String savePath);

	void onTaskFailed(String url, int errorCode, byte[] extMsg, String savePath);

	void onTaskCancel(String url);

}
