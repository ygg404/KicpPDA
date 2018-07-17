package com.kinde.kicppda.Utils.SQLiteHelper;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.kinde.kicppda.Utils.Public;

import java.util.Arrays;

/**
 * Created by YGG on 2018/6/30.
 */

public class DeleteBillHelper {

    public SQLiteDatabase db;
    public DBOpenHelper DBHelper;

    public DeleteBillHelper(Context mContext){
        DBHelper = new DBOpenHelper(mContext );
    }

    /**
     * 删除所有表
     * @return
     */
    public boolean DeleteAllDataFile(){
        String[] MainBill = { Public.GODOWN_MAIN_TABLE,
                              Public.ORDER_MAIN_TABLE,
                              Public.RETURN_MAIN_TABLE,
                              Public.ALLOT_MAIN_TABLE,
                              Public.CHECK_MAIN_TABLE,
                              Public.GodownX_MAIN_TABLE};

        try {
            db = DBHelper.getReadableDatabase();
            Cursor cursor = db.rawQuery("select name from sqlite_master where type='table' order by name", null);
            while(cursor.moveToNext()){
                //遍历出表名
                String name = cursor.getString(0);
                if( !name.equals("sqlite_sequence")) {
                    //主单则删除所有数据
                    if (Arrays.asList(MainBill).contains( name ) ) {
                        db.execSQL("Delete From '" + name + "'");
                        continue;
                    }
                    db.execSQL("DROP TABLE '" + name + "'");
                }
            }
        }catch (Exception ex){
            return false;
        }
        finally {
            db.close();
        }
        return true;
    }

    /**
     * 删除指定的表
     */
    public boolean DeleteFile(String TableName) {
        try {
            db = DBHelper.getReadableDatabase();
            String sqlStr = "DROP TABLE if exists '"+ TableName +"'";
            db.execSQL(sqlStr);
        }
        catch (Exception ex){
            return false;
        }
        finally {
            db.close();
        }
        return true;
    }

    /**
     * 删除表内某一条数据
     * @param TableName
     * @param key
     * @param keyValue
     * @return
     */
    public boolean DeleteTheData(String TableName , String key , String keyValue){
        try {
            db = DBHelper.getReadableDatabase();
            String sqlStr = "Delete from '"+ TableName +"'"
                    + " where "+ key + "= '"+ keyValue +"'";
            db.execSQL(sqlStr);
        }
        catch (Exception ex){
            return false;
        }
        finally {
            db.close();
        }
        return true;
    }
}
