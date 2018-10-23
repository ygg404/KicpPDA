package com.kinde.kicppda.BillingActivity;

import com.j256.ormlite.dao.ForeignCollection;
import com.kinde.kicppda.MDAO.GodownEntityDAO;
import com.kinde.kicppda.Models.AllotEntity;
import com.kinde.kicppda.Models.GodownBillingEntity;
import com.kinde.kicppda.Models.GodownEntity;
import com.kinde.kicppda.Models.GodownXEntity;
import com.kinde.kicppda.Models.OrderEntity;
import com.kinde.kicppda.Models.ReturnEntity;
import com.kinde.kicppda.Utils.ApiHelper;
import com.kinde.kicppda.Utils.Config;
import com.kinde.kicppda.Utils.Models.AllotBillingListResultMsg;
import com.kinde.kicppda.Utils.Models.AllotListResultMsg;
import com.kinde.kicppda.Utils.Models.CheckListResultMsg;
import com.kinde.kicppda.Utils.Models.GodownBillingListResultMsg;
import com.kinde.kicppda.Utils.Models.GodownListResultMsg;
import com.kinde.kicppda.Utils.Models.GroupXBillingListResultMsg;
import com.kinde.kicppda.Utils.Models.GroupXListResultMsg;
import com.kinde.kicppda.Utils.Models.OrderBillingListResultMsg;
import com.kinde.kicppda.Utils.Models.OrderListResultMsg;
import com.kinde.kicppda.Utils.Models.ReturnBillingListResultMsg;
import com.kinde.kicppda.Utils.Models.ReturnListResultMsg;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import static com.kinde.kicppda.Utils.ApiHelper.checkDateValid;

/**
 * Created by Lenovo on 2018/6/29.
 */

public class DownLoadBillHelper {

    //获取入库主单和明细
    public static String downLoadGodownBill (GetBillHelper gBillHelper, String datebegin,String dateend ,String billBarcode) throws Exception
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

            GodownListResultMsg godListc = ApiHelper.GetHttp(GodownListResultMsg.class,
                    Config.WebApiUrl + "GetGodownList?", query, Config.StaffId , Config.AppSecret ,true);
            godListc.setResult();


