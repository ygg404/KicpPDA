package com.kinde.kicppda.Utils.Models;

import com.alibaba.fastjson.JSON;
import com.kinde.kicppda.Models.OrderEntity;
import com.kinde.kicppda.Utils.Enum.StatusCodeEnum;

import java.util.List;

/**
 * Created by YGG on 2018/6/29.
 */

public class OrderListResultMsg extends HttpResponseMsg {
    public List<OrderEntity> Result;

    public List<OrderEntity> getResult(){
        return Result;
    }

    public void setResult(){
        if(StatusCode == StatusCodeEnum.Success.getValue() && !(Data == null || Data=="")){
            Result =  JSON.parseArray(Data.toString(), OrderEntity.class);
        }
        else{
            Result = null;
        }

    }
}
