package com.kinde.kicppda.ScanActivity;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteException;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
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

import com.imscs.barcodemanager.BarcodeManager;
import com.imscs.barcodemanager.BarcodeManager.OnEngineStatus;
import com.imscs.barcodemanager.Constants;
import com.imscs.barcodemanager.ScanTouchManager;
import com.kinde.kicppda.Models.OrderBillingEntity;
import com.kinde.kicppda.R;
import com.kinde.kicppda.Utils.Adialog;
import com.kinde.kicppda.Utils.Public;
import com.kinde.kicppda.Utils.SQLiteHelper.DeleteBillHelper;
import com.kinde.kicppda.Utils.SQLiteHelper.TableCreateHelper;
import com.kinde.kicppda.Utils.SQLiteHelper.TableQueryHelper;
import com.kinde.kicppda.decodeLib.DecodeSampleApplication;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by YGG on 2018/7/5.
 */

public class Scan_Order_Activity extends Activity implements  View.OnClickListener,OnEngineStatus {

    private Adialog aDialog;            //警告提示窗口
    private Spinner cmb_plist;             //单据号选择项
    private EditText tbBillDate;              //单据日期
    private EditText tbAgent;               //客户名称
    private EditText tbProduct;             //发货产品
    private EditText barCode;               //当前条码
    private ListView mListView;             //产品选择列表
    private ImageView mGoback;              //返回键
    private HorizontalScrollView mHorizontalScrollView;
    private ArrayAdapter<String> spinAdapter;       //单据号spinner的适配器
    private SimpleAdapter digAdapter;
    private List<HashMap<String, Object>> ProductInfo = new ArrayList<HashMap<String,Object>>();   //产品信息
    private Button btnLock;                 //锁定按钮
    private Button btnUpload;               //上传按钮
    private Button btnDelBill;              //删除按钮
    private boolean bLockMode = false;         //锁定模式
    private String productId = "";      //出库产品ID
    private List<OrderBillingEntity> orderbillingList = new ArrayList<OrderBillingEntity>();//出库单据明细

    private int curPreSet = 0;          //当前预设
    private int curCount = 0;           //当前数量
    private int billPreset = 0;         //本单预设
    private int billCount = 0;          //本单数量
    private List<String> barcode_exit = new ArrayList<>(); //已扫描的条码列表
    private TextView lbCurPreset;
    private TextView lbBillPreset;
    private TextView lbCurCount;
    private TextView lbBillCount;

    private DeleteBillHelper mDelBill;      //删除单据
    private TableCreateHelper mCreateBill;   //创建单据
    private TableQueryHelper mQueryBill;     //查询单据
    private List<String> orderNumList;         //发货单据编号列表

    //扫码
    public BarcodeManager mBarcodeManager = null;
    public final int SCANKEY_LEFT = 301;
    public final int SCANKEY_RIGHT = 300;
    public final int SCANKEY_CENTER = 302;
    public final int SCANTIMEOUT = 3000;
    long mScanAccount = 0;
    public boolean mbKeyDown = true;
    public boolean scanTouch_flag = true;
    public Handler mDoDecodeHandler;
    private WindowManager.LayoutParams windowManagerParams = null;
    private ScanTouchManager mScanTouchManager = null;

