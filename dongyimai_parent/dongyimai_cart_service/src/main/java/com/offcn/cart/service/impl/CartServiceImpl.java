package com.offcn.cart.service.impl;


import com.alibaba.dubbo.config.annotation.Service;
import com.offcn.cart.service.CartServcie;
import com.offcn.entity.Cart;
import com.offcn.mapper.TbItemMapper;
import com.offcn.pojo.TbItem;
import com.offcn.pojo.TbOrderItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class CartServiceImpl implements CartServcie {


    @Autowired
    private TbItemMapper itemMapper;

    @Autowired
    private RedisTemplate redisTemplate;

    /*
     * 添加商品至购物车
     * cartList 老的购物车列表
     * skuId 要添加的商品Id
     * num  要添加的商品数量
     *
     * */
    @Override
    public List<Cart> addGoodsToCartList(List<Cart> cartList, Long skuId, Integer num) {

        //查询要添加的商品
        TbItem item = itemMapper.selectByPrimaryKey(skuId);

        if (item == null) {
            throw new RuntimeException("该商品不存在");
        }

        if (item.getNum() < 1) {
            throw new RuntimeException("该商品库存不足,暂时无法购买");
        }
        if (!item.getStatus().equals("1")) {
            throw new RuntimeException("该商品暂不支持购买");
        }

        //添加时先判断商家是否存在
        Cart cart = findSellerBySellerIdInCartList(cartList, item.getSellerId());
        //商家不存在
        if (cart == null) {
            cart = new Cart();
            cart.setSellerId(item.getSellerId());
            cart.setSellerName(item.getSeller());

            List<TbOrderItem> list = new ArrayList<>();
            TbOrderItem orderItem = setOrderItem(num, item);
            list.add(orderItem);
            cart.setOrderItemList(list);
            cartList.add(cart);

        }
//        商家存在
        else {
            //差这次添加的商品,在该cart对象的购物明细中,是否存在

            TbOrderItem orderItem = findOrderItemByItemIdInItemList(cart.getOrderItemList(), item.getId());

            //不存在
            if (orderItem == null) {
                orderItem = setOrderItem(num, item);
                cart.getOrderItemList().add(orderItem);
            }
            //存在
            else {
                orderItem.setNum(orderItem.getNum() + num);

                orderItem.setTotalFee(new BigDecimal(orderItem.getPrice().doubleValue() * orderItem.getNum()));
                if (orderItem.getNum() < 1) {
                    //将购买数量少于 1 的商品从 明细列表 删除
                    cart.getOrderItemList().remove(orderItem);
                }
                if (cart.getOrderItemList().size() == 0) {
                    //当某个店家没有明细列表时 移除这个商家
                    cartList.remove(cart);
                }
            }
        }
        return cartList;
    }

    //往redis里面存储购物,明细
    @Override
    public void saveCartListToRedis(List<Cart> cartList, String userId) {
        //
        redisTemplate.boundHashOps("cartList").put(userId, cartList);

    }

    //从redis 中取出购物明细
    @Override
    public List<Cart> getCartListFromRedis(String userId) {
        List<Cart> cartList = (List<Cart>) redisTemplate.boundHashOps("cartList").get(userId);
        if (cartList == null) {
            cartList = new ArrayList<>();
        }

        return cartList;
    }


    //合并购物车列表/*
    // @param redis_cartList
    // @param cookie_cartList
    // */
    @Override
    public List<Cart> mergeCartList(List<Cart> redis_cartList, List<Cart> cookie_cartList) {
        for (Cart cart : cookie_cartList) {
            for (TbOrderItem orderItem : cart.getOrderItemList()) {
                  redis_cartList = addGoodsToCartList(redis_cartList, orderItem.getItemId(), orderItem.getNum());

            }
        }
        return redis_cartList;
    }

    /*
     * 设置购物对象明细
     * */
    private TbOrderItem setOrderItem(Integer num, TbItem item) {
        TbOrderItem orderItem = new TbOrderItem();

//        orderItem.setId(item.getId());
//        orderItem.setOrderId();
        orderItem.setItemId(item.getId());
        orderItem.setGoodsId(item.getGoodsId());
        orderItem.setTitle(item.getTitle());
        orderItem.setNum(num);
        orderItem.setPrice(item.getPrice());
        orderItem.setTotalFee(new BigDecimal(orderItem.getNum() * orderItem.getPrice().doubleValue()));
        orderItem.setPicPath(item.getImage());
        orderItem.setSellerId(item.getSellerId());
        return orderItem;
    }


    /*
     * 根据商品id 查找商品是否存在 商家的购物明细列表
     * */
    private TbOrderItem findOrderItemByItemIdInItemList(List<TbOrderItem> orderItemList, Long id) {

        for (TbOrderItem orderItem : orderItemList) {
            if (orderItem.getItemId().longValue() == id.longValue()) {
                return orderItem;
            }
        }
        return null;

    }


    /*
     * 根据商家id查询商家是否存在
     *
     *  */
    private Cart findSellerBySellerIdInCartList(List<Cart> cartList, String sellerId) {

        for (Cart cart : cartList) {
            if (cart.getSellerId().equals(sellerId)) {
                return cart;
            }
        }

        return null;

    }
}
