package com.kinde.kicppda.Models;

/**
 * Created by Lenovo on 2018/7/14.
 */

/**
 * 产品
 */
public class ProductEntity {

    /// 产品主键
    public String ProductId;

    /// 产品编码
    public String EnCode;

    /// 产品名称
    public String FullName;

    /// 单位
    public String Unit;

    /// 每箱单品数
    public int SinglePerBox;

    /// 代理价格
    public float AgencyPrice;

    /// 零售价格
    public float RetailPrice;

    /// 统一零售价格
    public float UnifiedPrice;

    /// 代理一级佣金
    public float AgencyCommissionFirst;

    /// 代理二级佣金
    public float AgencyCommissionSecond;

    /// 零售一级佣金
    public float RetailCommissionFirst;

    /// 零售二级佣金
    public float RetailCommissionSecond;

    /// 快速查询
    public String QuickQuery;

    /// 简拼
    public String SimpleSpelling;

    /// 图像
    public String HeadIcon;

    /// 排序码
    public int SortCode;

    /// 删除标记
    public int DeleteMark;

    /// 有效标志
    public int EnabledMark;

    /// 备注
    public String Description;

//    /// 创建日期
//    public Date CreateDate;

    /// 创建用户主键
    public String CreateUserId;

    /// 创建用户
    public String CreateUserName;

//    /// 修改日期
//    public Date ModifyDate;

    /// 修改用户主键
    public String ModifyUserId;

    /// 修改用户
    public String ModifyUserName;
}
