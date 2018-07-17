package com.kinde.kicppda.QueryActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Message;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.SimpleAdapter;
import android.widget.Spinner;

import com.kinde.kicppda.Models.AllotBillingEntity;
import com.kinde.kicppda.R;
import com.kinde.kicppda.Utils.Adialog;
import com.kinde.kicppda.Utils.ApiHelper;
import com.kinde.kicppda.Utils.Config;
import com.kinde.kicppda.Utils.Models.AllotScanDeleteResultMsg;
import com.kinde.kicppda.Utils.ProgersssDialog;
import com.kinde.kicppda.Utils.Public;
import com.kinde.kicppda.Utils.SQLiteHelper.DeleteBillHelper;
import com.kinde.kicppda.Utils.SQLiteHelper.ScanCreateHelper;
import com.kinde.kicppda.Utils.SQLiteHelper.TableQueryHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;

/**
 * Created by Lenovo on 2018/7/16.
 */

public class Query_Allot_Activity extends Activity implements View.OnClickListener{

    private String billNo   = "";           //单号
    private String billId   = "";           //单据id
    private String productId = "";          //产品id
    private List<AllotBillingEntity> allotbillingList = new ArrayList<>();//调拨单据明细
    private String MainFileName = "";//主单文件
    private String EntryFileName = "";//明细文件
    private String ScanFileName = "";//扫描文件

    private Adialog mAdialog;            //警告提示窗口
    private Spinner cmb_plist;              //单据号选择项
    private EditText tbWarehouseIn;            //调入仓名称
    private EditText tbWarehouseOut;            //调出仓名称
    private EditText tbProduct;             //调拨产品
    private EditText tbBarcode;             //条码
    private ListView dataGrid;             //条码查询列表
    private Button btnQuery;    //查询按钮
    private Button btnDel;      //删除按钮
    private Button btnQuit;     //退出按钮

    private ProgersssDialog mProgersssDialog;
    private ArrayAdapter<String> spinAdapter;  //单据号spinner的适配器
    private DeleteBillHelper mDelBill;      //删除单据
    private ScanCreateHelper mCreateBill;   //创建单据
    private TableQueryHelper mQueryBill;     //查询单据

    private List<String> allotNumList;      //调拨单据编号列表

