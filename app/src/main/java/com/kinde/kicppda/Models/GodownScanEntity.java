package com.kinde.kicppda.Models;

/**
 * Created by YGG on 2018/8/2 0002.
 */

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Date;

/**
 * 入库扫码单
 */
@DatabaseTable(tableName = "PD_SCAN")
public class GodownScanEntity {

    /// 扫码单主键
    @DatabaseField(generatedId = true )
    public int id ;

    /// 序列码
    @DatabaseField
    public String SerialNo;

    /// 产品ID
    @DatabaseField
    public String ProductId;

    /// 产品批次
    @DatabaseField
    public String LN;

    /// 产品日期
    @DatabaseField
    public Date PR;

    /// 数量
    @DatabaseField
    public int Qty;

    ///
    @DatabaseField
    public String ScanTag;

    /// 用户ID
    @DatabaseField
    public String CreateUserId;

    // 外部对象字段（确立关系的关键）
    //明细单
    @DatabaseField(foreign = true,foreignAutoRefresh = true)
    public GodownBillingEntity billEntityId;
}
