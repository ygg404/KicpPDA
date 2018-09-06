package com.kinde.kicppda.Models;

/**
 * Created by YGG on 2018/6/29.
 */

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Date;

@DatabaseTable(tableName = "OD_MAIN")
public class OrderEntity {

    /// 主键
    @DatabaseField(id = true)
    public String OrderId;

    /// 单据编码
    @DatabaseField
    public String OrderCode;

    /// 单据日期
    @DatabaseField
    public Date OrderDate;

    /// 代理主键
    @DatabaseField
    public String AgentId;

    /// 代理名称
    @DatabaseField
    public String AgentName;

    /// 备注
    @DatabaseField
    public String Description;

    /// 创建日期
    @DatabaseField
    public Date CreateDate;

    /// 创建用户
    @DatabaseField
    public String CreateUserId;

    /// 状态（0-未审核，1-已审核）
    @DatabaseField
    public int Status;

    // 一个主单可以对应多个明细（确立关系的关键）
    @ForeignCollectionField(eager = true)
    public ForeignCollection<OrderBillingEntity> bNotes;
}
