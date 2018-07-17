package com.kinde.kicppda.Utils.Models;

/**
 * Created by Lenovo on 2018/7/14.
 */

import com.alibaba.fastjson.JSON;
import com.kinde.kicppda.Models.ProductEntity;
import com.kinde.kicppda.Utils.Enum.StatusCodeEnum;

import java.util.List;

/**
 * 获取产品返回结果
 */
public class ProductResultMsg extends HttpResponseMsg {
    public List<ProductEntity> Result;

    public void setResult(){
        if(StatusCode == StatusCodeEnum.Success.getValue() && !(Data == null || Data=="")){
            Result =  JSON.parseArray(Data.toString(), ProductEntity.class);
        }
        else{
            Result = null;
        }

    }
}
