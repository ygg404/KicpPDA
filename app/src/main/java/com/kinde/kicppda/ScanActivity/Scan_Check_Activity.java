package com.kinde.kicppda.ScanActivity;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.imscs.barcodemanager.BarcodeManager;
import com.imscs.barcodemanager.BarcodeManager.OnEngineStatus;
import com.imscs.barcodemanager.Constants;
import com.imscs.barcodemanager.ScanTouchManager;
import com.j256.ormlite.dao.ForeignCollection;
import com.kinde.kicppda.MDAO.CheckEntityDAO;
import com.kinde.kicppda.MDAO.CheckScanDAO;
import com.kinde.kicppda.MDAO.ProductEntityDAO;
import com.kinde.kicppda.Models.CheckEntity;
import com.kinde.kicppda.Models.CheckScanEntity;
import com.kinde.kicppda.Models.ProductEntity;
import com.kinde.kicppda.R;
import com.kinde.kicppda.Utils.Adialog;
import com.kinde.kicppda.Utils.ApiHelper;
import com.kinde.kicppda.Utils.Config;
import com.kinde.kicppda.Utils.Models.CheckScanSaveResultMsg;
import com.kinde.kicppda.Utils.ProgersssDialog;
import com.kinde.kicppda.decodeLib.DecodeBaseActivity;
import com.kinde.kicppda.decodeLib.DecodeSampleApplication;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;

/**
 * Created by YGG on 2018/7/7.
 */

public class Scan_Check_Activity extends DecodeBaseActivity implements  View.OnClickListener,OnEngineStatus {

    private Adialog aDialog;               //警告提示窗口
    private Spinner cmb_plist;             //单据号选择项
    private EditText tbBillDate;            //单据日期
    private EditText tbWarehouse;           //仓库名称
    private EditText tbProduct;             //发货产品
    private EditText tbLN;                  //产品批次
    private EditText tbPR;                  //产品日期
    private EditText tbBarcode;               //当前条码
    private String billNo   = "";           //单号
    private String billId   = "";           //单据id
    private ImageView mGoback;              //返回键

    private ArrayAdapter<String> spinAdapter;       //单据号spinner的适配器
    private Button btnLock;                 //锁定按钮
    private Button btnUpload;               //上传按钮
    private Button btnDelBill;              //删除按钮
    private String productId = "";      //盘点产品ID
    //private List<OrderBillingEntity> orderbillingList = new ArrayList<OrderBillingEntity>();//出库单据明细
    private CheckEntity checkEntity;    //选定的主单

	private ProgersssDialog mProgersssDialog;

//    private int curPreSet = 0;          //当前预设
    private int curCount = 0;           //当前数量
//    private int billPreset = 0;         //本单预设
    private int billCount = 0;          //本单数量
    private List<String> barcode_exit = new ArrayList<>(); //已扫描的条码列表
//    private TextView lbCurPreset;
//    private TextView lbBillPreset;
    private TextView lbCurCount;
    private TextView lbBillCount;

