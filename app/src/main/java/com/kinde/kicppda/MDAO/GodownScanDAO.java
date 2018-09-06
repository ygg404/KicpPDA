package com.kinde.kicppda.MDAO;

import android.content.Context;
import android.support.annotation.IntegerRes;

import com.j256.ormlite.dao.Dao;
import com.kinde.kicppda.Models.GodownEntity;
import com.kinde.kicppda.Models.GodownScanEntity;
import com.kinde.kicppda.Utils.SQLiteHelper.DBHelper;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by YGG on 2018/8/2 0002.
 */

public class GodownScanDAO {
    private Context context;
    // ORMLite提供的DAO类对象，第一个泛型是要操作的数据表映射成的实体类；第二个泛型是这个实体类中ID的数据类型
    private Dao<GodownScanEntity, Integer> dao;

    public GodownScanDAO(Context context){
        this.context = context;
        try{
            this.dao = DBHelper.getInstance(context).getDao(GodownScanEntity.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 向GodownEntity表中添加一条数据
    public void insert(GodownScanEntity data) {
        try {
            dao.createOrUpdate(data);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 删除GodownEntity表中的一条数据
    public void delete(GodownScanEntity data) {
        try {
            dao.delete(data);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 查询所有记录
    public List<GodownScanEntity> queryForAll(){
        List<GodownScanEntity> entities = new ArrayList<>();
        try {
            entities = dao.queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return entities;
    }

    //

    // 通过条件查询主单
    public List<GodownScanEntity> queryByEq(String eq , String value) {
        List<GodownScanEntity> entities = null;
        try {
            entities = dao.queryForEq( eq ,value );
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return entities;
    }

}
