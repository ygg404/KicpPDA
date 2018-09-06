package com.kinde.kicppda.Models;

/**
 * Created by Lenovo on 2018/7/14.
 */

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * 仓库
 */
@DatabaseTable(tableName = "BaseWarehouse")
public class WarehouseEntity {

    /// 仓库主键
    @DatabaseField(id = true )
    public String WarehouseId;

    /// 仓库代码
    @DatabaseField
    public String EnCode;

    /// 仓库名称
    @DatabaseField
    public String FullName;

}
