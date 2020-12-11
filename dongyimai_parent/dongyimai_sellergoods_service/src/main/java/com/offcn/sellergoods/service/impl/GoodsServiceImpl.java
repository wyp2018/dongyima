package com.offcn.sellergoods.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.offcn.entity.PageResult;
import com.offcn.group.Goods;
import com.offcn.mapper.*;
import com.offcn.pojo.*;
import com.offcn.pojo.TbGoodsExample.Criteria;
import com.offcn.sellergoods.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

/**
 * goods服务实现层
 *
 * @author senqi
 */
@Service
public class GoodsServiceImpl implements GoodsService {

    @Autowired
    private TbGoodsMapper goodsMapper;

    @Autowired
    private TbGoodsDescMapper goodsDescMapper;

    @Autowired
    private TbItemMapper itemMapper;

    @Autowired
    private TbBrandMapper brandMapper;

    @Autowired
    private TbItemCatMapper itemCatMapper;

    @Autowired
    private TbSellerMapper sellerMapper;

    /**
     * 查询全部
     */
    @Override
    public List<TbGoods> findAll() {
        return goodsMapper.selectByExample(null);
    }

    /**
     * 分页
     */
    @Override
    public PageResult findPage(int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        Page<TbGoods> page = (Page<TbGoods>) goodsMapper.selectByExample(null);
        return new PageResult(page.getTotal(), page.getResult());
    }

    /**
     * 增加
     */
    @Override
    public void add(Goods goods) {


        goods.getGoods().setAuditStatus("1");
        //插入tb_goods表
        goodsMapper.insert(goods.getGoods());

        //设置关联
        goods.getGoodsDesc().setGoodsId(goods.getGoods().getId());
        goodsDescMapper.insert(goods.getGoodsDesc());

        insertItem(goods);


    }

    private void setItem(Goods goods, TbItem item) {
        List<Map> list = JSON.parseArray(goods.getGoodsDesc().getItemImages(), Map.class);
        if (list.size() > 0) {
            item.setImage((String) list.get(0).get("url"));
        }

        item.setCreateTime(new Date());
        item.setUpdateTime(new Date());
        item.setGoodsId(goods.getGoods().getId());
        String brandName = brandMapper.selectByPrimaryKey(goods.getGoods().getBrandId()).getName();
        item.setBrand(brandName);

        item.setCategoryid(goods.getGoods().getCategory3Id());
        String catName = itemCatMapper.selectByPrimaryKey(item.getCategoryid()).getName();
        item.setCategory(catName);

        item.setSellerId(goods.getGoods().getSellerId());
        String nickName = sellerMapper.selectByPrimaryKey(item.getSellerId()).getNickName();
        item.setSeller(nickName);
    }


    /**
     * 修改
     */
    @Override
    public void update(Goods goods) {

        //修改goods
        goodsMapper.updateByPrimaryKey(goods.getGoods());
        //修改goodsdesc
        goodsDescMapper.updateByPrimaryKey(goods.getGoodsDesc());
        //修改item
        TbItemExample ex = new TbItemExample();
        ex.createCriteria().andGoodsIdEqualTo(goods.getGoods().getId());

        int i = itemMapper.deleteByExample(ex);
        System.out.println(i);
        insertItem(goods);

    }

    //插入item
    private void insertItem(Goods goods) {
        if ("1".equals(goods.getGoods().getIsEnableSpec())) {

            for (TbItem item : goods.getItemList()) {

                String title = goods.getGoods().getGoodsName();
                Map<String, String> map = JSON.parseObject(item.getSpec(), Map.class);

                for (String key : map.keySet()) {
                    title += " " + map.get(key);
                }
                item.setTitle(title);
                setItem(goods, item);
                itemMapper.insert(item);
            }
        } else {
            TbItem item = new TbItem();
            String title = goods.getGoods().getGoodsName();
            item.setTitle(title);
            setItem(goods, item);
            item.setPrice(goods.getGoods().getPrice());
            item.setNum(999);
            item.setStatus("1");

            //不启用规格,一个spufu对应一个
            item.setIsDefault("1");
            item.setSpec("{}");
            itemMapper.insert(item);
        }
    }

