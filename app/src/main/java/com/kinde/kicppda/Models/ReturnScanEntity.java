package com.kinde.kicppda.Models;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by Administrator on 2018/8/3 0003.
 */

@DatabaseTable(tableName = "RD_SCAN")
public class ReturnScanEntity {
    /// 扫码单主键
    @DatabaseField(generatedId = true )
    public int id ;

    /// 序列码
    @DatabaseField
    public String SerialNo;

    /// 产品ID
    @DatabaseField
    public String ProductId;

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
    public ReturnBillingEntity billEntityId;
}
