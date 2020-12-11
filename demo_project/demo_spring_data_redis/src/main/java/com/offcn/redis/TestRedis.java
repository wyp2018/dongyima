package com.offcn.redis;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;
import java.util.Set;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring-redis.xml")
public class TestRedis {


    @Autowired
    private RedisTemplate redisTemplate;

    @Test
    public void testAdd(){

//        redisTemplate.boundValueOps("username").set("沙雕");
        String username = (String) redisTemplate.boundValueOps("username").get();

        System.out.println(username);


    }

    @Test
    public void testList(){
//        redisTemplate.boundListOps("lovers").leftPush("小花1");
//        redisTemplate.boundListOps("lovers").leftPush("小花2");
//        redisTemplate.boundListOps("lovers").leftPush("小花3");
//        redisTemplate.boundListOps("lovers").leftPush("小花4");
//        redisTemplate.boundListOps("lovers").leftPush("小花5");
        List lovers = redisTemplate.boundListOps("lovers").range(0, 1);
        System.out.println(lovers);

    }

    @Test
    public void testHash(){


//        redisTemplate.boundHashOps("namehash").put("key","value");
//        redisTemplate.boundHashOps("namehash").put("key","老王");
//        redisTemplate.boundHashOps("namehash").put("key","沙雕");
//        redisTemplate.boundHashOps("namehash").put("key","阿伟");

        Set b = redisTemplate.boundHashOps("namehash").keys();//取出所有的键值
        List a = redisTemplate.boundHashOps("namehash").values();//取出所有的值
        Object o = redisTemplate.boundHashOps("namehash").get("key");//根据小健取值

        System.out.println(a + "------" + b + "++++++" + o);
    }




}
