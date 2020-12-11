package com.offcn.listen;


import com.alibaba.fastjson.JSON;
import com.offcn.pojo.TbItem;
import com.offcn.search.service.ItemSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.jms.*;
import java.util.List;

@Component
public class DeleteSolrQueue implements MessageListener {

    @Autowired
    private ItemSearchService itemSearchService;

    @Override
    public void onMessage(Message message) {

        try {

            ObjectMessage obj = (ObjectMessage)message;
            Long[] ids = (Long[])obj.getObject();

            itemSearchService.deleteItem(ids);
            System.out.println("active delete solr success...");
        } catch (JMSException e) {
            e.printStackTrace();
        }

    }
}
