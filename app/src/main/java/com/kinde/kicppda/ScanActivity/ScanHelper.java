package com.kinde.kicppda.ScanActivity;

/**
 * Created by YGG on 2018/7/2.
 */

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.kinde.kicppda.Utils.Public;
import com.kinde.kicppda.Utils.SQLiteHelper.DBOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * 获取单据号
 */
public class ScanHelper {
    public SQLiteDatabase db;
    public DBOpenHelper DBHelper;

    public ScanHelper(Context mContext){
        DBHelper = new DBOpenHelper(mContext );
    }

    public List<String> getBillNum(String TableName){
        List<String> ScanBillingNum = new ArrayList();
        db = DBHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery("select GodownCode from '"+ TableName +"'" ,null);
        while(cursor.moveToNext()){
            //遍历出列名
            String name = cursor.getString(0);
            ScanBillingNum.add(name);
        }
        return ScanBillingNum;
    }

    public String getKeyValue(String colName ,String value){
        String keyValue = null;
        Cursor cursor = db.rawQuery("select "+ colName +" from '"+ Public.IN_MAIN_TABLE
                                    +"' where GodownCode = '" + value +"'" ,null);
        while(cursor.moveToNext()) {
            //遍历出键值
            keyValue = cursor.getString(0);
        }
        return keyValue;
    }

    public List<String[]> getProductInfo(String TableName ,String value){
        List<String[]> BillingInfoList = new ArrayList();
        String Sqlstr = "select EnCode,ProductName,LN,PR from '"+ TableName +"' Where EnCode like '%"+ value +"%'";
        Cursor cursor = db.rawQuery(Sqlstr , null);
        while(cursor.moveToNext()) {
            //遍历出键值
            String[] keyValue = new String[4];
            keyValue[0] = cursor.getString(0);
            keyValue[1] = cursor.getString(1);
            keyValue[2] = cursor.getString(2);
            keyValue[3] = cursor.getString(3);
            BillingInfoList.add(keyValue);
        }
        return BillingInfoList;
    }
}
