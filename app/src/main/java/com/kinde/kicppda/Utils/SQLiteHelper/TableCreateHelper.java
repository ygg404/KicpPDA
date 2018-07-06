package com.kinde.kicppda.Utils.SQLiteHelper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.kinde.kicppda.Utils.Public;

/**
 * Created by Lenovo on 2018/7/4.
 */

public class TableCreateHelper {
    public SQLiteDatabase db;
    public DBOpenHelper DBHelper;

    public TableCreateHelper(Context mContext){
        DBHelper = new DBOpenHelper(mContext );
    }

    /**
     * 创建入库扫码表
     * @return
     */
    public boolean Godown_Scan_Create(String billNo){
        try {
            String ScanFileName = billNo + Public.GodownScanType;
            //判断这张表是否存在，若存在，则跳过创建表操作
            String CREATE_GODOWN_SCAN_TABLE = "CREATE TABLE IF NOT EXISTS '" + ScanFileName + "'("
                    + "id integer primary key autoincrement,"
                    + "SerialNo text, "
                    + "ProductId text, "
                    + "LN text, "
                    + "PR text, "
                    + "Qty text, "
                    + "ScanTag text, "
                    + "CreateDate text, "
                    + "CreateUserId text )";
            db = DBHelper.getReadableDatabase();
            db.execSQL(CREATE_GODOWN_SCAN_TABLE);
        }catch (Exception ex){
            return false;
        }
        finally {
            db.close();
        }
        return true;
    }

    /**
     * 创建出库扫码表
     * @return
     */
    public boolean Order_Scan_Create(String billNo){
        try {
            String ScanFileName = billNo + Public.OrderScanType;
            //判断这张表是否存在，若存在，则跳过创建表操作
            String CREATE_ORDER_SCAN_TABLE = "CREATE TABLE IF NOT EXISTS '" + ScanFileName + "'("
                    + "id integer primary key autoincrement,"
                    + "SerialNo text, "
                    + "ProductId text, "
                    + "Qty text, "
                    + "ScanTag text, "
                    + "CreateDate text, "
                    + "CreateUserId text )";
            db = DBHelper.getReadableDatabase();
            db.execSQL(CREATE_ORDER_SCAN_TABLE);
        }catch (Exception ex){
            return false;
        }
        finally {
            db.close();
        }
        return true;
    }

    /**
     * 创建退货扫码表
     * @return
     */
    public boolean Return_Scan_Create(String billNo){
        try {
            String ScanFileName = billNo + Public.ReturnScanType;
            //判断这张表是否存在，若存在，则跳过创建表操作
            String CREATE_RETURN_SCAN_TABLE = "CREATE TABLE IF NOT EXISTS '" + ScanFileName + "'("
                    + "id integer primary key autoincrement,"
                    + "SerialNo text, "
                    + "ProductId text, "
                    + "Qty text, "
                    + "ScanTag text, "
                    + "CreateDate text, "
                    + "CreateUserId text )";
            db = DBHelper.getReadableDatabase();
            db.execSQL(CREATE_RETURN_SCAN_TABLE);
        }catch (Exception ex){
            return false;
        }
        finally {
            db.close();
        }
        return true;
    }

    /**
     * 创建调拨扫码表
     * @return
     */
    public boolean Allot_Scan_Create(String billNo){
        try {
            String ScanFileName = billNo + Public.AllotScanType;
            //判断这张表是否存在，若存在，则跳过创建表操作
            String CREATE_ALLOT_SCAN_TABLE = "CREATE TABLE IF NOT EXISTS '" + ScanFileName + "'("
                    + "id integer primary key autoincrement,"
                    + "SerialNo text, "
                    + "ProductId text, "
                    + "Qty text, "
                    + "ScanTag text, "
                    + "CreateDate text, "
                    + "CreateUserId text )";
            db = DBHelper.getReadableDatabase();
            db.execSQL(CREATE_ALLOT_SCAN_TABLE);
        }catch (Exception ex){
            return false;
        }
        finally {
            db.close();
        }
        return true;
    }
}
