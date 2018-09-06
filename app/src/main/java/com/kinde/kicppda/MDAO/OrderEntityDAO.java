package com.kinde.kicppda.MDAO;

import android.content.Context;

import com.j256.ormlite.dao.Dao;
import com.kinde.kicppda.Models.GodownEntity;
import com.kinde.kicppda.Models.OrderEntity;
import com.kinde.kicppda.Utils.SQLiteHelper.DBHelper;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by YGG on 2018/8/3 0003.
 */

public class OrderEntityDAO {
    private Context context;
    // ORMLite提供的DAO类对象，第一个泛型是要操作的数据表映射成的实体类；第二个泛型是这个实体类中ID的数据类型
    private Dao<OrderEntity, String> dao;

    public OrderEntityDAO(Context context){
        this.context = context;
        try{
            this.dao = DBHelper.getInstance(context).getDao(OrderEntity.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 向GodownEntity表中添加一条数据
    public void insert(OrderEntity data) {
        try {
            dao.createOrUpdate(data);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 删除GodownEntity表中的一条数据
    public void delete(OrderEntity data) {
        try {
            dao.delete(data);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 查询一条记录
     * @param billNo
     * @return
     */
    public OrderEntity queryForBillNo(String billNo){
        try {
            List<OrderEntity> entities = dao.queryForEq("OrderCode",billNo);
            if(entities != null && entities.size()>0){
                OrderEntity entity = entities.get(0);
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
    public List<OrderEntity> queryForAll(){
        List<OrderEntity> entities = new ArrayList<>();
        try {
            entities = dao.queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return entities;
    }


    // 通过条件查询主单
    public List<OrderEntity> queryByEq(String eq , String value) {
        List<OrderEntity> entities = null;
        try {
            entities = dao.queryForEq( eq ,value );
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return entities;
    }

    // 获取主单单据号
    public List<String> GetOrderCodeList() {
        List<String> OrderCodeList = new ArrayList<>();
        try {
            List<OrderEntity> entities = new ArrayList<>();
            entities = dao.queryForAll();
            for( OrderEntity entity : entities ){
                OrderCodeList.add(entity.OrderCode);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return OrderCodeList;
    }

}
