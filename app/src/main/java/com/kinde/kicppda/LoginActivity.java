package com.kinde.kicppda;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import com.kinde.kicppda.Utils.Adialog;
import com.kinde.kicppda.Utils.ApiHelper;
import com.kinde.kicppda.Utils.Config;
import com.kinde.kicppda.Utils.Models.HttpResponseMsg;
import com.kinde.kicppda.Utils.Models.Token;
import com.kinde.kicppda.Utils.Models.TokenResultMsg;
import com.kinde.kicppda.Utils.ProgersssDialog;

import java.util.HashMap;

/**
 * Created by YGG on 2018/5/25.
 */

public class LoginActivity extends AppCompatActivity {

    public static String currentStaffId = ""; //当前的用户id
    public static Token TokenResult = null;    //令牌

    private EditText id_login;
    private EditText password_login;
    private Button button_login;
    private ProgersssDialog mProgersssDialog;
    private Adialog mAdialog;

    Handler eHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            //登录加载dialog关闭
            mProgersssDialog.cancel();
            switch (msg.what) {
                case 0:
                    //do something,refresh UI;
                    mAdialog.failDialog(  msg.obj.toString() );
                    break;
                default:
                    break;
            }
        }
    };

        /**
     * 登录线程
     */
    Runnable loadRun = new Runnable(){
        @Override
        public void run() {

            // TODO Auto-generated method stub
            Message message = new Message();
            HashMap<String,String> query = new HashMap<String, String>();
            query.put("account",id_login.getText().toString());
            query.put("password",password_login.getText().toString());
            try{
                HttpResponseMsg msgc = ApiHelper.GetHttp(HttpResponseMsg.class, Config.WebApiUrl + "PdaLogin?", query, Config.StaffId ,"" ,false);
                if(msgc!=null)
                {
                    if(msgc.StatusCode != 200)
                    {
                        throw new Exception(msgc.Info);
                    }
                }
                else {
                    throw new Exception( "网络异常！" );
                }

                //登录获取token
                TokenResultMsg tokenmsg = ApiHelper.GetSignToken( Config.StaffId , Config.AppSecret);
                if(tokenmsg!=null)
                {
                    if(tokenmsg.StatusCode!= 200){
                        throw new Exception( tokenmsg.Info );
                    }
                    tokenmsg.setResult();
                    TokenResult = tokenmsg.getResult();
                    currentStaffId = id_login.getText().toString().trim();
                }
                else {
                    throw new Exception("网络异常！");
                }

            }catch (Exception ex){
                message.obj = ex.getMessage();
                eHandler .sendMessage(message);
                return;
            }


            //登录加载dialog关闭
            mProgersssDialog.cancel();

            //登录成功
            //新建一个Intent
            Intent intent = new Intent();
            //制定intent要启动的类
            intent.setClass(LoginActivity.this, MainActivity.class);
            //启动一个新的Activity 跳转主菜单
            startActivity(intent);
            //关闭当前的
            LoginActivity.this.finish();

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        id_login = (EditText)findViewById(R.id.login_id);
        password_login = (EditText)findViewById(R.id.login_password);
        button_login = (Button) findViewById(R.id.login_button);

        bindViews();
    }

    private void bindViews() {
        mAdialog = new Adialog(LoginActivity.this);
        LimitsEditEnter(id_login);
        LimitsEditEnter(password_login);

        mProgersssDialog = null;
        button_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mProgersssDialog = new ProgersssDialog(LoginActivity.this);
                new Thread(loadRun).start();

            }
        });
    }

    /**
     * 限制回车换行
     * @param et
     */
    private void LimitsEditEnter(EditText et){
        et.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_NUMPAD_ENTER) {
                    //TODO:回车键按下时要执行的操作
                    return true;
                }
                return false;
            }
        });
    }
}
