package com.kinde.kicppda.ScanDialog;

import android.app.Dialog;
import android.content.Context;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;

import com.kinde.kicppda.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by YGG on 2018/6/4.
 */

public class InScanDialog extends Dialog implements  View.OnClickListener {

    private Spinner numSpinner;
    private EditText billDate;
    private EditText inWarehouse;
    private EditText inProduct;
    private EditText inPr;
    private EditText inLn;
    private ListView mListView;
    private Context mContext;
    private ImageView mGoback;
    private HorizontalScrollView mHorizontalScrollView;
    private SimpleAdapter adapter;

    private  List<HashMap<String, Object>> data = new ArrayList<HashMap<String,Object>>();

    public InScanDialog(Context context) {
        super(context , R.style.AppTheme);
        mContext = context;

        //加载布局文件
        LayoutInflater inflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view=inflater.inflate(R.layout.scan_in, null);

        //dialog添加视图
        setContentView(view);
        //初始化视图
        initView();
        show();  //显示
    }

    private void initView(){
        billDate = (EditText)findViewById(R.id.bill_date);
        inWarehouse = (EditText)findViewById(R.id.in_warehouse);
        inProduct = (EditText)findViewById(R.id.in_product);
        inPr = (EditText)findViewById(R.id.in_pr);
        inLn = (EditText)findViewById(R.id.in_ln);
        mListView = (ListView)findViewById(R.id.in_list_view);
        mGoback = (ImageView)findViewById(R.id.go_back);
        mHorizontalScrollView =(HorizontalScrollView)findViewById(R.id.HorizontalScrollView);

        mGoback.setOnClickListener(this);



        //创建SimpleAdapter适配器将数据绑定到item显示控件上
        adapter = new SimpleAdapter(mContext, data, R.layout.item_inlist,
                new String[]{"pcode", "pname", "pln","pr"}, new int[]{R.id.pcode, R.id.pname, R.id.pln,R.id.pr});

        EnterListShow(inProduct);
        //实现列表的显示
        mListView.setAdapter(adapter);
        //条目点击事件
        mListView.setOnItemClickListener(new ItemClickListener());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bill_date:

                break;
            case R.id.go_back:
                dismiss();
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


                    HashMap<String, Object> item = new HashMap<String, Object>();
                    item.put("pcode", "1");
                    item.put("pname", "1");
                    item.put("pln", "1");
                    item.put("pr", "1");
                    data.add(item);
                    adapter.notifyDataSetChanged();
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
