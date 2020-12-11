package com.offcn.search.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.github.promeg.pinyinhelper.Pinyin;
import com.offcn.pojo.TbItem;
import com.offcn.search.service.ItemSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.query.*;
import org.springframework.data.solr.core.query.result.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service(timeout = 6000)
public class ItemSearchServiceImpl implements ItemSearchService {


    @Autowired
    private SolrTemplate solrTemplate;


    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public Map<String, Object> search(Map searchMap) {

        Map<String, Object> map = new HashMap<>();

        //如果搜索词中有空格,分词效果就失效了
        //去掉空格
        String keywords = (String) searchMap.get("keywords");
        String result = keywords.replace(" ", "");
        searchMap.put("keywords", result);

        //关键字查询
        hlSearch(searchMap, map);
        categorySearch(searchMap, map);

        String catName = (String) searchMap.get("category");


        if (!"".equals(catName)) {
            brandAndSpecSearch(catName, map);
        } else {
            List<String> catList = (List<String>) map.get("categoryList");
            if (catList.size() > 0) {

                brandAndSpecSearch(catList.get(0), map);
            }
        }

        return map;
    }

    //根据商品 spu id 移除solr库中的item(sku)
    @Override
    public void deleteItem(Long[] ids) {

        //条件删除
        Query query = new SimpleQuery();
        Criteria cri = new Criteria("item_goodsid").in(ids);

        query.addCriteria(cri);
        solrTemplate.delete(query);
        solrTemplate.commit();

    }

    //将传递过来的itemList 导入solr库
    @Override
    public void importItemLost(List<TbItem> itemList) {

        for (TbItem item : itemList) {

            //item中的spec属性是个字符串,我们需要标准的json格式
            Map<String, String> map = JSON.parseObject(item.getSpec(), Map.class);

            Map<String, String> newmap = new HashMap<>();
            for (String ma : map.keySet()) {
                newmap.put(Pinyin.toPinyin(ma, "").toLowerCase(), map.get(ma));
            }
            item.setSpecMap(newmap);
        }



        solrTemplate.saveBeans(itemList);
        solrTemplate.commit();
    }

    //根据分类名从缓存中获取品牌,规格列表
    private void brandAndSpecSearch(String catName, Map<String, Object> map) {


        Long typeId = (Long) redisTemplate.boundHashOps("itemCatList").get(catName);

        List<Map> brandList = (List<Map>) redisTemplate.boundHashOps("brandList").get(typeId);

        List<Map> specList = (List<Map>) redisTemplate.boundHashOps("specAndOptionList").get(typeId);
        map.put("brandList", brandList);
        map.put("specList", specList);


    }

    //根据关键字按照分组查询
    private void categorySearch(Map searchMap, Map<String, Object> map) {

        List<String> list = new ArrayList<>();
        String keywords = (String) searchMap.get("keywords");
        Query query = new SimpleQuery();
        Criteria c = new Criteria("item_keywords").is(keywords);
        query.addCriteria(c);

        GroupOptions options = new GroupOptions().addGroupByField("item_category");
        query.setGroupOptions(options);
        GroupPage<TbItem> result = solrTemplate.queryForGroupPage(query, TbItem.class);

        GroupResult<TbItem> groupResult = result.getGroupResult("item_category");

        Page<GroupEntry<TbItem>> groupEntries = groupResult.getGroupEntries();

        for (GroupEntry<TbItem> groupEntry : groupEntries.getContent()) {
            list.add(groupEntry.getGroupValue());
        }
        map.put("categoryList", list);


    }

    //关键字  +  高亮 + 分类 + 分页 + 排序
    private void hlSearch(Map searchMap, Map<String, Object> map) {
        String keywords = (String) searchMap.get("keywords");
        HighlightQuery query = new SimpleHighlightQuery();
        //设置高亮属性
        HighlightOptions options = new HighlightOptions().addField("item_title");
        options.setSimplePrefix("<span style='color:red'>");
        options.setSimplePostfix("</span>");
        query.setHighlightOptions(options);
        Criteria c = new Criteria("item_keywords").is(keywords);
        query.addCriteria(c);
        //添加除了关键字以外的条件
        //分类
        String category = (String) searchMap.get("category");
        if (!"".equals(category)) {

            Criteria catC = new Criteria("item_category").is(category);
            query.addCriteria(catC);
        }
        //品牌
        String brand = (String) searchMap.get("brand");
        if (!"".equals(brand)) {
            Criteria brandC = new Criteria("item_brand").is(brand);
            query.addCriteria(brandC);
        }
        //规格
        Map<String, String> specMap = (Map) searchMap.get("spec");
        if (specMap != null) {

            for (String key : specMap.keySet()) {
                Criteria specCri = new Criteria("item_spec_" + Pinyin.toPinyin(key, "").toLowerCase()).is(specMap.get(key));
                query.addCriteria(specCri);
            }

        }
        //价格:
        String price = (String) searchMap.get("price");
        if (!"".equals(price)) {
            String[] priceArr = price.split("-");

            //大于等于最小价格
            Criteria minPrice = new Criteria("item_price").greaterThanEqual(Integer.parseInt(priceArr[0]));
            query.addCriteria(minPrice);

            if (!"*".equals(priceArr[1])) {
                Criteria maxPrice = new Criteria("item_price").lessThanEqual(Integer.parseInt(priceArr[1]));
                query.addCriteria(maxPrice);
            }
        }

        //分页
        Integer pageNo = (Integer) searchMap.get("pageNo");
        Integer pageSize = (Integer) searchMap.get("pageSize");
        query.setRows(pageSize);
        query.setOffset((pageNo - 1) * pageSize);


        //排序
        String sortField = (String) searchMap.get("sortField");
        String sortVal = (String) searchMap.get("sortVal");

        if (!"".equals(sortVal)) {
            Sort sort = null;
            if ("desc".equals(sortVal)) {
                sort = new Sort(Sort.Direction.DESC, sortField);
            } else {
                sort = new Sort(Sort.Direction.ASC, sortField);
            }
            query.addSort(sort);

        }

        HighlightPage<TbItem> result = solrTemplate.queryForHighlightPage(query, TbItem.class);


        for (HighlightEntry<TbItem> entry : result.getHighlighted()) {
            TbItem item = entry.getEntity();

            if (entry.getHighlights().size() > 0 && entry.getHighlights().get(0).getSnipplets().size() > 0) {

                item.setTitle(entry.getHighlights().get(0).getSnipplets().get(0));
            }

        }


        map.put("rows", result.getContent());
        map.put("total", result.getTotalElements());
        map.put("totalPage", result.getTotalPages());
    }
}
