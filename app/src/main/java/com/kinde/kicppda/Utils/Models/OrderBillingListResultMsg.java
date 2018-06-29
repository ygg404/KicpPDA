package com.kinde.kicppda.Utils.Models;

import com.alibaba.fastjson.JSON;
import com.kinde.kicppda.Models.OrderBillingEntity;
import com.kinde.kicppda.Utils.Enum.StatusCodeEnum;

import java.util.List;

/**
 * Created by YGG on 2018/6/29.
 */

public class OrderBillingListResultMsg extends HttpResponseMsg {
    public List<OrderBillingEntity> Result;

    public List<OrderBillingEntity> getResult(){
        return Result;
    }

    public void setResult(){
        if(StatusCode == StatusCodeEnum.Success.getValue() && !(Data == null || Data=="")){
            Result =  JSON.parseArray(Data.toString(), OrderBillingEntity.class);
        }
        else{
            Result = null;
        }

    }
}