package com.kinde.kicppda.Models;

/**
 * Created by YGG on 2018/6/29.
 */

public class OrderBillingEntity {

    /// 主键
    public String OrderBillingId;

    /// 单主键
    public String OrderId;

    /// 产品Id
    public String ProductId;

    ///产品名称
    public String ProductName;

    /// 产品编码
    public String EnCode;

    /// 数量
    public int Qty;

    /// 实际数量
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
