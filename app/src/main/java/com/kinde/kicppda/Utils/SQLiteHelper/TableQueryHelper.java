package com.kinde.kicppda.Utils.SQLiteHelper;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.kinde.kicppda.Models.AllotBillingEntity;
import com.kinde.kicppda.Models.GodownBillingEntity;
import com.kinde.kicppda.Models.OrderBillingEntity;
import com.kinde.kicppda.Models.ReturnBillingEntity;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by YGG on 2018/7/4.
 */

public class TableQueryHelper {

    public SQLiteDatabase db;
    public DBOpenHelper DBHelper;

    public TableQueryHelper(Context mContext){
        DBHelper = new DBOpenHelper(mContext );
    }

    /**
     * 扫码单查询
     * @param TableName
     * @return
     */
    public List<String[]> ScanQuery(String TableName){
        List<String[]> ScanInfoList = new ArrayList();
        try {
            String Sqlstr = "select SerialNo,ProductId,Qty,ScanTag from '" + TableName + "'";
            db = DBHelper.getReadableDatabase();
            Cursor cursor = db.rawQuery(Sqlstr, null);
            while (cursor.moveToNext()) {
                //遍历出键值
                String[] keyValue = new String[4];
                keyValue[0] = cursor.getString(0);
                keyValue[1] = cursor.getString(1);
                keyValue[2] = cursor.getString(2);
                keyValue[3] = cursor.getString(3);
                ScanInfoList.add(keyValue);
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }finally {
            db.close();
        }
        return ScanInfoList;
    }

    /**
     * 获取单据号列表
     * @param TableName
     * @param keyName
     * @return
     */
    public List<String> getBillNum(String TableName , String keyName){
        List<String> ScanBillingNum = new ArrayList();
        try {
            db = DBHelper.getReadableDatabase();
            Cursor cursor = db.rawQuery("select " + keyName + " from '" + TableName + "'", null);
            while (cursor.moveToNext()) {
                //遍历出列名
                String name = cursor.getString(0);
                ScanBillingNum.add(name);
            }
        }catch (Exception ex){
            ex.printStackTrace();;
        }finally {
            db.close();
        }
        return ScanBillingNum;
    }

    /**
     * 获取产品信息
     * @param TableName
     * @param value
     * @return
     */
    public List<String[]> getProductInfo(String TableName ,String value){
        List<String[]> BillingInfoList = new ArrayList();
        try {
            String Sqlstr = "select ProductId,EnCode,ProductName,LN,PR from '" + TableName + "' Where EnCode like '%" + value + "%'";
            db = DBHelper.getReadableDatabase();
            Cursor cursor = db.rawQuery(Sqlstr, null);
            while (cursor.moveToNext()) {
                //遍历出键值
                String[] keyValue = new String[5];
                keyValue[0] = cursor.getString(0);
                keyValue[1] = cursor.getString(1);
                keyValue[2] = cursor.getString(2);
                keyValue[3] = cursor.getString(3);
                keyValue[4] = cursor.getString(4);
                BillingInfoList.add(keyValue);
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }finally {
            db.close();
        }
        return BillingInfoList;
    }

    /**
     * 获取产品信息
     * @param TableName
     * @param value
     * @return
     */
    public List<String[]> getProductMessage(String TableName ,String value){
        List<String[]> BillingInfoList = new ArrayList();
        try {
            String Sqlstr = "select ProductId,EnCode,ProductName from '" + TableName + "' Where EnCode like '%" + value + "%'";
            db = DBHelper.getReadableDatabase();
            Cursor cursor = db.rawQuery(Sqlstr, null);
            while (cursor.moveToNext()) {
                //遍历出键值
                String[] keyValue = new String[5];
                keyValue[0] = cursor.getString(0);
                keyValue[1] = cursor.getString(1);
                keyValue[2] = cursor.getString(2);
                BillingInfoList.add(keyValue);
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }finally {
            db.close();
        }
        return BillingInfoList;
    }

    /**
     * 查询某列值
     * @param colName
     * @param TableName
     * @param key
     * @param keyValue
     * @return
     */
    public String getKeyValue(String colName , String TableName , String  key , String keyValue ){
        String Value = null;
        try {
            db = DBHelper.getReadableDatabase();
            Cursor cursor = db.rawQuery("select " + colName + " from '" + TableName
                    + "' where "+ key + " = '" + keyValue + "'", null);
            while (cursor.moveToNext()) {
                //遍历出键值
                Value = cursor.getString(0);
            }
        }catch (Exception ex){
            ;
        }finally {
            db.close();
        }
        return Value;
    }

    /**
     * 入库明细查询
     * @param EntryFileName
     * @return
     */
    public List<GodownBillingEntity> queryGodownBilling(String EntryFileName) {
        List<GodownBillingEntity> gBillingList = new ArrayList<GodownBillingEntity>();
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try {
            String Sqlstr = "Select GodownBillingId,GodownId,ProductId,ProductName,EnCode,LN,PR,Qty,QtyFact from '"
                    + EntryFileName +"'";
            db = DBHelper.getReadableDatabase();
            Cursor cursor = db.rawQuery(Sqlstr, null);
            while (cursor.moveToNext()) {
                //遍历出键值
                GodownBillingEntity entity = new GodownBillingEntity();
                entity.GodownBillingId = cursor.getString(0);
                entity.GodownId = cursor.getString(1);
                entity.ProductId = cursor.getString(2);
                entity.ProductName = cursor.getString(3);
                entity.EnCode = cursor.getString(4);
                entity.LN = cursor.getString(5);
                entity.PR = cursor.getString(6)==null?null:format.parse(cursor.getString(6));
                entity.Qty = Integer.parseInt( cursor.getString(7));
                entity.QtyFact = Integer.parseInt( cursor.getString(8));

                gBillingList.add(entity);
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
        finally {
            db.close();
        }
        return gBillingList;
    }

    /**
     * 出库明细表查询
     * @param EntryFileName
     * @return
     */
    public List<OrderBillingEntity> queryOrderBilling(String EntryFileName){
        List<OrderBillingEntity> oBillingList = new ArrayList<OrderBillingEntity>();
        try {
            String Sqlstr = "Select OrderBillingId,OrderId,ProductId,ProductName,EnCode,Qty,QtyFact from '"
                    + EntryFileName +"'";
            db = DBHelper.getReadableDatabase();
            Cursor cursor = db.rawQuery(Sqlstr, null);
            while (cursor.moveToNext()) {
                //遍历出键值
                OrderBillingEntity entity = new OrderBillingEntity();
                entity.OrderBillingId = cursor.getString(0);
                entity.OrderId = cursor.getString(1);
                entity.ProductId = cursor.getString(2);
                entity.ProductName = cursor.getString(3);
                entity.EnCode = cursor.getString(4);
                entity.Qty = Integer.parseInt( cursor.getString(5));
                entity.QtyFact = Integer.parseInt( cursor.getString(6));

                oBillingList.add(entity);
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
        finally {
            db.close();
        }
        return oBillingList;
    }

    /**
     * 退货明细表查询
     * @param EntryFileName
     * @return
     */
    public List<ReturnBillingEntity> queryReturnBilling(String EntryFileName){
        List<ReturnBillingEntity> rBillingList = new ArrayList<ReturnBillingEntity>();
        try {
            String Sqlstr = "Select ReturnBillingId,ReturnId,ProductId,ProductName,EnCode,Qty,QtyFact from '"
                    + EntryFileName +"'";
            db = DBHelper.getReadableDatabase();
            Cursor cursor = db.rawQuery(Sqlstr, null);
            while (cursor.moveToNext()) {
                //遍历出键值
                ReturnBillingEntity entity = new ReturnBillingEntity();
                entity.ReturnBillingId = cursor.getString(0);
                entity.ReturnId = cursor.getString(1);
                entity.ProductId = cursor.getString(2);
                entity.ProductName = cursor.getString(3);
                entity.EnCode = cursor.getString(4);
                entity.Qty = Integer.parseInt( cursor.getString(5));
                entity.QtyFact = Integer.parseInt( cursor.getString(6));

                rBillingList.add(entity);
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
        finally {
            db.close();
        }
        return rBillingList;
    }

    /**
     * 调拨明细表查询
     * @param EntryFileName
     * @return
     */
    public List<AllotBillingEntity> queryAllotBilling(String EntryFileName){
        List<AllotBillingEntity> aBillingList = new ArrayList<AllotBillingEntity>();
        try {
            String Sqlstr = "Select AllotBillingId,AllotId,ProductId,ProductName,EnCode,Qty,QtyFact from '"
                    + EntryFileName +"'";
            db = DBHelper.getReadableDatabase();
            Cursor cursor = db.rawQuery(Sqlstr, null);
            while (cursor.moveToNext()) {
                //遍历出键值
                AllotBillingEntity entity = new AllotBillingEntity();
                entity.AllotBillingId = cursor.getString(0);
                entity.AllotId = cursor.getString(1);
                entity.ProductId = cursor.getString(2);
                entity.ProductName = cursor.getString(3);
                entity.EnCode = cursor.getString(4);
                entity.Qty = Integer.parseInt( cursor.getString(5));
                entity.QtyFact = Integer.parseInt( cursor.getString(6));

                aBillingList.add(entity);
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
        finally {
            db.close();
        }
        return aBillingList;
    }
}
