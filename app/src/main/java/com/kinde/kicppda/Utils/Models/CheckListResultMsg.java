package com.kinde.kicppda.Utils.Models;

import com.alibaba.fastjson.JSON;
import com.kinde.kicppda.Models.CheckEntity;
import com.kinde.kicppda.Utils.Enum.StatusCodeEnum;

import java.util.List;

/**
 * Created by YGG on 2018/6/29.
 */

public class CheckListResultMsg extends HttpResponseMsg {
    public List<CheckEntity> Result;

    public List<CheckEntity> getResult(){
        return Result;
    }

    public void setResult(){
        if(StatusCode == StatusCodeEnum.Success.getValue() && !(Data == null || Data=="")){
            Result =  JSON.parseArray(Data.toString(), CheckEntity.class);
        }
        else{
            Result = null;
        }

    }
}
