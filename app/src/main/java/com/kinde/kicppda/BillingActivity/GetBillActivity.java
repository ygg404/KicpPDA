package com.kinde.kicppda.BillingActivity;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.imscs.barcodemanager.BarcodeManager;
import com.imscs.barcodemanager.BarcodeManager.OnEngineStatus;
import com.imscs.barcodemanager.Constants;
import com.imscs.barcodemanager.ScanTouchManager;
import com.j256.ormlite.dao.ForeignCollection;
import com.kinde.kicppda.MDAO.GodownEntityDAO;
import com.kinde.kicppda.Models.GodownBillingEntity;
import com.kinde.kicppda.Models.GodownEntity;
import com.kinde.kicppda.R;
import com.kinde.kicppda.Utils.Adialog;
import com.kinde.kicppda.Utils.Enum.BillTypeEnum;
import com.kinde.kicppda.Utils.ProgersssDialog;
import com.kinde.kicppda.decodeLib.DecodeBaseActivity;
import com.kinde.kicppda.decodeLib.DecodeSampleApplication;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import static com.kinde.kicppda.BillingActivity.DownLoadBillHelper.downLoadAllotBill;
import static com.kinde.kicppda.BillingActivity.DownLoadBillHelper.downLoadCheckBill;
import static com.kinde.kicppda.BillingActivity.DownLoadBillHelper.downLoadGodownBill;
import static com.kinde.kicppda.BillingActivity.DownLoadBillHelper.downLoadGroupXBill;
import static com.kinde.kicppda.BillingActivity.DownLoadBillHelper.downLoadOrderBill;
import static com.kinde.kicppda.BillingActivity.DownLoadBillHelper.downLoadReturnBill;

/**
 * Created by YGG on 2018/6/1.
 */

public class GetBillActivity extends DecodeBaseActivity implements OnEngineStatus{

    private final int GodownType = 1;       //入库
    private final int OrderType = 2;        //发货
    private final int ReturnType = 3;  //退货
    private final int AllotType = 4; //调拨
    private final int CheckType = 5; //盘点
    private final int GxType = 6; //关联箱
    public GetBillHelper gBillHelper;

    private TextView billtitle;
    private EditText datebegin;
    private EditText dateend;
    private Button getbtn;
    private Button quitbtn;
    private EditText billBarcode;
    private ProgersssDialog mProgersssDialog;
    private Adialog mAdialog;

    public class DoDecodeThread extends Thread {
        public void run() {
            Looper.prepare();
            mDoDecodeHandler = new Handler();
            Looper.loop();
        }
    }

