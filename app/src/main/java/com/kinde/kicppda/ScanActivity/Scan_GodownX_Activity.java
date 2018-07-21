package com.kinde.kicppda.ScanActivity;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.imscs.barcodemanager.BarcodeManager;
import com.imscs.barcodemanager.BarcodeManager.OnEngineStatus;
import com.imscs.barcodemanager.Constants;
import com.imscs.barcodemanager.ScanTouchManager;
import com.kinde.kicppda.Models.GodownXBillingEntity;
import com.kinde.kicppda.R;
import com.kinde.kicppda.Utils.Adialog;
import com.kinde.kicppda.Utils.ApiHelper;
import com.kinde.kicppda.Utils.Config;
import com.kinde.kicppda.Utils.Models.GroupXScanSaveResultMsg;
import com.kinde.kicppda.Utils.ProgersssDialog;
import com.kinde.kicppda.Utils.Public;
import com.kinde.kicppda.Utils.SQLiteHelper.DeleteBillHelper;
import com.kinde.kicppda.Utils.SQLiteHelper.ScanCreateHelper;
import com.kinde.kicppda.Utils.SQLiteHelper.TableQueryHelper;
import com.kinde.kicppda.decodeLib.DecodeBaseActivity;
import com.kinde.kicppda.decodeLib.DecodeSampleApplication;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by YGG on 2018/6/4.
 */

public class Scan_GodownX_Activity extends DecodeBaseActivity implements  View.OnClickListener,OnEngineStatus {

    private Adialog mAdialog;                //警告提示窗口
    private Spinner cmb_plist;              //单据号选择项
    private EditText tbBillDate;            //单据日期
    private EditText tbProduct;             //关联产品
    private EditText tbPR;                  //生产日期
    private EditText tbLN;                  //生产批次
    private EditText tbBarcode;               //当前条码
    private String billNo   = "";           //单号
    private String billId   = "";           //单据id
    private ListView mListView;             //产品选择列表
    private ImageView mGoback;              //返回键
    private HorizontalScrollView mHorizontalScrollView;
    private ArrayAdapter<String> spinAdapter;       //单据号spinner的适配器
    private SimpleAdapter digAdapter;
    private List<HashMap<String, Object>> ProductInfo = new ArrayList<HashMap<String,Object>>();   //产品信息
    private Button btnLock;                 //锁定按钮
    private Button btnUpload;               //上传按钮
    private Button btnDelBill;              //删除按钮
    private String productId = "";      //产品ID
    private List<GodownXBillingEntity> godownXbillingList = new ArrayList<GodownXBillingEntity>();//入库单据明细
    private ProgersssDialog mProgersssDialog;

    private int curPreSet = 0;          //当前预设
    private int curCount = 0;           //当前数量
    private int billPreset = 0;         //本单预设
    private int billCount = 0;          //本单数量
    private List<String> barcode_exit = new ArrayList<>(); //已扫描的条码列表
    private TextView lbCurPreset;
    private TextView lbBillPreset;
    private TextView lbCurCount;
    private TextView lbBillCount;
    private TextView lbCurGroup;

    private String MainFileName = "";//主单表
    private String EntryFileName = "";//明细表
    private String ScanFileName = "";//扫描表

    private DeleteBillHelper mDelBill;      //删除单据
    private ScanCreateHelper mCreateBill;   //创建单据
    private TableQueryHelper mQueryBill;     //查询单据
	private ScanBillingHelper mScanBill;     //扫码单据
    private List<String> godownXNumList;      //关联箱单据编号列表

