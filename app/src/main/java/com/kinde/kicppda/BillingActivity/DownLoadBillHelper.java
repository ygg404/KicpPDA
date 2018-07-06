package com.kinde.kicppda.BillingActivity;

import com.kinde.kicppda.Models.AllotEntity;
import com.kinde.kicppda.Models.GodownEntity;
import com.kinde.kicppda.Models.OrderEntity;
import com.kinde.kicppda.Models.ReturnEntity;
import com.kinde.kicppda.Utils.ApiHelper;
import com.kinde.kicppda.Utils.Config;
import com.kinde.kicppda.Utils.Models.AllotBillingListResultMsg;
import com.kinde.kicppda.Utils.Models.AllotListResultMsg;
import com.kinde.kicppda.Utils.Models.CheckListResultMsg;
import com.kinde.kicppda.Utils.Models.GodownBillingListResultMsg;
import com.kinde.kicppda.Utils.Models.GodownListResultMsg;
import com.kinde.kicppda.Utils.Models.OrderBillingListResultMsg;
import com.kinde.kicppda.Utils.Models.OrderListResultMsg;
import com.kinde.kicppda.Utils.Models.ReturnBillingListResultMsg;
import com.kinde.kicppda.Utils.Models.ReturnListResultMsg;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by Lenovo on 2018/6/29.
 */

public class DownLoadBillHelper {