    private List<String> checkNumList;         //盘点单据编号列表
	private Context mContext;
    private View layout;
    private PopupWindow popupWindow;
    private ListView mListView;
    private SimpleAdapter digAdapter;
    private List<HashMap<String, Object>> ProductInfo = new ArrayList<HashMap<String,Object>>();   //产品信息
    private List<HashMap<String, Object>> ScanInfoList = new ArrayList<HashMap<String,Object>>();   //扫码表信息


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scan_check);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        windowManagerParams = ((DecodeSampleApplication) getApplication()).getWindowParams();

        //initialize ScanTouch and set clicklistener
        mScanTouchManager = new ScanTouchManager(getApplicationContext(), windowManagerParams);
        mScanTouchManager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                doScanInBackground();
            }
        });
        mScanTouchManager.setVisibility(View.INVISIBLE);


        initView();

        mDoDecodeThread = new DoDecodeThread();
        mDoDecodeThread.start();
    }



    private void initView(){
		mContext = this.getApplicationContext();
        aDialog = new Adialog(Scan_Check_Activity.this);

        bLockMode = false;
        btnLock = (Button)findViewById(R.id.btn_Lock);
        btnUpload = (Button)findViewById(R.id.btn_Upload);
        btnDelBill = (Button)findViewById(R.id.btn_DelBill);

        cmb_plist = (Spinner)findViewById(R.id.num_spinner);
        tbBillDate = (EditText)findViewById(R.id.bill_date);
        tbWarehouse = (EditText)findViewById(R.id.tbWarehouse);
        tbProduct = (EditText)findViewById(R.id.tbProduct);
        tbLN = (EditText)findViewById(R.id.tbLN);
        tbPR = (EditText)findViewById(R.id.tbPR);
        mGoback = (ImageView)findViewById(R.id.go_back);
        tbBarcode = (EditText)findViewById(R.id.tbBarcode);
//        lbCurPreset = (TextView)findViewById(R.id.lbCurPreset);
//        lbBillPreset = (TextView)findViewById(R.id.lbBillPreset);
        lbCurCount = (TextView)findViewById(R.id.lbCurCount);
        lbBillCount = (TextView)findViewById(R.id.lbBillCount);

        mGoback.setOnClickListener(this);
        btnLock.setOnClickListener(this);
        btnDelBill.setOnClickListener(this);
        btnUpload.setOnClickListener(this);
        ViewClear();

        //初始化单据选项列表
        checkNumList = new CheckEntityDAO(mContext).GetCheckCodeList();
        spinAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, checkNumList);
        spinAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        cmb_plist.setAdapter(spinAdapter);
        cmb_plist.setOnItemSelectedListener(
                new Spinner.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                        ViewClear();
                        billNo = cmb_plist.getSelectedItem().toString().trim();
                        BillingLoad();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> arg0) {
                    }
                });

        EnterListShow(tbProduct);
        tbProduct.requestFocus();
    }

    //产品选项 PopupWindow 初始化
    private void queryPopInit(View v){
        LayoutInflater inflater=LayoutInflater.from(v.getContext());
        //R.layout.scale_view 这个是里pop里放的XML文件
        layout=inflater.inflate(R.layout.query_listview, null);
        //findViewById(R.id.mainlayout)   这个是你的POP要放的View,后面是宽和高
        popupWindow = new PopupWindow(findViewById(R.id.scan_check),MATCH_PARENT,MATCH_PARENT,true);
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
        digAdapter = new SimpleAdapter(Scan_Check_Activity.this, ProductInfo, R.layout.query_list_items,
                new String[]{"qcode", "qname" }, new int[]{R.id.qcode, R.id.qname});

        String keyValue = tbProduct.getText().toString();
		List<ProductEntity> productList = new ProductEntityDAO(mContext).queryForAll();
        for( ProductEntity product : productList){
			if(product.EnCode.contains(keyValue) || product.FullName.contains(keyValue)){
			    HashMap<String, Object> item = new HashMap<String, Object>();
	            item.put("qid", product.ProductId);
	            item.put("qcode", product.EnCode);
	            item.put("qname", product.FullName);
	            ProductInfo.add(item);
			}

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
                tbPR.setText("");
                tbLN.setText("");
                popupWindow.dismiss();
                LoadBarcodes();
            }
        });
    }

    //明细加载
    private void BillingLoad(){

        //获取主单信息
        checkEntity = new CheckEntityDAO(mContext).queryForBillNo(billNo);
        billId = checkEntity.CheckId;
        tbBillDate.setText( checkEntity.CheckDate == null?"":formatter.format(checkEntity.CheckDate) );
        tbWarehouse.setText( checkEntity.WarehouseName );
    }
    private void ViewClear(){
        tbWarehouse.setText("");
        tbBillDate.setText("");
        tbProduct.setText("");
        tbLN.setText("");
        tbPR.setText("");
//        lbCurPreset.setText("0");
//        lbBillPreset.setText("0");
        lbCurCount.setText("0");
        lbBillCount.setText("0");

    }

    //解除锁定
    private void Unlock()
    {
        cmb_plist.setEnabled(true);
        tbWarehouse.setEnabled(true);
        tbBillDate.setEnabled(true);
        tbProduct.setEnabled(true);
        btnDelBill.setEnabled(true);
        btnUpload.setEnabled(true);

        btnLock.setText("锁定扫描");
        bLockMode = false;
        mScanTouchManager.setVisibility(View.INVISIBLE);
    }

    //锁定扫描
    private void Lock()
    {
        cmb_plist.setEnabled(false);
        tbWarehouse.setEnabled(false);
        tbBillDate.setEnabled(false);
        tbProduct.setEnabled(false);
        btnDelBill.setEnabled(false);
        btnUpload.setEnabled(false);

        btnLock.setText("解除锁定");
        bLockMode = true;
        mScanTouchManager.setVisibility(View.VISIBLE);
    }

    //锁定扫描事件
    private void LockEvent(){
        if (bLockMode)
        {
            Unlock();
        }
        else
        {
            if (cmb_plist.getSelectedItem() == null )
            {
                aDialog.warnDialog( "请选择单号！" );
                return;
            }
            if (tbBillDate.getText().toString().isEmpty())
            {
                aDialog.warnDialog( "单据日期不能为空，请重新选择单据！" );
                return;
            }
            if (tbWarehouse.getText().toString().isEmpty())
            {
                aDialog.warnDialog( "盘点仓库不能为空，请重新选择单据！" );
                return;
            }

            if (tbProduct.getText().toString().isEmpty())
            {
                aDialog.warnDialog("请选择盘点产品！");
                return;
            }

            LoadBarcodes();
//            if (billPreset == billCount)
//            {
////                D300SysUI.PlaySound(Public.SoundPath);
//                aDialog.warnDialog("单据扫描完成！");
//                return;
//            }
//            if (curCount == curPreSet)
//            {
////                D300SysUI.PlaySound(Public.SoundPath);
//                aDialog.warnDialog("当前产品扫描完成!");
//                return;
//            }

            Lock();
        }
    }

    // 加载已扫描条码到内存
    private void LoadBarcodes()
    {
        barcode_exit.clear();

        curCount = 0;
        billCount = 0;
        int Qty = 0;
        String curProductId;
        List<CheckScanEntity> scanInfoList = new CheckScanDAO(mContext).queryForAll();
        for(CheckScanEntity scanInfo : scanInfoList){
            barcode_exit.add(scanInfo.SerialNo);
            curProductId = scanInfo.ProductId;
            Qty = scanInfo.Qty;
            if ( curProductId.equals( productId ) )
            {
                curCount += Qty;
            }
            billCount += Qty;
        }

        lbCurCount.setText( String.valueOf(curCount) );
        lbBillCount.setText( String.valueOf(billCount) );
    }

    Handler eHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    //do something,refresh UI;
                    aDialog.failDialog(msg.obj.toString());
                    //登录加载dialog关闭
                    mProgersssDialog.cancel();
                    break;
                case 1:
                    lbCurCount.setText( String.valueOf(curCount) );
                    lbBillCount.setText( String.valueOf(billCount) );
