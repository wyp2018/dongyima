package com.offcn.sellergoods.controller;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



@RestController
@RequestMapping("/index")
public class IndexController {

    @RequestMapping("/getName")
    public String getName(){

        //使用Spring security 的api获取用户名
        String loginName = SecurityContextHolder.getContext().getAuthentication().getName();

        return loginName ;
    }


}

