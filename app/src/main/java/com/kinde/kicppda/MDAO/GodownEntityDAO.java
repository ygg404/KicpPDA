package com.kinde.kicppda.MDAO;

/**
 * Created by YGG on 2018/8/2 0002.
 */

import android.content.Context;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.j256.ormlite.dao.Dao;
import com.kinde.kicppda.Models.GodownEntity;
import com.kinde.kicppda.Utils.SQLiteHelper.DBHelper;

/**
 * 操作GodownEntity数据表的Dao类，封装这操作GodownEntity表的所有操作
 * 通过DatabaseHelper类中的方法获取ORMLite内置的DAO类进行数据库中数据的操作
 * <p>
 * 调用dao的create()方法向表中添加数据
 * 调用dao的delete()方法删除表中的数据
 * 调用dao的update()方法修改表中的数据
 * 调用dao的queryForAll()方法查询表中的所有数据
 */
public class GodownEntityDAO {
    private Context context;
    // ORMLite提供的DAO类对象，第一个泛型是要操作的数据表映射成的实体类；第二个泛型是这个实体类中ID的数据类型
    private Dao<GodownEntity, String> dao;

    public GodownEntityDAO(Context context){
        this.context = context;
        try{
            this.dao = DBHelper.getInstance(context).getDao(GodownEntity.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 向GodownEntity表中添加一条数据
    public void insert(GodownEntity data) {
        try {
            dao.createOrUpdate(data);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 删除GodownEntity表中的一条数据
    public void delete(GodownEntity data) {
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
    public GodownEntity queryForBillNo(String billNo){
        try {
            List<GodownEntity> entities = dao.queryForEq("GodownCode",billNo);
            if(entities != null && entities.size()>0){
                GodownEntity entity = entities.get(0);
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
    public List<GodownEntity> queryForAll(){
        List<GodownEntity> entities = new ArrayList<>();
        try {
            entities = dao.queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return entities;
    }


    // 通过条件查询主单
    public List<GodownEntity> queryByEq(String eq , String value) {
        List<GodownEntity> entities = null;
        try {
            entities = dao.queryForEq( eq ,value );
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return entities;
    }

    // 通过条件查询主单
    public List<String> GetGodownCodeList() {
        List<String> GodownCodeList = new ArrayList<>();
        try {
            List<GodownEntity> entities = new ArrayList<>();
            entities = dao.queryForAll();
            for( GodownEntity entity : entities ){
                GodownCodeList.add(entity.GodownCode);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return GodownCodeList;
    }
}
