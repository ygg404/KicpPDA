package com.kinde.kicppda.Models;

/**
 * Created by YGG on 2018/7/7.
 */

import java.util.Date;

/**
 * 生产关联箱明细
 */
public class GodownXBillingEntity {

    /// 主键
    public String GodownXBillingId;

    /// 单主键
    public String GodownXId;

    /// 产品Id
    public String ProductId;

    ///产品名称
    public String ProductName;

    /// 产品编码
    public String EnCode;

    /// 生产批次
    public String LN;

    /// 生产日期
    public Date PR;

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
