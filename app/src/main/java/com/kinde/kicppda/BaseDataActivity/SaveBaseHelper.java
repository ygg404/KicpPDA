package com.kinde.kicppda.BaseDataActivity;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.kinde.kicppda.Models.AgentEntity;
import com.kinde.kicppda.Models.ProductEntity;
import com.kinde.kicppda.Models.WarehouseEntity;
import com.kinde.kicppda.Utils.Public;
import com.kinde.kicppda.Utils.SQLiteHelper.DBOpenHelper;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by YGG on 2018/7/14.
 */

public class SaveBaseHelper {
    public SQLiteDatabase db;
    public DBOpenHelper DBHelper;

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");


    public SaveBaseHelper(Context mContext){
        DBHelper = new DBOpenHelper(mContext );
    }

    /**
     * 保存客户基础文件
     * @return
     */
    public void SaveCustomerDataFile(List<AgentEntity> aEntity) throws Exception{
        try {
            for (AgentEntity attr : aEntity) {
                db = DBHelper.getWritableDatabase();
                db.execSQL("DELETE FROM "+ Public.B_CUSTOMER_File  +" WHERE AgentId= '" + attr.AgentId+"'");
                //保存主单之前删除相同单据号的主单据
                db.execSQL("insert into "+ Public.B_CUSTOMER_File+
                        "(AgentId ,EnCode,FullName) " +"values('"+
                        attr.AgentId+"','"+ attr.EnCode+"','"+ attr.FullName+"')"
                );
            }
        }catch (Exception ex){
            throw new Exception(ex.getMessage());
        }
        finally {
            db.close();
        }
    }

    /**
     * 保存产品基础文件
     * @return
     */
    public void SaveProductDataFile(List<ProductEntity> pEntity) throws Exception{
        try {
            for (ProductEntity attr : pEntity) {
                db = DBHelper.getWritableDatabase();
                db.execSQL("DELETE FROM "+ Public.B_INVENTORY_File  +" WHERE ProductId = '" + attr.ProductId+"'");
                //保存主单之前删除相同单据号的主单据
                db.execSQL("insert into "+ Public.B_INVENTORY_File+
                        "(ProductId ,EnCode,ProductName) " +"values('"+
                        attr.ProductId+"','"+ attr.EnCode+"','"+ attr.FullName+"')"
                );
            }
        }catch (Exception ex){
            throw new Exception(ex.getMessage());
        }
        finally {
            db.close();
        }
    }

    /**
     * 保存仓库基础文件
     * @return
     */
    public void SaveStockDataFile(List<WarehouseEntity> wEntity) throws Exception{
        try {
            for (WarehouseEntity attr : wEntity) {
                db = DBHelper.getWritableDatabase();
                db.execSQL("DELETE FROM "+ Public.B_WAREHOUSE_File  +" WHERE WarehouseId = '" + attr.WarehouseId+"'");
                //保存主单之前删除相同单据号的主单据
                db.execSQL("insert into "+ Public.B_WAREHOUSE_File+
                        "(WarehouseId ,EnCode,FullName) " +"values('"+
                        attr.WarehouseId+"','"+ attr.EnCode+"','"+ attr.FullName+"')"
                );
            }
        }catch (Exception ex){
            throw new Exception(ex.getMessage());
        }
        finally {
            db.close();
        }
    }
}
