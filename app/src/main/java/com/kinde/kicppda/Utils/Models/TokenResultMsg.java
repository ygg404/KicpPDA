package com.kinde.kicppda.Utils.Models;

import com.alibaba.fastjson.JSON;
import com.kinde.kicppda.Utils.Enum.StatusCodeEnum;

/**
 * Created by YGG on 2018/5/30.
 */

public class TokenResultMsg extends HttpResponseMsg{
    private Token Result;

    public Token getResult(){
        return Result;
    }

    public void setResult(){
        if(StatusCode == StatusCodeEnum.Success.getValue()){
            Result = JSON.parseObject(Data.toString(), Token.class);
        }
        else{
            Result = null;
        }

    }

}