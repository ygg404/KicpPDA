package com.kinde.kicppda.Models;

import java.util.Date;

/**
 * Created by YGG on 2018/6/11.
 */

/**
 * 入库明细单
 */
public class GodownBillingEntity {

    /// 入库单开单表主键
    public String GodownBillingId;

    /// 入库单主键
    public String GodownId;

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

    /// 实际数量
    public int QtyFact;


    /// 中盒多少瓶
    public int SinglePerBox;

    /// 大箱多少盒
    public int SingleBoxPerBigBox;

    /// 创建日期
    public Date CreateDate;

    /// 创建用户
    public String CreateUserId;

    /// 用户名
    public String CreateUserName;

}
