package com.offcn.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.offcn.service.UserService;

@Service
public class UserServiceImpl implements UserService {

    @Override
    public String toHi() {
        return "种";
    }


}
