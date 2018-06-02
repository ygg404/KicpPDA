package com.kinde.kicppda.Utils;

/**
 * Created by YGG on 2018/5/26.
 */

import com.alibaba.fastjson.JSON;
import com.kinde.kicppda.Utils.Models.TokenResultMsg;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

/**
 * webapi接口帮助类
 */
public class ApiHelper {

    /**获取时间戳
     * 1970年1月1日到当前时间的毫秒数
     * @return
     */
    public static String GetTimeStamp()
    {
        Date nowDate  =  new Date();
        long timeSpan = nowDate.getTime();
        return String.valueOf(timeSpan);
    }

    /**
     * 获取随机数
     * @return
     */
    public static String GetRandom()
    {
        Random rd = new Random(System.currentTimeMillis());
        int i = rd.nextInt(Integer.MAX_VALUE);
        return String.valueOf(i);
    }



    public static <T> void writeExact(List<T> list, T item) {
        list.add(item);
    }

    /**
     * GET 接口
     * @param clazz
     * @param webApi
     * @param querymap
     * @param staffId
     * @param appSecret
     * @param sign
     * @param <T>
     * @return
     */
    public static  <T> T GetHttp(Class<T> clazz ,String webApi, HashMap<String, String> querymap, int staffId, String appSecret, boolean sign){
        String msg = null;
        T msgClass = null;

        try {
            //请求的数据
            String dataGet = "";
            for (String key : querymap.keySet()) {
                dataGet += "&" + key + "=" + URLEncoder.encode(querymap.get(key), "UTF-8");
                //System.out.println("key= "+ key + " and value= " + map.get(key));
            }
            dataGet = dataGet.substring(1, dataGet.length());
            //get请求的url
            URL url=new URL( webApi + dataGet);
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            //设置请求方式,请求超时信息
            conn.setRequestMethod("GET");
            conn.setReadTimeout(5000);
            conn.setConnectTimeout(5000);
            // 设置请求的头
            conn.setRequestProperty("staffid", String.valueOf(staffId) );
            conn.setRequestProperty("timestamp", ApiHelper.GetTimeStamp()); //发起请求时的时间戳（单位：毫秒）
            conn.setRequestProperty("nonce", ApiHelper.GetRandom()); //发起请求时的随机数
          //  conn.setRequestProperty("query", query);
            //开启连接
            conn.connect();
            InputStream inputStream=null;
            BufferedReader reader=null;
            //如果应答码为200的时候，表示成功的请求带了，这里的HttpURLConnection.HTTP_OK就是200
            if(conn.getResponseCode()==HttpURLConnection.HTTP_OK){
                //获得连接的输入流
                inputStream=conn.getInputStream();
                //转换成一个加强型的buffered流
                reader=new BufferedReader(new InputStreamReader(inputStream));
                //把读到的内容赋值给result
                String result = reader.readLine();
                msg = result;

            }
            //关闭流和连接
            reader.close();
            inputStream.close();
            conn.disconnect();

            msgClass = JSON.parseObject(  msg , clazz  );
        }catch (Exception ex){
            ;
        }
        finally {

            return msgClass;
        }

    }

    /**
     * 获取token接口
     * @param staffId
     * @param appSecret
     * @return
     */
    public static TokenResultMsg GetSignToken(int staffId, String appSecret){
        String tokenApi = Config.WebApiUrl + "GetToken?";
        HashMap<String,String> query = new HashMap<String, String>();
        query.put("staffId", String.valueOf(staffId));
        TokenResultMsg tokenResultMsg = GetHttp(TokenResultMsg.class ,tokenApi,query , Config.StaffId, Config.AppSecret,false);
        return tokenResultMsg;
    }
}
