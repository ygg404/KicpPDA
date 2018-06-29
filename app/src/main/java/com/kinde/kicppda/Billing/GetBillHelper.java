package com.kinde.kicppda.Billing;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.kinde.kicppda.Models.AllotBillingEntity;
import com.kinde.kicppda.Models.AllotEntity;
import com.kinde.kicppda.Models.CheckEntity;
import com.kinde.kicppda.Models.GodownBillingEntity;
import com.kinde.kicppda.Models.GodownEntity;
import com.kinde.kicppda.Models.OrderBillingEntity;
import com.kinde.kicppda.Models.OrderEntity;
import com.kinde.kicppda.Models.ReturnBillingEntity;
import com.kinde.kicppda.Models.ReturnEntity;
import com.kinde.kicppda.Utils.SQLiteHelper.DBOpenHelper;
import com.kinde.kicppda.Utils.SQLiteHelper.SqlTableCreate;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by Lenovo on 2018/6/12.
 */

public class GetBillHelper {

    public SQLiteDatabase db;
    public DBOpenHelper DBHelper;

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    public final String IN_MAIN_TABLE = "inMainBill";           //入库主单
    public final String ORDER_MAIN_TABLE = "orderMainBill";     //订单主单
    public final String RETURN_MAIN_TABLE = "returnMainBill";   //退货主单
    public final String ALLOT_MAIN_TABLE = "allotMainBill";     //调拨主单
    public final String CHECK_MAIN_TABLE = "checkMainBill";     //盘点主单

