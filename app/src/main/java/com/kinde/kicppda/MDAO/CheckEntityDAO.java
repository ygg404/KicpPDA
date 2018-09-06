package com.kinde.kicppda.MDAO;

import android.content.Context;

import com.j256.ormlite.dao.Dao;
import com.kinde.kicppda.Models.CheckEntity;
import com.kinde.kicppda.Models.GodownEntity;
import com.kinde.kicppda.Utils.SQLiteHelper.DBHelper;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by YGG on 2018/8/4 0004.
 */

public class CheckEntityDAO {
    private Context context;
    // ORMLite提供的DAO类对象，第一个泛型是要操作的数据表映射成的实体类；第二个泛型是这个实体类中ID的数据类型
    private Dao<CheckEntity, String> dao;

    public CheckEntityDAO(Context context){
        this.context = context;
        try{
            this.dao = DBHelper.getInstance(context).getDao(CheckEntity.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 向GodownEntity表中添加一条数据
    public void insert(CheckEntity data) {
        try {
            dao.createOrUpdate(data);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 删除GodownEntity表中的一条数据
    public void delete(CheckEntity data) {
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
    public CheckEntity queryForBillNo(String billNo){
        try {
            List<CheckEntity> entities = dao.queryForEq("CheckCode",billNo);
            if(entities != null && entities.size()>0){
                CheckEntity entity = entities.get(0);
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
    public List<CheckEntity> queryForAll(){
        List<CheckEntity> entities = new ArrayList<>();
        try {
            entities = dao.queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return entities;
    }


    // 通过条件查询主单
    public List<CheckEntity> queryByEq(String eq , String value) {
        List<CheckEntity> entities = null;
        try {
            entities = dao.queryForEq( eq ,value );
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return entities;
    }

    // 通过条件查询主单
    public List<String> GetCheckCodeList() {
        List<String> CheckCodeList = new ArrayList<>();
        try {
            List<CheckEntity> entities = new ArrayList<>();
            entities = dao.queryForAll();
            for( CheckEntity entity : entities ){
                CheckCodeList.add(entity.CheckCode);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return CheckCodeList;
    }
}
