package com.nokia.vlm.update;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.widget.RemoteViews;

import com.qx.framelib.dialog.LoadingDialog;
import com.qx.framelib.event.listener.UIEventListener;
import com.qx.framelib.utlis.TextUtil;
import com.qx.framelib.utlis.ZLog;
import com.nokia.vlm.R;
import com.nokia.vlm.event.QXEventDispatcherEnum;
import com.nokia.vlm.ui.QXApp;
import com.nokia.vlm.utils.SelfUpdateDialogUtils;

import java.io.File;


/**
 * @author ZhaoWei
 *         created at 2016/4/7.
 *         下载 更新 服务
 */
public class UpdateService extends Service implements UIEventListener {
    private static final int NOTIFICATION_ID = 1001;
    private NotificationManager notificationManager;
    private Notification notification;

    private int current_update_type = 1;

    private LoadingDialog loadingDialog;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        ZLog.d(getClass().getName(), "onCreate");
        registerEventListener();
    }

    private void registerEventListener() {
        QXApp.getAppSelf().getEventController().addUIEventListener(QXEventDispatcherEnum.UI_EVENT_SELF_UPDATE_DOWNLOAD_ING, this);
        QXApp.getAppSelf().getEventController().addUIEventListener(QXEventDispatcherEnum.UI_EVENT_SELF_UPDATE_DOWNLOAD_END, this);
        QXApp.getAppSelf().getEventController().addUIEventListener(QXEventDispatcherEnum.UI_EVENT_SELF_UPDATE_DOWNLOAD_START, this);
    }

    private void removeEventListener() {
        QXApp.getAppSelf().getEventController().removeUIEventListener(QXEventDispatcherEnum.UI_EVENT_SELF_UPDATE_DOWNLOAD_ING, this);
        QXApp.getAppSelf().getEventController().removeUIEventListener(QXEventDispatcherEnum.UI_EVENT_SELF_UPDATE_DOWNLOAD_END, this);
        QXApp.getAppSelf().getEventController().removeUIEventListener(QXEventDispatcherEnum.UI_EVENT_SELF_UPDATE_DOWNLOAD_START, this);
    }

    private void initNotification() {
        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notification = new Notification();
        notification.icon = R.drawable.icon_app;
        notification.tickerText = "正在下载...";
        notification.when = System.currentTimeMillis();

        notification.flags = Notification.FLAG_ONGOING_EVENT;

    }

    private synchronized void updateNotification(int progress) {
        if (notificationManager != null && notification != null) {
            RemoteViews remoteView = new RemoteViews(getPackageName(), R.layout.notification_layout);
            remoteView.setProgressBar(R.id.progress, 100, progress, false);
            notification.contentView = remoteView;
            notificationManager.notify(NOTIFICATION_ID, notification);
        }
    }

    private void cancalNotification() {
        if (notificationManager != null && notification != null) {
            notificationManager.cancelAll();
            notification = null;
            notificationManager = null;
            stopSelf();
        }
    }

