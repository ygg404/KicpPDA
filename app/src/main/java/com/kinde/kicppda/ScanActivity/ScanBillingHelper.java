package com.kinde.kicppda.ScanActivity;

/**
 * Created by YGG on 2018/7/10.
 */

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.kinde.kicppda.Utils.SQLiteHelper.DBOpenHelper;

import java.text.SimpleDateFormat;

/**
 * 保存扫码明细
 */
public class ScanBillingHelper {
    public SQLiteDatabase db;
    public DBOpenHelper DBHelper;

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");


    public ScanBillingHelper(Context mContext){
        DBHelper = new DBOpenHelper(mContext );
    }

    public void GodownScanSave(String tableName ,String[] insertData ) throws Exception{
        try{
            db = DBHelper.getWritableDatabase();
            //保存主单之前删除相同单据号的主单据
            db.execSQL("insert into '"+ tableName+
                    "'(SerialNo,ProductId,LN,PR,Qty,CreateDate,CreateUserId) " +"values('"+
                    insertData[0]+"','"+ insertData[1]+"','"+ insertData[2] + "','" +
                    insertData[3]+"','"+ insertData[4]+"','"+ insertData[5]+"','"+
                    insertData[6]+"')"
            );
        }catch (Exception ex){
            throw new Exception(ex.getMessage());
        }
    }

    public void OrderScanSave(String tableName ,String[] insertData ) throws Exception{
        try{
            db = DBHelper.getWritableDatabase();
            //保存主单之前删除相同单据号的主单据
            db.execSQL("insert into '"+ tableName+
                    "'(SerialNo,ProductId,Qty,CreateDate,CreateUserId) " +"values('"+
                    insertData[0]+"','"+ insertData[1]+"','"+ insertData[2] + "','" +
                    insertData[3]+"','"+ insertData[4]+"')"
            );
        }catch (Exception ex){
            throw new Exception(ex.getMessage());
        }
    }

    public void ReturnScanSave(String tableName ,String[] insertData ) throws Exception{
        try{
            db = DBHelper.getWritableDatabase();
            //保存主单之前删除相同单据号的主单据
            db.execSQL("insert into '"+ tableName+
                    "'(SerialNo,ProductId,Qty,CreateDate,CreateUserId) " +"values('"+
                    insertData[0]+"','"+ insertData[1]+"','"+ insertData[2] + "','" +
                    insertData[3]+"','"+ insertData[4]+"')"
            );
        }catch (Exception ex){
            throw new Exception(ex.getMessage());
        }
    }

    public void AllotScanSave(String tableName ,String[] insertData ) throws Exception{
        try{
            db = DBHelper.getWritableDatabase();
            //保存主单之前删除相同单据号的主单据
            db.execSQL("insert into '"+ tableName+
                    "'(SerialNo,ProductId,Qty,CreateDate,CreateUserId) " +"values('"+
                    insertData[0]+"','"+ insertData[1]+"','"+ insertData[2] + "','" +
                    insertData[3]+"','"+ insertData[4]+"')"
            );
        }catch (Exception ex){
            throw new Exception(ex.getMessage());
        }
    }

    public void GodownXScanSave(String tableName ,String[] insertData ) throws Exception{
        try{
            db = DBHelper.getWritableDatabase();
            //保存主单之前删除相同单据号的主单据
            db.execSQL("insert into '"+ tableName+
                    "'(SerialNo,ProductId,LN,PR,GroupNo,CreateDate,CreateUserId) " +"values('"+
                    insertData[0]+"','"+ insertData[1]+"','"+ insertData[2] + "','" +
                    insertData[3]+"','"+ insertData[4]+"','"+ insertData[5] +"','"+ insertData[6] + "')"
            );
        }catch (Exception ex){
            throw new Exception(ex.getMessage());
        }
    }
}
