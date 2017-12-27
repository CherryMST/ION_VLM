package com.qx.framelib.callback;




import com.qx.framelib.utlis.HandlerUtils;

import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;

public class CallbackHelper<ICallback extends ActionCallback> {

    protected ReferenceQueue<ICallback> mCallbackReferenceQueue;
    protected ArrayList<WeakReference<ICallback>> mWeakCallbackArrayList;

    public CallbackHelper() {
        mCallbackReferenceQueue = new ReferenceQueue<ICallback>();
        mWeakCallbackArrayList = new ArrayList<WeakReference<ICallback>>();
    }

    public void register(ICallback cb) {
        if (cb == null) {
            return;
        }
        synchronized (mWeakCallbackArrayList) {
            // 每次注册的时候清理已经被系统回收的对象
            Reference<? extends ICallback> releaseListener = null;
            while ((releaseListener = mCallbackReferenceQueue.poll()) != null) {
                mWeakCallbackArrayList.remove(releaseListener);
            }

            try {
                // 弱引用处理
                for (WeakReference<ICallback> weakListener : mWeakCallbackArrayList) {
                    ICallback listenerItem = weakListener.get();
                    if (listenerItem == cb) {
                        return;
                    }
                }

            } catch (ConcurrentModificationException e) {
            }

            WeakReference<ICallback> weakListener = new WeakReference<ICallback>(cb, mCallbackReferenceQueue);
            this.mWeakCallbackArrayList.add(weakListener);
        }
    }

    public void unregister(ICallback cb) {
        if (cb == null) {
            return;
        }

        synchronized (mWeakCallbackArrayList) {
            try {
                for (WeakReference<ICallback> weakListener : mWeakCallbackArrayList) {
                    ICallback listenerItem = weakListener.get();
                    if (listenerItem == cb) {
                        mWeakCallbackArrayList.remove(weakListener);
                        return;
                    }
                }

            } catch (ConcurrentModificationException e) {
            }
        }
    }

    public void unregisterAll() {
        synchronized (mWeakCallbackArrayList) {
            mWeakCallbackArrayList.clear();
        }
    }

    public void broadcast(final Caller<ICallback> caller) {
        synchronized (mWeakCallbackArrayList) {

            try {
                for (WeakReference<ICallback> weakListener : mWeakCallbackArrayList) {
                    ICallback listener = weakListener.get();
                    if (listener != null) {
                        try {
                            if (caller != null) {
                                caller.call(listener);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            } catch (ConcurrentModificationException e) {
            }
        }
    }

    public void broadcastInMainThread(final Caller<ICallback> caller) {
        HandlerUtils.getMainHandler().post(new Runnable() {

            @Override
            public void run() {
                broadcast(caller);
            }
        });
    }

    public void delayBroadcastInMainThread(final Caller<ICallback> caller, final long delay) {
        HandlerUtils.getMainHandler().postDelayed(new Runnable() {

            @Override
            public void run() {
                broadcast(caller);
            }
        }, delay);
    }

    public interface Caller<T> {
        public void call(T cb);
    }

}
