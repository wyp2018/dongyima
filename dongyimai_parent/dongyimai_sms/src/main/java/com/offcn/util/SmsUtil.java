package com.offcn.util;

import org.apache.http.HttpResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class SmsUtil {


    @Value("${host}")
    private String host;

    @Value("${path}")
    private String path;

    @Value("${method}")
    private String method;

    @Value("${appcode}")
    private String appcode;

    @Value("${tpl_id}")
    private String tpl_id;
    public  HttpResponse sendSms(String mobile,String code) {

        Map<String, String> headers = new HashMap<String, String>();
        //  英文空格：Authorization:APPCODE 83359fd73fe94948385f570e3c139105
        headers.put("Authorization", "APPCODE " + appcode);

        Map<String, String> querys = new HashMap<String, String>();
        querys.put("mobile", mobile);
        querys.put("param", "code:" + code);
        querys.put("tpl_id", tpl_id);

        Map<String, String> bodys = new HashMap<String, String>();
        try {
            // 拷贝 资源\工具类 中的 HttpUtils.java
            HttpResponse response = HttpUtils.doPost(host, path, method, headers, querys, bodys);

            return response;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }




}
