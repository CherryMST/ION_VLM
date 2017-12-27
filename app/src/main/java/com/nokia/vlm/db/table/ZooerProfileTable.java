package com.nokia.vlm.db.table;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.qx.framelib.application.ZooerApp;
import com.qx.framelib.db.helper.SQLiteDatabaseWrapper;
import com.qx.framelib.db.helper.SqliteHelper;
import com.qx.framelib.db.table.IBaseTable;
import com.nokia.vlm.db.helper.ZooerDbHelper;
import com.nokia.vlm.entity.UserInfo;


/**
 * 用户信息表主要用于保存用户资料信息
 */
public class ZooerProfileTable implements IBaseTable {

    public static final String TABLE_NAME = "zooer_profile";

    public static final String SQL_CREATE = "CREATE TABLE if not exists zooer_profile ("
            + "_id INTEGER PRIMARY KEY AUTOINCREMENT,"
            + "userId TEXT,"
            + "createTime INTEGER,"
            + "data BLOB"
            + ");";

    public ZooerProfileTable() {

    }

    public ZooerProfileTable(Context context) {

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

    /**
     * 用户资料发生变化的时候
     *
     * @param userInfo
     * @return
     */
    public boolean updateUserInfo(UserInfo userInfo) {
        if (userInfo == null) {
            return false;
        }

        byte[] blobData = userInfo.encodeUserInfo();
        final SQLiteDatabaseWrapper db = getHelper().getWritableDatabaseWrapper();
        ContentValues values = new ContentValues();
        values.put("data", blobData);

        return db.update(TABLE_NAME, values, "userId=?", new String[]{String.valueOf(userInfo.id)}) > 0;
    }


    /**
     * 添加新的用户资料信息
     *
     * @param userInfo
     * @return
     */
    public boolean addUserInfo(UserInfo userInfo) {
        if (userInfo == null) {
            return false;
        }

        byte[] blobData = userInfo.encodeUserInfo();
        final SQLiteDatabaseWrapper db = getHelper().getWritableDatabaseWrapper();
        ContentValues values = new ContentValues();
        values.put("userId", String.valueOf(userInfo.id));
        values.put("data", blobData);
        values.put("createTime", System.currentTimeMillis());
        return db.insert(TABLE_NAME, null, values) > 0;
    }

    /**
     * 删除一条记录
     *
     * @param userInfo
     * @return
     */
    public boolean delUserInfo(UserInfo userInfo) {
//        if (userInfo == null) {
//            return false;
//        }
//
//        final SQLiteDatabaseWrapper db = getHelper().getWritableDatabaseWrapper();
//        return db.delete(TABLE_NAME, "userId=?", new String[]{userInfo.user_id}) > 0;
        return clear();
    }

    /**
     * 清空数据库
     *
     * @return
     */
    public boolean clear() {
        final SQLiteDatabaseWrapper db = getHelper().getWritableDatabaseWrapper();
        return db.delete(TABLE_NAME, null, null) > 0;
    }


    /**
     * 获取数据库记录
     */
    public UserInfo getLastUserInfo() {
        final SQLiteDatabaseWrapper db = getHelper().getReadableDatabaseWrapper();
        Cursor cursor = null;
        UserInfo userInfo = null;
        try {
            cursor = db.query(TABLE_NAME, new String[]{"userId, data"}, null, null, null, null, "createTime desc");
            if (cursor != null && cursor.moveToFirst()) {
                do {
//					String userId = cursor.getString(0);
                    byte[] data = cursor.getBlob(1);
                    userInfo = UserInfo.decodeUserInfo(data);
                    break;
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return userInfo;
    }

    @Override
    public String[] getAlterSQL(int oldVersion, int newVersion) {

        return null;
    }

    @Override
    public void beforeTableAlter(int oldVersion, int newVersion,
                                 SQLiteDatabase db) {
        // TODO Auto-generated method stub

    }

    @Override
    public void afterTableAlter(int oldVersion, int newVersion,
                                SQLiteDatabase db) {
        // TODO Auto-generated method stub

    }

    @Override
    public SqliteHelper getHelper() {
        // TODO Auto-generated method stub
        return ZooerDbHelper.get(ZooerApp.getAppSelf());
    }

}