    //
//    /**
//     * 根据ID获取实体
//     *
//     * @param id
//     * @return
//     */
    @Override
    public Goods findOne(Long id) {

        TbGoods goods = goodsMapper.selectByPrimaryKey(id);

        TbGoodsDesc goodsDesc = goodsDescMapper.selectByPrimaryKey(id);
        TbItemExample ex = new TbItemExample();
        ex.createCriteria().andGoodsIdEqualTo(id);
        List<TbItem> itemList = itemMapper.selectByExample(ex);

        return new Goods(goods,goodsDesc,itemList);
    }

    /**
     * 批量删除
     */
    @Override
    public void delete(Long[] ids) {
        for (Long id : ids) {

            TbGoods good = new TbGoods();
            good.setId(id);
            good.setIsDelete("1");
            goodsMapper.updateByPrimaryKeySelective(good);

            TbItemExample ex = new TbItemExample();
            ex.createCriteria().andGoodsIdEqualTo(id);
            List<TbItem> itemList = itemMapper.selectByExample(ex);
            for (TbItem item : itemList) {
                item.setStatus("3");
                itemMapper.updateByPrimaryKey(item);
            }
        }
    }

    /**
     * 分页+查询
     */
    @Override
    public PageResult findPage(TbGoods goods, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);

        TbGoodsExample example = new TbGoodsExample();
        Criteria criteria = example.createCriteria();

        if (goods != null) {
            if (goods.getSellerId() != null && goods.getSellerId().length() > 0) {
                criteria.andSellerIdEqualTo(goods.getSellerId() );
            }
            if (goods.getGoodsName() != null && goods.getGoodsName().length() > 0) {
                criteria.andGoodsNameLike("%" + goods.getGoodsName() + "%");
            }
            if (goods.getAuditStatus() != null && goods.getAuditStatus().length() > 0) {
                criteria.andAuditStatusLike("%" + goods.getAuditStatus() + "%");
            }
            if (goods.getIsMarketable() != null && goods.getIsMarketable().length() > 0) {
                criteria.andIsMarketableLike("%" + goods.getIsMarketable() + "%");
            }
            if (goods.getCaption() != null && goods.getCaption().length() > 0) {
                criteria.andCaptionLike("%" + goods.getCaption() + "%");
            }
            if (goods.getSmallPic() != null && goods.getSmallPic().length() > 0) {
                criteria.andSmallPicLike("%" + goods.getSmallPic() + "%");
            }
            if (goods.getIsEnableSpec() != null && goods.getIsEnableSpec().length() > 0) {
                criteria.andIsEnableSpecLike("%" + goods.getIsEnableSpec() + "%");
            }
            if (goods.getIsDelete() != null && goods.getIsDelete().length() > 0) {
                criteria.andIsDeleteLike("%" + goods.getIsDelete() + "%");
            }
        }



        criteria.andIsDeleteIsNull();
        example.setOrderByClause("id desc");


        Page<TbGoods> page = (Page<TbGoods>) goodsMapper.selectByExample(example);
        return new PageResult(page.getTotal(), page.getResult());
    }

    @Override
    public void updateStatus(Long[] ids, String status) {
        for (Long id : ids) {
            TbGoods goods = new TbGoods();
            goods.setId(id);
            goods.setAuditStatus(status);
            goodsMapper.updateByPrimaryKeySelective(goods);
        }
    }

    @Override
    public List<TbItem> findItemListBySpuId(Long[] ids, String status) {

        TbItemExample ex = new TbItemExample();
        TbItemExample.Criteria c = ex.createCriteria();


        c.andGoodsIdIn(Arrays.asList(ids));
        c.andStatusEqualTo(status);
        return itemMapper.selectByExample(ex);
    }

}
