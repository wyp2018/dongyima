package com.offcn.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.offcn.service.UserService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// @Controller
// 使用RestController可以让该类中的所有方法，都以
// json格式返回数据，即相当于：@ResponseBody
@RestController
public class HelloController {

    // 开始消费远程服务
    @Reference
    private UserService hell;

    @RequestMapping("/hi")
    public String hi() {
        return hell.toHi();
    }

}
