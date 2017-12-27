package com.nokia.vlm.update;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * 缓存写入文件的工具类。跟调用方同步
 */
public class SyncCacheWriter implements CacheFileWriter {

	private long mReceivedNotifyLength = 0;

	private long mNotifyLength = -1;

	private long mSaveLength = -1;

	private RandomAccessFile mAccessFile;

	private ByteArrayOutputStream mBaos = null;

	private OnSaveListener mOnSaveListener;
	
	private IoStatistics mIoStatic;

	public SyncCacheWriter(String path, long notifyLength, long saveLength, OnSaveListener onSaveListener) {
		try {
			this.mAccessFile = new RandomAccessFile(path, "rw");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		this.mNotifyLength = notifyLength;
		this.mSaveLength = saveLength;
		this.mOnSaveListener = onSaveListener;
		this.mIoStatic = new IoStatistics();
		this.mBaos = new ByteArrayOutputStream();
	}

	@Override
	public void seek(long offset) {
		if (mAccessFile != null) {
			try {
				mAccessFile.seek(offset);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void write(byte[] data, int length) {
		if (data == null || data.length == 0) {
			return;
		}
		this.mIoStatic.add(new IoStatistics.Item(length, System.currentTimeMillis()));
		mReceivedNotifyLength += length;
		this.mBaos.write(data, 0, length);
		if (mOnSaveListener != null) {
			mOnSaveListener.onSave2Cache(length);
		}
		if (mReceivedNotifyLength >= this.mNotifyLength) {
			if (mOnSaveListener != null) {
				mOnSaveListener.onNotify(mReceivedNotifyLength, this.mIoStatic.getSpeed());
			}
			mReceivedNotifyLength = 0;
		}
		if (this.mBaos.size() >= this.mSaveLength) {
			try {
				flush();
			} catch (Throwable e) {
				e.printStackTrace();
				if (mOnSaveListener != null) {
				    mOnSaveListener.onSave2FileFailed(e);
				}
			}
		}
	}

	protected void flush() throws IOException {
		if (mAccessFile == null) {
			return;
		}
		byte[] data = this.mBaos.toByteArray();
		mAccessFile.write(data);
		if (mOnSaveListener != null) {
			mOnSaveListener.onSave2File(data.length);
		}
		this.mBaos.reset();
	}

	@Override
	public void recycle() {
		try {
			flush();
		} catch (Throwable e) {
			e.printStackTrace();
			if (mOnSaveListener != null) {
			    mOnSaveListener.onSave2FileFailed(e);
			}
		}
		if (mBaos != null) {
			try {
				mBaos.close();
			} catch (IOException e) {
			}
		}
		if (mAccessFile != null) {
			try {
				mAccessFile.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}