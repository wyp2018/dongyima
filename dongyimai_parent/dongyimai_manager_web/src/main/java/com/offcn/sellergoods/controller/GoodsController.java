package com.offcn.sellergoods.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.offcn.entity.PageResult;
import com.offcn.entity.Result;
import com.offcn.group.Goods;
import com.offcn.pojo.TbGoods;
import com.offcn.pojo.TbItem;
import com.offcn.sellergoods.service.GoodsService;
import org.apache.activemq.command.ActiveMQQueue;
import org.apache.activemq.command.ActiveMQTopic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * goodscontroller
 *
 * @author senqi
 */
@RestController
@RequestMapping("/goods")
public class GoodsController {

    @Reference
    private GoodsService goodsService;

    @Autowired
    private JmsTemplate jmsTemplate;

    @Resource(name = "import_solr_queue")
    private ActiveMQQueue importSolrQueue;

    @Resource(name = "delete_solr_queue")
    private ActiveMQQueue deleteSolrQueue;

    @Autowired
    @Qualifier("create_page_topic")
    private ActiveMQTopic createPageTopic;

    @Autowired
    @Qualifier("delete_page_topic")
    private ActiveMQTopic deletePageTopic;

/*
    //为了单独测试页面生成服务,写个测试url
	@RequestMapping("/create")
	public void create(Long goodsId){
		itemPageService.createPage(goodsId);
	}*/

    //
//	/**
//	 * 返回全部列表
//	 * @return
//	 */
    @RequestMapping("/findAll")
    public List<TbGoods> findAll() {
        return goodsService.findAll();
    }


    //	/**
//	 * 返回全部列表
//	 * @return
//	 */
    @RequestMapping("/findPage")
    public PageResult findPage(int page, int rows) {
        return goodsService.findPage(page, rows);
    }

    //	/**
//	 * 增加
//	 * @param goods
//	 * @return
//	 */
    @RequestMapping("/add")
    public Result add(@RequestBody Goods goods) {
        try {
            goodsService.add(goods);
            return new Result(true, "增加成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "增加失败");
        }
    }

    /**
     * 修改
     *
     * @param goods
     * @return
     */
    @RequestMapping("/update")
    public Result update(@RequestBody Goods goods) {
        try {
            goodsService.update(goods);
            return new Result(true, "修改成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "修改失败");
        }
    }


    @RequestMapping("/updateStatus")
    public Result updateStatus(Long[] ids, String status) {
        try {

            //审核通过:导入solr库
            if ("1".equals(status)) {
                //通过id找对应的正常的sku列表
                List<TbItem> itemList = goodsService.findItemListBySpuId(ids, status);

//                itemSearchService.importItemLost(itemList);
               //开始发送消息:说我要导入solr库
                String s = JSON.toJSONString(itemList);

                jmsTemplate.convertAndSend(importSolrQueue,s);

                //生成商品详情页
                for (Long id : ids) {
                   jmsTemplate.convertAndSend(createPageTopic,ids);
                }
            }
            //驳回:移除
            if ("2".equals(status)) {
              jmsTemplate.convertAndSend(deleteSolrQueue,ids);

              //驳回时删除页面
              jmsTemplate.convertAndSend(deletePageTopic,ids);
            }

            goodsService.updateStatus(ids, status);
            return new Result(true, "审核成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "审核失败");
        }
    }

    /**
     * 获取实体
     *
     * @param id
     * @return
     */
    @RequestMapping("/findOne")
    public Goods findOne(Long id) {
        return goodsService.findOne(id);
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @RequestMapping("/delete")
    public Result delete(Long[] ids) {
        try {
            goodsService.delete(ids);
            return new Result(true, "删除成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "删除失败");
        }
    }

    //	/**
//	 * 查询+分页
//	 * @param brand
//	 * @param page
//	 * @param rows
//	 * @return
//	 */
    @RequestMapping("/search")
    public PageResult search(@RequestBody TbGoods goods, int page, int size) {
        return goodsService.findPage(goods, page, size);
    }

}
