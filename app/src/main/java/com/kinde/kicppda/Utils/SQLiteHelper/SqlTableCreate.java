package com.kinde.kicppda.Utils.SQLiteHelper;

/**
 * Created by YGG on 2018/6/9.
 */

import android.database.sqlite.SQLiteDatabase;

import com.kinde.kicppda.Utils.Public;

public class SqlTableCreate {
    //入库主单表
    public static final String CREATE_IN_MAIN_TABLE = "create table inMainBill ("
            + "GodownId text primary key, "
            + "GodownCode text, "
            + "GodownDate text, "
            + "WarehouseId text, "
            + "WarehouseName text, "
            + "Description text, "
            + "CreateDate text, "
            + "CreateUserId text, "
            + "Status smallint)";
    //出库主单表
    public static final String CREATE_ORDER_MAIN_TABLE = "create table orderMainBill ("
            + "OrderId text primary key, "
            + "OrderCode text, "
            + "OrderDate text, "
            + "AgentId text, "
            + "AgentName text, "
            + "Description text, "
            + "CreateDate text, "
            + "CreateUserId text, "
            + "Status smallint)";
    //退货主单表
    public static final String CREATE_RETURN_MAIN_TABLE = "create table returnMainBill ("
            + "ReturnId text primary key, "
            + "ReturnCode text, "
            + "ReturnDate text, "
            + "WarehouseId text, "
            + "WarehouseName text, "
            + "Description text, "
            + "CreateDate text, "
            + "CreateUserId text, "
            + "Status smallint)";
    //调拨主单表
    public static final String CREATE_ALLOT_MAIN_TABLE = "create table allotMainBill ("
            + "AllotId text primary key, "
            + "AllotCode text, "
            + "AllotDate text, "
            + "WarehouseIdOut text, "
            + "WarehouseIdIn text, "
            + "WarehouseNameOut text, "
            + "WarehouseNameIn text, "
            + "Description text, "
            + "CreateDate text, "
            + "CreateUserId text, "
            + "Status smallint)";
    //盘点主单表
    public static final String CREATE_CHECK_MAIN_TABLE = "create table checkMainBill ("
            + "CheckId text primary key, "
            + "CheckCode text, "
            + "CheckDate text, "
            + "WarehouseId text, "
            + "WarehouseName text, "
            + "Description text, "
            + "CreateDate text, "
            + "CreateUserId text, "
            + "CreateUserName text, "
            + "Status smallint)";
    //关联箱主单表
    public static final String CREATE_GROUPX_MAIN_TABLE = "create table groupxMainBill ("
            + "GodownXId text primary key, "
            + "GodownXCode text, "
            + "GodownXDate text, "
            + "Description text, "
            + "CreateDate text, "
            + "CreateUserId text, "
            + "CreateUserName text, "
            + "Status smallint)";

    //创建入库明细表
    public static Boolean In_Billing_Create(SQLiteDatabase db , String billNo) throws Exception{
        String EntryFileName = billNo + Public.GodownBillingType;
        String CREATE_IN_BILL_TABLE = "create table '" + EntryFileName + "'("
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
                + "CreateUserId text, "
                + "CreateUserName text)";

        try {
            db.execSQL("drop table if exists '" + EntryFileName + "'");
            db.execSQL(CREATE_IN_BILL_TABLE);
        }catch (Exception ex){
            throw ex;

        }
        return true;
    }

    //创建出库明细表
    public static Boolean Order_Billing_Create(SQLiteDatabase db , String billNo) throws Exception{
        String EntryFileName = billNo + Public.OrderBillingType;
        String CREATE_ORDER_BILL_TABLE = "create table '" + EntryFileName + "'("
                + "id integer primary key autoincrement,"
                + "OrderBillingId text, "
                + "OrderId text, "
                + "ProductId text, "
                + "ProductName text, "
                + "EnCode text, "
                + "Qty text, "
                + "QtyFact text, "
                + "SingleBoxPerBigBox text, "
                + "SinglePerBox text, "
                + "CreateUserId text, "
                + "CreateUserName text)";

        try {
            db.execSQL("drop table if exists '" + EntryFileName + "'");
            db.execSQL(CREATE_ORDER_BILL_TABLE);
        }catch (Exception ex){
            throw ex;

        }
        return true;
    }

    //创建退货明细表
    public static Boolean Return_Billing_Create(SQLiteDatabase db , String billNo) throws Exception{
        String EntryFileName = billNo + Public.ReturnBillingType;
        String CREATE_RETURN_BILL_TABLE = "create table '" + EntryFileName + "'("
                + "id integer primary key autoincrement,"
                + "ReturnBillingId text, "
                + "ReturnId text, "
                + "ProductId text, "
                + "ProductName text, "
                + "EnCode text, "
                + "Qty text, "
                + "QtyFact text, "
                + "SingleBoxPerBigBox text, "
                + "SinglePerBox text, "
                + "CreateUserId text, "
                + "CreateUserName text)";

        try {
            db.execSQL("drop table if exists '" + EntryFileName + "'");
            db.execSQL(CREATE_RETURN_BILL_TABLE);
        }catch (Exception ex){
            throw ex;

        }
        return true;
    }

    //创建调拨明细表
    public static Boolean Allot_Billing_Create(SQLiteDatabase db , String billNo) throws Exception{
        String EntryFileName = billNo + Public.AllotBillingType;
        String CREATE_ALLOT_BILL_TABLE = "create table '" + EntryFileName + "'("
                + "id integer primary key autoincrement,"
                + "AllotBillingId text, "
                + "AllotId text, "
                + "ProductId text, "
                + "ProductName text, "
                + "EnCode text, "
                + "Qty text, "
                + "QtyFact text, "
                + "SingleBoxPerBigBox text, "
                + "SinglePerBox text, "
                + "LN text, "
                + "PR text, "
                + "CreateUserId text, "
                + "CreateUserName text)";

        try {
            db.execSQL("drop table if exists '" + EntryFileName + "'");
            db.execSQL(CREATE_ALLOT_BILL_TABLE);
        }catch (Exception ex){
            throw ex;

        }
        return true;
    }

    //创建关联箱明细表
    public static Boolean GroupX_Billing_Create(SQLiteDatabase db , String billNo) throws Exception{
        String EntryFileName = billNo + Public.GroupXBillingType;
        String CREATE_ALLOT_BILL_TABLE = "create table '" + EntryFileName + "'("
                + "id integer primary key autoincrement,"
                + "GodownXBillingId text, "
                + "GodownXId text, "
                + "ProductId text, "
                + "ProductName text, "
                + "EnCode text, "
                + "LN text, "
                + "PR text, "
                + "Qty text, "
                + "QtyFact text, "
                + "SingleBoxPerBigBox text, "
                + "SinglePerBox text, "
                + "CreateUserId text, "
                + "CreateUserName text)";
        try {
            db.execSQL("drop table if exists '" + EntryFileName + "'");
            db.execSQL(CREATE_ALLOT_BILL_TABLE);
        }catch (Exception ex){
            throw ex;
        }
        return true;
    }

}
