package com.nokia.vlm.update;


import com.qx.framelib.net.NetWorkUtil;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.params.CoreConnectionPNames;

public class HttpClientUtils {
	
		// 失败最多重试次数
		private static int MAX_HTTP_RETRY = 3;

		// 最多连接个数
		static final int TOTAL_CONNECTIONS = 100;

		// 单个路由最多连接数
		static final int MAX_CONNECTIONS_PER_ROUTE = 50;

		// 从连接池中取连接的最大超时时间
		static final int MAX_TIMEOUT = 1000;

		// 默认的连接超时时间
		static final int CONNECTION_TIMEOUT_DEFAULT = 10;

		// 默认的分片接收超时时间
		static final int SO_TIMEOUT_DEFAULT = 30;

		// 分片大小
		static final int BUFFER_SIZE_DEFAULT = 8 * 1024;

		// 2g环境下的网络超时时间
		static final int CONNECTION_TIMEOUT_2G = 15;

		// 3g环境下的网络超时时间
		static final int CONNECTION_TIMEOUT_3G = 10;

		// wifi环境下的网络超时时间
		static final int CONNECTION_TIMEOUT_WIFI = 5;

		// 2g环境下的两个分片之间的超时时间
		static final int SO_TIMEOUT_2G = 45;

		// 3g环境下的两个分片之间的超时时间
		static final int SO_TIMEOUT_3G = 40;

		// wifi环境下的两个分片之间的超时时间
		static final int SO_TIMEOUT_WIFI = 30;
		
		
		/**
		 * 重新设置代理
		 * 
		 * @param httpClient
		 */
		public static void proxyHttpClient(HttpClient httpClient) {
//			if (NetworkUtil.isMobileNetWork(BaseApplicationImpl.getContext())) {
//				String defaultHost = Proxy.getDefaultHost();
//				int defaultPort = Proxy.getDefaultPort();
//				if (defaultHost != null && defaultPort != -1) {
//					HttpHost proxy = new HttpHost(defaultHost, defaultPort);
//					httpClient.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, proxy);
//					return;
//				}
//			}
//			httpClient.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, null);
		}

		/**
		 * 重新设置超时时间
		 * 
		 * @param httpClient
		 */
		public static void timeoutHttpClient(HttpClient httpClient) {
			int connectionTimeout = 10;
			int soTimeout = 30;

			int netType = NetWorkUtil.getGroupNetType();
			if (netType == NetWorkUtil.GROUP_NETTYPE_WIFI) {
				connectionTimeout = CONNECTION_TIMEOUT_WIFI;
				soTimeout = SO_TIMEOUT_WIFI;
			} else if (netType == NetWorkUtil.GROUP_NETTYPE_3g) {
				connectionTimeout = CONNECTION_TIMEOUT_3G;
				soTimeout = SO_TIMEOUT_3G;
			} else if (netType == NetWorkUtil.GROUP_NETTYPE_2g) {
				connectionTimeout = CONNECTION_TIMEOUT_2G;
				soTimeout = SO_TIMEOUT_2G;
			} 
			httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, connectionTimeout * 1000);
			httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, soTimeout * 1000);
		}


	public static boolean isHtmlPage(HttpResponse response) {
		String contentType = HttpClientUtils.getHeader(response, "Content-Type");
		return contentType != null && contentType.startsWith("text");
	}

	public static String getHeader(HttpResponse response, String name) {
		Header[] headers = response.getHeaders(name);
		if (headers != null && headers.length > 0) {
			for (Header header : headers) {
				if (header.getName().equalsIgnoreCase(name)) {
					return header.getValue();
				}
			}
		}
		return null;
	}
}
