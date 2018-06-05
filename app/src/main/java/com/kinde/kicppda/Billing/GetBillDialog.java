package com.kinde.kicppda.Billing;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.kinde.kicppda.R;
import com.kinde.kicppda.Utils.Enum.BillTypeEnum;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by YGG on 2018/6/1.
 */

public class GetBillDialog extends Dialog{
    private TextView billtitle;
    private TextView datebegin;
    private TextView dateend;
    private Button getbtn;
    private Button quitbtn;

    public GetBillDialog(Context context ,int type) {
        super(context ,R.style.AppTheme);
        //加载布局文件
        LayoutInflater inflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view=inflater.inflate(R.layout.get_bill, null);

        //dialog添加视图
        setContentView(view);
        //初始化视图
        initView(type);
        show();  //显示
    }

    /**
     * 初始化对单据话框
     */
    private void initView(int type)
    {
        billtitle = (TextView)findViewById(R.id.bill_Title);
        quitbtn = (Button)findViewById(R.id.quit_btn);
        getbtn =(Button)findViewById(R.id.get_btn);
        datebegin = (TextView)findViewById(R.id.date_begin);
        dateend =(TextView)findViewById(R.id.date_end);

        //获取当前日期
        Date dnow = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        datebegin.setText(sdf.format(dnow));
        dateend.setText(sdf.format(dnow));


        String Title = "";

        if( type == BillTypeEnum.intype.getValue() )
        {
            Title = "获取" + BillTypeEnum.intype.getTypeName()+"单据";
        }
        else if( type == BillTypeEnum.ordertype.getValue() )
        {
            Title = "获取" + BillTypeEnum.ordertype.getTypeName()+"单据";
        }
        //设置标题
        billtitle.setText(Title.toString());

        quitbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

}
