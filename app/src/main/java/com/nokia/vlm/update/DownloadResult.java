package com.nokia.vlm.update;

public class DownloadResult {
	
	public static final int CODE_OK = 0;								// 正常下载执行
	
	public static final int CODE_CANCELED = 1;							// 取消
	
	public static final int CODE_UNDEFINED = -1000;						// 未定义

	public static final int CODE_OVERLOAD_MAX_REDIRECT = -1;			// 超出最多跳转次数

	public static final int CODE_RECEIVE_HTMLPAGE = -11;				// 收到的是一个html页面

	public static final int CODE_SPACE_NOT_ENOUGH = -12;				// 空间不足

	public static final int CODE_CREATE_FILE_FAILED = -13;				// 创建文件失败
	
	public static final int CODE_FILE_NOT_EXIST = -14; 					// 文件不存在
	
	public static final int CODE_NETWORK_UNAVAIABLE = -15; 				// 网络不存在
	
	public static final int CODE_URL_EMPTY = -16; 						// url为空
	
	public static final int CODE_WRITE_FILE_FAILED = -17; 				// 写文件失败
	
	public static final int CODE_URL_ERROR = -21; 						// url错误

	public static final int CODE_CLIENT_PROTOCOL_EXCEPTION = -22; 		// 协议错误

	public static final int CODE_CONNECTION_TIMEOUT_EXCEPTION = -23;	// 连接超时

	public static final int CODE_CONNECTION_EXCEPTION = -24; 			// 连接异常

	public static final int CODE_SOCKET_TIMEOUT_EXCEPTION = -25; 		// socket超时

	public static final int CODE_SOCKET_EXCEPTION = -26; 				// socket异常

	public static final int CODE_IO_EXCEPTION = -27; 					// io异常

	public static final int CODE_EXCEPTION = -28; 						// 其他http异常
	
	public static final int CODE_UNKNOWN_HOST_EXCEPTION = -29; 			// 未知域名
	
	public static final int CODE_HOST_CONNECTION_EXCEPTION = -30; 		// 连接被拒绝会属于此类异常


}
