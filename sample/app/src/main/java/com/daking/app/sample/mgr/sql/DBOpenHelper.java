package com.daking.app.sample.mgr.sql;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * 创建数据库
 * Created by daking on 15/11/8.
 */
public class DBOpenHelper extends SQLiteOpenHelper {

    protected final String TAG = this.getClass().getSimpleName(); // 日志输出标志

    private static final String DB_NAME = "sample.db"; //数据库名称
    private static final int DB_VERSION = 4; //数据库版本

    public DBOpenHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE account_table(id INTEGER PRIMARY KEY AUTOINCREMENT,name VARCHAR(20)) ");
        Log.i(TAG, TAG + " onCreate()");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // 不断升级
        for (int i = oldVersion + 1; i <= newVersion; i++) {
            switch (i) {
                case 2:
                    db.execSQL("CREATE TABLE setting_table(sid INTEGER PRIMARY KEY AUTOINCREMENT,sName VARCHAR(20)) ");
                    Log.i(TAG, TAG + " onUpgrade() 1->2");
                    break;
                case 3:
                    db.execSQL("CREATE TABLE address_table(aId INTEGER PRIMARY KEY AUTOINCREMENT,aName VARCHAR(20)) ");
                    Log.i(TAG, TAG + " onUpgrade() 2->3");
                    break;
                case 4:
                    db.execSQL("CREATE TABLE order_table(oId INTEGER PRIMARY KEY AUTOINCREMENT,oName VARCHAR(20)) ");
                    Log.i(TAG, TAG + " onUpgrade() 3->4");
                    break;
            }
        }
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // 不断降价,依次对之前每个版本的升级进行回滚
        for (int i = oldVersion; i > newVersion; i--) {
            switch (i) {
                case 4:
                    db.execSQL("DROP TABLE order_table");
                    Log.i(TAG, TAG + " onDowngrade() 4->3");
                    break;
                case 3:
                    db.execSQL("DROP TABLE address_table");
                    Log.i(TAG, TAG + " onDowngrade() 3->2");
                    break;
                case 2:
                    db.execSQL("DROP TABLE setting_table");
                    Log.i(TAG, TAG + " onDowngrade() 2->1");
                    break;
            }
        }
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);

        Log.i(TAG, TAG + " onOpen()");
    }
}
