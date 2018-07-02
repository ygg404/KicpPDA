package com.kinde.kicppda.BillingActivity;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.kinde.kicppda.Utils.SQLiteHelper.DBOpenHelper;

import java.util.Arrays;

/**
 * Created by YGG on 2018/6/30.
 */

public class DeleteBillHelper {

    public final String IN_MAIN_TABLE = "inMainBill";           //入库主单
    public final String ORDER_MAIN_TABLE = "orderMainBill";     //订单主单
    public final String RETURN_MAIN_TABLE = "returnMainBill";   //退货主单
    public final String ALLOT_MAIN_TABLE = "allotMainBill";     //调拨主单
    public final String CHECK_MAIN_TABLE = "checkMainBill";     //盘点主单

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
        String[] MainBill = {IN_MAIN_TABLE,ORDER_MAIN_TABLE,RETURN_MAIN_TABLE,ALLOT_MAIN_TABLE,CHECK_MAIN_TABLE };

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
}
