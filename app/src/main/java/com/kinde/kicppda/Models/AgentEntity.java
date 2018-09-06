package com.kinde.kicppda.Models;



/**
 * Created by Lenovo on 2018/7/14.
 */


import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 *  代理
 */
@DatabaseTable(tableName = "BaseCustomer")
public class AgentEntity {

    /// 代理主键
    @DatabaseField(id = true )
    public String AgentId;

    /// 代理代码
    @DatabaseField
    public String EnCode ;

    /// 代理名称
    @DatabaseField
    public String FullName ;

}