    public class DoDecodeThread extends Thread {
        public void run() {
            Looper.prepare();
            mDoDecodeHandler = new Handler();
            Looper.loop();
        }
    }
    private DoDecodeThread mDoDecodeThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scan_order);
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

        mDelBill = new DeleteBillHelper(Scan_Order_Activity.this);
        mCreateBill = new TableCreateHelper(Scan_Order_Activity.this);
        mQueryBill = new TableQueryHelper(Scan_Order_Activity.this);
        initView();

        mDoDecodeThread = new DoDecodeThread();
        mDoDecodeThread.start();
    }

    private void initView(){
        aDialog = new Adialog(Scan_Order_Activity.this);

        bLockMode = false;
        btnLock = (Button)findViewById(R.id.btn_Lock);
        btnUpload = (Button)findViewById(R.id.btn_Upload);
        btnDelBill = (Button)findViewById(R.id.btn_DelBill);

        cmb_plist = (Spinner)findViewById(R.id.num_spinner);
        tbBillDate = (EditText)findViewById(R.id.bill_date);
        tbAgent = (EditText)findViewById(R.id.tbAgent);
        tbProduct = (EditText)findViewById(R.id.tbProduct);
        mListView = (ListView)findViewById(R.id.in_list_view);
        mGoback = (ImageView)findViewById(R.id.go_back);
        mHorizontalScrollView =(HorizontalScrollView)findViewById(R.id.HorizontalScrollView);
        barCode = (EditText)findViewById(R.id.bar_code);
        lbCurPreset = (TextView)findViewById(R.id.lbCurPreset);
        lbBillPreset = (TextView)findViewById(R.id.lbBillPreset);
        lbCurCount = (TextView)findViewById(R.id.lbCurCount);
        lbBillCount = (TextView)findViewById(R.id.lbBillCount);

        mGoback.setOnClickListener(this);
        btnLock.setOnClickListener(this);
        btnDelBill.setOnClickListener(this);
        btnUpload.setOnClickListener(this);
        ViewClear();

        //初始化单据选项列表
        orderNumList = mQueryBill.getBillNum(Public.ORDER_MAIN_TABLE , "OrderCode");
        spinAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, orderNumList);
        spinAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        cmb_plist.setAdapter(spinAdapter);
        cmb_plist.setOnItemSelectedListener(
                new Spinner.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                        ViewClear();
                        BillingLoad( cmb_plist.getSelectedItem().toString() + Public.OrderBillingType );
                        mCreateBill.Order_Scan_Create( cmb_plist.getSelectedItem().toString() );  //创建扫码表
                        tbBillDate.setText(   mQueryBill.getKeyValue("OrderDate", Public.ORDER_MAIN_TABLE ,
                                "OrderCode",cmb_plist.getSelectedItem().toString() ) );
                        tbAgent.setText(  mQueryBill.getKeyValue("AgentName", Public.ORDER_MAIN_TABLE ,
                                "OrderCode",cmb_plist.getSelectedItem().toString() )  );
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> arg0) {
                    }
                });

        mListView.bringToFront();
        //创建SimpleAdapter适配器将数据绑定到item显示控件上
        digAdapter = new SimpleAdapter(Scan_Order_Activity.this, ProductInfo, R.layout.item_inlist,
                new String[]{"pcode", "pname", "pln","pr"}, new int[]{R.id.pcode, R.id.pname, R.id.pln,R.id.pr});
        EnterListShow(tbProduct);
        //实现列表的显示
        mListView.setAdapter(digAdapter);
        //条目点击事件
        mListView.setOnItemClickListener(new ItemClickListener());



    }

    //重写与ContextMenu相关方法
//    @Override
    //重写上下文菜单的创建方法
