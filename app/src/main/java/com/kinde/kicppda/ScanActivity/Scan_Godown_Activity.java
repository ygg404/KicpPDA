package com.kinde.kicppda.ScanActivity;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;

import com.kinde.kicppda.R;
import com.kinde.kicppda.Utils.Public;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by YGG on 2018/6/4.
 */

public class Scan_Godown_Activity extends Activity implements  View.OnClickListener {

    private Spinner numSpinner;             //单据号选择项
    private EditText billDate;              //单据日期
    private EditText Warehouse;           //入库仓库
    private EditText Product;             //入库产品
    private EditText Pr;                  //生产日期
    private EditText Ln;                  //生产批次
    private ListView mListView;             //产品选择列表
    private ImageView mGoback;              //返回键
    private HorizontalScrollView mHorizontalScrollView;
    private ArrayAdapter<String> spinAdapter;       //单据号spinner的适配器
    private SimpleAdapter digAdapter;
    private  List<HashMap<String, Object>> ProductInfo = new ArrayList<HashMap<String,Object>>();   //产品信息

    private ScanHelper mScanHelper;         //单据号获取帮助类
    private List<String> godownNumList;         //入库单据编号列表

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scan_godown);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        initView();
    }

    private void initView(){
        numSpinner = (Spinner)findViewById(R.id.num_spinner);
        billDate = (EditText)findViewById(R.id.bill_date);
        Warehouse = (EditText)findViewById(R.id.in_warehouse);
        Product = (EditText)findViewById(R.id.in_product);
        Pr = (EditText)findViewById(R.id.in_pr);
        Ln = (EditText)findViewById(R.id.in_ln);
        mListView = (ListView)findViewById(R.id.in_list_view);
        mGoback = (ImageView)findViewById(R.id.go_back);
        mHorizontalScrollView =(HorizontalScrollView)findViewById(R.id.HorizontalScrollView);

        mGoback.setOnClickListener(this);

        //初始化单据选项列表
        mScanHelper = new ScanHelper(Scan_Godown_Activity.this);
        godownNumList = mScanHelper.getBillNum(Public.IN_MAIN_TABLE);
        spinAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, godownNumList);
        spinAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        numSpinner.setAdapter(spinAdapter);
        numSpinner.setOnItemSelectedListener(
                new Spinner.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

                        billDate.setText(   mScanHelper.getKeyValue("GodownDate", numSpinner.getSelectedItem().toString() ) );
                        Warehouse.setText(  mScanHelper.getKeyValue("WarehouseName", numSpinner.getSelectedItem().toString() )  );
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> arg0) {
                                                 }
                });

        mListView.bringToFront();
        //创建SimpleAdapter适配器将数据绑定到item显示控件上
        digAdapter = new SimpleAdapter(Scan_Godown_Activity.this, ProductInfo, R.layout.item_inlist,
                new String[]{"pcode", "pname", "pln","pr"}, new int[]{R.id.pcode, R.id.pname, R.id.pln,R.id.pr});
        EnterListShow(Product);
        //实现列表的显示
        mListView.setAdapter(digAdapter);
        //条目点击事件
        mListView.setOnItemClickListener(new ItemClickListener());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bill_date:

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
            HashMap<String, Object> getdata = (HashMap<String, Object>) listView.getItemAtPosition(position);

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
                    String tableName = numSpinner.getSelectedItem().toString() + Public.GodownBillingType;
                    String keyValue = Product.getText().toString();
                    List<String[]> BillInfoList = mScanHelper.getProductInfo( tableName , keyValue);

                    for( String[] billInfo : BillInfoList){
                        HashMap<String, Object> item = new HashMap<String, Object>();
                        item.put("pcode", billInfo[0]);
                        item.put("pname", billInfo[1]);
                        item.put("pln", billInfo[2]);
                        item.put("pr", billInfo[3]);
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
}
