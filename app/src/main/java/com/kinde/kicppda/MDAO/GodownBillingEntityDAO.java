package com.kinde.kicppda.MDAO;

import android.content.Context;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.kinde.kicppda.Models.GodownBillingEntity;
import com.kinde.kicppda.Models.GodownEntity;
import com.kinde.kicppda.Utils.SQLiteHelper.DBHelper;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by YGG on 2018/8/2 0002.
 */

public class GodownBillingEntityDAO {
    private Context context;
    // ORMLite提供的DAO类对象，第一个泛型是要操作的数据表映射成的实体类；第二个泛型是这个实体类中ID的数据类型
    private Dao<GodownBillingEntity, String> dao;

    public GodownBillingEntityDAO(Context context){
        this.context = context;
        try{
            this.dao = DBHelper.getInstance(context).getDao(GodownBillingEntity.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 向GodownEntity表中添加一条数据
    public void insert(GodownBillingEntity data) {
        try {
            dao.createOrUpdate(data);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 删除GodownEntity表中的一条数据
    public void delete(GodownBillingEntity data) {
        try {
            dao.delete(data);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 查询所有记录
    public List<GodownBillingEntity> queryForAll(){
        List<GodownBillingEntity> entities = new ArrayList<>();
        try {
            entities = dao.queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return entities;
    }


    // 通过条件查询明细单
    public List<GodownBillingEntity> queryByEq(String eq , String value) {
        List<GodownBillingEntity> entities = new ArrayList<>();
        try {
            entities = dao.queryForEq( eq ,value );
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return entities;
    }

    /**
     * 通过产品ID查询一条记录
     * @param productId
     * @return
     */
    public GodownBillingEntity queryForProductId(String productId){
        try {
            List<GodownBillingEntity> entities = dao.queryForEq("ProductId",productId);
            if(entities != null && entities.size()>0){
                GodownBillingEntity entity = entities.get(0);
                return entity;
            }else{
                return null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("查找出现异常.");
        }
    }
//    // 通过产品编号查询明细单
//    public List<GodownBillingEntity> queryByContain(String value) {
//        List<GodownBillingEntity> entities = new ArrayList<>();
//        try {
//            QueryBuilder builder = dao.queryBuilder();
//            builder.where().like("EnCode", "%" + value + "%").or().like("ProductName", "%" + value + "%");
//            entities = builder.query();
//        }catch (Exception ex){
//            ex.printStackTrace();
//        }
//        return entities;
//    }
}
