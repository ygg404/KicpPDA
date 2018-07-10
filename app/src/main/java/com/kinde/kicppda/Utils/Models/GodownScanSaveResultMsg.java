package com.kinde.kicppda.Utils.Models;

import com.kinde.kicppda.Utils.Enum.StatusCodeEnum;

/**
 * Created by YGG on 2018/7/5.
 */

/**
 * 入库扫描明细保存返回结果
 */
public class GodownScanSaveResultMsg extends HttpResponseMsg {
    public int Qty;

    public void setResult(){
        if(StatusCode == StatusCodeEnum.Success.getValue() && !(Data == null || Data=="")){
            Qty =  Integer.parseInt( Data.toString() );
        }
        else{
            Qty = 0;
        }

    }
}
