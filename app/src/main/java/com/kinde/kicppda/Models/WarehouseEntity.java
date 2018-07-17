package com.kinde.kicppda.Models;

/**
 * Created by Lenovo on 2018/7/14.
 */

/**
 * 仓库
 */
public class WarehouseEntity {

    /// 仓库主键
    public String WarehouseId;

    /// 机构主键
    public String OrganizeId;

    /// 仓库代码
    public String EnCode;

    /// 仓库名称
    public String FullName;

    /// 仓库简称
    public String ShortName;

    /// 负责人主键
    public String Manager;

    /// 外线电话
    public String OuterPhone;

    /// 内线电话
    public String InnerPhone;

    /// 电子邮件
    public String Email;

    /// 仓库传真
    public String Fax;

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
