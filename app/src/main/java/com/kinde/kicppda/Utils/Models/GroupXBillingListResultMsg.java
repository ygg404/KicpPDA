package com.kinde.kicppda.Utils.Models;

import com.alibaba.fastjson.JSON;
import com.kinde.kicppda.Models.GodownXBillingEntity;
import com.kinde.kicppda.Utils.Enum.StatusCodeEnum;

import java.util.List;

/**
 * Created by Lenovo on 2018/7/7.
 */

/**
 * 生产关联箱明细单保存
 */
public class GroupXBillingListResultMsg extends HttpResponseMsg{
    public List<GodownXBillingEntity> Result;

    public void setResult(){
        if(StatusCode == StatusCodeEnum.Success.getValue() && !(Data == null || Data=="")){
            Result =  JSON.parseArray(Data.toString(), GodownXBillingEntity.class);
        }
        else{
            Result = null;
        }

    }
}
