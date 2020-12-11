package com.offcn.search.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.offcn.mapper.TbGoodsDescMapper;
import com.offcn.mapper.TbGoodsMapper;
import com.offcn.mapper.TbItemCatMapper;
import com.offcn.mapper.TbItemMapper;
import com.offcn.page.service.ItemPageService;
import com.offcn.pojo.TbGoods;
import com.offcn.pojo.TbGoodsDesc;
import com.offcn.pojo.TbItem;
import com.offcn.pojo.TbItemExample;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ItemPageServiceImpl implements ItemPageService {

    @Autowired
    private TbGoodsMapper goodsMapper;

    @Autowired
    private TbGoodsDescMapper goodsDescMapper;

    @Autowired
    private TbItemMapper itemMapper;

    @Autowired
    private TbItemCatMapper itemCatMapper;

    @Value("${page}")
    private String page;

    @Autowired
    private FreeMarkerConfigurer freeMarkerConfigurer;


    @Override
    public void createPage(Long goodsId) {

        //使用模板技术生成静态页面


        try {

            //有个模板
            Configuration conf = freeMarkerConfigurer.getConfiguration();

            Template template = conf.getTemplate("item.ftl");

            //准备模板需要的数据 tb_goods
            TbGoods goods = goodsMapper.selectByPrimaryKey(goodsId);


            //tb_goodsDesc
            TbGoodsDesc goodsDesc = goodsDescMapper.selectByPrimaryKey(goodsId);
            //tb_item
            TbItemExample ex = new TbItemExample();
            TbItemExample.Criteria c = ex.createCriteria();
            c.andGoodsIdEqualTo(goodsId);

            c.andStatusEqualTo("1");
            ex.setOrderByClause("is_default desc");

            List<TbItem> itemList = itemMapper.selectByExample(ex);

            //分类的名字
            String cat1Name = itemCatMapper.selectByPrimaryKey(goods.getCategory1Id()).getName();
            String cat2Name = itemCatMapper.selectByPrimaryKey(goods.getCategory2Id()).getName();
            String cat3Name = itemCatMapper.selectByPrimaryKey(goods.getCategory3Id()).getName();

            Map dataModel = new HashMap();
            dataModel.put("goods",goods);

            dataModel.put("goodsDesc",goodsDesc);

            dataModel.put("itemList",itemList);

            dataModel.put("itemCat1",cat1Name);
            dataModel.put("itemCat2",cat2Name);
            dataModel.put("itemCat3",cat3Name);

            //让模板和数据生成数据
            Writer out = new FileWriter(  page + goodsId + ".html");
            template.process(dataModel,out);

            out.close();

        } catch (Exception e) {
            e.printStackTrace();
        }




    }

    @Override
    public void deletePage(Long goodsId) {

       new File(page + goodsId + ".html").delete();
    }
}
