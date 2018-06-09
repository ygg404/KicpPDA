package com.kinde.kicppda.decodeLib;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteException;
import android.widget.EditText;

import com.imscs.barcodemanager.BarcodeManager;
import com.imscs.barcodemanager.BarcodeManager.OnEngineStatus;
import com.imscs.barcodemanager.BarcodeManager.ScanResult;
import com.imscs.barcodemanager.Constants;

/**
 * Created by Lenovo on 2018/6/6.
 */

public class DecodeScan extends Activity implements OnEngineStatus {

    public final int ID_SCANSETTING = 0x12;
    public final int ID_CLEAR_SCREEN = 0x13;
    public final int ID_SCANTOUCH = 0x14;

    public BarcodeManager mBarcodeManager = null;

//    public final int SCANKEY_LEFT = 301;
//    public final int SCANKEY_RIGHT = 300;
//    public final int SCANKEY_CENTER = 302;
    public final int SCANTIMEOUT = 3000;
    long mScanAccount = 0;
    public boolean mbKeyDown = true;
    public boolean scanTouch_flag = true;
    public Handler mDoDecodeHandler;
    //扫码后更改EditText控件内容
    public EditText etBar;

    public class DoDecodeThread extends Thread {
        public void run() {
            Looper.prepare();

            mDoDecodeHandler = new Handler();

            Looper.loop();
        }
    }

    public BarcodeManager BarManagerCreate(Context mContext , EditText et){
        etBar = et;
        //m.findViewById(R.id.bartxt);
        mBarcodeManager = new BarcodeManager(mContext ,this);
        return mBarcodeManager;
    }

    public DecodeScan(){
        mDoDecodeThread = new DoDecodeThread();
        mDoDecodeThread.start();
    }

    private DoDecodeThread mDoDecodeThread;

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
                    String strDecodeResult = "";
                    ScanResult decodeResult = (ScanResult) msg.obj;

                    int codeid = decodeResult.codeid;
                    int aimid = decodeResult.aimid;
                    int iLength = decodeResult.len;

                    strDecodeResult = "Decode Result: " + decodeResult.result
                            + "\r\n" + "CodeID: " + "("
                            + String.valueOf((char) codeid) + "/"
                            + String.valueOf((char) aimid) + ")" + "\r\n"
                            + "Decode Length: " + iLength + "\r\n"
                            + "Success Count: " + mScanAccount + "\r\n";
                    etBar.setText(decodeResult.result);
//                mDecodeResultEdit.setText(strDecodeResult);
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
    public int scanResult(boolean suc,ScanResult result) {
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
}