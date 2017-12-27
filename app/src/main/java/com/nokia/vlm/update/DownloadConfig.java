package com.nokia.vlm.update;


import com.qx.framelib.utlis.FileUtils;
import com.qx.framelib.utlis.SecureUtils;

/**
 * 下载过程的参数配置
 * 
 */
public class DownloadConfig {

	/**
	 * 默认最多同时1个任务在执行
	 */
	public static final int DEFAULT_MAX_TASK = 1;

	/**
	 * 默认分片大小。0表示不分片
	 */
	public static final int DEFAULT_CHUNK_SIZE = 0;

	/**
	 * 2g的网络环境下单个分片大小最多400k
	 */
	public static final int MAX_CHUNK_SIZE_2G = 400 * 1024;

	/**
	 * 3g的网络环境下单个分片大小最多900k
	 */
	public static final int MAX_CHUNK_SIZE_3G = 900 * 1024;
	
	/**
	 * wifi下单个分片大小最多2m
	 */
	public static final int MAX_CHUNK_SIZE_WIFI = 2 * 1024 * 1024;

	/**
	 * socket buffer=16k wifi
	 */
	public static final int SOCKET_BUFFER_WIFI = 16 * 1024;
	
	/**
	 * socket buffer=16k 3g
	 */
	public static final int SOCKET_BUFFER_3G = 16 * 1024;
	
	/**
	 * socket buffer=18k 2G
	 */
	public static final int SOCKET_BUFFER_2G = 18 * 1024;

	/**
	 * 500k保存一次 wifi
	 */
	public static final int CAHCE_LENGTH_TO_SAVE = 500 * 1024;

	/**
	 * 2G 下载速度保存一次，2G速度为8K左右，约每3秒保存一次
	 */
	public static final int CAHCE_2G_LENGTH_TO_SAVE = 24 * 1024;

	/**
	 * 3G 下载速度保存一次，3G速度为150K左右，约每3秒保存一次
	 */
	public static final int CAHCE_3G_LENGTH_TO_SAVE = 300 * 1024;

	/**
	 * 2g下每4k通知UI刷新一次
	 */
	public static final int NOTIFY_UI_CHANGE_2G = 4 * 1024;

	/**
	 * 3g跟wifi环境下36k通知UI刷新一次
	 */
	public static final int NOTIFY_UI_CHANGE_3G_WIFI = 100 * 1024;
	
	/**
	 * 302跳转最多只能有8次
	 */
	public static final int MAX_HTTP_REDIRECT = 8;
	
	/**
	 * 下载的失败重试次数
	 */
	public static final int MAX_RETRY = 3;

	/**
	 * 默认的下载目录
	 * 
	 * @return
	 */
	public static final String getDefaultSaveDir() {
		return FileUtils.getApkDir();
	}

	/**
	 * 默认以请求的url的md5作为文件名
	 * 
	 * @return
	 */
	public static final String getDefaultSaveName(String srcUrl) {
		return SecureUtils.md5Encode(srcUrl);
	}

}
