package com.offcn.cart.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.offcn.cart.service.CartServcie;
import com.offcn.entity.Cart;
import com.offcn.entity.Result;
import com.offcn.util.CookieUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequestMapping("/cart")
public class CartController {


    @Reference
    private CartServcie cartServcie;

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private HttpServletResponse response;

    /*
     * 获取购物车列表
     * */
    @RequestMapping("/findCartList")
    public List<Cart> findCartList() {
        //判断有没有登录
        String loginName = SecurityContextHolder.getContext().getAuthentication().getName();

        System.out.println(loginName);
        String cartListStr = CookieUtil.getCookieValue(request, "cartList", "UTF-8");

        //对空值处理
        if (cartListStr == null || cartListStr.equals("")) {
            cartListStr = "[]";
        }

        List<Cart> cookie_cartList = JSON.parseArray(cartListStr, Cart.class);
        //未登录
        if ("anonymousUser".equals(loginName)) {

            return cookie_cartList;
        }
        //已登录
        else {

            List<Cart> redis_cartList = cartServcie.getCartListFromRedis(loginName);

            //将cookie 的购物车列表 同步到redis 中
            if(cookie_cartList.size()>0){
               redis_cartList =  cartServcie.mergeCartList(redis_cartList,cookie_cartList);

                cartServcie.saveCartListToRedis(redis_cartList,loginName);

                //删除本地cookie
                CookieUtil.deleteCookie(request,response,"cartList");

                System.out.println("he bing cartList over...");
            }
            return redis_cartList;
        }

    }

    @RequestMapping("/addToCart")
    public Result addToCart(Long skuId, Integer num) {
        try {

            response.setHeader("Access-Control-Allow-Origin", "http://localhost:9009");
            response.setHeader("Access-Control-Allow-Credentials", "true");


            //判断有没有登录
            String loginName = SecurityContextHolder.getContext().getAuthentication().getName();

            System.out.println("Login Name is :" + loginName);

            //已经存在的老购物车在哪
            //2种购物车
            List<Cart> oldList = findCartList();

            List<Cart> newList = cartServcie.addGoodsToCartList(oldList, skuId, num);

            if ("anonymousUser".equals(loginName)) {
                //存到cookie 里面
                CookieUtil.setCookie(request, response, "cartList", JSON.toJSONString(newList), 31 * 24 * 3600, "utf-8");

            } else {
                cartServcie.saveCartListToRedis(newList, loginName);
            }
//            cartServcie.saveCartListToRedis(newList,);
            return new Result(true, "添加购物车成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, e.getMessage());
        }

    }


}
