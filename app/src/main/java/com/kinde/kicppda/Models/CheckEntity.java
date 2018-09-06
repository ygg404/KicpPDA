package com.kinde.kicppda.Models;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Date;

/**
 * Created by YGG on 2018/6/29.
 */

@DatabaseTable(tableName = "CD_MAIN")
public class CheckEntity {

    /// 入主键
    @DatabaseField(id = true )
    public String CheckId;

    /// 单据编码
    @DatabaseField
    public String CheckCode;

    /// 单据日期
    @DatabaseField
    public Date CheckDate;

    /// 仓库
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

    // 一个盘点主单可以对应多个扫描（确立关系的关键）
    @ForeignCollectionField(eager = true)
    public ForeignCollection<CheckScanEntity> sNotes;
}