//                    if (billCount == billPreset)
//                    {
//                      //  D300SysUI.PlaySound(Public.SoundPath);
//                        aDialog.okDialog("本单已扫描完成!");
//                        break;
//                    }
//                    if (curCount == curPreSet)
//                    {
//                        aDialog.okDialog("当前产品扫描完成!");
//                    }
                    break;
                default:
                    break;
            }
        }
    };

    /**
     *盘点保存明细
     */
    Runnable PostCheckScanRun = new Runnable() {
        @Override
        public void run() {
            Message mess =  new Message();
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            try {
                //上传扫描明细
                String barcode = tbBarcode.getText().toString().trim();
                int staffId = Config.StaffId;
                String appSecret = Config.AppSecret;
                Date datePR = ApiHelper.checkDateValid(tbPR.getText().toString().trim());
                String ln = tbLN.getText().toString().trim();
                String pr = datePR==null?"":formatter.format(datePR);
                HashMap<String, String> query = new HashMap<String, String>();
                query.put("checkId", billId);
                query.put("productId", productId);
                query.put("serialNo", barcode );
                query.put("ln" , ln );
                query.put("pr" , pr );
                CheckScanSaveResultMsg scanResult = ApiHelper.GetHttp(CheckScanSaveResultMsg.class, Config.WebApiUrl + "PostCheckSerialNo?",
                            query, staffId, appSecret, true);
                scanResult.setResult();

                if(scanResult.StatusCode!=200) {
                    if (scanResult.Info.equals("different") ){
                        scanResult = ApiHelper.GetHttp(CheckScanSaveResultMsg.class, Config.WebApiUrl + "PostCheckSerialNo2?",
                                query, staffId, appSecret, true);
                        scanResult.setResult();

                        if(scanResult.StatusCode!=200){
                            throw new Exception( scanResult.Info );
                        }

                        if (scanResult.Qty == 0)
                        {
                            //  D300SysUI.PlaySound(Public.SoundPath);
                            throw new Exception("异常：入库数量为0！");
                        }
                    } else {
                        throw new Exception(scanResult.Info);
                    }
                }

                if (scanResult.Qty == 0)
                {
                  //  D300SysUI.PlaySound(Public.SoundPath);
                    throw new Exception("异常：入库数量为0！");
                }

                CheckScanEntity scanData = new CheckScanEntity();
                scanData.SerialNo = barcode;
                scanData.ProductId = productId;
                scanData.LN = ln;
                scanData.PR = datePR;
                scanData.Qty = scanResult.Qty;
                scanData.CreateUserId = checkEntity.CreateUserId;
                //扫码单 与 主单的 对应关系
                scanData.chId = checkEntity;
                new CheckScanDAO(mContext).insert(scanData);

                curCount += scanResult.Qty;
                billCount += scanResult.Qty;
                barcode_exit.add( barcode );//加到内存中

            }catch (Exception ex){
                mess.obj = ex.getMessage();
                eHandler.sendMessage(mess);
                return;
            }

            mess.what = 1;
            eHandler.sendMessage(mess);
            mProgersssDialog.cancel();
        }
    };
     //扫码处理
    public void HandleBarcode(String barCode)
    {
        if (bLockMode)
        {
            String barcode = barCode.trim().replace("*", "").replace("http://kd315.net?b=", "").replace("http://kd315.net/?b=", "")
                    .replace("http://test.kd315.cn/mk/result?b=","").replace(" ", "").replace("Y", "").replace("X", "");
            if (barcode.length() != 15)
            {
                aDialog.failDialog("条码长度不正确！");
                return;
            }
            barcode = barcode.substring(0, 14);

            //检查条码是否重复
            if (barcode_exit.contains(barcode))
            {
                aDialog.warnDialog("条码" + barcode + "已扫描，请不要重复扫描。");
                return;
            }

            tbBarcode.setText(barcode);//当前条码

            mProgersssDialog = new ProgersssDialog(Scan_Check_Activity.this);
            mProgersssDialog.setMsg("扫码上传中");
            new Thread(PostCheckScanRun).start();
        }
        else
        {

        }

        return;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //锁定扫描
            case R.id.btn_Lock:
                LockEvent();
                break;
            //删除单据
            case R.id.btn_DelBill:
                boolean ok = true;
                try {
                    // 从主单信息中取出关联的扫码列表信息
                    ForeignCollection<CheckScanEntity> scanEntities = checkEntity.sNotes;
                    Iterator<CheckScanEntity> scanIterator = scanEntities.iterator();
                    while (scanIterator.hasNext()) {
                            CheckScanEntity scanEntity = scanIterator.next();
                            new CheckScanDAO(mContext).delete(scanEntity);
                    }
                    new CheckEntityDAO(mContext).delete(checkEntity);
                }
                catch (Exception ex){
                    ok =false;
                }
                finally {
                    if(ok){
                        aDialog.okDialog("删除单据成功！");
                    }else{
                        aDialog.failDialog("删除单据失败！");
                    }
                }
                initView(); //重启
                break;
            case R.id.go_back:
                finish();
                break;
        }
    }

    /**
     * 回车换行显示表格
     * @param et
     */
    private void EnterListShow(EditText et){
        et.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction()==KeyEvent.ACTION_DOWN) {
                    //TODO:回车键按下时要执行的操作
                    queryPopInit(v);
                    return true;
                }
                return false;
            }
        });
    }


    private Handler ScanResultHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Constants.DecoderReturnCode.RESULT_SCAN_SUCCESS:
                    mScanAccount++;
                    BarcodeManager.ScanResult decodeResult = (BarcodeManager.ScanResult) msg.obj;
                    HandleBarcode(decodeResult.result);
