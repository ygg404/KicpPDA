package com.kinde.kicppda.Utils.Models;

import com.kinde.kicppda.Utils.Enum.StatusCodeEnum;

/**
 * Created by Lenovo on 2018/7/5.
 */

/**
 * 出库扫描明细保存返回结果
 */
public class OrderScanSaveResultMsg extends HttpResponseMsg{
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
