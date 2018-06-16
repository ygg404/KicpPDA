package com.kinde.kicppda.Models;

/**
 * Created by Lenovo on 2018/6/11.
 */

import java.util.Date;

/**
 * 入库单
 */
public class GodownEntity {

    /// 入库单主键
    public String GodownId;

    /// 单据编码
    public String GodownCode;

    /// 单据日期
    public Date GodownDate;

    /// 仓库主键
    public String WarehouseId;

    /// 仓库名称
    public String WarehouseName;

    /// 备注
    public String Description;

    /// 创建日期
    public Date CreateDate;

    /// 创建用户
    public String CreateUserId;

    /// 创建用户
    public String CreateUserName;

    /// 状态（0-未审核，1-已审核）
    public int Status;
}
