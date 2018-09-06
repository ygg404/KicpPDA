package com.kinde.kicppda.BillingActivity;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.kinde.kicppda.MDAO.AllotBillingEntityDAO;
import com.kinde.kicppda.MDAO.AllotEntityDAO;
import com.kinde.kicppda.MDAO.GodownBillingEntityDAO;
import com.kinde.kicppda.MDAO.GodownEntityDAO;
import com.kinde.kicppda.MDAO.OrderBillingEntityDAO;
import com.kinde.kicppda.MDAO.OrderEntityDAO;
import com.kinde.kicppda.MDAO.ReturnBillingEntityDAO;
import com.kinde.kicppda.MDAO.ReturnEntityDAO;
import com.kinde.kicppda.Models.AllotBillingEntity;
import com.kinde.kicppda.Models.AllotEntity;
import com.kinde.kicppda.Models.CheckEntity;
import com.kinde.kicppda.Models.GodownBillingEntity;
import com.kinde.kicppda.Models.GodownEntity;
import com.kinde.kicppda.Models.GodownXBillingEntity;
import com.kinde.kicppda.Models.GodownXEntity;
import com.kinde.kicppda.Models.OrderBillingEntity;
import com.kinde.kicppda.Models.OrderEntity;
import com.kinde.kicppda.Models.ReturnBillingEntity;
import com.kinde.kicppda.Models.ReturnEntity;
import com.kinde.kicppda.Utils.Public;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by YGG on 2018/6/12.
 */

public class GetBillHelper {

    public SQLiteDatabase db;
    public DBOpenHelper DBHelper;
    public Context mContext;
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");


    public GetBillHelper(Context context){

        mContext = context;
       // DBHelper = new DBOpenHelper(mContext );
    }
    /**
     * 保存入库主单文件
     * @return
     */
    public boolean SaveGoDownDataFile(List<GodownEntity> gEntity){
        try {
            for (GodownEntity attr : gEntity) {
                new GodownEntityDAO(mContext).insert(attr);
            }
        }catch (Exception ex){
            return false;
        }
        return true;
    }

    /**
     * 保存入库明细单文件
     * @return
     */
    public boolean SaveGoDownBillingDataFile(GodownEntity gEntity, List<GodownBillingEntity> gBillEntity){
        try {
            for (GodownBillingEntity attr : gBillEntity) {
                attr.godId = gEntity;
                new GodownBillingEntityDAO(mContext).insert(attr);

            }
        }catch (Exception ex){
            return false;
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
                new OrderEntityDAO(mContext).insert(attr);
            }
        }catch (Exception ex){
            return false;
        }
        return true;
    }
    /**
     * 保存出库明细单文件
     * @return
     */
    public boolean SaveOrderBillingDataFile(OrderEntity orderEntity, List<OrderBillingEntity> oBillEntity){
        try {
            for (OrderBillingEntity attr : oBillEntity) {
                attr.ordId = orderEntity;
                new OrderBillingEntityDAO(mContext).insert(attr);
            }
        }catch (Exception ex){
            return false;
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
                new ReturnEntityDAO(mContext).insert(attr);
            }
        }catch (Exception ex){
            return false;
        }
        return true;
    }

    /**
     * 保存退货明细单文件
     * @return
     */
    public boolean SaveReturnBillingDataFile(ReturnEntity rEntity, List<ReturnBillingEntity> rBillEntity){
        try {
            for (ReturnBillingEntity attr : rBillEntity) {
                attr.reId = rEntity;
                new ReturnBillingEntityDAO(mContext).insert(attr);
            }
        }catch (Exception ex){
            return false;
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
                new AllotEntityDAO(mContext).insert(attr);
            }
        }catch (Exception ex){
            return false;
        }
        return true;
    }

    /**
     * 保存调拨明细单文件
     * @return
     */
    public boolean SaveAllotBillingDataFile(AllotEntity aEntity, List<AllotBillingEntity> aBillEntity){
        try {
            for (AllotBillingEntity attr : aBillEntity) {
                attr.alId = aEntity;
                new AllotBillingEntityDAO(mContext).insert(attr);
            }
        }catch (Exception ex){
            return false;
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
                db.execSQL("DELETE FROM "+ Public.CHECK_MAIN_TABLE  +" WHERE CheckCode= '" + attr.CheckCode+"'");
                //保存主单之前删除相同单据号的主单据
                db.execSQL("insert into "+ Public.CHECK_MAIN_TABLE+
                        "(CheckId ,CheckCode,CheckDate,WarehouseId,WarehouseName,Description,CreateDate,CreateUserId,CreateUserName,Status) "
                        +"values('"+
                        attr.CheckId+"','"+ attr.CheckCode+"','"+(attr.CheckDate==null?"":sdf.format(attr.CheckDate))+"','"+
                        attr.WarehouseId+"','"+attr.WarehouseName+"','"+ attr.Description+"','"+
                        (attr.CreateDate==null?"":sdf.format(attr.CreateDate))+"','"+attr.CreateUserId+"','"
                        +attr.CreateUserName+"',"+String.valueOf(attr.Status)+")"
                );
                //创建盘点明细表
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

    /**
     * 保存关联箱主单文件
     * @return
     */
    public boolean SaveGroupXDataFile(List<GodownXEntity> gxEntity){
        try {
            for (GodownXEntity attr : gxEntity) {
                db = DBHelper.getWritableDatabase();
                db.execSQL("DELETE FROM "+ Public.GodownX_MAIN_TABLE  +" WHERE GodownXCode= '" + attr.GodownXCode+"'");
                //保存主单之前删除相同单据号的主单据
                db.execSQL("insert into "+ Public.GodownX_MAIN_TABLE+
                        "(GodownXId ,GodownXCode,GodownXDate,Description,CreateDate,CreateUserId,CreateUserName,Status) "
                        +"values('"+
                        attr.GodownXId+"','"+ attr.GodownXCode+"','"+(attr.GodownXDate==null?"":sdf.format(attr.GodownXDate))+"','"
                        + attr.Description+"','"+ (attr.CreateDate==null?"":sdf.format(attr.CreateDate))+"','"+attr.CreateUserId+"','"
                        +attr.CreateUserName+"',"+String.valueOf(attr.Status)+")"
                );
                //创建关联箱明细表
                SqlTableCreate.GroupX_Billing_Create(db , attr.GodownXCode);
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
     * 保存关联箱明细单文件
     * @return
     */
    public boolean SaveGroupXBillingDataFile(String gxEntityCode, List<GodownXBillingEntity> gxBillEntity){
        try {
            for (GodownXBillingEntity attr : gxBillEntity) {
                db = DBHelper.getWritableDatabase();
                //保存主单之前删除相同单据号的主单据
                db.execSQL("insert into '"+ gxEntityCode +  Public.GodownXBillingType +"'"+
                        "(GodownXBillingId,GodownXId,ProductId,ProductName,EnCode,"
                        +"Qty,QtyFact,SinglePerBox,SingleBoxPerBigBox,LN,PR,"
                        +"CreateUserId,CreateUserName)" +"values('"
                        +attr.GodownXBillingId+"','"+ attr.GodownXId+"','"+ attr.ProductId+"','"+attr.ProductName+"','"
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
}
