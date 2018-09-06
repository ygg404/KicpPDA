package com.kinde.kicppda.MDAO;

import android.content.Context;

import com.j256.ormlite.dao.Dao;
import com.kinde.kicppda.Models.AgentEntity;
import com.kinde.kicppda.Models.GodownXEntity;
import com.kinde.kicppda.Models.ProductEntity;
import com.kinde.kicppda.Utils.SQLiteHelper.DBHelper;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ygg on 2018/8/3 0003.
 */

public class ProductEntityDAO {
    private Context context;
    // ORMLite提供的DAO类对象，第一个泛型是要操作的数据表映射成的实体类；第二个泛型是这个实体类中ID的数据类型
    private Dao<ProductEntity, String> dao;

    public ProductEntityDAO(Context context){
        this.context = context;
        try{
            this.dao = DBHelper.getInstance(context).getDao(ProductEntity.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 向ProductEntity表中添加一条数据
    public void insert(ProductEntity data) {
        try {
            dao.createOrUpdate(data);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 删除ProductEntity表中的一条数据
    public void delete(ProductEntity data) {
        try {
            dao.delete(data);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 通过产品id查询一条记录
     * @param Pid
     * @return
     */
    public ProductEntity queryForID(String Pid){
        try {
            List<ProductEntity> entities = dao.queryForEq("ProductId",Pid);
            if(entities != null && entities.size()>0){
                ProductEntity entity = entities.get(0);
                return entity;
            }else{
                return null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("查找出现异常.");
        }
    }

    // 查询所有记录
    public List<ProductEntity> queryForAll(){
        List<ProductEntity> entities = new ArrayList<>();
        try {
            entities = dao.queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return entities;
    }


    // 通过条件查询明细单
    public List<ProductEntity> queryByEq(String eq , String value) {
        List<ProductEntity> entities = new ArrayList<>();
        try {
            entities = dao.queryForEq( eq ,value );
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return entities;
    }
}
