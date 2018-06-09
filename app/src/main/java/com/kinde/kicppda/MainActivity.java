package com.kinde.kicppda;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kinde.kicppda.Billing.GetBillActivity;
import com.kinde.kicppda.ScanDialog.InScanDialog;


public class MainActivity extends Activity implements View.OnClickListener {

    private TextView datachannel;
    private TextView syschannel;
    private  TextView prodchannel;

    //Fragment Object
    private FragmentManager fManager;
    private MyFragment fg;
    private TextView in_content;
    private TextView order_content;
    private TextView return_content;
    private TextView allot_content;
    private TextView check_content;
    private Button bill_btn;
    private Button scan_btn;
    private Button query_btn;
    private LinearLayout menu_content;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        fManager = getFragmentManager();
        datachannel = (TextView)findViewById(R.id.data_channel);
        syschannel = (TextView)findViewById(R.id.sys_channel);
        prodchannel = (TextView)findViewById(R.id.prod_channel);
        datachannel.setOnClickListener(this);
        syschannel.setOnClickListener(this);
        prodchannel.setOnClickListener(this);
    }

    //隐藏所有Fragment
    private void hideAllFragment(FragmentTransaction fragmentTransaction) {
        if (fg != null) fragmentTransaction.hide(fg);
    }

    /**
     * 重置所有文本的选中状态
     */
    protected void setSelected(){
        datachannel.setSelected(false);
        syschannel.setSelected(false);
        prodchannel.setSelected(false);

    }

    /**
     * 重置所有订单入库等选中状态
     */
    protected  void setMenus(){
        in_content.setBackgroundResource(R.drawable.textview_border);
        order_content.setBackgroundResource(R.drawable.textview_border);
        return_content.setBackgroundResource(R.drawable.textview_border);
        allot_content.setBackgroundResource(R.drawable.textview_border);
        check_content.setBackgroundResource(R.drawable.textview_border);
    }

    /**
     * 监听Back键按下事件
     * 注意:
     * super.onBackPressed()会自动调用finish()方法,关闭
     * 当前Activity.
     * 若要屏蔽Back键盘,注释该行代码即可
     */
    @Override
    public void onBackPressed() {
        Context mContext = MainActivity.this;
        // 创建退出对话框
        AlertDialog alert = null;
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        // 设置对话框标题
        alert = builder.setIcon(R.mipmap.ic_launcher)
                .setTitle("系统提示：")
                .setMessage("确定要退出吗？")
                .setCancelable(false)
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ;
                    }
                })
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                }).create();             //创建AlertDialog对象
        alert.show();                    //显示对话框
    }

    @Override
    public void onClick(View v) {
        FragmentTransaction fTransaction = fManager.beginTransaction();
        hideAllFragment(fTransaction);
        switch (v.getId()) {
            //数据管理
            case R.id.data_channel:
                setSelected();
                datachannel.setSelected(true);
                if(fg == null){
                    fg = new MyFragment();
                    fTransaction.add(R.id.ly_content,fg);
                }else{
                    fTransaction.show(fg);
                }
                break;
            //系统管理
            case R.id.sys_channel:
                setSelected();
                syschannel.setSelected(true);
                break;
            //生产管理
            case  R.id.prod_channel:
                setSelected();
                prodchannel.setSelected(true);
                break;
            case R.id.inmenu:
                setMenus();
                fTransaction.show(fg);
               in_content.setBackgroundResource(R.drawable.textview_sborder);
                menu_content.setVisibility(View.VISIBLE);
                break;
            case R.id.ordermenu:
                setMenus();
                fTransaction.show(fg);
                order_content.setBackgroundResource(R.drawable.textview_sborder);
                menu_content.setVisibility(View.VISIBLE);
                break;
            case R.id.returnmenu:
                setMenus();
                fTransaction.show(fg);
                return_content.setBackgroundResource(R.drawable.textview_sborder);
                menu_content.setVisibility(View.VISIBLE);
                break;
            case R.id.allotmenu:
                setMenus();
                fTransaction.show(fg);
                allot_content.setBackgroundResource(R.drawable.textview_sborder);
                menu_content.setVisibility(View.VISIBLE);
                break;
            case R.id.checkmenu:
                setMenus();
                fTransaction.show(fg);
                check_content.setBackgroundResource(R.drawable.textview_sborder);
                menu_content.setVisibility(View.VISIBLE);
                break;
            //单据
            case R.id.billbtn:
                fTransaction.show(fg);
                //新建一个显式意图，第一个参数为当前Activity类对象，第二个参数为你要打开的Activity类
                Intent intent =new Intent(MainActivity.this,GetBillActivity.class);
                //用Bundle携带数据
                Bundle bundle=new Bundle();
                //传递参数为billType
                bundle.putString("billType", "1");
                intent.putExtras(bundle);
                startActivity(intent);
                break;
            //采集
            case R.id.scanbtn:
                fTransaction.show(fg);
                new InScanDialog(MainActivity.this );
                break;
            //查询
            case R.id.querybtn:
                fTransaction.show(fg);
                break;
            default:
                break;
        }
        fTransaction.commit();

    }

    public class MyFragment extends Fragment {

        private String content;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fg_datacontent,container,false);
            FragmentBind(view);
            return view;
        }
    }

    /**
     * 初始化 Fragment
      * @param view
     */
    public void FragmentBind(View view){
        in_content = (TextView)view.findViewById(R.id.inmenu);
        order_content =(TextView)view.findViewById(R.id.ordermenu);
        return_content = (TextView)view.findViewById(R.id.returnmenu);
        allot_content = (TextView)view.findViewById(R.id.allotmenu);
        check_content = (TextView)view.findViewById(R.id.checkmenu);
        bill_btn  = (Button)view.findViewById(R.id.billbtn);
        scan_btn  = (Button)view.findViewById(R.id.scanbtn);
        query_btn = (Button)view.findViewById(R.id.querybtn);
        menu_content =(LinearLayout)view.findViewById(R.id.menucontent);

        bill_btn.setOnClickListener(this);
        scan_btn.setOnClickListener(this);
        query_btn.setOnClickListener(this);
        in_content.setOnClickListener(this);
        order_content.setOnClickListener(this);
        return_content.setOnClickListener(this);
        allot_content.setOnClickListener(this);
        check_content.setOnClickListener(this);
    }

}
