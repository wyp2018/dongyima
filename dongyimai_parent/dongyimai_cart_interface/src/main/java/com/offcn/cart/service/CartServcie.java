package com.offcn.cart.service;

import com.offcn.entity.Cart;

import java.util.List;

public interface CartServcie {

    /*
    * 添加商品至购物车
    * cartList 老的购物车列表
    * skuId 要添加的商品Id
    * num  要添加的商品数量
    *
    * */
    public List<Cart> addGoodsToCartList(List<Cart> cartList, Long skuId,Integer num);

    public void saveCartListToRedis(List<Cart> cartList,String userId);

    public List<Cart> getCartListFromRedis(String userId);

    //合并购物车
    public List<Cart> mergeCartList(List<Cart> redis_cartList,List<Cart> cookie_cartList);


}
