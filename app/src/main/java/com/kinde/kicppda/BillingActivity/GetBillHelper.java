package com.kinde.kicppda.BillingActivity;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.kinde.kicppda.MDAO.AllotBillingEntityDAO;
import com.kinde.kicppda.MDAO.AllotEntityDAO;
import com.kinde.kicppda.MDAO.CheckEntityDAO;
import com.kinde.kicppda.MDAO.GodownBillingEntityDAO;
import com.kinde.kicppda.MDAO.GodownEntityDAO;
import com.kinde.kicppda.MDAO.GodownXBillingEntityDAO;
import com.kinde.kicppda.MDAO.GodownXEntityDAO;
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
                new CheckEntityDAO(mContext).insert(attr);
            }
        }catch (Exception ex){
            return false;
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
                new GodownXEntityDAO(mContext).insert(attr);
            }
        }catch (Exception ex){
            return false;
        }
        return true;
    }

    /**
     * 保存关联箱明细单文件
     * @return
     */
    public boolean SaveGroupXBillingDataFile(GodownXEntity gxEntity, List<GodownXBillingEntity> gxBillEntity){
        try {
            for (GodownXBillingEntity attr : gxBillEntity) {
                attr.gxId = gxEntity;
                new GodownXBillingEntityDAO(mContext).insert(attr);
            }
        }catch (Exception ex){
            return false;
        }
        return true;
    }
}
