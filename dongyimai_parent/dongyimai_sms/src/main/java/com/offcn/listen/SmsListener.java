package com.offcn.listen;

import com.offcn.util.SmsUtil;
import org.apache.http.HttpResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageListener;

@Component
public class SmsListener implements MessageListener {

    @Autowired
    private SmsUtil smsUtil;

    @Override
    public void onMessage(Message message) {

        try {
            MapMessage map = (MapMessage)message;

            String mobile = map.getString("mobile");
            String code = map.getString("code");


            System.out.println(">>>>>mobile:" + mobile + ">>>code:" + code);

            HttpResponse resp = smsUtil.sendSms(mobile,code);
            int statusCode = resp.getStatusLine().getStatusCode();


            System.out.println("activeMq: send sms success... "  + statusCode);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