//    private void showDialog(String text) {
//        if (loadingDialog == null) {
//            loadingDialog = new LoadingDialog(QXApp.getCurrentActivity());
//
//            loadingDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
//            loadingDialog.setCancelable(false);
//            loadingDialog.setCanceledOnTouchOutside(false);
//
//            loadingDialog.show(text);
//        }
////        else {
////            loadingDialog.setText(text);
////        }
//    }

    private void dismissDialog() {
        if (loadingDialog != null) {
            loadingDialog.dismiss();
            stopSelf();
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        ZLog.d(getClass().getName(), "onStartCommand");
        current_update_type = intent.getIntExtra("current_update_type", 1);
        String url = intent.getStringExtra("url");

        if (!TextUtil.isEmpty(url)) {
            startDownload(url);
        }

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ZLog.d(getClass().getName(), "onDestroy");
        removeEventListener();
    }

    private void startDownload(String url) {

        DownloadTask task = new DownloadTask(url);
        task.addListener(new DownloadTaskListener() {
            @Override
            public void onTaskAlreadyCompleted(String url, String savePath) {
//                ZLog.d(getClass().getName(), "onTaskAlreadyCompleted: url-->" + url + "   savePath:" + savePath);
            }

            @Override
            public void onTaskStarted(String url) {
                ZLog.d(getClass().getName(), "onTaskStarted: url-->" + url);
                Message message = new Message();
                message.what = QXEventDispatcherEnum.UI_EVENT_SELF_UPDATE_DOWNLOAD_START;
                message.arg1 = current_update_type;
                QXApp.getAppSelf().getEventDispatcher().sendMessage(message);
            }

            @Override
            public void onTaskUrlStarted(long chunkId, String url, String finalUrl, long totalLength, long begin, long size) {

            }

            @Override
            public void onTaskUrlRedirect(long chunkId, String url, String nextUrl) {

            }

            @Override
            public void onTaskUrlChunkSizeAdjusted(long chunkId, String url, long newSize) {

            }

            @Override
            public void onTaskUrlCompleted(long chunkId, String url, long receivedLength) {

            }

            @Override
            public void onTaskUrlFailed(long chunkId, String url, long receivedLength, int code, Throwable e) {

            }

            @Override
            public void onTaskUrlCanceled(long chunkId, String url, long receivedLength) {

            }

            @Override
            public void onTaskSizeDetermined(String url, long length) {

            }

            @Override
            public void onTaskReceived(String url, long totalLength, long length, double speed) {
//                ZLog.d(getClass().getName(), "onTaskReceived: totalLength-->" + totalLength + "   length-->" + length);

                int progress = (int) (length * 100 / totalLength);

                Message message = new Message();
                message.what = QXEventDispatcherEnum.UI_EVENT_SELF_UPDATE_DOWNLOAD_ING;
                message.arg1 = current_update_type;
                message.arg2 = progress;
//                ZLog.d(getClass().getName(), "progress:" + progress);
                QXApp.getAppSelf().getEventDispatcher().sendMessage(message);

            }

            @Override
            public void onTaskCompleted(String url, String savePath) {
//                ZLog.d(getClass().getName(), "onTaskCompleted: url-->" + url + "   savePath-->" + savePath);
                Message message = new Message();
                message.what = QXEventDispatcherEnum.UI_EVENT_SELF_UPDATE_DOWNLOAD_END;
                message.obj = savePath;
                message.arg1 = current_update_type;
                QXApp.getAppSelf().getEventDispatcher().sendMessage(message);
            }

            @Override
            public void onTaskFailed(String url, int errorCode, byte[] extMsg, String savePath) {

            }

            @Override
            public void onTaskCancel(String url) {

            }
        });

        DownloadManager.getInstance().start(task);


    }

    @Override
    public void handleUIEvent(Message msg) {

        if (msg.what == QXEventDispatcherEnum.UI_EVENT_SELF_UPDATE_DOWNLOAD_START) {//开始下载
            int type = msg.arg1;
            if (type == SelfUpdateDialogUtils.SELF_UPDATE_FORCE) {//强制升级

//                showDialog("升级中...(0%)");

            } else {
                initNotification();
                updateNotification(0);
            }
        }

        if (msg.what == QXEventDispatcherEnum.UI_EVENT_SELF_UPDATE_DOWNLOAD_ING) {//下载中
            ZLog.d(getClass().getName(), "UI_EVENT_SELF_UPDATE_DOWNLOAD_ING:" + msg.arg1);
            int type = msg.arg1;
            int progress = msg.arg2;
            if (type == SelfUpdateDialogUtils.SELF_UPDATE_COMMON) {//普通升级
                updateNotification(progress);
            } else {
//                showDialog("升级中...(" + progress + "%)");
            }
        }

        if (msg.what == QXEventDispatcherEnum.UI_EVENT_SELF_UPDATE_DOWNLOAD_END) {//完成
            String filepath = (String) msg.obj;
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setDataAndType(Uri.fromFile(new File(filepath)), "application/vnd.android.package-archive");
            startActivity(intent);
            if (msg.arg1 == SelfUpdateDialogUtils.SELF_UPDATE_COMMON) { //普通升级
                cancalNotification();
            } else {//强制升级
                dismissDialog();
                android.os.Process.killProcess(android.os.Process.myPid());
            }

        }

    }
}
