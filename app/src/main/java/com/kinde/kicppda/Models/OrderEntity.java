package com.kinde.kicppda.Models;

/**
 * Created by YGG on 2018/6/29.
 */

import java.util.Date;

public class OrderEntity {

    /// 主键
    public String OrderId;

    /// 单据编码
    public String OrderCode;

    /// 单据日期
    public Date OrderDate;

    /// 代理主键
    public String AgentId;

    /// 代理名称
    public String AgentName;

    /// 备注
    public String Description;

    /// 创建日期
    public Date CreateDate;

    /// 创建用户
    public String CreateUserId;

    /// 状态（0-未审核，1-已审核）
    public int Status;
}
