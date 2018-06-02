package com.kinde.kicppda.Utils.Billing;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import com.kinde.kicppda.R;

/**
 * Created by YGG on 2018/6/1.
 */

public class GetBillDialog extends Dialog{
    public GetBillDialog(Context context) {
        super(context, R.style.progress_dialog);
        //加载布局文件
        LayoutInflater inflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view=inflater.inflate(R.layout.getbill, null);
    }
}
