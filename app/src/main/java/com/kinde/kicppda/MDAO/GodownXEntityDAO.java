package com.kinde.kicppda.MDAO;

import android.content.Context;

import com.j256.ormlite.dao.Dao;
import com.kinde.kicppda.Models.GodownXEntity;
import com.kinde.kicppda.Utils.SQLiteHelper.DBHelper;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by YGG on 2018/8/14 0014.
 */

public class GodownXEntityDAO {
    private Context context;
    // ORMLite提供的DAO类对象，第一个泛型是要操作的数据表映射成的实体类；第二个泛型是这个实体类中ID的数据类型
    private Dao<GodownXEntity, String> dao;

    public GodownXEntityDAO(Context context){
        this.context = context;
        try{
            this.dao = DBHelper.getInstance(context).getDao(GodownXEntity.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 向GodownEntity表中添加一条数据
    public void insert(GodownXEntity data) {
        try {
            dao.createOrUpdate(data);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 删除GodownEntity表中的一条数据
    public void delete(GodownXEntity data) {
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
    public GodownXEntity queryForBillNo(String billNo){
        try {
            List<GodownXEntity> entities = dao.queryForEq("GodownXCode",billNo);
            if(entities != null && entities.size()>0){
                GodownXEntity entity = entities.get(0);
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
    public List<GodownXEntity> queryForAll(){
        List<GodownXEntity> entities = new ArrayList<>();
        try {
            entities = dao.queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return entities;
    }


    // 通过条件查询主单
    public List<GodownXEntity> queryByEq(String eq , String value) {
        List<GodownXEntity> entities = null;
        try {
            entities = dao.queryForEq( eq ,value );
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return entities;
    }

    // 通过条件查询主单
    public List<String> GetGodownXCodeList() {
        List<String> GodownXCodeList = new ArrayList<>();
        try {
            List<GodownXEntity> entities = new ArrayList<>();
            entities = dao.queryForAll();
            for( GodownXEntity entity : entities ){
                GodownXCodeList.add(entity.GodownXCode);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return GodownXCodeList;
    }
}
