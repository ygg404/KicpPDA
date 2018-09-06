package com.kinde.kicppda.BaseDataActivity;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;

import com.kinde.kicppda.MDAO.AgentEntityDAO;
import com.kinde.kicppda.MDAO.ProductEntityDAO;
import com.kinde.kicppda.MDAO.WarehouseEntityDAO;
import com.kinde.kicppda.Models.AgentEntity;
import com.kinde.kicppda.Models.ProductEntity;
import com.kinde.kicppda.Models.WarehouseEntity;
import com.kinde.kicppda.R;
import com.kinde.kicppda.Utils.Adialog;
import com.kinde.kicppda.Utils.ApiHelper;
import com.kinde.kicppda.Utils.Config;
import com.kinde.kicppda.Utils.Models.AgentListResultMsg;
import com.kinde.kicppda.Utils.Models.ProductResultMsg;
import com.kinde.kicppda.Utils.Models.WarehouseListResultMsg;
import com.kinde.kicppda.Utils.ProgersssDialog;

/**
 * Created by YGG on 2018/7/14.
 */


/**
 * 基础库
 */
public class DownBaseDataActivity extends Activity implements View.OnClickListener {

    private final int BASE_ALL = 0;
    private final int BASE_AGENT = 1;
    private final int BASE_PRODUCT = 2;
    private final int BASE_WAREHOUSE = 3;

    private Button downAllBtn;
    private Button downAgentBtn;
    private Button downProductBtn;
    private Button downStockBtn;
    private ImageView goBack;

    private Adialog mAdialog;
    private ProgersssDialog mProgersssDialog;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.down_basedata);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        bindView();
    }

    private void bindView(){
        mAdialog = new Adialog(this);
        mContext = this.getApplicationContext();

        downAllBtn = (Button)findViewById(R.id.downAllBtn);
        downAgentBtn = (Button)findViewById(R.id.downAgentBtn);
        downProductBtn = (Button)findViewById(R.id.downProductBtn);
        downStockBtn =(Button)findViewById(R.id.downStockBtn);
        goBack = (ImageView)findViewById(R.id.go_back);

        downAllBtn.setOnClickListener(this);
        downAgentBtn.setOnClickListener(this);
        downProductBtn.setOnClickListener(this);
        downStockBtn.setOnClickListener(this);
        goBack.setOnClickListener(this);
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


    /**
     * 下载基础库线程
     */
    public class DownBaseDataThread implements Runnable {

        private int baseType = BASE_ALL;
        public DownBaseDataThread(int type)
        {
            this.baseType = type;
        }

        @Override
        public void run() {
            // TODO Auto-generated method stub
            Message message = new Message();
            switch (this.baseType){
                case BASE_ALL:
                    try {
                        downAgentList();
                        downProdcutList();
                        downWarehouseList();
                    }catch (Exception ex){
                        message.obj = ex.getMessage();
                        eHandler.sendMessage(message);
                        return;
                    }
                    break;
                case BASE_AGENT:
                    try {
                        downAgentList();
                    }catch (Exception ex){
                        message.obj = ex.getMessage();
                        eHandler.sendMessage(message);
                        return;
                    }
                    break;
                case BASE_PRODUCT:
                    try {
                        downProdcutList();
                    }catch (Exception ex){
                        message.obj = ex.getMessage();
                        eHandler.sendMessage(message);
                        return;
                    }
                    break;
                case BASE_WAREHOUSE:
                    try {
                        downWarehouseList();
                    }catch (Exception ex){
                        message.obj = ex.getMessage();
                        eHandler.sendMessage(message);
                        return;
                    }
                    break;
                default:
                    break;
            }
            message.what = 1;
            message.obj = "下载完成！";
            eHandler.sendMessage(message);

        }
    }


    @Override
    public void onClick(View v) {
        Thread thread;
        DownBaseDataThread mBaseDataDown;
        switch (v.getId()) {
            //下载所有库
            case R.id.downAllBtn:
                mBaseDataDown = new DownBaseDataThread(BASE_ALL);
                thread = new Thread(mBaseDataDown);
                thread.start();
                mProgersssDialog = new ProgersssDialog(DownBaseDataActivity.this);
                mProgersssDialog.setMsg("下载中");
                break;
            //下载客户库
            case R.id.downAgentBtn:
                mBaseDataDown = new DownBaseDataThread(BASE_AGENT);
                thread = new Thread(mBaseDataDown);
                thread.start();
                mProgersssDialog = new ProgersssDialog(DownBaseDataActivity.this);
                mProgersssDialog.setMsg("下载中");
                break;
            //下载产品库
            case R.id.downProductBtn:
                mBaseDataDown = new DownBaseDataThread(BASE_PRODUCT);
                thread = new Thread(mBaseDataDown);
                thread.start();
                mProgersssDialog = new ProgersssDialog(DownBaseDataActivity.this);
                mProgersssDialog.setMsg("下载中");
                break;
            //下载仓库库
            case R.id.downStockBtn:
                mBaseDataDown = new DownBaseDataThread(BASE_WAREHOUSE);
                thread = new Thread(mBaseDataDown);
                thread.start();
                mProgersssDialog = new ProgersssDialog(DownBaseDataActivity.this);
                mProgersssDialog.setMsg("下载中");
                break;
            //返回键
            case R.id.go_back:
                finish();
                break;
            default:
                break;

        }
    }

    private void downAgentList()throws Exception{
        try{
            AgentListResultMsg AgentList = ApiHelper.GetHttp(AgentListResultMsg.class,
                    Config.WebApiUrl + "GetAgentList?", null, Config.StaffId , Config.AppSecret ,true);
            AgentList.setResult();
            //插入客户表 数据
            for(AgentEntity entity : AgentList.Result){
                new AgentEntityDAO(mContext).insert(entity);
            }

        }catch (Exception ex){
            throw new Exception(ex.getMessage());
        }
    }

    private void downProdcutList() throws Exception{
        try{
            ProductResultMsg  ProductResult = ApiHelper.GetHttp(ProductResultMsg.class,
                    Config.WebApiUrl + "GetProductList?", null, Config.StaffId , Config.AppSecret ,true);
            ProductResult.setResult();
            //插入产品表 数据
            for(ProductEntity entity : ProductResult.Result){
                new ProductEntityDAO(mContext).insert(entity);
            }
        }catch (Exception ex){
            throw new Exception( ex.getMessage());
        }
    }

    private void downWarehouseList()throws Exception{
        try{
            WarehouseListResultMsg WarehouseList = ApiHelper.GetHttp(WarehouseListResultMsg.class,
                    Config.WebApiUrl + "GetWarehouseList?", null, Config.StaffId , Config.AppSecret ,true);
            WarehouseList.setResult();
            //插入仓库表 数据
            for(WarehouseEntity entity : WarehouseList.Result){
                new WarehouseEntityDAO(mContext).insert(entity);
            }
        }catch (Exception ex){
            throw new Exception( ex.getMessage());
        }
    }
}
