package com.kinde.kicppda.Utils.Models;

import com.alibaba.fastjson.JSON;
import com.kinde.kicppda.Models.GodownEntity;
import com.kinde.kicppda.Utils.Enum.StatusCodeEnum;

import java.util.List;

/**
 * Created by Lenovo on 2018/6/11.
 */

public class GodownListResultMsg extends HttpResponseMsg {
    public List<GodownEntity> Result;

    public List<GodownEntity> getResult(){
        return Result;
    }

    public void setResult(){
        if(StatusCode == StatusCodeEnum.Success.getValue() && !(Data == null || Data=="")){
            Result =  JSON.parseArray(Data.toString(), GodownEntity.class);
        }
        else{
            Result = null;
        }

    }
}
