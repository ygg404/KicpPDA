package com.kinde.kicppda.Utils.SQLiteHelper;

/**
 * Created by YGG on 2018/6/9.
 */

import android.database.sqlite.SQLiteDatabase;

public class SqlTableCreate {
    //入库主单表
    public static final String CREATE_IN_MAIN_TABLE = "create table inMainBill ("
            + "id integer primary key autoincrement,"
            + "GodownId text, "
            + "GodownCode text, "
            + "GodownDate text, "
            + "WarehouseId text, "
            + "WarehouseName text, "
            + "Description text, "
            + "CreateDate text, "
            + "CreateUserId text, "
            + "Status text)";
    //出库主单表
    public static final String CREATE_ORDER_MAIN_TABLE = "create table orderMainBill ("
            + "id integer primary key autoincrement,"
            + "OrderId text, "
            + "OrderCode text, "
            + "OrderDate text, "
            + "AgentId text, "
            + "AgentName text, "
            + "Description text, "
            + "CreateDate text, "
            + "CreateUserId text, "
            + "Status text)";
    //退货主单表
    public static final String CREATE_RETURN_MAIN_TABLE = "create table returnMainBill ("
            + "id integer primary key autoincrement,"
            + "ReturnId text, "
            + "ReturnCode text, "
            + "ReturnDate text, "
            + "WarehouseId text, "
            + "WarehouseName text, "
            + "Description text, "
            + "CreateDate text, "
            + "CreateUserId text, "
            + "Status text)";
    //调拨主单表
    public static final String CREATE_ALLOT_MAIN_TABLE = "create table allotMainBill ("
            + "id integer primary key autoincrement,"
            + "AllotId text, "
            + "AllotCode text, "
            + "AllotDate text, "
            + "WarehouseIdOut text, "
            + "WarehouseIdIn text, "
            + "WarehouseNameOut text, "
            + "WarehouseNameIn text, "
            + "Description text, "
            + "CreateDate text, "
            + "CreateUserId text, "
            + "Status text)";
    //盘点主单表
    public static final String CREATE_CHECK_MAIN_TABLE = "create table checkMainBill ("
            + "id integer primary key autoincrement,"
            + "CheckId text, "
            + "CheckCode text, "
            + "CheckDate text, "
            + "WarehouseId text, "
            + "WarehouseName text, "
            + "Description text, "
            + "CreateDate text, "
            + "CreateUserId text, "
            + "Status text)";

    //创建入库明细表
    public static Boolean In_Billing_Create(SQLiteDatabase db , String billNo){
        String EntryFileName = billNo + "-Billing";
        String CREATE_IN_BILL_TABLE = "drop table if exists " + EntryFileName + ";"
            +"create table " + EntryFileName + "("
                + "id integer primary key autoincrement,"
                + "GodownBillingId text, "
                + "GodownId text, "
                + "ProductId text, "
                + "ProductName text, "
                + "EnCode text, "
                + "LN text, "
                + "PR text, "
                + "Qty text, "
                + "QtyFact text, "
                + "SinglePerBox text, "
                + "SingleBoxPerBigBox text, "
                + "CreateDate text, "
                + "CreateUserId text, "
                + "CreateUserName text, "
                + "Status text)";

        try {
            db.execSQL(CREATE_IN_BILL_TABLE);
        }catch (Exception ex){
            return false;
        }
        return true;
    }

    //创建入库采集表
    public static Boolean In_Scan_Create(SQLiteDatabase db , String billNo) {
        String ScanFileName = billNo + "-Scan";
        //判断这张表是否存在，若存在，则跳过创建表操作
        String CREATE_IN_SCAN_TABLE = "CREATE TABLE IF NOT EXISTS " + ScanFileName + "("
                + "id integer primary key autoincrement,"
                + "barcode text, "
                + "productId text, "
                + "ln text, "
                + "pr text, "
                + "Qty text, "
                + "CreateDate text, "
                + "CreateUserId text )";
        try {
            db.execSQL(CREATE_IN_SCAN_TABLE);
        } catch (Exception ex) {
            return false;
        }
        return true;
    }
}
