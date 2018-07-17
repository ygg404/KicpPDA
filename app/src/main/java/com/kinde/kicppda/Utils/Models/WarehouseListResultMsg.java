package com.kinde.kicppda.Utils.Models;

import com.alibaba.fastjson.JSON;
import com.kinde.kicppda.Models.WarehouseEntity;
import com.kinde.kicppda.Utils.Enum.StatusCodeEnum;

import java.util.List;

/**
 * Created by Lenovo on 2018/7/14.
 */

public class WarehouseListResultMsg extends HttpResponseMsg {
    public List<WarehouseEntity> Result;

    public void setResult(){
        if(StatusCode == StatusCodeEnum.Success.getValue() && !(Data == null || Data=="")){
            Result =  JSON.parseArray(Data.toString(), WarehouseEntity.class);
        }
        else{
            Result = null;
        }

    }

}
