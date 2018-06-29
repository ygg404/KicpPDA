package com.kinde.kicppda.Utils.Models;

import com.alibaba.fastjson.JSON;
import com.kinde.kicppda.Models.AllotEntity;
import com.kinde.kicppda.Utils.Enum.StatusCodeEnum;

import java.util.List;

/**
 * Created by YGG on 2018/6/29.
 */

public class AllotListResultMsg extends HttpResponseMsg{
    public List<AllotEntity> Result;

    public List<AllotEntity> getResult(){
        return Result;
    }

    public void setResult(){
        if(StatusCode == StatusCodeEnum.Success.getValue() && !(Data == null || Data=="")){
            Result =  JSON.parseArray(Data.toString(), AllotEntity.class);
        }
        else{
            Result = null;
        }

    }
}
