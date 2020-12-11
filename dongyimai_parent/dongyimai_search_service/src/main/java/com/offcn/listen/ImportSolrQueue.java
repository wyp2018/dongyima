package com.offcn.listen;


import com.alibaba.fastjson.JSON;
import com.offcn.pojo.TbItem;
import com.offcn.search.service.ItemSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import java.util.List;

@Component
public class ImportSolrQueue implements MessageListener {

    @Autowired
    private ItemSearchService itemSearchService;

    @Override
    public void onMessage(Message message) {

        try {

            TextMessage txt = (TextMessage)message;

            String msg = txt.getText();

            List<TbItem> itemList = JSON.parseArray(msg,TbItem.class);
            itemSearchService.importItemLost(itemList);
            System.out.println("active import solr success...");
        } catch (JMSException e) {
            e.printStackTrace();
        }

    }
}
