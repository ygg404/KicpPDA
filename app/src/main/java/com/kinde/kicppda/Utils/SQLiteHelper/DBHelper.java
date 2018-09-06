package com.kinde.kicppda.Utils.SQLiteHelper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;


import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.kinde.kicppda.Models.AgentEntity;
import com.kinde.kicppda.Models.AllotBillingEntity;
import com.kinde.kicppda.Models.AllotEntity;
import com.kinde.kicppda.Models.AllotScanEntity;
import com.kinde.kicppda.Models.CheckEntity;
import com.kinde.kicppda.Models.CheckScanEntity;
import com.kinde.kicppda.Models.GodownBillingEntity;
import com.kinde.kicppda.Models.GodownEntity;
import com.kinde.kicppda.Models.GodownScanEntity;
import com.kinde.kicppda.Models.GodownXBillingEntity;
import com.kinde.kicppda.Models.GodownXEntity;
import com.kinde.kicppda.Models.GodownXScanEntity;
import com.kinde.kicppda.Models.OrderBillingEntity;
import com.kinde.kicppda.Models.OrderEntity;
import com.kinde.kicppda.Models.OrderScanEntity;
import com.kinde.kicppda.Models.ProductEntity;
import com.kinde.kicppda.Models.ReturnBillingEntity;
import com.kinde.kicppda.Models.ReturnEntity;
import com.kinde.kicppda.Models.ReturnScanEntity;
import com.kinde.kicppda.Models.WarehouseEntity;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by YGG on 2018/8/1 0001.
 */

public class DBHelper extends OrmLiteSqliteOpenHelper {

    // 数据库名称
    private static final String DB_NAME = "data.db";

    //版本号
    private static final int DB_VERSOIN = 1;

    // 用来存放 Dao 的键值对集合
    private Map<String,Dao> daos = new HashMap<String,Dao>();


    private static DBHelper instance;

    // 获取本类单例对象的方法
    public static synchronized DBHelper getInstance(Context context) {
        if (instance == null) {
            synchronized (DBHelper.class) {
                if (instance == null) {
                    instance = new DBHelper(context);
                }
            }
        }
        return instance;
    }


    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSOIN);
    }

    public DBHelper(Context context, String databaseName, SQLiteDatabase.CursorFactory factory, int databaseVersion) {
        super(context, databaseName, factory, databaseVersion);

    }
    /**
     * 创建表的操作
     * @param database
     * @param connectionSource
     */
    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        try {

            TableUtils.createTableIfNotExists(connectionSource, GodownEntity.class);
            TableUtils.createTableIfNotExists(connectionSource, GodownBillingEntity.class);
            TableUtils.createTableIfNotExists(connectionSource, GodownScanEntity.class);

            TableUtils.createTableIfNotExists(connectionSource, OrderEntity.class);
            TableUtils.createTableIfNotExists(connectionSource, OrderBillingEntity.class);
            TableUtils.createTableIfNotExists(connectionSource, OrderScanEntity.class);

            TableUtils.createTableIfNotExists(connectionSource, ReturnEntity.class);
            TableUtils.createTableIfNotExists(connectionSource, ReturnBillingEntity.class);
            TableUtils.createTableIfNotExists(connectionSource, ReturnScanEntity.class);

            TableUtils.createTableIfNotExists(connectionSource, AllotEntity.class);
            TableUtils.createTableIfNotExists(connectionSource, AllotBillingEntity.class);
            TableUtils.createTableIfNotExists(connectionSource, AllotScanEntity.class);

            TableUtils.createTableIfNotExists(connectionSource, CheckEntity.class);
            TableUtils.createTableIfNotExists(connectionSource, CheckScanEntity.class);

            TableUtils.createTableIfNotExists(connectionSource, GodownXEntity.class);
            TableUtils.createTableIfNotExists(connectionSource, GodownXBillingEntity.class);
            TableUtils.createTableIfNotExists(connectionSource, GodownXScanEntity.class);

            TableUtils.createTableIfNotExists(connectionSource, ProductEntity.class);
            TableUtils.createTableIfNotExists(connectionSource, WarehouseEntity.class);
            TableUtils.createTableIfNotExists(connectionSource, AgentEntity.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 这里进行更新表操作
     * @param database
     * @param connectionSource
     * @param oldVersion
     * @param newVersion
     */
    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        try {
            TableUtils.dropTable(connectionSource,GodownEntity.class,true);
            TableUtils.dropTable(connectionSource,GodownBillingEntity.class,true);
            TableUtils.dropTable(connectionSource, GodownScanEntity.class ,true);

            TableUtils.dropTable(connectionSource, OrderEntity.class,true);
            TableUtils.dropTable(connectionSource, OrderBillingEntity.class ,true);
            TableUtils.dropTable(connectionSource,OrderScanEntity.class ,true);

            TableUtils.dropTable(connectionSource, ReturnEntity.class,true);
            TableUtils.dropTable(connectionSource, ReturnBillingEntity.class,true);
            TableUtils.dropTable(connectionSource, ReturnScanEntity.class,true);

            TableUtils.dropTable(connectionSource, AllotEntity.class,true);
            TableUtils.dropTable(connectionSource, AllotBillingEntity.class,true);
            TableUtils.dropTable(connectionSource, AllotScanEntity.class,true);

            TableUtils.createTableIfNotExists(connectionSource, CheckEntity.class);
            TableUtils.createTableIfNotExists(connectionSource, CheckScanEntity.class);

            TableUtils.createTableIfNotExists(connectionSource, GodownXEntity.class);
            TableUtils.createTableIfNotExists(connectionSource, GodownXBillingEntity.class);
            TableUtils.createTableIfNotExists(connectionSource, GodownXScanEntity.class);

            TableUtils.dropTable(connectionSource, ProductEntity.class,true);
            TableUtils.dropTable(connectionSource, WarehouseEntity.class,true);
            TableUtils.dropTable(connectionSource, AgentEntity.class,true);
            onCreate(database,connectionSource);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 通过类来获得指定的 dao
     * @param clazz
     * @return
     * @throws SQLException
     */
        /*public synchronized Dao getDao(Class clazz) throws SQLException {
            Dao dao = null;
            String className = clazz.getSimpleName();
            if(daos.containsKey(className)){
                dao = super.getDao(clazz);
                daos.put(className,dao);
            }
            return dao;
        }*/

    /**
     * 释放资源
     */
    public void close(){
        super.close();
        for(String key : daos.keySet()){
            Dao dao = daos.get(key);
            dao = null;
        }
    }
}