    private View layout;
    private PopupWindow popupWindow;
    private ListView mListView;
    private SimpleAdapter digAdapter;
    private SimpleAdapter barAdapter;
    private List<HashMap<String, Object>> ProductInfo = new ArrayList<HashMap<String,Object>>();   //产品信息
    private List<HashMap<String, Object>> ScanInfo = new ArrayList<HashMap<String,Object>>();   //扫码表信息

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.query_allot);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        bindView();
    }

    private void bindView()
    {
        mDelBill = new DeleteBillHelper(Query_Allot_Activity.this);
        mCreateBill = new ScanCreateHelper(Query_Allot_Activity.this);
        mQueryBill = new TableQueryHelper(Query_Allot_Activity.this);

        cmb_plist = (Spinner)findViewById(R.id.num_spinner);
        tbWarehouseIn = (EditText)findViewById(R.id.tbWarehouseIn);
        tbWarehouseOut = (EditText) findViewById(R.id.tbWarehouseOut);
        tbProduct = (EditText)findViewById(R.id.in_product);
        dataGrid = (ListView)findViewById(R.id.query_view);
        tbBarcode = (EditText)findViewById(R.id.tbBarcode);
        btnQuery = (Button)findViewById(R.id.btn_query);
        btnQuit = (Button)findViewById(R.id.btn_quit);
        btnDel  = (Button)findViewById(R.id.btn_delete);
        btnQuery.setOnClickListener(this);
        btnQuit.setOnClickListener(this);
        btnDel.setOnClickListener(this);

        tbProduct.requestFocus();

        //初始化单据选项列表
        allotNumList = mQueryBill.getBillNum(Public.ALLOT_MAIN_TABLE , "AllotCode");
        spinAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, allotNumList);
        spinAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        cmb_plist.setAdapter(spinAdapter);
        cmb_plist.setOnItemSelectedListener(
                new Spinner.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                        billNo = cmb_plist.getSelectedItem().toString().trim();
                        SetFilePath(billNo);
                        billId = mQueryBill.getKeyValue("AllotId" , MainFileName , "AllotCode",billNo);
                        tbWarehouseIn.setText(  mQueryBill.getKeyValue("WarehouseNameIn", MainFileName ,"AllotCode",billNo )  );
                        tbWarehouseOut.setText(  mQueryBill.getKeyValue("WarehouseNameOut", MainFileName ,"AllotCode",billNo) );
                        tbProduct.setText("");
                        tbBarcode.setText("");
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> arg0) {
                    }
                });
        //产品按下enter键 弹出产品查询PopupWindow
        tbProduct.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(keyCode == KeyEvent.KEYCODE_ENTER && event.getAction()==KeyEvent.ACTION_DOWN) {
                    //TODO:返回键按下时要执行的操作
                    queryPopInit(v);
                    return true;
                }
                return false;
            }
        });


        //初始化条码扫码列表
        //创建SimpleAdapter适配器将数据绑定到item显示控件上
        barAdapter = new SimpleAdapter(Query_Allot_Activity.this, ScanInfo, R.layout.query_codegrid_item,
                new String[]{"iBarcode", "iProduct", "iCount"}, new int[]{R.id.iBarcode, R.id.iProduct, R.id.iCount});
        dataGrid = (ListView) findViewById(R.id.query_view);

    }

    //产品选项 PopupWindow 初始化
    private void queryPopInit(View v){
        LayoutInflater inflater=LayoutInflater.from(v.getContext());
        //R.layout.scale_view 这个是里pop里放的XML文件
        layout=inflater.inflate(R.layout.query_listview, null);
        //findViewById(R.id.mainlayout)   这个是你的POP要放的View,后面是宽和高
        popupWindow = new PopupWindow(findViewById(R.id.query_order),MATCH_PARENT,MATCH_PARENT,true);
        popupWindow.setTouchable(true);
        layout.setFocusableInTouchMode(true);
        //设置pop的内容
        popupWindow.setContentView(layout);
        //这个是显示位置
        popupWindow.showAtLocation(v, Gravity.CENTER,0,0);

        layout.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction()==KeyEvent.ACTION_DOWN){
                    popupWindow.dismiss();
                    return true;
                }
                return false;
            }
        });

        ProductInfo.clear();
        mListView = (ListView)layout.findViewById(R.id.in_list_view);
        //创建SimpleAdapter适配器将数据绑定到item显示控件上
        digAdapter = new SimpleAdapter(Query_Allot_Activity.this, ProductInfo, R.layout.query_list_item,
                new String[]{"qcode", "qname", "qln","qpr"}, new int[]{R.id.qcode, R.id.qname, R.id.qln,R.id.qpr});

        String keyValue = tbProduct.getText().toString();
        List<String[]> BillInfoList = mQueryBill.getProductMessage( EntryFileName , keyValue);
        for( String[] billInfo : BillInfoList){
            HashMap<String, Object> item = new HashMap<String, Object>();
            item.put("qid", billInfo[0]);
            item.put("qcode", billInfo[1]);
            item.put("qname", billInfo[2]);
            ProductInfo.add(item);
        }

        //实现列表的显示
        digAdapter.notifyDataSetChanged();
        mListView.setAdapter(digAdapter);

        //列表点击选项
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ListView listView = (ListView) parent;
                HashMap<String, Object> productData = (HashMap<String, Object>) listView.getItemAtPosition(position);

                productId = productData.get("qid").toString();
                tbProduct.setText( productData.get("qname").toString() );
                popupWindow.dismiss();

            }
        });
    }


    //设置单据保存表
    private void SetFilePath(String billNo)
    {
        MainFileName = Public.ORDER_MAIN_TABLE;
        EntryFileName =  billNo  + Public.OrderBillingType;
        ScanFileName = billNo + Public.OrderScanType;
    }

    /**
     * 远程删除线程
     */
    Runnable PostDeleteRun = new Runnable() {
        @Override
        public void run() {
            Message mess =  new Message();
            for( HashMap<String ,Object> scanAttr :ScanInfo) {
                try {
                    int staffId = Config.StaffId;
                    String appSecret = Config.AppSecret;
                    HashMap<String, String> query = new HashMap<String, String>();
                    query.put("allotId", billId);
                    query.put("serialno", scanAttr.get("iBarcode").toString());

                    AllotScanDeleteResultMsg delResult = ApiHelper.GetHttp(AllotScanDeleteResultMsg.class, Config.WebApiUrl + "OrderScanDelete?",
                            query, staffId, appSecret, true);
                    delResult.setResult();

                    if (delResult.StatusCode != 200) {
                        //有异常忽略继续
                        continue;
                    }
                    ScanInfo.remove(scanAttr);
                } catch (Exception ex) {
                    mess.obj = ex.getMessage();
                    // eHandler.sendMessage(mess);
                }
            }
            mess.what = 1;
            //eHandler.sendMessage(mess);
            //mProgersssDialog.cancel();
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            //查询按钮事件
            case R.id.btn_query:
                ScanInfo.clear();
                List<String[]>scanInfoList = mQueryBill.ScanQuery( ScanFileName);
                for( String[] scanInfo : scanInfoList){
                    HashMap<String, Object> item = new HashMap<String, Object>();
                    item.put("iBarcode", scanInfo[0]);                      //条形码
                    item.put("iProduct", mQueryBill.getKeyValue("ProductName", EntryFileName,"ProductId",scanInfo[1])); //产品名
                    item.put("iCount", scanInfo[2]);  //数量
                    ScanInfo.add(item);
                }

                //实现列表的显示
                barAdapter.notifyDataSetChanged();
                dataGrid.setAdapter(barAdapter);
                //列表点击选项
                dataGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        ListView listView = (ListView) parent;
                        HashMap<String, Object> barData = (HashMap<String, Object>) listView.getItemAtPosition(position);

                    }
                });
                break;
            //删除按钮事件
            case R.id.btn_delete:
                if (ScanInfo.size() <1)
                {
                    mAdialog.warnDialog("没有可删除的数据，请查询出你要删除的数据！");
                    break;
                }

                AlertDialog.Builder builder = new AlertDialog.Builder(Query_Allot_Activity.this);
                AlertDialog alert = builder.setIcon(R.mipmap.success)
                        .setTitle("系统提示：")
                        .setMessage("确定删除查询出来的相关数据吗?")
                        .setCancelable(false)
                        .setNegativeButton("取消",new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                return;
                            }
                        })
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ;
                            }
                        }).create();             //创建AlertDialog对象
                alert.show();                  //显示对话框
                mProgersssDialog = new ProgersssDialog(Query_Allot_Activity.this);
                mProgersssDialog.setMsg("删除中");
                new Thread(PostDeleteRun).start();
                break;
            //退出按钮事件
            case R.id.btn_quit:
                finish();
                break;
            default:
                break;
        }
    }

}

