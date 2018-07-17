package com.kinde.kicppda.Utils.Models;

/**
 * Created by Lenovo on 2018/7/16.
 */

import com.kinde.kicppda.Utils.Enum.StatusCodeEnum;

/**
 *  调拨扫描明细删除返回结果
 */
public class AllotScanDeleteResultMsg extends HttpResponseMsg{
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
