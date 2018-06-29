package com.kinde.kicppda.Models;

import java.util.Date;

/**
 * Created by YGG on 2018/6/29.
 */

public class AllotEntity {

    /// 调拨单主键
    public String AllotId;

    /// 单据编码
    public String AllotCode;

    /// 单据日期
    public Date AllotDate;

    /// 移出仓库主键
    public String WarehouseIdOut;

    /// 移入仓库主键
    public String WarehouseIdIn;

    /// 移出仓库
    public String WarehouseNameOut;

    /// 移入仓库
    public String WarehouseNameIn;

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
