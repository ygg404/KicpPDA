package com.kinde.kicppda.Utils.Models;

import com.alibaba.fastjson.JSON;
import com.kinde.kicppda.Models.ReturnBillingEntity;
import com.kinde.kicppda.Utils.Enum.StatusCodeEnum;

import java.util.List;

/**
 * Created by YGG on 2018/6/29.
 */

public class ReturnBillingListResultMsg extends HttpResponseMsg {
    public List<ReturnBillingEntity> Result;

    public List<ReturnBillingEntity> getResult(){
        return Result;
    }

    public void setResult(){
        if(StatusCode == StatusCodeEnum.Success.getValue() && !(Data == null || Data=="")){
            Result =  JSON.parseArray(Data.toString(), ReturnBillingEntity.class);
        }
        else{
            Result = null;
        }

    }
}
