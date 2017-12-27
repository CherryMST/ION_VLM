package com.qx.framelib.manager;

import android.content.Context;
import android.content.IntentFilter;

import com.qx.framelib.net.APN;
import com.qx.framelib.receiver.NetworkMonitorReceiver;
import com.qx.framelib.utlis.ZLog;

import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.util.concurrent.ConcurrentLinkedQueue;

public class NetworkMonitorManager {
	
	
	private Context mContext;
	
	private NetworkMonitorReceiver mNetworkMonitorReceiver;
	
	// 用listener使用弱应用对象
	protected ReferenceQueue<ConnectivityChangeListener> mListenerReferenceQueue;
	protected ConcurrentLinkedQueue<WeakReference<ConnectivityChangeListener>> mWeakListenerArrayList;
	
	protected NetworkMonitorManager() {
		mListenerReferenceQueue = new ReferenceQueue<ConnectivityChangeListener>();
		mWeakListenerArrayList = new ConcurrentLinkedQueue<WeakReference<ConnectivityChangeListener>>();
		mNetworkMonitorReceiver = new NetworkMonitorReceiver();
	}
	
	protected void register(ConnectivityChangeListener listener) {
		if (listener == null) {
			return;
		}

		// 每次注册的时候清理已经被系统回收的对象
		Reference<? extends ConnectivityChangeListener> releaseListener = null;
		while ((releaseListener = mListenerReferenceQueue.poll()) != null) {
			mWeakListenerArrayList.remove(releaseListener);
		}

		// 弱引用处理
		for (WeakReference<ConnectivityChangeListener> weakListener : mWeakListenerArrayList) {
			ConnectivityChangeListener listenerItem = weakListener.get();
			if (listenerItem == listener) {
				return;
			}
		}
		WeakReference<ConnectivityChangeListener> weakListener = new WeakReference<ConnectivityChangeListener>(listener, mListenerReferenceQueue);
		this.mWeakListenerArrayList.add(weakListener);
		ZLog.d("NetWorkManager",this.mWeakListenerArrayList.size()+"size");
	}
	
	protected void unregister(ConnectivityChangeListener listener) {
		if (listener == null) {
			return;
		}

		// 弱引用处理
		for (WeakReference<ConnectivityChangeListener> weakListener : mWeakListenerArrayList) {
			ConnectivityChangeListener listenerItem = weakListener.get();
			if (listenerItem == listener) {
				mWeakListenerArrayList.remove(weakListener);
				return;
			}
		}
	}

	protected void init(Context context) {
		try { // IllegalArgumentException: Receiver requested to register for uid 10049 was previously registered for uid 10033 
			mContext = context.getApplicationContext();
			IntentFilter filter = new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE");
			mContext.registerReceiver(mNetworkMonitorReceiver, filter);
		} catch (Exception e) {
			
		}
	}
	
	protected void notifyConnected(APN apn){
		for (WeakReference<ConnectivityChangeListener> weakListener : mWeakListenerArrayList) {
			ConnectivityChangeListener listenerItem = weakListener.get();
			if (listenerItem != null) {
                try
                {
					ZLog.d("NetWorkManager", "notifyConnected");
                    listenerItem.onConnected(apn);//catch一下，以免哪里有异常造成crash
                }
                catch (Exception ex){
                    ex.printStackTrace();
                }
			}
		}
	}
	
	protected void notifyDisconnected(APN apn){
		for (WeakReference<ConnectivityChangeListener> weakListener : mWeakListenerArrayList) {
			ConnectivityChangeListener listenerItem = weakListener.get();
			if (listenerItem != null) {
				listenerItem.onDisconnected(apn);
			}
		}
	}
	
	protected void notifyChanged(APN apn1, APN apn2){
		for (WeakReference<ConnectivityChangeListener> weakListener : mWeakListenerArrayList) {
			ConnectivityChangeListener listenerItem = weakListener.get();
			if (listenerItem != null) {
				listenerItem.onConnectivityChanged(apn1, apn2);
			}
		}
	}
	
	/**
	 * wifi --> mobile:	onDisconnected(wifi) --> onConnected(mobile)<br/>
	 * 
	 * mobile --> wifi:	onConnectivityChanged(mobile, wifi)
	 */
	public interface ConnectivityChangeListener{
		
		/**
		 * 连上网络
		 * @param apn 当前使用的apn
		 */
		void onConnected(APN apn);
		
		/**
		 * 从网络断开网络热点
		 * @param apn 断开前的apn
		 */
		void onDisconnected(APN apn);
		
		/**
		 * 网络情况变化
		 * @param apn1  变化前的apn
		 * @param apn2  变化后的apn
		 */
		void onConnectivityChanged(APN apn1, APN apn2);
		
	}

}
