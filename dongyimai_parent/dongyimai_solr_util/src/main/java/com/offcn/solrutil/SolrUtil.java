package com.offcn.solrutil;


import com.alibaba.fastjson.JSON;
import com.github.promeg.pinyinhelper.Pinyin;
import com.offcn.mapper.TbItemMapper;
import com.offcn.pojo.TbItem;
import com.offcn.pojo.TbItemExample;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath*:spring-*.xml")
public class SolrUtil {

    @Autowired
    private SolrTemplate solrTemplate;

    @Autowired
    private  TbItemMapper itemMapper;

    @Test
    public void  testImport(){

        TbItemExample ex = new TbItemExample();
        ex.createCriteria().andStatusEqualTo("1");
        List<TbItem> itemList = itemMapper.selectByExample(ex);
        for (TbItem item : itemList) {

            //item中的spec属性是个字符串,我们需要标准的json格式
            Map<String,String> map = JSON.parseObject(item.getSpec(), Map.class);

            Map<String,String> hashmap = new HashMap<>();
            for (String ma : map.keySet()) {

                hashmap.put(  Pinyin.toPinyin(ma,"").toLowerCase(),map.get(ma));

            }
            item.setSpecMap(hashmap);
            System.out.println(item.getTitle() + ">>>>>" + item.getPrice());
        }
        System.out.println("import begin ...");
        solrTemplate.saveBeans(itemList);
        solrTemplate.commit();
        System.out.println("over");
    }


    @Test
    public  void testPinyin(){

        String s = Pinyin.toPinyin("重复了吗", "").toLowerCase();
        System.out.println(s);

    }
}