    private int CurProductCount = 0;//当前组添加产品个数
    private int CurGroupXCount = 0;//当期添加的组个数
    private int CurSerailNoAddType = 1; //1:产品，2:箱
    private List<String> CurProductNoArr = new ArrayList<>();   //序号组合


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scan_gx);
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

        mDelBill = new DeleteBillHelper(Scan_GodownX_Activity.this);
        mCreateBill = new ScanCreateHelper(Scan_GodownX_Activity.this);
        mQueryBill = new TableQueryHelper(Scan_GodownX_Activity.this);
		mScanBill  =  new ScanBillingHelper(Scan_GodownX_Activity.this);
        initView();

        mDoDecodeThread = new DoDecodeThread();
        mDoDecodeThread.start();
    }

    //设置单据保存文件
    private void SetFilePath(String billNo)
    {
        MainFileName  = Public.GodownX_MAIN_TABLE;
        EntryFileName = billNo  + Public.GodownXBillingType;
        ScanFileName  =  billNo +  Public.GodownXScanType;
    }

    private void initView(){
        CurProductNoArr.clear();
        mAdialog = new Adialog(Scan_GodownX_Activity.this);
        bLockMode = false;
        btnLock = (Button)findViewById(R.id.btn_Lock);
        btnUpload = (Button)findViewById(R.id.btn_Upload);
        btnDelBill = (Button)findViewById(R.id.btn_DelBill);

        cmb_plist = (Spinner)findViewById(R.id.num_spinner);
        tbBillDate = (EditText)findViewById(R.id.bill_date);
        tbProduct = (EditText)findViewById(R.id.tbProduct);
        tbPR = (EditText)findViewById(R.id.pr);
        tbLN = (EditText)findViewById(R.id.ln);
        mListView = (ListView)findViewById(R.id.in_list_view);
        mGoback = (ImageView)findViewById(R.id.go_back);
        mHorizontalScrollView =(HorizontalScrollView)findViewById(R.id.HorizontalScrollView);
        tbBarcode = (EditText)findViewById(R.id.tbBarcode);
        lbCurPreset = (TextView)findViewById(R.id.lbCurPreset);
        lbBillPreset = (TextView)findViewById(R.id.lbBillPreset);
        lbCurCount = (TextView)findViewById(R.id.lbCurCount);
        lbBillCount = (TextView)findViewById(R.id.lbBillCount);
        lbCurGroup = (TextView)findViewById(R.id.curGroup);

        mGoback.setOnClickListener(this);
        btnLock.setOnClickListener(this);
        btnDelBill.setOnClickListener(this);
        btnUpload.setOnClickListener(this);
        ViewClear();

        //初始化单据选项列表
        godownXNumList = mQueryBill.getBillNum(Public.GodownX_MAIN_TABLE , "GodownXCode");
        spinAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, godownXNumList);
        spinAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        cmb_plist.setAdapter(spinAdapter);
        cmb_plist.setOnItemSelectedListener(
                new Spinner.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                        ViewClear();
                        billNo = cmb_plist.getSelectedItem().toString().trim();
                        SetFilePath(billNo);
						BillingLoad( EntryFileName );
						mCreateBill.GodownX_Scan_Create( billNo );  //创建扫码表
                        billId = mQueryBill.getKeyValue("GodownXId" , MainFileName , "GodownXCode",billNo);
						tbBillDate.setText( mQueryBill.getKeyValue("GodownXDate", MainFileName ,"GodownXCode",billNo) );
                        tbBillDate.setText(   mQueryBill.getKeyValue("GodownXDate", MainFileName ,"GodownXCode",billNo ) );
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> arg0) {
                    }
                });
        tbBarcode.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(keyCode == KeyEvent.KEYCODE_ENTER && event.getAction()==KeyEvent.ACTION_DOWN){
                    //TODO:返回键按下时要执行的操作
                    if(bLockMode){
                        HandleBarcode( tbBarcode.getText().toString().trim());
                    }
                    return true;
                }
                return false;
            }
        });
        tbProduct.requestFocus();
        mListView.bringToFront();
        //创建SimpleAdapter适配器将数据绑定到item显示控件上
        digAdapter = new SimpleAdapter(Scan_GodownX_Activity.this, ProductInfo, R.layout.item_inlist,
                new String[]{"pcode", "pname", "pln","pr"}, new int[]{R.id.pcode, R.id.pname, R.id.pln,R.id.pr});
        EnterListShow(tbProduct);
        //实现列表的显示
        mListView.setAdapter(digAdapter);
        //条目点击事件
        mListView.setOnItemClickListener(new ItemClickListener());


        tbPR.setOnKeyListener(new View.OnKeyListener(){
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
                    //TODO:回车键按下时要执行的操作
                    return true;
                }
                return false;
            }
        });
    }

    private void ViewClear(){

        tbBillDate.setText("");
        tbProduct.setText("");
        tbPR.setText("");
        tbLN.setText("");
        lbCurPreset.setText("0");
        lbBillPreset.setText("0");
        lbCurCount.setText("0");
        lbBillCount.setText("0");
        mHorizontalScrollView.setVisibility(View.INVISIBLE);
    }

    //加载明细信息
    private void BillingLoad(String EntryFileName){
        billPreset = 0;
        billCount = 0;
        godownXbillingList = mQueryBill.queryGodownXBilling(EntryFileName);
        for( GodownXBillingEntity entity : godownXbillingList)
        {
            billPreset += entity.Qty;
            //本单数量
            billCount += entity.QtyFact;
        }
        curPreSet = 0;
        lbCurPreset.setText( String.valueOf(curPreSet) );
        lbBillPreset.setText( String.valueOf(billPreset) );

        curCount = 0;
        //  billCount = 0;
        lbCurCount.setText( String.valueOf(curCount) );
        lbBillCount.setText( String.valueOf(billCount) );

//        groupCount =
//        lbCurGroup.setText();
    }
    //解除锁定
    private void Unlock()
    {
        cmb_plist.setEnabled(true);
        tbBillDate.setEnabled(true);
        tbProduct.setEnabled(true);
        tbPR.setEnabled(true);
        tbLN.setEnabled(true);
        btnDelBill.setEnabled(true);
        btnUpload.setEnabled(true);

        btnLock.setText("锁定扫描");
        bLockMode = false;
        barcode_exit.clear();

        mScanTouchManager.setVisibility(View.INVISIBLE);

    }

    //锁定扫描
    private void Lock()
    {
        cmb_plist.setEnabled(false);
        tbBillDate.setEnabled(false);
        tbProduct.setEnabled(false);
        tbPR.setEnabled(false);
        tbLN.setEnabled(false);
        btnDelBill.setEnabled(false);
        btnUpload.setEnabled(false);

        btnLock.setText("解除锁定");
        bLockMode = true;
        barcode_exit.clear();
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
                mAdialog.warnDialog( "请选择单号！" );
                return;
            }
            if (tbBillDate.getText().toString().isEmpty())
            {
                mAdialog.warnDialog( "单据日期不能为空，请重新选择单据！" );
                return;
            }

            if (tbProduct.getText().toString().isEmpty())
            {
                mAdialog.warnDialog("请选择关联产品！");
                return;
            }

            LoadBarcodes();
            if (billPreset == billCount)
            {
//                D300SysUI.PlaySound(Public.SoundPath);
                mAdialog.warnDialog("单据扫描完成！");
                return;
            }
            if (curCount == curPreSet)
            {
//                D300SysUI.PlaySound(Public.SoundPath);
                mAdialog.warnDialog("当前产品扫描完成!");
                return;
            }

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
        List<String[]>scanInfoList = mQueryBill.ScanQuery( ScanFileName );
        for(String[] scanInfo : scanInfoList){
            barcode_exit.add(scanInfo[0]);
            curProductId = scanInfo[1];
            Qty = Integer.parseInt(scanInfo[2]);
            if (curProductId.equals(productId) )
            {
                curCount += Qty;
            }
        }

        lbCurCount.setText( String.valueOf(curCount) );
        lbBillCount.setText( String.valueOf(billCount) );
    }

    Handler eHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {
                //do something,refresh UI;
                case 0:
                    String barcode = tbBarcode.getText().toString().trim();
                    if( msg.obj.toString().equals("OK1")){
                        //加载dialog关闭
                        mProgersssDialog.cancel();
                        curCount++;
                        billCount++;
                        lbCurCount.setText( String.valueOf(curCount) );
                        lbBillCount.setText( String.valueOf(billCount) );
                        CurProductCount ++;
                        CurProductNoArr.add( barcode );
                        lbCurGroup.setText( String.valueOf(CurProductCount) + "/" + String.valueOf(CurGroupXCount));
                    }else if ( msg.obj.toString().equals("OK2") ) {
                        //加载dialog关闭
                        mProgersssDialog.cancel();
                        Toast.makeText(Scan_GodownX_Activity.this,"请录入箱号！",Toast.LENGTH_SHORT ).show();
                        curCount++;
                        billCount++;
                        lbCurCount.setText( String.valueOf(curCount) );
                        lbBillCount.setText( String.valueOf(billCount) );
                        CurProductCount++;
                        CurSerailNoAddType = 2; //录入箱号
                        CurProductNoArr.add( barcode );
                        lbCurGroup.setText( String.valueOf(CurProductCount) + "/" + String.valueOf(CurGroupXCount));
                    }else if( msg.obj.toString().equals("OK3")  ){
                        new Thread(PostGroupXRun).start();
                        mProgersssDialog.setMsg("保存组合中");

                    }
                    else {
                        //加载dialog关闭
                        mProgersssDialog.cancel();
                        mAdialog.failDialog(msg.obj.toString());
                    }
                    break;
                case 1:
                    mProgersssDialog.cancel();
                    CurProductNoArr.clear();
                    CurProductCount = 0;
                   // CurGroupXCount += 1;
                    CurSerailNoAddType = 1; //开始下一组
                    mAdialog.okDialog(msg.obj.toString());
                    lbCurGroup.setText( String.valueOf(CurProductCount) + "/" + String.valueOf(CurGroupXCount));
                    break;
                default:
                    break;
            }
        }
    };

    /**
     * 产品箱关联检查序号
     */
    Runnable CheckGroupXRun = new Runnable() {
        @Override
        public void run() {
            Message mess =  new Message();
            try {
                String barcode = tbBarcode.getText().toString().trim();
                int staffId = Config.StaffId;
                String appSecret = Config.AppSecret;
                HashMap<String, String> query = new HashMap<String, String>();
                query.put("godownXId", billId);
                query.put("productId", productId);

                String LN = mQueryBill.getKeyValue("LN", EntryFileName, "ProductId", productId);
                String PR = mQueryBill.getKeyValue("PR", EntryFileName, "ProductId", productId);
                query.put("ln", LN);
                query.put("pr", PR);
                query.put("serialNo", barcode );
                query.put("curSerailNoAddType", String.valueOf(CurSerailNoAddType));
                query.put("curProductCount", String.valueOf(CurProductCount));

                GroupXScanSaveResultMsg scanResult = ApiHelper.GetHttp(GroupXScanSaveResultMsg.class, Config.WebApiUrl + "CheckGroupXSerialNo?",
                            query, staffId, appSecret, true);
               // scanResult.setResult();
                if(scanResult.StatusCode!=200){
                    throw new Exception( scanResult.Info );
                }

//                String[] insertData = new String[7];
//                insertData[0] = barcode;
//                insertData[1] = productId;
//                insertData[2] = LN;
//                insertData[3] = PR;
//                insertData[4] = "";
//                insertData[5] = mQueryBill.getKeyValue("CreateDate", EntryFileName, "ProductId", productId);
//                insertData[6] = mQueryBill.getKeyValue("CreateUserId", EntryFileName, "ProductId", productId);
//                mScanBill.GodownXScanSave(ScanFileName , insertData);


            }catch (Exception ex){
                mess.obj = ex.getMessage();
                eHandler.sendMessage(mess);
                return;
            }
        }
    };


    /**
     * 产品箱关联添加组合关系
     */
    Runnable PostGroupXRun = new Runnable() {
        @Override
        public void run() {
            Message mess = new Message();
            try {
                String serialNo = "";
                for(String attr : CurProductNoArr){
                    serialNo = serialNo + attr + ",";
                }
                String serialNoX = tbBarcode.getText().toString().trim();

                int staffId = Config.StaffId;
                String appSecret = Config.AppSecret;
                HashMap<String, String> query = new HashMap<String, String>();
                query.put("godownXId", billId);
                query.put("productId", productId);
                String LN = mQueryBill.getKeyValue("LN", EntryFileName, "ProductId", productId);
                String PR = mQueryBill.getKeyValue("PR", EntryFileName, "ProductId", productId);
                query.put("ln", LN);
                query.put("pr", PR);
                query.put("serialNo", serialNo );
                query.put("serialNoX", serialNoX );

                GroupXScanSaveResultMsg saveResult = ApiHelper.GetHttp(GroupXScanSaveResultMsg.class, Config.WebApiUrl + "PostGroupXSerialNo?",
                        query, staffId, appSecret, true);
                if(saveResult.StatusCode!=200){
                    throw new Exception( saveResult.Info );
                }

            }catch (Exception ex){
                mess.obj = ex.getMessage().toString();
                eHandler.sendMessage(mess);
                return;
            }
            mess.what =1;
            mess.obj = "保存组合成功！";
            eHandler.sendMessage(mess);

        }
    };

     //扫码处理
    public void HandleBarcode(String barCode)
    {
        if (bLockMode)
        {
            String barcode = barCode.trim().replace("*", "").replace("http://kd315.net?b=", "").replace("http://kd315.net/?b=", "")
                    .replace("http://test.kd315.cn/mk/result?b=","").replace(" ", "").replace("X","").replace("Y","");
            if (barcode.length() != 15)
            {
                mAdialog.failDialog("条码长度不正确！");
                return;
            }
            barcode = barcode.substring(0, 14);

            //检查条码是否重复
            if (barcode_exit.contains(barcode))
            {
                mAdialog.warnDialog("条码" + barcode + "已扫描，请不要重复扫描。");
                return;
            }

            tbBarcode.setText(barcode);//当前条码

            mProgersssDialog = new ProgersssDialog(Scan_GodownX_Activity.this);
            mProgersssDialog.setMsg("扫码上传中");
            new Thread(CheckGroupXRun).start();
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
               
                boolean ok = mDelBill.DeleteTheData( MainFileName , "GodownXCode" , billNo )
                        && mDelBill.DeleteFile( EntryFileName )
                        && mDelBill.DeleteFile( ScanFileName );
                if(ok){
                    mAdialog.okDialog("删除单据成功！");
                }else{
                    mAdialog.failDialog("删除单据失败！");
                }
                initView(); //重启
                break;
            case R.id.go_back:
                finish();
                break;
        }
    }

    //获取点击事件
    private final class ItemClickListener implements AdapterView.OnItemClickListener {

        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            ListView listView = (ListView) parent;
            HashMap<String, Object> productData = (HashMap<String, Object>) listView.getItemAtPosition(position);

            productId = productData.get("pid").toString();
            tbProduct.setText( productData.get("pname").toString() );
            tbLN.setText( productData.get("pln").toString() );
            tbPR.setText( productData.get("pr").toString() );
            for( GodownXBillingEntity gEntity : godownXbillingList)
            {
                if(gEntity.ProductId.equals(productId)){
                    curPreSet = gEntity.Qty;
                    lbCurPreset.setText( String.valueOf( curPreSet ) ); //当前预设
                    CurGroupXCount = gEntity.SinglePerBox;
                    lbCurGroup.setText( String.valueOf(CurProductCount) + "/" + String.valueOf(CurGroupXCount));
                    break;
                }
            }

            mHorizontalScrollView.setVisibility(View.INVISIBLE);

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
                    ProductInfo.clear();
    
                    String keyValue = tbProduct.getText().toString();
                    List<String[]> BillInfoList = mQueryBill.getProductInfo( EntryFileName , keyValue);

                    for( String[] billInfo : BillInfoList){
                        HashMap<String, Object> item = new HashMap<String, Object>();
                        item.put("pid", billInfo[0]);
                        item.put("pcode", billInfo[1]);
                        item.put("pname", billInfo[2]);
                        item.put("pln", billInfo[3]);
                        item.put("pr", billInfo[4]);
                        ProductInfo.add(item);
                    }
                    digAdapter.notifyDataSetChanged();
                    mHorizontalScrollView.setVisibility(View.VISIBLE);
                    return true;
                }
                if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction()==KeyEvent.ACTION_DOWN){
                    //TODO:返回键按下时要执行的操作
                    mHorizontalScrollView.setVisibility(View.INVISIBLE);
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
