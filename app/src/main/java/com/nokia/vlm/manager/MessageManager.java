package com.nokia.vlm.manager;


import com.qx.framelib.utlis.TextUtil;
import com.qx.framelib.utlis.ZLog;
import com.nokia.vlm.db.table.ZooerMessageTable;
import com.nokia.vlm.entity.Message;
import com.nokia.vlm.event.QXEventDispatcherEnum;
import com.nokia.vlm.ui.QXApp;

import java.util.ArrayList;
import java.util.List;

public class MessageManager {
    private ZooerMessageTable table;

    private static MessageManager instanse;

    private List<Message> list;

    private int count;

    private MessageManager() {
        table = new ZooerMessageTable();
    }

    public synchronized static MessageManager getInstanse() {
        if (instanse == null) {
            instanse = new MessageManager();
        }
        return instanse;
    }

    public synchronized boolean addMessage(Message message) {
        boolean isSuccess = table.addMessage(message);
        if (isSuccess) {
            list = getAllData();
            if (list == null) {
                list = new ArrayList<>();
            }
            list.add(0, message);
        }
        return isSuccess;
    }


    public synchronized void addMessage(List<Message> list) {
        if (list == null || list.isEmpty()) return;
        table.addMessage(list);
        if (this.list == null) {
            this.list = new ArrayList<>(list);
        } else
            this.list.addAll(0, list);
    }

    public synchronized boolean updateMessage(Message message) {
        boolean isSuccess = table.updateMessage(message);

        return isSuccess;
    }

    public synchronized List<Message> getAllData() {
        if (list == null) {
            list = table.getAllData();
            return list;
        }
        ZLog.d(list == null ? "list null " : "list size:" + list.size());
        return list;
    }

    public synchronized String getLastId() {
        return table.getLastId();
    }

    public synchronized int getUnReadCount() {
        return table.getUnReadCount();
    }

    /**
     * 更新 数据
     *
     * @param postion
     * @param message
     */
    public synchronized void updateMessage(int postion, Message message) {
        if (list != null) {
            list.set(postion, message);
        }
    }

    /**
     * 清空数据
     *
     * @return
     */
    public synchronized boolean clearAll() {
        if (list != null) {
            list.removeAll(list);
            ZLog.d("clearAll", "size:" + list.size());
        }
        return table.clearAll();
    }

    public synchronized boolean delMessageById(String id) {
        if (TextUtil.isEmpty(id)) return false;
        return table.delMessageById(id);
    }

    /**
     * @param type 0：push  1：本地
     */
    public synchronized void showDot(int type) {
        android.os.Message message = QXApp.getAppSelf().getEventDispatcher().obtainMessage();
        message.what = QXEventDispatcherEnum.UI_EVENT_PUSH_MESSAGE;
        message.arg1 = type;
        QXApp.getAppSelf().getEventDispatcher().sendMessage(message);

    }

    public synchronized void hideDot() {
        android.os.Message message = QXApp.getAppSelf().getEventDispatcher().obtainMessage();
        message.what = QXEventDispatcherEnum.UI_EVENT_READ_END_MESSAGE;

        QXApp.getAppSelf().getEventDispatcher().sendMessage(message);
    }
}