    public GetBillHelper(Context mContext){
        DBHelper = new DBOpenHelper(mContext );
    }
    /**
     * 保存入库主单文件
     * @return
     */
    public boolean SaveGoDownDataFile(List<GodownEntity> gEntity){
        try {
            for (GodownEntity attr : gEntity) {
                db = DBHelper.getWritableDatabase();
                db.execSQL("DELETE FROM "+ IN_MAIN_TABLE  +" WHERE GodownCode= '" + attr.GodownCode+"'");
                //保存主单之前删除相同单据号的主单据
                db.execSQL("insert into "+ IN_MAIN_TABLE+
                        "(GodownId ,GodownCode,GodownDate,WarehouseId,WarehouseName,Description,CreateDate,CreateUserId,Status) " +"values('"+
                         attr.GodownId+"','"+ attr.GodownCode+"','"+(attr.GodownDate==null?"":sdf.format(attr.GodownDate))+"','"+
                        attr.WarehouseId+"','"+attr.WarehouseName+"','"+ attr.Description+"','"+
                        (attr.CreateDate==null?"":sdf.format(attr.CreateDate))+"','"+attr.CreateUserId+"',"+String.valueOf(attr.Status)+")"
                );

                //创建入库明细表
                SqlTableCreate.In_Billing_Create(db , attr.GodownCode);


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
     * 保存入库明细单文件
     * @return
     */
    public boolean SaveGoDownBillingDataFile(String gEntityCode, List<GodownBillingEntity> gBillEntity){
        try {
            for (GodownBillingEntity attr : gBillEntity) {
                db = DBHelper.getWritableDatabase();
                //保存主单之前删除相同单据号的主单据
                db.execSQL("insert into '"+ gEntityCode + "-Billing'"+
                        "(GodownBillingId,GodownId,ProductId,ProductName,EnCode,"
                        +"LN,PR,Qty,QtyFact,SinglePerBox,SingleBoxPerBigBox,"
                        +"CreateUserId,CreateUserName)" +"values('"+
                        attr.GodownBillingId+"','"+ attr.GodownId+"','"+ attr.ProductId+"','"+attr.ProductName+"','"
                        +attr.EnCode+"','"+attr.LN+"','"+(attr.PR==null?"":sdf.format(attr.PR))+"','"
                        +attr.Qty+"','"+attr.QtyFact+"','"+attr.SinglePerBox+"','"+attr.SingleBoxPerBigBox+"','"
                        +attr.CreateUserId+"','"+attr.CreateUserName+"')"
                );
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
     * 保存出库主单文件
     * @return
     */
    public boolean SaveOrderDataFile(List<OrderEntity> oEntity){
        try {
            for (OrderEntity attr : oEntity) {
                db = DBHelper.getWritableDatabase();
                db.execSQL("DELETE FROM "+ ORDER_MAIN_TABLE  +" WHERE OrderCode= '" + attr.OrderCode+"'");
                //保存主单之前删除相同单据号的主单据
                db.execSQL("insert into "+ ORDER_MAIN_TABLE+
                        "(OrderId ,OrderCode,OrderDate,AgentId,AgentName,Description,CreateDate,CreateUserId,Status) " +"values('"+
                        attr.OrderId+"','"+ attr.OrderCode+"','"+(attr.OrderDate==null?"":sdf.format(attr.OrderDate))+"','"+
                        attr.AgentId+"','"+attr.AgentName+"','"+ attr.Description+"','"+
                        (attr.CreateDate==null?"":sdf.format(attr.CreateDate))+"','"+attr.CreateUserId+"',"+String.valueOf(attr.Status)+")"
                );
                //创建出库明细表
                SqlTableCreate.Order_Billing_Create(db , attr.OrderCode);
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
     * 保存出库明细单文件
     * @return
     */
    public boolean SaveOrderBillingDataFile(String oEntityCode, List<OrderBillingEntity> oBillEntity){
        try {
            for (OrderBillingEntity attr : oBillEntity) {
                db = DBHelper.getWritableDatabase();
                //保存主单之前删除相同单据号的主单据
                db.execSQL("insert into '"+ oEntityCode + "-Billing'"+
                        "(OrderBillingId,OrderId,ProductId,ProductName,EnCode,"
                        +"Qty,QtyFact,SinglePerBox,SingleBoxPerBigBox,"
                        +"CreateUserId,CreateUserName)" +"values('"+
                        attr.OrderBillingId+"','"+ attr.OrderId+"','"+ attr.ProductId+"','"+attr.ProductName+"','"
                        +attr.EnCode+"','" +attr.Qty+"','"+attr.SinglePerBox+"','"+attr.SingleBoxPerBigBox+"','"
                        +attr.CreateUserId+"','"+attr.CreateUserName+"')"
                );
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
     * 保存退货主单文件
     * @return
     */
    public boolean SaveReturnDataFile(List<ReturnEntity> rEntity){
        try {
            for (ReturnEntity attr : rEntity) {
                db = DBHelper.getWritableDatabase();
                db.execSQL("DELETE FROM "+ RETURN_MAIN_TABLE  +" WHERE ReturnCode= '" + attr.ReturnCode+"'");
                //保存主单之前删除相同单据号的主单据
                db.execSQL("insert into "+ RETURN_MAIN_TABLE+
                        "(ReturnId ,ReturnCode,ReturnDate,WarehouseId,WarehouseName,Description,CreateDate,CreateUserId,Status) " +"values('"+
                        attr.ReturnId+"','"+ attr.ReturnCode+"','"+(attr.ReturnDate==null?"":sdf.format(attr.ReturnDate))+"','"+
                        attr.WarehouseId+"','"+attr.WarehouseName+"','"+ attr.Description+"','"+
                        (attr.CreateDate==null?"":sdf.format(attr.CreateDate))+"','"+attr.CreateUserId+"',"+String.valueOf(attr.Status)+")"
                );
                //创建出库明细表
                SqlTableCreate.Order_Billing_Create(db , attr.ReturnCode);
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
     * 保存退货明细单文件
     * @return
     */
    public boolean SaveReturnBillingDataFile(String rEntityCode, List<ReturnBillingEntity> rBillEntity){
        try {
            for (ReturnBillingEntity attr : rBillEntity) {
                db = DBHelper.getWritableDatabase();
                //保存主单之前删除相同单据号的主单据
                db.execSQL("insert into '"+ rEntityCode + "-Billing'"+
                        "(ReturnBillingId,ReturnId,ProductId,ProductName,EnCode,"
                        +"Qty,QtyFact,SinglePerBox,SingleBoxPerBigBox,"
                        +"CreateUserId,CreateUserName)" +"values('"+
                        attr.ReturnBillingId+"','"+ attr.ReturnId+"','"+ attr.ProductId+"','"+attr.ProductName+"','"
                        +attr.EnCode+"','" +attr.Qty+"','"+attr.QtyFact+"','"+attr.SinglePerBox+"','"
                        +attr.SingleBoxPerBigBox+"','" +attr.CreateUserId+"','"+attr.CreateUserName+"')"
                );
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
     * 保存调拨主单文件
     * @return
     */
    public boolean SaveAllotDataFile(List<AllotEntity> aEntity){
        try {
            for (AllotEntity attr : aEntity) {
                db = DBHelper.getWritableDatabase();
                db.execSQL("DELETE FROM "+ ALLOT_MAIN_TABLE  +" WHERE ReturnCode= '" + attr.AllotCode+"'");
                //保存主单之前删除相同单据号的主单据
                db.execSQL("insert into "+ ALLOT_MAIN_TABLE+
                        "(AllotId ,AllotCode,AllotDate,WarehouseIdOut,WarehouseIdIn,WarehouseNameOut,WarehouseNameIn," +
                        "Description,CreateDate,CreateUserId,Status) " +"values('"+
                        attr.AllotId+"','"+ attr.AllotCode+"','"+(attr.AllotDate==null?"":sdf.format(attr.AllotDate))+"','"+
                        attr.WarehouseIdOut+"','"+attr.WarehouseIdIn+"','"+attr.WarehouseNameOut+"','"+attr.WarehouseNameIn+"','"+ attr.Description+"','"+
                        (attr.CreateDate==null?"":sdf.format(attr.CreateDate))+"','"+attr.CreateUserId+"',"+String.valueOf(attr.Status)+")"
                );
                //创建出库明细表
                SqlTableCreate.Order_Billing_Create(db , attr.AllotCode);
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
     * 保存调拨明细单文件
     * @return
     */
    public boolean SaveAllotBillingDataFile(String aEntityCode, List<AllotBillingEntity> aBillEntity){
        try {
            for (AllotBillingEntity attr : aBillEntity) {
                db = DBHelper.getWritableDatabase();
                //保存主单之前删除相同单据号的主单据
                db.execSQL("insert into '"+ aEntityCode + "-Billing'"+
                        "(AllotBillingId,AllotId,ProductId,ProductName,EnCode,"
                        +"Qty,QtyFact,SinglePerBox,SingleBoxPerBigBox,LN,PR,"
                        +"CreateUserId,CreateUserName)" +"values('"
                        +attr.AllotBillingId+"','"+ attr.AllotId+"','"+ attr.ProductId+"','"+attr.ProductName+"','"
                        +attr.EnCode+"','" +attr.Qty+"','"+attr.QtyFact+"','"+attr.SinglePerBox+"','" +attr.SingleBoxPerBigBox+"','"
                        +attr.LN+"','"+(attr.PR==null?"":sdf.format(attr.PR))+"','" +attr.CreateUserId+"','"+attr.CreateUserName+"')"
                );
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
     * 保存盘点主单文件
     * @return
     */
    public boolean SaveCheckDataFile(List<CheckEntity> cEntity){
        try {
            for (CheckEntity attr : cEntity) {
                db = DBHelper.getWritableDatabase();
                db.execSQL("DELETE FROM "+ CHECK_MAIN_TABLE  +" WHERE CheckCode= '" + attr.CheckCode+"'");
                //保存主单之前删除相同单据号的主单据
                db.execSQL("insert into "+ ORDER_MAIN_TABLE+
                        "(CheckId ,CheckCode,CheckDate,WarehouseId,WarehouseName,Description,CreateDate,CreateUserId,CreateUserName,Status) "
                        +"values('"+
                        attr.CheckId+"','"+ attr.CheckCode+"','"+(attr.CheckDate==null?"":sdf.format(attr.CheckDate))+"','"+
                        attr.WarehouseId+"','"+attr.WarehouseName+"','"+ attr.Description+"','"+
                        (attr.CreateDate==null?"":sdf.format(attr.CreateDate))+"','"+attr.CreateUserId+"',"
                        +attr.CreateUserName+"',"+String.valueOf(attr.Status)+")"
                );
                //创建出库明细表
               // SqlTableCreate.Order_Billing_Create(db , attr.OrderCode);
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
