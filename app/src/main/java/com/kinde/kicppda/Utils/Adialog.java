package com.kinde.kicppda.Utils;

/**
 * Created by Lenovo on 2018/7/2.
 */

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import com.kinde.kicppda.R;

/**
 * 提示窗口
 */
public class Adialog{
    AlertDialog alert = null;
    AlertDialog.Builder builder;

    public Adialog(Context mContext){
        builder = new AlertDialog.Builder(mContext);
    }

    public void deleteOkDialog(){
        alert = builder.setIcon(R.mipmap.fail)
                .setTitle("系统提示：")
                .setMessage("删除成功！")
                .setCancelable(false)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ;
                    }
                }).create();             //创建AlertDialog对象
        alert.show();                  //显示对话框
    }

    public void deleteFailDialog(){
        alert = builder.setIcon(R.mipmap.fail)
                .setTitle("系统提示：")
                .setMessage("删除失败！")
                .setCancelable(false)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ;
                    }
                }).create();             //创建AlertDialog对象
        alert.show();                    //显示对话框
    }
}
