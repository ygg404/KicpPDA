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
import com.kinde.kicppda.Utils.Enum.BillTypeEnum;


public class MainActivity extends Activity implements View.OnClickListener {

    //单据类型
    private int BillType;

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

    private LinearLayout inBtnView;
    private LinearLayout orderBtnView;
    private LinearLayout returnBtnView;
    private LinearLayout allotBtnView;
    private LinearLayout checkBtnView;

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
        in_content.setSelected(false);
        order_content.setSelected(false);
        return_content.setSelected(false);
        allot_content.setSelected(false);
        check_content.setSelected(false);

        inBtnView.setSelected(false);
        orderBtnView.setSelected(false);
        returnBtnView.setSelected(false);
        allotBtnView.setSelected(false);
        checkBtnView.setSelected(false);

        inBtnView.removeAllViews();
        orderBtnView.removeAllViews();
        returnBtnView.removeAllViews();
        allotBtnView.removeAllViews();
        checkBtnView.removeAllViews();
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
        LayoutInflater inflater = LayoutInflater.from(this);
        LinearLayout sub = (LinearLayout) inflater.inflate(R.layout.fg_data_btn, null);
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
            case R.id.in_channel:
                setMenus();
                fTransaction.show(fg);
                in_content.setSelected(true);
                inBtnView.setSelected(true);
                inBtnView.addView(sub);
                btnViewInit();
                BillType = 1;
                break;
            case R.id.order_channel:
                setMenus();
                fTransaction.show(fg);
                order_content.setSelected(true);
                orderBtnView.setSelected(true);
                orderBtnView.addView(sub);
                btnViewInit();
                BillType = 2;
                break;
            case R.id.return_channel:
                setMenus();
                fTransaction.show(fg);
                return_content.setSelected(true);
                returnBtnView.setSelected(true);
                returnBtnView.addView(sub);
                btnViewInit();
                BillType = 3;
                break;
            case R.id.allot_channel:
                setMenus();
                fTransaction.show(fg);
                allot_content.setSelected(true);
                allotBtnView.setSelected(true);
                allotBtnView.addView(sub);
                btnViewInit();
                BillType = 4;
                break;
            case R.id.check_channel:
                setMenus();
                fTransaction.show(fg);
                check_content.setSelected(true);
                checkBtnView.setSelected(true);
                checkBtnView.addView(sub);
                btnViewInit();
                BillType = 5;
                break;
            //单据
            case R.id.bill_btn:
                fTransaction.show(fg);
                //新建一个显式意图，第一个参数为当前Activity类对象，第二个参数为你要打开的Activity类
                Intent intent =new Intent(MainActivity.this,GetBillActivity.class);
                //用Bundle携带数据
                Bundle bundle=new Bundle();
                //传递参数为billType
                bundle.putString("billType", String.valueOf(BillType));
                intent.putExtras(bundle);
                startActivity(intent);
                break;
            //采集
            case R.id.scan_btn:
                fTransaction.show(fg);
                new InScanDialog(MainActivity.this );
                break;
            //查询
            case R.id.query_btn:
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
            View view = inflater.inflate(R.layout.fg_datamanager,container,false);
            FragmentBind(view);
            return view;
        }
    }

    /**
     * 初始化 Fragment
      * @param view
     */
    public void FragmentBind(View view){
        in_content = (TextView)view.findViewById(R.id.in_channel);
        order_content = (TextView)view.findViewById(R.id.order_channel);
        return_content = (TextView)view.findViewById(R.id.return_channel);
        allot_content = (TextView)view.findViewById(R.id.allot_channel);
        check_content = (TextView)view.findViewById(R.id.check_channel);

        inBtnView = (LinearLayout)view.findViewById(R.id.in_btn_view);
        orderBtnView = (LinearLayout)view.findViewById(R.id.order_btn_view);;
        returnBtnView = (LinearLayout)view.findViewById(R.id.return_btn_view);;
        allotBtnView = (LinearLayout)view.findViewById(R.id.allot_btn_view);;
        checkBtnView = (LinearLayout)view.findViewById(R.id.check_btn_view);;

        in_content.setOnClickListener(this);
        order_content.setOnClickListener(this);
        return_content.setOnClickListener(this);
        allot_content.setOnClickListener(this);
        check_content.setOnClickListener(this);

    }

    /**
     * 按钮初始化
     */
    public void btnViewInit(){
        bill_btn  = (Button)findViewById(R.id.bill_btn);
        scan_btn  = (Button)findViewById(R.id.scan_btn);
        query_btn = (Button)findViewById(R.id.query_btn);

        bill_btn.setOnClickListener(this);
        scan_btn.setOnClickListener(this);
        query_btn.setOnClickListener(this);
    }

}