    //获取入库主单和明细
    public static String downLoadGodownBill(GetBillHelper gBillHelper, String datebegin,String dateend ,String billBarcode)
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
            return ex.getMessage();
        }

        GodownListResultMsg godListc = ApiHelper.GetHttp(GodownListResultMsg.class,
                Config.WebApiUrl + "GetGodownList?", query, Config.StaffId , Config.AppSecret ,true);
        godListc.setResult();

        if(godListc!=null)
        {
            if(godListc.StatusCode != 200)
            {
                return godListc.Info;
            }
            if( godListc.Result == null || godListc.Result.isEmpty())
            {
                return  "无相关数据！";
            }

        }
        else {
            return "网络异常！";
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
                    return godListc.Info;

                }
                if( godBillListc.Result == null || godBillListc.Result.isEmpty())
                {
                    return "无相关数据！";
                }

            }
            else {
                return "网络异常！";
            }
            gBillHelper.SaveGoDownBillingDataFile(godEntity.GodownCode , godBillListc.Result);

        }
        return "单据获取成功！";
    }

    //获取出库主单和明细
    public static String downLoadOrderBill(GetBillHelper gBillHelper, String datebegin,String dateend ,String billBarcode)
    {
        //获取主单
        HashMap<String,String> query = new HashMap<String, String>();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

        try {
            Date dateBegin = checkDateValid(datebegin);
            Date dateEnd = checkDateValid(dateend);
            query.put("orderCode",billBarcode);
            query.put("beginDate", dateBegin==null?"":formatter.format(dateBegin) );
            query.put("endDate", dateEnd==null?"":formatter.format(dateEnd));
            query.put("status", "1");
            query.put("parentAgentId", "0");
        }catch (Exception ex){
            return ex.getMessage();
        }

        OrderListResultMsg orderListc = ApiHelper.GetHttp(OrderListResultMsg.class,
                Config.WebApiUrl + "GetOrderList?", query, Config.StaffId , Config.AppSecret ,true);

        if(orderListc!=null)
        {
            orderListc.setResult();
            if(orderListc.StatusCode != 200)
            {
                return orderListc.Info;
            }
            if( orderListc.Result == null || orderListc.Result.isEmpty())
            {
                return  "无相关数据！";
            }

        }
        else {
            return "网络异常！";
        }
        //保存入库主单
        gBillHelper.SaveOrderDataFile(orderListc.Result);


        //获取主单明细，并保存
        for( OrderEntity orderEntity : orderListc.Result)
        {
            query.clear();
            query.put("orderId" , orderEntity.OrderId);
            OrderBillingListResultMsg ordBillListc = ApiHelper.GetHttp(OrderBillingListResultMsg.class,
                    Config.WebApiUrl + "GetOrderBillingListByOrderId?", query, Config.StaffId , Config.AppSecret ,true);

            if(ordBillListc!=null)
            {
                ordBillListc.setResult();
                if(ordBillListc.StatusCode != 200)
                {
                    return ordBillListc.Info;

                }
                if( ordBillListc.Result == null || ordBillListc.Result.isEmpty())
                {
                    return "无相关数据！";
                }

            }
            else {
                return "网络异常！";
            }
            gBillHelper.SaveOrderBillingDataFile(orderEntity.OrderCode , ordBillListc.Result);

        }
        return "单据获取成功！";
    }

    //获取退货主单和明细
    public static String downLoadReturnBill(GetBillHelper gBillHelper, String datebegin,String dateend ,String billBarcode)
    {
        //获取主单
        HashMap<String,String> query = new HashMap<String, String>();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

        try {
            Date dateBegin = checkDateValid(datebegin);
            Date dateEnd = checkDateValid(dateend);
            query.put("returnCode",billBarcode);
            query.put("beginDate", dateBegin==null?"":formatter.format(dateBegin) );
            query.put("endDate", dateEnd==null?"":formatter.format(dateEnd));
            query.put("status", "1");
        }catch (Exception ex){
            return ex.getMessage();
        }

        ReturnListResultMsg returnListc = ApiHelper.GetHttp(ReturnListResultMsg.class,
                Config.WebApiUrl + "GetReturnList?", query, Config.StaffId , Config.AppSecret ,true);

        if(returnListc!=null)
        {
            returnListc.setResult();

            if(returnListc.StatusCode != 200)
            {
                return returnListc.Info;
            }
            if( returnListc.Result == null || returnListc.Result.isEmpty())
            {
                return  "无相关数据！";
            }

        }
        else {
            return "网络异常！";
        }
        //保存退货主单
        gBillHelper.SaveReturnDataFile(returnListc.Result);


        //获取退货明细，并保存
        for( ReturnEntity returnEntity : returnListc.Result)
        {
            query.clear();
            query.put("returnId" , returnEntity.ReturnId);
            ReturnBillingListResultMsg returnBillListc = ApiHelper.GetHttp(ReturnBillingListResultMsg.class,
                    Config.WebApiUrl + "GetReturnBillingListByReturnId?", query, Config.StaffId , Config.AppSecret ,true);
            returnBillListc.setResult();
            if(returnBillListc!=null)
            {
                if(returnBillListc.StatusCode != 200)
                {
                    return returnBillListc.Info;

                }
                if( returnBillListc.Result == null || returnBillListc.Result.isEmpty())
                {
                    return "无相关数据！";
                }

            }
            else {
                return "网络异常！";
            }
            gBillHelper.SaveReturnBillingDataFile(returnEntity.ReturnCode , returnBillListc.Result);

        }
        return "单据获取成功！";
    }

    //获取调拨主单和明细
    public static String downLoadAllotBill(GetBillHelper gBillHelper, String datebegin,String dateend ,String billBarcode)
    {
        //获取主单
        HashMap<String,String> query = new HashMap<String, String>();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

        try {
            Date dateBegin = checkDateValid(datebegin);
            Date dateEnd = checkDateValid(dateend);
            query.put("allotCode",billBarcode);
            query.put("beginDate", dateBegin==null?"":formatter.format(dateBegin) );
            query.put("endDate", dateEnd==null?"":formatter.format(dateEnd));
            query.put("status", "1");
        }catch (Exception ex){
            return ex.getMessage();
        }

        AllotListResultMsg allotListc = ApiHelper.GetHttp(AllotListResultMsg.class,
                Config.WebApiUrl + "GetAllotList?", query, Config.StaffId , Config.AppSecret ,true);

        if(allotListc!=null)
        {
            allotListc.setResult();

            if(allotListc.StatusCode != 200)
            {
                return allotListc.Info;
            }
            if( allotListc.Result == null || allotListc.Result.isEmpty())
            {
                return  "无相关数据！";
            }

        }
        else {
            return "网络异常！";
        }
        //保存调拨主单
        gBillHelper.SaveAllotDataFile(allotListc.Result);


        //获取调拨明细，并保存
        for( AllotEntity allotEntity : allotListc.Result)
        {
            query.clear();
            query.put("allotId" , allotEntity.AllotId);
            AllotBillingListResultMsg allotBillListc = ApiHelper.GetHttp(AllotBillingListResultMsg.class,
                    Config.WebApiUrl + "GetAllotBillingListByAllotId?", query, Config.StaffId , Config.AppSecret ,true);
            allotBillListc.setResult();
            if(allotBillListc!=null)
            {
                if(allotBillListc.StatusCode != 200)
                {
                    return allotBillListc.Info;

                }
                if( allotBillListc.Result == null || allotBillListc.Result.isEmpty())
                {
                    return "无相关数据！";
                }

            }
            else {
                return "网络异常！";
            }
            gBillHelper.SaveAllotBillingDataFile(allotEntity.AllotCode , allotBillListc.Result);

        }
        return "单据获取成功！";
    }

    //获取盘点主单和明细
    public static String downLoadCheckBill(GetBillHelper gBillHelper, String datebegin,String dateend ,String billBarcode)
    {
        //获取主单
        HashMap<String,String> query = new HashMap<String, String>();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

        try {
            Date dateBegin = checkDateValid(datebegin);
            Date dateEnd = checkDateValid(dateend);
            query.put("checkCode",billBarcode);
            query.put("beginDate", dateBegin==null?"":formatter.format(dateBegin) );
            query.put("endDate", dateEnd==null?"":formatter.format(dateEnd));
            query.put("status", "1");
        }catch (Exception ex){
            return ex.getMessage();
        }

        CheckListResultMsg checkListc = ApiHelper.GetHttp(CheckListResultMsg.class,
                Config.WebApiUrl + "GetCheckList?", query, Config.StaffId , Config.AppSecret ,true);

        if(checkListc!=null)
        {
            checkListc.setResult();

            if(checkListc.StatusCode != 200)
            {
                return checkListc.Info;
            }
            if( checkListc.Result == null || checkListc.Result.isEmpty())
            {
                return  "无相关数据！";
            }

        }
        else {
            return "网络异常！";
        }
        //保存调拨主单
        gBillHelper.SaveCheckDataFile(checkListc.Result);

        return "单据获取成功！";
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
