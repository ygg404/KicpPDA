package com.kinde.kicppda.Models;

import java.util.Date;
import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;
/**
 * Created by YGG on 2018/6/11.
 */

/**
 * 入库明细单
 */
@DatabaseTable(tableName = "PD_BILL")
public class GodownBillingEntity {

    /// 入库单开单表主键
    @DatabaseField(id = true)
    public String GodownBillingId;

    /// 入库单主键
    @DatabaseField
    public String GodownId;

    /// 产品Id
    @DatabaseField
    public String ProductId;

    /// 产品名称
    @DatabaseField
    public String ProductName;

    /// 产品编码
    @DatabaseField
    public String EnCode;

    /// 生产批次
    @DatabaseField
    public String LN;

    /// 生产日期
    @DatabaseField
    public Date PR;

    /// 数量
    @DatabaseField
    public int Qty;

    /// 实际数量
    @DatabaseField
    public int QtyFact;


    /// 中盒多少瓶
    @DatabaseField
    public int SinglePerBox;

    /// 大箱多少盒
    @DatabaseField
    public int SingleBoxPerBigBox;

    /// 创建用户
    @DatabaseField
    public String CreateUserId;

    /// 用户名
    @DatabaseField
    public String CreateUserName;

    // 外部对象字段（确立关系的关键）
    //主单
    @DatabaseField(foreign = true,foreignAutoRefresh = true)
    public GodownEntity godId;


    // 一个明细可以对应多个扫描记录（确立关系的关键）
    @ForeignCollectionField(eager = true)
    public ForeignCollection<GodownScanEntity> sNotes;
}
