package com.nokia.vlm.update;



public interface DownloadRunnableListener {

	void onStarted(long id, String url, String finalUrl, long totalLength, long begin, long length);

	void onFileLengthDetermined(long id, String url, long length, boolean rangeable);
	
	void onReceived(long id, String url, long length, double speed);
	
	void onSaveReceived(long id, String url, long length);
	
	void onRedirect(long id, String url, String prevUrl, String nextUrl);
	
	void onFinalUrl(long id, String url, String finalUrl);
	
	void onCompleted(long id, String url, long begin, long receivedLength, DownloadRunnable.DownloadRunnableConfig config);
	
	void onFailed(long id, String url, long receivedLength, int code, Throwable exception);
	
	void onFinallyFailed(String url, int code, byte[] extMsg);
	
	void onCanceled(long id, String url, long receivedLength);
	
	void onChunkSizeAdjusted(long id, String url, long oldSize, long newSize);

}
