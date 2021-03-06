package com.kinde.kicppda.Models;

/**
 * Created by Lenovo on 2018/6/11.
 */

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Date;

/**
 * 入库单
 */
@DatabaseTable(tableName = "PD_MAIN")
public class GodownEntity {

    /// 入库单主键
    @DatabaseField(id = true )
    public String GodownId;

    /// 单据编码
    @DatabaseField
    public String GodownCode;

    /// 单据日期
    @DatabaseField
    public Date GodownDate;

    /// 仓库主键
    @DatabaseField
    public String WarehouseId;

    /// 仓库名称
    @DatabaseField
    public String WarehouseName;

    /// 备注
    @DatabaseField
    public String Description;

    /// 创建日期
    @DatabaseField
    public Date CreateDate;

    /// 创建用户
    @DatabaseField
    public String CreateUserId;

    /// 创建用户
    @DatabaseField
    public String CreateUserName;

    /// 状态（0-未审核，1-已审核）
    @DatabaseField
    public int Status;

    // 一个主单可以对应多个明细（确立关系的关键）
    @ForeignCollectionField(eager = true)
    public ForeignCollection<GodownBillingEntity> bNotes;

}
