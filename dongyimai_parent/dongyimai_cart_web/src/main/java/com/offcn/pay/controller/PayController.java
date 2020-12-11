package com.offcn.pay.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.offcn.entity.Result;
import com.offcn.pay.service.AlipayService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.UUID;

/**
 * controller
 *
 * @author Administrator
 */
@RestController
@RequestMapping("/pay")
public class PayController {

    @Reference
    private AlipayService alipayService;

    @RequestMapping("/createCode")
    public Map createCode() {

        return alipayService.createCode(UUID.randomUUID().toString(),"0.01");
    }

    @RequestMapping("/queryPayStatus")
    public Result queryPayStatus(String out_trade_no) {

        Result result = null;
        int x = 0;
        try {

            while (true) {


                Map map = alipayService.queryPayStatus(out_trade_no);
                if (map.get("tradestatus") != null && map.get("tradestatus").equals("TRADE_SUCCESS")) {
                    result = new Result(true, "支付成功");
                    break;
                }
                if (map.get("tradestatus") != null && map.get("tradestatus").equals("TRADE_CLOSED")) {
                    result = new Result(true, "未付款交易超时关闭");
                    break;
                }
                if (map.get("tradestatus") != null && map.get("tradestatus").equals("TRADE_FINISHED")) {
                    result = new Result(true, "交易结束");
                    break;
                }
                Thread.sleep(3000);
                if (x > 20) {
                result = new Result(false,"二维码失效");
                break;

                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            result = new Result(false, "查询支付异常");
        }

        return result;
    }


}
