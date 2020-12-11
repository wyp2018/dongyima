package com.offcn.pay.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradePrecreateRequest;
import com.alipay.api.request.AlipayTradeQueryRequest;
import com.alipay.api.response.AlipayTradePrecreateResponse;
import com.alipay.api.response.AlipayTradeQueryResponse;
import com.offcn.pay.service.AlipayService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.Map;

@Service
public class AlipayServiceImpl implements AlipayService {

    @Autowired
    private AlipayClient alipayClient;

    @Override
    public Map createCode(String out_trade_no, String total_amount) {

        Map<String, String> map = new HashMap<String, String>();

        // 创建预下单请求对象
        AlipayTradePrecreateRequest request = new AlipayTradePrecreateRequest();

        // 设置业务参数
        request.setBizContent("{\"out_trade_no\":\"" + out_trade_no + "\","
                + "\"total_amount\":\"" + total_amount + "\","
                + "\"subject\":\"东易买商城收款\"}");

        // 发出预下单业务请求
        try {
            AlipayTradePrecreateResponse response = alipayClient.execute(request);

            // 从相应对象读取相应结果
            String code = response.getCode();
            System.out.println("alipay return code: " + code);

            // 全部的响应结果
            String body = response.getBody();
            System.out.println("alipay return result body: " + body);

            // 10000 表示预下单成功
            if (code.equals("10000")) {
                map.put("qrcode", response.getQrCode());
                map.put("out_trade_no", response.getOutTradeNo());
                map.put("total_amount", total_amount);
            } else {
                System.out.println("alipay fail: " + body);
            }
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }

        return map;
    }


    /**
     * 交易查询接口alipay.trade.query
     * 获取指定订单编号的，交易状态
     */
    @Override
    public Map<String, String> queryPayStatus(String out_trade_no) {

        Map<String, String> map = new HashMap<String, String>();

        AlipayTradeQueryRequest request = new AlipayTradeQueryRequest();
        // 设置业务参数
        request.setBizContent("{\"out_trade_no\":\"" + out_trade_no + "\"}");

        // 发出请求
        try {
            AlipayTradeQueryResponse response = alipayClient.execute(request);
            String code = response.getCode();

            System.out.println("query pay status code: " + code);
            System.out.println("query pay body: " + response.getBody());

            // 状态码等于10000，表示请求成功
            if (code.equals("10000")) {
                map.put("out_trade_no", out_trade_no);
                map.put("tradestatus", response.getTradeStatus());
            }
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        return map;
    }

}