    Handler eHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            //登录加载dialog关闭
            mProgersssDialog.cancel();
            switch (msg.what) {
                //错误提示
                case 0:
                    //do something,refresh UI;
                    mAdialog.failDialog( msg.obj.toString() );
                    break;
                //成功提示
                case 1:
                    //do something,refresh UI;
                    mAdialog.okDialog( msg.obj.toString() );
                    break;
                default:
                    break;
            }
        }
    };

    private DoDecodeThread mDoDecodeThread;

    //单据类型
    private int billType = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN,   //非全屏
                WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
        setContentView(R.layout.get_bill);
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

        // DBHelper = new DBOpenHelper(this );

        //新页面接收数据
        Bundle bundle = this.getIntent().getExtras();
        //接收billType值
        String billTypeStr = bundle.getString("billType");
        billType = Integer.parseInt(billTypeStr);
        //初始化视图
        initializeUI();

        mDoDecodeThread = new DoDecodeThread();
        mDoDecodeThread.start();
    }

    /**
     * 获取单据线程
     */
    Runnable GetBillingRun = new Runnable(){
        @Override
        public void run() {

            // TODO Auto-generated method stub
            String dateBeginValue = datebegin.getText().toString().trim();
            String dateEndValue = dateend.getText().toString().trim();
            String billBarValue = billBarcode.getText().toString().trim();
            Message message = new Message();
            message.what = 1;

            switch (billType){
                case GodownType:
                    try {
                        message.obj = downLoadGodownBill(gBillHelper, dateBeginValue, dateEndValue, billBarValue);
                        eHandler.sendMessage(message);
                    }catch ( Exception ex){
                        message.what = 0;
                        message.obj = ex.getMessage();
                        eHandler.sendMessage(message);
                    }
                    break;
                case OrderType:
                    try {
                        message.obj = downLoadOrderBill(gBillHelper, dateBeginValue, dateEndValue, billBarValue);
                        eHandler.sendMessage(message);
                    }catch ( Exception ex){
                        message.what = 0;
                        message.obj = ex.getMessage();
                        eHandler.sendMessage(message);
                    }
                    break;
                case ReturnType:
                    try {
                        message.obj = downLoadReturnBill(gBillHelper, dateBeginValue, dateEndValue, billBarValue);
                        eHandler.sendMessage(message);
                    }catch ( Exception ex){
                        message.what = 0;
                        message.obj = ex.getMessage();
                        eHandler.sendMessage(message);
                    }
                    break;
                case AllotType:
                    try {
                        message.obj = downLoadAllotBill(gBillHelper, dateBeginValue, dateEndValue, billBarValue);
                        eHandler.sendMessage(message);
                    }catch ( Exception ex){
                        message.what = 0;
                        message.obj = ex.getMessage();
                        eHandler.sendMessage(message);
                    }
                    break;
                case CheckType:
                    try {
                        message.obj = downLoadCheckBill(gBillHelper, dateBeginValue, dateEndValue, billBarValue);
                        eHandler.sendMessage(message);
                    }catch ( Exception ex){
                        message.what = 0;
                        message.obj = ex.getMessage();
                        eHandler.sendMessage(message);
                    }
                    break;
                case GxType:
                    try {
                        message.obj = downLoadGroupXBill(gBillHelper, dateBeginValue, dateEndValue, billBarValue);
                        eHandler.sendMessage(message);
                    }catch ( Exception ex){
                        message.what = 0;
                        message.obj = ex.getMessage();
                        eHandler.sendMessage(message);
                    }
                    break;
                default:
                    break;
            }

        }
    };

    /**
     * 初始化对单据话框
     */
    private void initializeUI()
    {
        bLockMode = true;
        billtitle = (TextView)findViewById(R.id.bill_Title);
        quitbtn = (Button)findViewById(R.id.quit_btn);
        getbtn =(Button)findViewById(R.id.get_btn);
        datebegin = (EditText)findViewById(R.id.date_begin);
        dateend =(EditText)findViewById(R.id.date_end);
        billBarcode = (EditText)findViewById(R.id.bill_barcode);
        //获取当前日期
        Date dnow = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        datebegin.setText(sdf.format(dnow));
        dateend.setText(sdf.format(dnow));
        //数据库操作初始化
        gBillHelper = new GetBillHelper(this);
        mAdialog = new Adialog(this);

        String Title = "";
        if( billType == BillTypeEnum.godownType.getValue() )
        {
            Title = "获取" + BillTypeEnum.godownType.getTypeName()+"单据";
        }
        else if( billType == BillTypeEnum.orderType.getValue() )
        {
            Title = "获取" + BillTypeEnum.orderType.getTypeName()+"单据";
        }
        else if( billType == BillTypeEnum.returnType.getValue() )
        {
            Title = "获取" + BillTypeEnum.returnType.getTypeName()+"单据";
        }
        else if( billType == BillTypeEnum.allotType.getValue() )
        {
            Title = "获取" + BillTypeEnum.allotType.getTypeName()+"单据";
        }
        else if( billType == BillTypeEnum.checkType.getValue() )
        {
            Title = "获取" + BillTypeEnum.checkType.getTypeName()+"单据";
        }
        else if( billType == BillTypeEnum.gxType.getValue() )
        {
            Title = "获取" + BillTypeEnum.gxType.getTypeName()+"单据";
        }

        //设置标题
        billtitle.setText(Title.toString());
        quitbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        getbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(GetBillingRun).start();
                mProgersssDialog = new ProgersssDialog(GetBillActivity.this);
                mProgersssDialog.setMsg("获取单据中");
            }
        });
    }


    private Handler ScanResultHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Constants.DecoderReturnCode.RESULT_SCAN_SUCCESS:
                    mScanAccount++;
                    BarcodeManager.ScanResult decodeResult = (BarcodeManager.ScanResult) msg.obj;

                    billBarcode.setText(decodeResult.result);
                    if (mBarcodeManager != null) {
             //           mBarcodeManager.beepScanSuccess();
                    }
                    break;

                case Constants.DecoderReturnCode.RESULT_SCAN_FAIL: {
                    if (mBarcodeManager != null) {
                //        mBarcodeManager.beepScanFail();
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
