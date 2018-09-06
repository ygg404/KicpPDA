package com.kinde.kicppda.Models;

/**
 * Created by Lenovo on 2018/7/14.
 */

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * 产品
 */
@DatabaseTable(tableName = "BaseInventory")
public class ProductEntity {

    /// 产品主键
    @DatabaseField(id = true )
    public String ProductId;

    /// 产品编码
    @DatabaseField
    public String EnCode;

    /// 产品名称
    @DatabaseField
    public String FullName;

}
