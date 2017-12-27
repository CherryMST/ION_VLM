package com.nokia.vlm.update;

public interface OnSaveListener {

	void onSave2Cache(long length);

	void onNotify(long length, double speed);

	void onSave2File(long length);
	
	void onSave2FileFailed(Throwable e);
}
