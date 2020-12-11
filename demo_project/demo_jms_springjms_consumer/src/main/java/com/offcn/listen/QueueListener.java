package com.offcn.listen;

import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;


@Component
public class QueueListener implements MessageListener {


    public void onMessage(Message message) {
        try {
            TextMessage txt = (TextMessage) message;

            String msg = txt.getText();
            System.out.println("接收到的消息:" + msg);
        } catch (JMSException e) {
            e.printStackTrace();
        }


    }
}
