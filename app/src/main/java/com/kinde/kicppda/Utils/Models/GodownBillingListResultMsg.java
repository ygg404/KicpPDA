package com.kinde.kicppda.Utils.Models;

import com.alibaba.fastjson.JSON;
import com.kinde.kicppda.Models.GodownBillingEntity;
import com.kinde.kicppda.Utils.Enum.StatusCodeEnum;

import java.util.List;

/**
 * Created by YGG on 2018/6/11.
 */

public class GodownBillingListResultMsg extends HttpResponseMsg{
    public List<GodownBillingEntity> Result;

    public List<GodownBillingEntity> getResult(){
        return Result;
    }

    public void setResult(){
        if(StatusCode == StatusCodeEnum.Success.getValue()){
            List<GodownBillingEntity> Result = (List<GodownBillingEntity>) JSON.parseObject(Data.toString(), GodownBillingEntity.class);
        }
        else{
            Result = null;
        }

    }
}
