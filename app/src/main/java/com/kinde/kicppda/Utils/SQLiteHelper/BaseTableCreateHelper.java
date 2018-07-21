package com.kinde.kicppda.Utils.SQLiteHelper;

/**
 * Created by Lenovo on 2018/7/14.
 */

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.kinde.kicppda.Utils.Public;

/**
 * 基础库创建
 */
public class BaseTableCreateHelper {
    public SQLiteDatabase db;
    public DBOpenHelper DBHelper;

    public BaseTableCreateHelper(Context mContext){
        DBHelper = new DBOpenHelper(mContext );
    }

    public void BaseAgentCreate() throws Exception{
        try {
            //判断这张表是否存在，若存在，则跳过创建表操作
            String CREATE_BaseAgent_TABLE = "CREATE TABLE IF NOT EXISTS '" + Public.B_CUSTOMER_File + "'("
                    + "AgentId text primary key, "
                    + "EnCode text, "
                    + "FullName text )";
            db = DBHelper.getReadableDatabase();
            db.execSQL(CREATE_BaseAgent_TABLE);
        }catch (Exception ex){
            throw new Exception(ex.getMessage());
        }
        finally {
            db.close();
        }
    }

    public void BaseProductCreate() throws Exception{
        try {
            //判断这张表是否存在，若存在，则跳过创建表操作
            String CREATE_BaseProduct_TABLE = "CREATE TABLE IF NOT EXISTS '" + Public.B_INVENTORY_File + "'("
                    + "ProductId text primary key, "
                    + "EnCode text, "
                    + "ProductName text )";
            db = DBHelper.getReadableDatabase();
            db.execSQL(CREATE_BaseProduct_TABLE);
        }catch (Exception ex){
            throw new Exception(ex.getMessage());
        }
        finally {
            db.close();
        }
    }

    public void BaseWarehouseCreate() throws Exception{
        try {
            //判断这张表是否存在，若存在，则跳过创建表操作
            String CREATE_BaseAgent_TABLE = "CREATE TABLE IF NOT EXISTS '" + Public.B_WAREHOUSE_File + "'("
                    + "WarehouseId text primary key, "
                    + "EnCode text, "
                    + "FullName text )";
            db = DBHelper.getReadableDatabase();
            db.execSQL(CREATE_BaseAgent_TABLE);
        }catch (Exception ex){
            throw new Exception(ex.getMessage());
        }
        finally {
            db.close();
        }
    }
}
