package com.kinde.kicppda.Billing;

import android.os.Handler;
import android.os.Message;

import com.kinde.kicppda.Models.GodownEntity;
import com.kinde.kicppda.Utils.ApiHelper;
import com.kinde.kicppda.Utils.Config;
import com.kinde.kicppda.Utils.Models.GodownBillingListResultMsg;
import com.kinde.kicppda.Utils.Models.GodownListResultMsg;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by Lenovo on 2018/6/29.
 */

public class DownLoadBillHelper {

    //获取入库主单和明细
    public static void downLoadINBill(Handler eHandler,Message message,GetBillHelper gBillHelper, String datebegin,String dateend ,String billBarcode)
    {
        //获取主单
        HashMap<String,String> query = new HashMap<String, String>();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

        try {
            Date dateBegin = checkDateValid(datebegin);
            Date dateEnd = checkDateValid(dateend);
            query.put("godownCode",billBarcode);
            query.put("beginDate", dateBegin==null?"":formatter.format(dateBegin) );
            query.put("endDate", dateEnd==null?"":formatter.format(dateEnd));
            query.put("status", "1");
        }catch (Exception ex){
            message.obj = ex.getMessage();
            eHandler.sendMessage(message);
            return;
        }

        GodownListResultMsg godListc = ApiHelper.GetHttp(GodownListResultMsg.class,
                Config.WebApiUrl + "GetGodownList?", query, Config.StaffId , Config.AppSecret ,true);
        godListc.setResult();

        if(godListc!=null)
        {
            if(godListc.StatusCode != 200)
            {
                message.obj = godListc.Info;
                eHandler.sendMessage(message);
                return;
            }
            if( godListc.Result == null || godListc.Result.isEmpty())
            {
                message.obj = "无相关数据！";
                eHandler.sendMessage(message);
                return;
            }

        }
        else {
            message.obj = "网络异常！";
            eHandler.sendMessage(message);
            return;
        }
        //保存入库主单
        gBillHelper.SaveGoDownDataFile(godListc.Result);


        //获取主单明细，并保存
        for( GodownEntity godEntity : godListc.Result)
        {
            query.clear();
            query.put("godownId" , godEntity.GodownId);
            GodownBillingListResultMsg godBillListc = ApiHelper.GetHttp(GodownBillingListResultMsg.class,
                    Config.WebApiUrl + "GetGodownBillingListByGodownId?", query, Config.StaffId , Config.AppSecret ,true);
            godBillListc.setResult();
            if(godBillListc!=null)
            {
                if(godBillListc.StatusCode != 200)
                {
                    message.obj = godListc.Info;
                    eHandler.sendMessage(message);
                    return;
                }
                if( godBillListc.Result == null || godBillListc.Result.isEmpty())
                {
                    message.obj = "无相关数据！";
                    eHandler.sendMessage(message);
                    return;
                }

            }
            else {
                message.obj = "网络异常！";
                eHandler.sendMessage(message);
                return;
            }
            gBillHelper.SaveGoDownBillingDataFile(godEntity.GodownCode , godBillListc.Result);
        }
    }

    /**
     * 日期格式 是否有效
     * @param dateValue
     * @return
     */
    private static Date checkDateValid(String dateValue) throws Exception{
        StringBuffer sb = new StringBuffer();
        if (dateValue == null || dateValue.isEmpty()) {
            return null;
        }
        else {
            try {
                SimpleDateFormat fdate = new SimpleDateFormat("yyyy-MM-dd");
                sb.append(dateValue.trim()).insert(4, "-");
                sb.insert(7, "-");
                // fdate.parse( sb.toString() );
                return fdate.parse( sb.toString() );
            } catch(Exception ex)
            {
                throw new Exception("日期格式必须是yyyyMMdd");
            }
        }
    }
}
