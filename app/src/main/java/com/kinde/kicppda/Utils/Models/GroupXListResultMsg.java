package com.kinde.kicppda.Utils.Models;

import com.alibaba.fastjson.JSON;
import com.kinde.kicppda.Models.GodownXEntity;
import com.kinde.kicppda.Utils.Enum.StatusCodeEnum;

import java.util.List;

/**
 * Created by Lenovo on 2018/7/7.
 */

/**
 * 生产关联箱主单保存
 */
public class GroupXListResultMsg extends HttpResponseMsg{
    public List<GodownXEntity> Result;

    public void setResult(){
        if(StatusCode == StatusCodeEnum.Success.getValue() && !(Data == null || Data=="")){
            Result =  JSON.parseArray(Data.toString(), GodownXEntity.class);
        }
        else{
            Result = null;
        }

    }
}
