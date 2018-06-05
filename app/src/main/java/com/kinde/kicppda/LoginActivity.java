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
import android.widget.Toast;

import com.kinde.kicppda.Utils.ApiHelper;
import com.kinde.kicppda.Utils.Config;
import com.kinde.kicppda.Utils.Models.HttpResponseMsg;
import com.kinde.kicppda.Utils.Models.TokenResultMsg;
import com.kinde.kicppda.Utils.ProgersssDialog;

import java.util.HashMap;

/**
 * Created by Lenovo on 2018/5/25.
 */

public class LoginActivity extends AppCompatActivity {

    private EditText id_login;
    private EditText password_login;
    private Button button_login;
    private ProgersssDialog mProgersssDialog;

    Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    //do something,refresh UI;
                    Toast.makeText(LoginActivity.this , msg.obj.toString() ,Toast.LENGTH_LONG).show();
                    //登录加载dialog关闭
                    mProgersssDialog.cancel();
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
            //String msg = ApiHelper.GetHttp( Config.WebApiUrl + "PdaLogin?", query, Config.StaffId ,"" ,false);
            HttpResponseMsg msgc = ApiHelper.GetHttp(HttpResponseMsg.class, Config.WebApiUrl + "PdaLogin?", query, Config.StaffId ,"" ,false);

/*
            if(msg!=null)
            {
                try {
                    JSONObject json_msg = new JSONObject(msg);
                    if ((Integer)json_msg.get("StatusCode") != 200) {
                        message.obj = json_msg.get("Info");
                        mHandler .sendMessage(message);
                        return;
                    }
                }catch (Exception ex){
                    message.obj = ex.toString();
                    mHandler .sendMessage(message);
                    return;
                }
            }
            else {
                message.obj = "网络异常！";
                mHandler .sendMessage(message);
                return;
            }
            */

            //登录获取token
            TokenResultMsg tokenmsg = ApiHelper.GetSignToken( Config.StaffId , Config.AppSecret);
            /*
            if(tokenmsg != null)
            {
                try {
                    JSONObject json_token = new JSONObject(tokenmsg);

                   // Token person = JSON.parseObject(json_token.get("Data").toString(), Token.class);

                    TokenResultMsg p = JSON.parseObject(tokenmsg, TokenResultMsg.class);
                    //(TokenResultMsg)JSON.parse(result)
                    p.setResult();
                    if ((Integer)json_token.get("StatusCode") != 200) {
                        message.obj = json_token.get("Info");
                        mHandler .sendMessage(message);
                        return;
                    }

                }catch (Exception ex){
                    message.obj = ex.toString();
                    mHandler .sendMessage(message);
                    return;
                }
            }
            else {
                message.obj = "网络异常！";
                mHandler .sendMessage(message);
                return;
            }
            */
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
