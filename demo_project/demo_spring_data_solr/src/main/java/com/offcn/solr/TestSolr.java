package com.offcn.solr;


import com.offcn.pojo.TbItem;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.query.Criteria;
import org.springframework.data.solr.core.query.Query;
import org.springframework.data.solr.core.query.SimpleQuery;
import org.springframework.data.solr.core.query.result.ScoredPage;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring-solr.xml")
public class TestSolr {

    @Autowired
    private SolrTemplate solrTemplate;

    @Test
    public void testAdd(){

        TbItem item = new TbItem();
        item.setId(2L);
        item.setBrand("小米为");
        item.setCategory("手机pluse");
        item.setGoodsId(1L);
        item.setSeller("小米1号专卖店");
        item.setTitle("红米Mate9");
        item.setPrice(new BigDecimal(2200));

        solrTemplate.saveBean(item);
        solrTemplate.commit();
    }

    @Test
    public void testDel(){

//        solrTemplate.deleteById("1");

        Query query = new SimpleQuery("*:*");


        solrTemplate.delete(query);

        solrTemplate.commit();

    }


    @Test
    public void testAddmany(){

        List<TbItem> list = new ArrayList<>();

        for(int i=1;i<51;i++){
            TbItem item = new TbItem();
            item.setId(Long.valueOf(i));
            item.setBrand("华为");
            item.setCategory("手机");
            item.setGoodsId(1L);
            item.setSeller("华为"+ i+ "号专卖店");
            item.setTitle("华为Mate"  + i);
            item.setPrice(new BigDecimal(2200 + i));
            list.add(item);
        }


        solrTemplate.saveBeans(list );
        solrTemplate.commit();
    }

    @Test
    public void testQuery(){

        Query query = new SimpleQuery("*:*");
        Criteria c1 = new Criteria("item_price").lessThanEqual(2220);
        query.addCriteria(c1);
        Criteria c2 = new Criteria("item_seller").is("2");
        query.addCriteria(c2);

        int pageSize = 20;
//        int pageNo = 2;
        query.setRows(pageSize);
//        query.setOffset((pageNo - 1)*pageSize);

        Sort sort = new Sort(Sort.Direction.DESC,"item_price");
        query.addSort(sort);
        ScoredPage<TbItem> result = solrTemplate.queryForPage(query, TbItem.class);

        long total = result.getTotalElements();

        List<TbItem> list = result.getContent();

        System.out.println("总记录为" + total);
        for (TbItem item : list) {
            System.out.println(item.getId() + ">>>>>" + item.getTitle() + ">>>>" + item.getPrice());
        }

    }




}
