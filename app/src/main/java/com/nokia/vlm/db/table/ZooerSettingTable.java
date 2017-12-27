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

import java.util.Map;

public class ZooerSettingTable implements IBaseTable {

    public static final String TABLE_NAME = "zooer_setting";

    public static final String SQL_CREATE = "CREATE TABLE if not exists zooer_setting ("
            + "_id INTEGER PRIMARY KEY AUTOINCREMENT,"
            + "uqid TEXT," + "key TEXT," //唯一值
            + "value TEXT,"
            + "data BLOB"
            + ");";

    public ZooerSettingTable() {

    }

    public ZooerSettingTable(Context context) {

    }


    public boolean update(String uqid, String key, String value) {
        return update(uqid, key, value, null);
    }

    public boolean update(String uqid, String key, String value, byte[] data) {
        final SQLiteDatabaseWrapper db = getHelper().getWritableDatabaseWrapper();
        ContentValues values = new ContentValues();
        values.put("value", value);
        if (data != null) {
            values.put("data", data);
        }
        return db.update(TABLE_NAME, values, "uqid=? and key=?", new String[]{uqid, key}) > 0;
    }

    public boolean update(String key, String value) {
        return update("", key, value);
    }

    public boolean add(String uqid, String key, String value) {
        return add(uqid, key, value, null);
    }

    public boolean add(String uqid, String key, String value, byte[] data) {
        final SQLiteDatabaseWrapper db = getHelper().getWritableDatabaseWrapper();
        ContentValues values = new ContentValues();
        values.put("uqid", uqid);
        values.put("key", key);
        values.put("value", value);
        if (data != null) {
            values.put("data", data);
        }
        return db.insert(TABLE_NAME, null, values) > 0;
    }

    public boolean add(String key, String value) {
        return add("", key, value);
    }

    public String get(String uqid, String key) {
        final SQLiteDatabaseWrapper db = getHelper().getReadableDatabaseWrapper();
        Cursor cursor = null;
        try {
            cursor = db.query(TABLE_NAME, new String[]{"value"}, "uqid=? and key=?", new String[]{uqid, key}, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                return cursor.getString(0);
            }
        } catch (Exception e) {

        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return "";
    }

    public byte[] getBlob(String uqid, String key) {
        final SQLiteDatabaseWrapper db = getHelper().getReadableDatabaseWrapper();
        Cursor cursor = null;
        try {
            cursor = db.query(TABLE_NAME, new String[]{"data"}, "uqid=? and key=?", new String[]{uqid, key}, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                return cursor.getBlob(0);
            }
        } catch (Exception e) {

        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return null;
    }

    public String get(String key) {
        return get("", key);
    }

    public void getAll(Map<String, String> map) {
        final SQLiteDatabaseWrapper db = getHelper().getReadableDatabaseWrapper();
        Cursor cursor = null;
        try {
            cursor = db.query(TABLE_NAME, new String[]{"uqid, key, value"}, null, null, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    String uqid = cursor.getString(0);
                    String key = cursor.getString(1);
                    String value = cursor.getString(2);
                    if (value != null) {
                        map.put(uqid + "_" + key, value);
                    }
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    @Override
    public int tableVersion() {
        // TODO Auto-generated method stub
        return 1;
    }

    @Override
    public String tableName() {
        // TODO Auto-generated method stub
        return TABLE_NAME;
    }

    @Override
    public String createTableSQL() {
        // TODO Auto-generated method stub
        return SQL_CREATE;
    }

    @Override
    public String[] getAlterSQL(int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
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
