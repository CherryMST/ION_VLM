package com.qx.framelib.event;

import android.os.Message;

import com.qx.framelib.event.listener.UIEventListener;

import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class EventController implements EventListener {

    private ConcurrentHashMap<Integer, List<WeakReference<UIEventListener>>> mUIEventListeners = null;
    // 用listener使用弱应用对象
    protected ReferenceQueue<UIEventListener> mUIListenerReferenceQueue;

    private static EventController sInstance = null;

    public static synchronized EventController getInstance() {
        if (sInstance == null) {
            sInstance = new EventController();
        }
        return sInstance;
    }

    private EventController() {
        mUIEventListeners = new ConcurrentHashMap<Integer, List<WeakReference<UIEventListener>>>();
        mUIListenerReferenceQueue = new ReferenceQueue<UIEventListener>();
    }

    public void addUIEventListener(int eventId, UIEventListener listener) {
        if (listener == null) {
            return;
        }
        synchronized (mUIEventListeners) {
            // 每次注册的时候清理已经被系统回收的对象
            Reference<? extends UIEventListener> releaseListener = null;
            List<WeakReference<UIEventListener>> list = mUIEventListeners.get(eventId);
            if (list != null) {
                while ((releaseListener = mUIListenerReferenceQueue.poll()) != null) {
                    list.remove(releaseListener);
                }
            }
            // 注册
            if (list == null) {
                list = new ArrayList<WeakReference<UIEventListener>>();
                mUIEventListeners.put(eventId, list);
            }
            for (WeakReference<UIEventListener> weakListener : list) {
                UIEventListener listenerItem = weakListener.get();
                if (listenerItem == listener) {
                    return;
                }
            }
            WeakReference<UIEventListener> newListener = new WeakReference<UIEventListener>(listener, mUIListenerReferenceQueue);
            list.add(newListener);
        }
    }

    public void removeUIEventListener(int eventId, UIEventListener listener) {
        if (listener == null) {
            return;
        }
        synchronized (mUIEventListeners) {
            List<WeakReference<UIEventListener>> list = mUIEventListeners.get(eventId);
            if (list != null) {
                for (Iterator<WeakReference<UIEventListener>> i = list.iterator(); i.hasNext(); ) {
                    WeakReference<UIEventListener> weakListener = i.next();
                    if (weakListener.get() == listener) {
                        list.remove(weakListener);
                        if (list.isEmpty()) {
                            mUIEventListeners.remove(eventId);
                        }
                        return;
                    }
                }

            }
        }
    }


    private void handleUIEvent(Message msg) {
        List<WeakReference<UIEventListener>> lt = mUIEventListeners.get(msg.what);
        if (lt != null) {
            List<WeakReference<UIEventListener>> list = new ArrayList<WeakReference<UIEventListener>>(lt);
            if (list != null) {
                for (Iterator<WeakReference<UIEventListener>> i = list.iterator(); i.hasNext(); ) {
                    UIEventListener listener = i.next().get();
                    if (listener != null) {
                        listener.handleUIEvent(msg);
                    }
                }
            }
        }
    }

    @Override
    public void handleEvent(Message msg) {
        if (msg.what > EventDispatcherEnum.UI_EVENT_BEGIN && msg.what < EventDispatcherEnum.UI_EVENT_END) {
            handleUIEvent(msg);
        }
    }

}