//    public void onCreateContextMenu(ContextMenu menu, View v,
//                                    ContextMenu.ContextMenuInfo menuInfo) {
//        //子菜单部分：
//        MenuInflater inflator = new MenuInflater(this);
//        inflator.inflate(R.menu.menu_sub, menu);
//        super.onCreateContextMenu(menu, v, menuInfo);
//    }


    private void ViewClear(){
        tbAgent.setText("");
        tbBillDate.setText("");
        tbProduct.setText("");
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
        orderbillingList = mQueryBill.queryOrderBilling(EntryFileName);
        for( OrderBillingEntity entity : orderbillingList)
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

    }
    //解除锁定
    private void Unlock()
    {
        cmb_plist.setEnabled(true);
        tbAgent.setEnabled(true);
        tbBillDate.setEnabled(true);
        tbProduct.setEnabled(true);
        btnDelBill.setEnabled(true);
        btnUpload.setEnabled(true);

        btnLock.setText("锁定扫描");
        bLockMode = false;
        barcode_exit.clear();

        mScanTouchManager.setVisibility(View.INVISIBLE);
        // OpenScan(false);
    }

    //锁定扫描
    private void Lock()
    {
        cmb_plist.setEnabled(false);
        tbAgent.setEnabled(false);
        tbBillDate.setEnabled(false);
        tbProduct.setEnabled(false);
        btnDelBill.setEnabled(false);
        btnUpload.setEnabled(false);

        btnLock.setText("解除锁定");
        bLockMode = true;
        barcode_exit.clear();
        mScanTouchManager.setVisibility(View.VISIBLE);
        // OpenScan(true);
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
            if (tbAgent.getText().toString().isEmpty())
            {
                aDialog.warnDialog( "入库仓库不能为空，请重新选择单据！" );
                return;
            }

            if (tbProduct.getText().toString().isEmpty())
            {
                aDialog.warnDialog("请选择入库产品！");
                return;
            }

            LoadBarcodes();
            if (billPreset == billCount)
            {
//                D300SysUI.PlaySound(Public.SoundPath);
                aDialog.warnDialog("单据扫描完成！");
                return;
            }
            if (curCount == curPreSet)
            {
//                D300SysUI.PlaySound(Public.SoundPath);
                aDialog.warnDialog("当前产品扫描完成!");
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
        List<String[]>scanInfoList = mQueryBill.ScanQuery( cmb_plist.getSelectedItem().toString() + Public.GodownScanType);
        for(String[] scanInfo : scanInfoList){
            barcode_exit.add(scanInfo[0]);
            curProductId = scanInfo[1];
            Qty = Integer.parseInt(scanInfo[2]);
            if ( curProductId.equals( productId ) )
            {
                curCount += Qty;
            }
        }

        lbCurCount.setText( String.valueOf(curCount) );
        lbBillCount.setText( String.valueOf(billCount) );
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
                String BillNum = cmb_plist.getSelectedItem().toString();      //单据号
                boolean ok = mDelBill.DeleteTheData( Public.ORDER_MAIN_TABLE , "OrderCode" , BillNum )
                        && mDelBill.DeleteFile(BillNum + Public.OrderBillingType)
                        && mDelBill.DeleteFile(BillNum + Public.OrderScanType);
                if(ok){
                    aDialog.okDialog("删除单据成功！");
                }else{
                    aDialog.failDialog("删除单据失败！");
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

            for( OrderBillingEntity oEntity : orderbillingList)
            {
                if(oEntity.ProductId.equals(productId)){
                    curPreSet = oEntity.Qty;
                    lbCurPreset.setText( String.valueOf( curPreSet ) ); //当前预设
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
                    String tableName = cmb_plist.getSelectedItem().toString() + Public.OrderBillingType;
                    String keyValue = tbProduct.getText().toString();
                    List<String[]> BillInfoList = mQueryBill.getProductMessage( tableName , keyValue);

                    for( String[] billInfo : BillInfoList){
                        HashMap<String, Object> item = new HashMap<String, Object>();
                        item.put("pid", billInfo[0]);
                        item.put("pcode", billInfo[1]);
                        item.put("pname", billInfo[2]);
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

    private void doScanInBackground() {
        mDoDecodeHandler.post(new Runnable() {

            @Override
            public void run() {
                if (mBarcodeManager != null) {
                    // TODO Auto-generated method stub
                    try {
                        synchronized (mBarcodeManager) {
                            mBarcodeManager.executeScan(SCANTIMEOUT);
                        }

                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }


    public void DoScan() throws Exception {
        doScanInBackground();
    }

    private Handler ScanResultHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Constants.DecoderReturnCode.RESULT_SCAN_SUCCESS:
                    mScanAccount++;
                    BarcodeManager.ScanResult decodeResult = (BarcodeManager.ScanResult) msg.obj;
                    barCode.setText(decodeResult.result);
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

    public void cancleScan() throws Exception {
        if (mBarcodeManager != null) {
            mBarcodeManager.exitScan();
        }
    }

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
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        switch (keyCode) {

            case SCANKEY_LEFT:
            case SCANKEY_CENTER:
            case SCANKEY_RIGHT:
                if(bLockMode) {
                    try {
                        if (mbKeyDown) {
                            DoScan();
                            mbKeyDown = false;
                        }
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
                return true;
            case KeyEvent.KEYCODE_BACK:
                this.finish();
                return true;
            default:
                return super.onKeyDown(keyCode, event);
        }
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        switch (keyCode) {

            case SCANKEY_LEFT:
            case SCANKEY_CENTER:
            case SCANKEY_RIGHT:
                if(bLockMode) {
                    try {
                        mbKeyDown = true;
                        cancleScan();
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
                return true;
            default:
                return super.onKeyUp(keyCode, event);
        }
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

    @Override
    protected void onPause() {
        super.onPause();

        if (mBarcodeManager != null) {
            try {
                mBarcodeManager.release();
                mBarcodeManager = null;
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        //remove ScanTouch
        if (scanTouch_flag) {
            mScanTouchManager.removeScanTouch();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (mBarcodeManager != null) {
            try {
                mBarcodeManager.release();
                mBarcodeManager = null;
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

}

