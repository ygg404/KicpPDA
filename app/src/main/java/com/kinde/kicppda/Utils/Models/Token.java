package com.kinde.kicppda.Utils.Models;

/**
 * Created by YGG on 2018/5/25.
 */

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

/**
 * 令牌
 */
public class Token implements Serializable{

    /// 用户名
    public int StaffId;

    /// 用户名对应签名Token
    public UUID SignToken;

    /// Token过期时间
    public Date ExpireTime;

    @Override
    public String toString() {
        return "Token{" +
                "StaffId='" + StaffId + '\'' +
                ", SignToken=" + SignToken + '\''+
                ", ExpireTime=" + ExpireTime + '\''+
                '}';
    }
}
