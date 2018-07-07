package com.kinde.kicppda.Utils.Enum;

/**
 * Created by Lenovo on 2018/6/2.
 */

/**
 * 获取单据的种类
 */
public enum BillTypeEnum {
    intype(1),       //入库
    ordertype(2),         //发货
    returntype(3),  //退货
    allottype(4),//调拨
    checktype(5), //盘点
    groupxtype(6); //关联箱

    private final int value;
    //构造方法必须是private或者默认
    private BillTypeEnum(int value) {
        this.value = value;
    }

    public int getValue() {
        return this.value;
    }

    public BillTypeEnum valueOf(int value) {
        switch (value) {
            case 1:
                return BillTypeEnum.intype;
            case 2:
                return BillTypeEnum.ordertype;
            case 3:
                return BillTypeEnum.returntype;
            case 4:
                return BillTypeEnum.allottype;
            case 5:
                return BillTypeEnum.checktype;
            case 6:
                return BillTypeEnum.groupxtype;
            default:
                return null;
        }
    }

    public String getTypeName(){
        switch (value) {
            case 1:
                return "入库";
            case 2:
                return "发货";
            case 3:
                return "退货";
            case 4:
                return "调拨";
            case 5:
                return "盘点";
            case 6:
                return "生产关联箱";
            default:
                return null;
        }
    }


}
