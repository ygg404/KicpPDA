package com.kinde.kicppda.Models;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Date;

/**
 * Created by YGG on 2018/8/3 0003.
 */

@DatabaseTable(tableName = "AD_SCAN")
public class AllotScanEntity {
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
    public AllotBillingEntity billEntityId;

}
