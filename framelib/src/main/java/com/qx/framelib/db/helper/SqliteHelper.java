package com.qx.framelib.db.helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.SystemClock;

import com.qx.framelib.db.table.IBaseTable;


public abstract class SqliteHelper extends SQLiteOpenHelper {
    private static final String TAG = SqliteHelper.class.getSimpleName();

    public SqliteHelper(Context context, String name, CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public abstract Class<?>[] getTables();

    public abstract int getDBVersion();


    @Override
    @Deprecated
    /**
     * 不使用此方法，使用getWritableDatabaseWrapper方法来代替
     */
    public synchronized SQLiteDatabase getWritableDatabase() {
        final SQLiteDatabase db = super.getWritableDatabase();
        while (db.isDbLockedByCurrentThread() || db.isDbLockedByOtherThreads()) {
            SystemClock.sleep(10l);
        }

        return db;
    }

    @Override
    @Deprecated
    /**
     * 不再使用此方法，使用getReadableDatabaseWrapper来代替
     */
    public synchronized SQLiteDatabase getReadableDatabase() {
        final SQLiteDatabase db = super.getReadableDatabase();
        while (db.isDbLockedByCurrentThread() || db.isDbLockedByOtherThreads()) {
            SystemClock.sleep(10l);
        }
        return db;
    }

    /**
     * 获取可写的db操作对象
     *
     * @return
     */
    public synchronized SQLiteDatabaseWrapper getWritableDatabaseWrapper() {
        SQLiteDatabase db = null;
        try {
            db = getWritedb();
        } catch (Throwable throwable) {
            throwable.printStackTrace();//失败了再尝试恢复获取db对象，如果再失败则不再尝试
            SystemClock.sleep(10l);
            try {
                db = getWritedb();
            } catch (Throwable throwable1) {
                throwable1.printStackTrace();
            }
        }

        return new SQLiteDatabaseWrapper(db);
    }

    private SQLiteDatabase getWritedb() {
        SQLiteDatabase db;
        db = super.getWritableDatabase();
        while (db.isDbLockedByCurrentThread() || db.isDbLockedByOtherThreads()) {
            SystemClock.sleep(10l);
        }
        return db;
    }

    /**
     * 获取可读的db操作对象
     *
     * @return
     */
    public synchronized SQLiteDatabaseWrapper getReadableDatabaseWrapper() {
        SQLiteDatabase db = null;
        try {
            db = getReaddb();
        } catch (Throwable throwable) {
            throwable.printStackTrace();//失败了再尝试恢复获取db对象，如果再失败则不再尝试
            SystemClock.sleep(10l);
            try {
                db = getReaddb();
            } catch (Throwable throwable1) {
                throwable1.printStackTrace();
            }
        }
        return new SQLiteDatabaseWrapper(db);
    }

    private SQLiteDatabase getReaddb() {
        SQLiteDatabase db = super.getReadableDatabase();
        while (db.isDbLockedByCurrentThread() || db.isDbLockedByOtherThreads()) {
            SystemClock.sleep(10l);
        }
        return db;
    }


    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);

//        int version = db.getVersion();
//        if (version == 0) {
//        } else if (version < getDBVersion()) {
//            this.onUpgrade(db, version, getDBVersion());
//        } else if (version > getDBVersion()) {
//            this.onDowngrade(db, version, getDBVersion());
//        }
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        createTable(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//        // 一步步往上升级
//        for (int i = oldVersion; i < newVersion; i++) {
//            for (Class<?> baseTable : getTables()) {
//                try {
//                    IBaseTable table = (IBaseTable) baseTable.newInstance();
//                    table.beforeTableAlter(i, i+1,db);
//                    String[] sql = table.getAlterSQL(i, i + 1);
//                    if (sql != null ) {
//                        for( int j = 0; j < sql.length; j ++){
//                            db.execSQL(sql[j]);
//                        }
//                    }
//                    table.afterTableAlter(i, i+1,db);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        }
        deleteTable(db);
        createTable(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        //Log.d("com.qq.connect", "onDowngrade sqliteHelper");

        deleteTable(db);
        createTable(db);
    }


    public void createTable(SQLiteDatabase db) {
        for (Class<?> baseTable : getTables()) {
            String sql = null;
            try {
                IBaseTable table = (IBaseTable) baseTable.newInstance();
                sql = table.createTableSQL();
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (sql != null && sql.length() > 0) {
                db.execSQL(sql);
            }
        }
    }


    private void deleteTable(SQLiteDatabase db) {
        for (Class<?> baseTable : getTables()) {
            try {
                IBaseTable table = (IBaseTable) baseTable.newInstance();
                db.delete(table.tableName(), null, null);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public int getSumTableVer() {
        int ver = 0;
        for (Class<?> baseTable : getTables()) {
            try {
                IBaseTable table = (IBaseTable) baseTable.newInstance();
                ver += table.tableVersion();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return ver;
    }


}
