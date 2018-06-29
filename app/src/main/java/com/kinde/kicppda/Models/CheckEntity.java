package com.kinde.kicppda.Models;

import java.util.Date;

/**
 * Created by YGG on 2018/6/29.
 */

public class CheckEntity {

    /// 入主键
    public String CheckId;

    /// 单据编码
    public String CheckCode;

    /// 单据日期
    public Date CheckDate;

    /// 仓库
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
