package com.kinde.kicppda.Billing;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteException;
import android.view.KeyEvent;
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
import com.kinde.kicppda.R;
import com.kinde.kicppda.Utils.Enum.BillTypeEnum;
import com.kinde.kicppda.Utils.SQLiteHelper.DBOpenHelper;
import com.kinde.kicppda.decodeLib.DecodeSampleApplication;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
/**
 * Created by YGG on 2018/6/1.
 */

public class GetBillActivity extends Activity implements OnEngineStatus{

    public SQLiteDatabase db;
    public DBOpenHelper DBHelper;

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

    private TextView billtitle;
    private EditText datebegin;
    private EditText dateend;
    private Button getbtn;
    private Button quitbtn;
    private EditText billBarcode;

   
    public class DoDecodeThread extends Thread {
        public void run() {
            Looper.prepare();

            mDoDecodeHandler = new Handler();

            Looper.loop();
        }
    }


    private DoDecodeThread mDoDecodeThread;
	
    //单据类型
    private int billType = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
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

        DBHelper = new DBOpenHelper(this );

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
     * 初始化对单据话框
     */
    private void initializeUI()
    {
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


        String Title = "";

        if( billType == BillTypeEnum.intype.getValue() )
        {
            Title = "获取" + BillTypeEnum.intype.getTypeName()+"单据";
        }
        else if( billType == BillTypeEnum.ordertype.getValue() )
        {
            Title = "获取" + BillTypeEnum.ordertype.getTypeName()+"单据";
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
                db = DBHelper.getReadableDatabase();
                Cursor cursor = db.rawQuery("select name from sqlite_master where type='table' order by name", null);
                while(cursor.moveToNext()){
                    //遍历出表名
                    String name = cursor.getString(0);

                }
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

                    billBarcode.setText(decodeResult.result);
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
                try {
                    if (mbKeyDown) {
                        DoScan();
                        mbKeyDown = false;
                    }
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
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
                try {
                     mbKeyDown = true;
                     cancleScan();
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
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
