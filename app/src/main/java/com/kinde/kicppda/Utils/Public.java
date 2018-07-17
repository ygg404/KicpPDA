package com.kinde.kicppda.Utils;

/**
 * Created by YGG on 2018/7/2.
 */


public class Public {
    public static final String GODOWN_MAIN_TABLE = "godownMainBill";    //入库主单
    public static final String ORDER_MAIN_TABLE = "orderMainBill";     //订单主单
    public static final String RETURN_MAIN_TABLE = "returnMainBill";   //退货主单
    public static final String ALLOT_MAIN_TABLE = "allotMainBill";     //调拨主单
    public static final String CHECK_MAIN_TABLE = "checkMainBill";     //盘点主单
    public static final String GodownX_MAIN_TABLE = "godownxMainBill";   //关联箱主单

    public static final String B_INVENTORY_File = "BaseInventory";  //产品资料
    public static final String B_CUSTOMER_File = "BaseCustomer";    //客户资料
    public static final String B_WAREHOUSE_File = "BaseWarehouse"; //仓库资料

    public static final String GodownBillingType = "-PD_Billing";   //入库明细单后缀
    public static final String OrderBillingType  = "-OD_Billing";   //出库明细单后缀
    public static final String ReturnBillingType = "-RD_Billing";   //退货明细单后缀
    public static final String AllotBillingType  = "-AD_Billing";   //调拨明细单后缀
    public static final String GodownXBillingType = "-XD_Billing";   //关联箱明细单后缀

    public static final String GodownScanType = "-PD_Scan";   //入库扫码单后缀
    public static final String OrderScanType  = "-OD_Scan";   //出库扫码单后缀
    public static final String ReturnScanType = "-RD_Scan";   //退货扫码单后缀
    public static final String AllotScanType  = "-AD_Scan";   //调拨扫码单后缀
    public static final String CheckScanType  = "-CD_Scan";   //调拨扫码单后缀
    public static final String GodownXScanType  = "-XD_Scan";   //关联箱扫码单后缀
}
