package com.kinde.kicppda.Models;

import java.util.Date;

/**
 * Created by YGG on 2018/7/7.
 */

/**
 * 生产关联箱主单
 */
public class GodownXEntity {
    /// 关联箱单主键
    public String GodownXId;

    /// 单据编码
    public String GodownXCode;

    /// 单据日期
    public Date GodownXDate;

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
