package com.kinde.kicppda.Models;

import java.util.Date;

/**
 * Created by Lenovo on 2018/6/29.
 */

public class AllotBillingEntity {

    /// 调拨单开单表主键
    public String AllotBillingId;

    /// 调拨单主键
    public String AllotId;

    /// 产品Id
    public String ProductId;

    /// 产品名称
    public String ProductName;

    /// 产品编码
    public String EnCode;

    /// 生产批次
    public String LN;

    /// 生产日期
    public Date PR;

    /// 数量
    public int Qty;

    /// 数量
    public int QtyFact;

    /// 中盒多少瓶
    public int SinglePerBox;

    /// 大箱多少盒
    public int SingleBoxPerBigBox;

    /// 创建用户
    public String CreateUserId;

    /// 用户名
    public String CreateUserName;
}
