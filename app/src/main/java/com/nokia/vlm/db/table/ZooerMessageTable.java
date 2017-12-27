package com.nokia.vlm.db.table;//package com.qixiang.mahjong.db.table;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.qx.framelib.application.ZooerApp;
import com.qx.framelib.db.helper.SQLiteDatabaseWrapper;
import com.qx.framelib.db.helper.SqliteHelper;
import com.qx.framelib.db.table.IBaseTable;
import com.qx.framelib.utlis.ZLog;
import com.nokia.vlm.db.helper.ZooerDbHelper;
import com.nokia.vlm.entity.Message;
import com.nokia.vlm.manager.UserInfoManager;

import java.util.ArrayList;
import java.util.List;

/**
 * 消息
 */

public class ZooerMessageTable implements IBaseTable {

    public static final String TABLE_NAME = "zooer_message";

    public static final String SQL_CREATE = "CREATE TABLE if not exists zooer_message ("
            + "_id INTEGER PRIMARY KEY AUTOINCREMENT,"
            + "messageId TEXT,"
            + "createTime INTEGER,"
            + "data BLOB,"
            + "userId TEXT,"
            + "status INTEGER"
            + ");";

    /**
     * 添加消息
     *
     * @param message
     * @return
     */
    public boolean addMessage(Message message) {
        if (message == null) {
            return false;
        }
        ZLog.d("添加消息");
        byte[] blobData = message.encode();
        final SQLiteDatabaseWrapper db = getHelper().getWritableDatabaseWrapper();
        ContentValues values = new ContentValues();
        values.put("messageId", message.id);
        values.put("data", blobData);
        values.put("createTime", message.addtime);
        values.put("status", message.isRead);
        values.put("userId", UserInfoManager.getInstance().getUserInfo().id);
        return db.insert(TABLE_NAME, null, values) > 0;

    }

    public void addMessage(List<Message> list) {
        if (list == null) return;
        for (Message message : list) {
            addMessage(message);
        }
    }


    public boolean updateMessage(Message message) {
        if (message == null) {
            return false;
        }
        byte[] blobData = message.encode();
        final SQLiteDatabaseWrapper db = getHelper().getWritableDatabaseWrapper();
        ContentValues values = new ContentValues();
        values.put("data", blobData);
        values.put("status", message.isRead);

        return db.update(TABLE_NAME, values, "messageId=? and userId=?", new String[]{message.id + "", UserInfoManager.getInstance().getUserInfo().id}) > 0;
    }

    public List<Message> getAllData() {
        SQLiteDatabaseWrapper db = getHelper().getWritableDatabaseWrapper();
        String sql = "select * from " + TABLE_NAME + " where userId=? order by createTime desc";
        Cursor cursor = db.rawQuery(sql, new String[]{UserInfoManager.getInstance().getUserInfo().id});
        List<Message> list = new ArrayList<>();
        try {
            if (cursor != null) {
                while (cursor.moveToNext()) {

                    byte[] bytes = cursor.getBlob(cursor.getColumnIndex("data"));
                    Message message = Message.decode(bytes);
                    list.add(message);
                }

                return list;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return null;
    }

    public String getLastId() {
        SQLiteDatabaseWrapper db = getHelper().getWritableDatabaseWrapper();
        String sql = "select * from " + TABLE_NAME + " where userId=?  order by createTime desc";
        Cursor cursor = db.rawQuery(sql, new String[]{UserInfoManager.getInstance().getUserInfo().id});
        try {
            if (cursor != null) {
                String lastId = "0";
                if (cursor.moveToFirst()) {
                    byte[] bytes = cursor.getBlob(cursor.getColumnIndex("data"));
                    Message message = Message.decode(bytes);
                    lastId = message.id;
                }
                return lastId;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return "0";
    }

    /**
     * 是否有未读消息
     *
     * @return
     */
    public int getUnReadCount() {

        SQLiteDatabaseWrapper db = getHelper().getWritableDatabaseWrapper();
//        String sql = "select * from " + TABLE_NAME + " where status='0'";
        Cursor cursor = db.query(TABLE_NAME, new String[]{"count(*)"}, "status=? and userId=?", new String[]{"0", UserInfoManager.getInstance().getUserInfo().id}, null, null, null);
        try {

            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    int count = cursor.getInt(0);
                    return count;
                } else {
                    return 0;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            ZLog.d("Exception", e.getMessage());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return 0;
    }


    public boolean clearAll() {
        SQLiteDatabaseWrapper db = getHelper().getWritableDatabaseWrapper();
        return db.delete(TABLE_NAME, null, null) > 0;
    }

    public boolean delMessageById(String id) {
        SQLiteDatabaseWrapper db = getHelper().getWritableDatabaseWrapper();
        return db.delete(TABLE_NAME, "messageId=? and userId=?", new String[]{id, UserInfoManager.getInstance().getUserInfo().id}) > 0;
    }

    @Override
    public int tableVersion() {
        // TODO Auto-generated method stub
        return 1;
    }

    @Override
    public String tableName() {
        return TABLE_NAME;
    }

    @Override
    public String createTableSQL() {
        return SQL_CREATE;
    }


    @Override
    public String[] getAlterSQL(int oldVersion, int newVersion) {
        return new String[0];
    }

    @Override
    public void beforeTableAlter(int oldVersion, int newVersion, SQLiteDatabase db) {

    }

    @Override
    public void afterTableAlter(int oldVersion, int newVersion, SQLiteDatabase db) {

    }

    @Override
    public SqliteHelper getHelper() {
        return ZooerDbHelper.get(ZooerApp.getAppSelf());
    }
}