            if(godListc.StatusCode != 200)
            {
                throw new Exception(godListc.Info);
            }
            if( godListc.Result == null || godListc.Result.isEmpty())
            {
                throw new Exception("无相关数据！");
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

                if(godBillListc.StatusCode != 200)
                {
                    throw new Exception(godListc.Info);
                }
                if( godBillListc.Result == null || godBillListc.Result.isEmpty())
                {
                    continue;
                }

                gBillHelper.SaveGoDownBillingDataFile(godEntity , godBillListc.Result);

            }


        }catch (Exception ex){
            throw new Exception( ex.getMessage());
        }


        return "单据获取成功！";
    }

    //获取出库主单和明细
    public static String downLoadOrderBill(GetBillHelper gBillHelper, String datebegin,String dateend ,String billBarcode) throws Exception
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
            OrderListResultMsg orderListc = ApiHelper.GetHttp(OrderListResultMsg.class,
                    Config.WebApiUrl + "GetOrderList?", query, Config.StaffId , Config.AppSecret ,true);

            orderListc.setResult();
            if(orderListc.StatusCode != 200)
            {
                throw new Exception( orderListc.Info );
            }
            if( orderListc.Result == null || orderListc.Result.isEmpty())
            {
                throw new Exception( "无相关数据！");
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
                ordBillListc.setResult();

                if(ordBillListc.StatusCode != 200)
                {
                    throw new Exception( ordBillListc.Info );
                }
                if( ordBillListc.Result == null || ordBillListc.Result.isEmpty())
                {
                    continue;
                }

                gBillHelper.SaveOrderBillingDataFile(orderEntity , ordBillListc.Result);
            }
        }catch (Exception ex){
            throw new Exception( ex.getMessage() );
        }
        return "单据获取成功！";
    }

    //获取退货主单和明细
    public static String downLoadReturnBill(GetBillHelper gBillHelper, String datebegin,String dateend ,String billBarcode) throws Exception
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

            ReturnListResultMsg returnListc = ApiHelper.GetHttp(ReturnListResultMsg.class,
                    Config.WebApiUrl + "GetReturnList?", query, Config.StaffId , Config.AppSecret ,true);
            returnListc.setResult();

            if(returnListc.StatusCode != 200)
            {
                throw new Exception( returnListc.Info );
            }
            if( returnListc.Result == null || returnListc.Result.isEmpty())
            {
                throw new Exception( "无相关数据！");
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

                if(returnBillListc.StatusCode != 200)
                {
                    throw new Exception( returnBillListc.Info );
                }
                if( returnBillListc.Result == null || returnBillListc.Result.isEmpty())
                {
                    continue;
                }

                gBillHelper.SaveReturnBillingDataFile(returnEntity , returnBillListc.Result);
            }
        }catch (Exception ex){
            throw new Exception( ex.getMessage() );
        }
        return "单据获取成功！";
    }

    //获取调拨主单和明细
    public static String downLoadAllotBill(GetBillHelper gBillHelper, String datebegin,String dateend ,String billBarcode) throws Exception
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
            AllotListResultMsg allotListc = ApiHelper.GetHttp(AllotListResultMsg.class,
                    Config.WebApiUrl + "GetAllotList?", query, Config.StaffId , Config.AppSecret ,true);
            allotListc.setResult();

            if(allotListc.StatusCode != 200)
            {
                throw new Exception( allotListc.Info );
            }
            if( allotListc.Result == null || allotListc.Result.isEmpty())
            {
                throw new Exception( "无相关数据！" );
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

                if(allotBillListc.StatusCode != 200)
                {
                    throw new Exception( allotBillListc.Info );
                }
                if( allotBillListc.Result == null || allotBillListc.Result.isEmpty())
                {
                    continue;
                }
                gBillHelper.SaveAllotBillingDataFile(allotEntity , allotBillListc.Result);

            }
        }catch (Exception ex){
            throw new Exception( ex.getMessage() );
        }
        return "单据获取成功！";
    }

    //获取盘点主单
    public static String downLoadCheckBill(GetBillHelper gBillHelper, String datebegin,String dateend ,String billBarcode) throws Exception
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

            CheckListResultMsg checkListc = ApiHelper.GetHttp(CheckListResultMsg.class,
                    Config.WebApiUrl + "GetCheckList?", query, Config.StaffId , Config.AppSecret ,true);
            checkListc.setResult();

            if(checkListc.StatusCode != 200)
            {
                throw new Exception( checkListc.Info );
            }
            if( checkListc.Result == null || checkListc.Result.isEmpty())
            {
                throw new Exception( "无相关数据！" );
            }

            //保存盘点主单
            gBillHelper.SaveCheckDataFile(checkListc.Result);
        }catch (Exception ex){
            throw new Exception( ex.getMessage() );
        }

        return "单据获取成功！";
    }

    //获取关联箱主单和明细
    public static String downLoadGroupXBill(GetBillHelper gBillHelper, String datebegin,String dateend ,String billBarcode) throws Exception
    {
        //获取主单
        HashMap<String,String> query = new HashMap<String, String>();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

        try {
            Date dateBegin = checkDateValid(datebegin);
            Date dateEnd = checkDateValid(dateend);
            query.put("groupXCode",billBarcode);
            query.put("beginDate", dateBegin==null?"":formatter.format(dateBegin) );
            query.put("endDate", dateEnd==null?"":formatter.format(dateEnd));
            query.put("status", "1");

            GroupXListResultMsg groupxListc = ApiHelper.GetHttp(GroupXListResultMsg.class,
                    Config.WebApiUrl + "GetGroupXList?", query, Config.StaffId , Config.AppSecret ,true);
            groupxListc.setResult();

            if(groupxListc.StatusCode != 200)
            {
                throw new Exception( groupxListc.Info );
            }
            if( groupxListc.Result == null || groupxListc.Result.isEmpty())
            {
                throw new Exception(  "无相关数据！" );
            }
            //保存调拨主单
            gBillHelper.SaveGroupXDataFile(groupxListc.Result);

            //获取调拨明细，并保存
            for( GodownXEntity gxEntity : groupxListc.Result)
            {
                query.clear();
                query.put("godownXId" , gxEntity.GodownXId);
                GroupXBillingListResultMsg gxBillListc = ApiHelper.GetHttp(GroupXBillingListResultMsg.class,
                        Config.WebApiUrl + "GetGroupXBillingListByGodownXId?", query, Config.StaffId , Config.AppSecret ,true);
                gxBillListc.setResult();

                if(gxBillListc.StatusCode != 200)
                {
                    throw new Exception( gxBillListc.Info );
                }
                if( gxBillListc.Result == null || gxBillListc.Result.isEmpty())
                {
                    continue;
                }

                gBillHelper.SaveGroupXBillingDataFile(gxEntity , gxBillListc.Result);
            }
        }catch (Exception ex){
            throw new Exception( ex.getMessage() );
        }
        return "单据获取成功！";
    }


}
