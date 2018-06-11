package com.kinde.kicppda.Utils.SQLiteHelper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import static android.content.ContentValues.TAG;

/**
 * Created by YGG on 2018/6/9.
 */

public class DBOpenHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "kicp_pda.db";//数据库名字
    private static final int DATABASE_VERSION = 1;//数据库版本号


    public DBOpenHelper(Context context) {
        this(context, DATABASE_NAME, null, DATABASE_VERSION);
        Log.d(TAG,"New CustomSQLiteOpenHelper First");
    }

    private DBOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);//调用到SQLiteOpenHelper中
        Log.d(TAG,"New CustomSQLiteOpenHelper");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            db.execSQL(SqlTableCreate.CREATE_IN_MAIN_TABLE);
            db.execSQL(SqlTableCreate.CREATE_ORDER_MAIN_TABLE);
            db.execSQL(SqlTableCreate.CREATE_RETURN_MAIN_TABLE);
            db.execSQL(SqlTableCreate.CREATE_ALLOT_MAIN_TABLE);
            db.execSQL(SqlTableCreate.CREATE_CHECK_MAIN_TABLE);
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}

