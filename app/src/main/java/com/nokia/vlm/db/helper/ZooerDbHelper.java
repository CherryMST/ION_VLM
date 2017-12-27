package com.nokia.vlm.db.helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

import com.qx.framelib.db.helper.SqliteHelper;
import com.nokia.vlm.db.table.ZooerMessageTable;
import com.nokia.vlm.db.table.ZooerProfileTable;
import com.nokia.vlm.db.table.ZooerSettingTable;


public class ZooerDbHelper extends SqliteHelper {

    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "zooer_helper.db";

    private volatile static SqliteHelper instance;

    /**
     * 后续有表的增加或减少，这里进行维护
     */
    private static final Class<?>[] TABLESS = {
            ZooerSettingTable.class
            , ZooerProfileTable.class
          ,  ZooerMessageTable.class
    };

    public static synchronized SqliteHelper get(Context context) {
        if (instance == null) {
            instance = new ZooerDbHelper(context, DB_NAME, null, DB_VERSION);
        }
        return instance;
    }

    public ZooerDbHelper(Context context, String name, CursorFactory factory, int version) {
        super(context, DB_NAME, null, version);
        super.createTable(getWritableDatabase());
    }

    @Override
    public Class<?>[] getTables() {
        // TODO Auto-generated method stub
        return TABLESS;
    }

    @Override
    public int getDBVersion() {
        // TODO Auto-generated method stub
        return DB_VERSION;
    }

}
