package com.nokia.vlm.update;


/**
 * 缓存到一定大小后再写入磁盘
 * 
 */
public interface CacheFileWriter {

	void seek(long offset);

	void write(byte[] data, int length);

	void recycle();

}
