package com.offcn.entity;

import com.offcn.pojo.TbOrderItem;

import java.io.Serializable;
import java.util.List;

public class Cart implements Serializable {

    //商家ID
    private String sellerId;

    //商家名称
    private String sellerName;

    //购物车明细
    private List<TbOrderItem> orderItemList;
    //getter  and setter  ......

    public Cart() {
    }

    public Cart(String sellerId, String sellerName, List<TbOrderItem> orderItemList) {
        this.sellerId = sellerId;
        this.sellerName = sellerName;
        this.orderItemList = orderItemList;
    }

    public String getSellerId() {
        return sellerId;
    }

    public void setSellerId(String sellerId) {
        this.sellerId = sellerId;
    }

    public String getSellerName() {
        return sellerName;
    }

    public void setSellerName(String sellerName) {
        this.sellerName = sellerName;
    }

    public List<TbOrderItem> getOrderItemList() {
        return orderItemList;
    }

    public void setOrderItemList(List<TbOrderItem> orderItemList) {
        this.orderItemList = orderItemList;
    }
}
