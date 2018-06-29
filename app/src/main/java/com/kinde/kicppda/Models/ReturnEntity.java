package com.kinde.kicppda.Models;

import java.util.Date;

/**
 * Created by YGG on 2018/6/29.
 */

public class ReturnEntity {

    /// 退货单主键
    public String ReturnId;

    /// 单据编码
    public String ReturnCode;

    /// 单据日期
    public Date ReturnDate;

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
