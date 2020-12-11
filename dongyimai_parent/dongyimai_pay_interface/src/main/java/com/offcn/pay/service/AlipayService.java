package com.offcn.pay.service;

import java.util.Map;

public interface AlipayService {

    //生成支付宝文档

    //预下单方法
    //得到二维码
    public Map createCode(String out_trade_no,String total_amount);

    /**
     * 查询支付状态
     * @param out_trade_no
     */
    public Map queryPayStatus(String out_trade_no);


}
