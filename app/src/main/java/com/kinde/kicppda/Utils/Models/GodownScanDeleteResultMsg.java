package com.kinde.kicppda.Utils.Models;

import com.kinde.kicppda.Utils.Enum.StatusCodeEnum;

/**
 * Created by YGG on 2018/7/12.
 */

/**
 * 入库扫描明细删除返回结果
 */
public class GodownScanDeleteResultMsg extends HttpResponseMsg{
    public String Result;

    public void setResult(){
        if(StatusCode == StatusCodeEnum.Success.getValue() && !(Data == null || Data=="")){
            Result =   Data.toString() ;
        }
        else{
            Result = null;
        }
    }

}
