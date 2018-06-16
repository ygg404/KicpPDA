package com.kinde.kicppda.Billing;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.kinde.kicppda.Models.GodownEntity;
import com.kinde.kicppda.Utils.SQLiteHelper.DBOpenHelper;

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
