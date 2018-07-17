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

    public void setResult(){
        if(StatusCode == StatusCodeEnum.Success.getValue() && !(Data == null || Data=="")){
            Result =  JSON.parseArray(Data.toString(), GodownBillingEntity.class);
        }
        else{
            Result = null;
        }

    }
}
