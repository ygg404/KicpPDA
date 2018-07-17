package com.kinde.kicppda.Utils.Models;

import com.alibaba.fastjson.JSON;
import com.kinde.kicppda.Models.AllotBillingEntity;
import com.kinde.kicppda.Utils.Enum.StatusCodeEnum;

import java.util.List;

/**
 * Created by Lenovo on 2018/6/29.
 */

public class AllotBillingListResultMsg extends HttpResponseMsg{
    public List<AllotBillingEntity> Result;

    public void setResult(){
        if(StatusCode == StatusCodeEnum.Success.getValue() && !(Data == null || Data=="")){
            Result =  JSON.parseArray(Data.toString(), AllotBillingEntity.class);
        }
        else{
            Result = null;
        }

    }
}
