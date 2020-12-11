package com.offcn.test;

import com.offcn.util.HttpUtils;
import org.apache.http.HttpResponse;

import java.util.HashMap;
import java.util.Map;

public class TestMsg {

    public static void main(String[] args) {
        String host = "http://dingxin.market.alicloudapi.com";
        String path = "/dx/sendSms";
        String method = "POST";
        // 输入自己的appcode
        String appcode = "284644a673134b4c830677fcf43eaeda";

        Map<String, String> headers = new HashMap<String, String>();
        //  英文空格：Authorization:APPCODE 83359fd73fe94948385f570e3c139105
        headers.put("Authorization", "APPCODE " + appcode);

        Map<String, String> querys = new HashMap<String, String>();
        querys.put("mobile", "15237227793");
        querys.put("param", "code:999000");
        querys.put("tpl_id", "TP1711063");

        Map<String, String> bodys = new HashMap<String, String>();
        try {
            // 拷贝 资源\工具类 中的 HttpUtils.java
            HttpResponse response = HttpUtils.doPost(host, path, method, headers, querys, bodys);

            System.out.println(response.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
