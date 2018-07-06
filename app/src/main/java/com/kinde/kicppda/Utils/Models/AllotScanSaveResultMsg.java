package com.kinde.kicppda.Utils.Models;

import com.kinde.kicppda.Utils.Enum.StatusCodeEnum;

/**
 * Created by YGG on 2018/7/5.
 */

/**
 * 调拨扫描明细保存返回结果
 */
public class AllotScanSaveResultMsg extends HttpResponseMsg {
    private int Qty;

    public int getResult(){
        return Qty;
    }

    public void setResult(){
        if(StatusCode == StatusCodeEnum.Success.getValue() && !(Data == null || Data=="")){
            Qty =  Integer.parseInt( Data.toString() );
        }
        else{
            Qty = 0;
        }

    }
}