//                    billBarcode.setText(decodeResult.result);
                    if (mBarcodeManager != null) {
                        mBarcodeManager.beepScanSuccess();
                    }
                    break;

                case Constants.DecoderReturnCode.RESULT_SCAN_FAIL: {
                    if (mBarcodeManager != null) {
                        mBarcodeManager.beepScanFail();
                    }
//                mDecodeResultEdit.setText("Scan failed");

                }
                break;
                case Constants.DecoderReturnCode.RESULT_DECODER_READY: {
                    // Enable all sysbology if needed
                    // try {
                    // mDecodeManager.enableSymbology(SymbologyID.SYM_ALL);   //enable all Sym
                    // } catch (RemoteException e) {
                    // // TODO Auto-generated catch block
                    // e.printStackTrace();
                    // }
                }
                break;
                default:
                    super.handleMessage(msg);
                    break;
            }
        }
    };

    @Override
    public void onEngineReady() {
        // TODO Auto-generated method stub
        ScanResultHandler.sendEmptyMessage(Constants.DecoderReturnCode.RESULT_DECODER_READY);
    }

    @Override
    public int scanResult(boolean suc,BarcodeManager.ScanResult result) {
        // TODO Auto-generated method stub
        Message m = new Message();
        m.obj = result;
        if (suc){
            // docode successfully
            m.what = Constants.DecoderReturnCode.RESULT_SCAN_SUCCESS;
        }else{
            m.what = Constants.DecoderReturnCode.RESULT_SCAN_FAIL;

        }
        ScanResultHandler.sendMessage(m);
        return 0;
    }

    @Override
    protected void onResume() {
        super.onResume();

        if ( mBarcodeManager == null) {
            // initialize decodemanager
            mBarcodeManager = new BarcodeManager(this ,this);

        }

        mScanTouchManager.createScanTouch();
    }

}


