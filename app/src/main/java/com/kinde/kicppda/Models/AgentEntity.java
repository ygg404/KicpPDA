package com.kinde.kicppda.Models;



/**
 * Created by Lenovo on 2018/7/14.
 */


/**
 *  代理
 */
public class AgentEntity {

    /// 代理主键
    public String AgentId;

    /// 父级主键
    public String ParentId ;

    /// 代理代码
    public String EnCode ;

    /// 代理名称
    public String FullName ;

    /// 供货单位
    public String SupplierAgentId ;

    /// 代理性质
    public String ItemDetailId ;

    /// 电话
    public String Phone ;

    /// 传真
    public String Fax ;

    /// 邮编
    public String Postalcode ;

    /// 电子邮箱
    public String Email ;

    /// 开户银行
    public String Bank ;

    /// 银行帐号
    public String BanKNo ;

    /// 微信OpenId
    public String OpenId ;

    /// 负责人
    public String Manager ;

    /// 负责人手机
    public String ManagerMobile ;

    /// 密码
    public String ManagerPassword ;

    /// 密码秘钥
    public String ManagerSecretkey ;

    /// 负责人身份证
    public String ManagerIDNumber ;

    /// 身份证正面
    public String ManagerIDCardFrontPhoto ;

    /// 身份证背面
    public String ManagerIDCardBehindPhoto ;

    /// 省主键
    public String ProvinceId ;

    /// 市主键
    public String CityId ;

    /// 县/区主键
    public String CountyId ;

    /// 详细地址
    public String Address ;

    /// 层
    public int Layer;

    /// 排序码
    public int SortCode;

    /// 删除标记
    public int DeleteMark;

    /// 有效标志
    public int EnabledMark;

    /// 审核标志
    public int AuditMark ;

    /// 备注
    public String Description ;

//    /// 创建日期
//    public Date CreateDate ;

    /// 创建用户主键
    public String CreateUserId ;

    /// 创建用户
    public String CreateUserName ;

//    /// 修改日期
//    public Date ModifyDate ;

    /// 修改用户主键
    public String ModifyUserId ;

    /// 修改用户
    public String ModifyUserName ;
}
