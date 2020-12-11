package com.offcn.user.controller;

import java.util.List;

import com.offcn.util.PhoneFormatCheckUtils;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.alibaba.dubbo.config.annotation.Reference;
import com.offcn.pojo.TbUser;
import com.offcn.user.service.UserService;

import com.offcn.entity.PageResult;
import com.offcn.entity.Result;

/**
 * 用户表controller
 *
 * @author Administrator
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Reference
    private UserService userService;

    @Autowired
    private RedisTemplate redisTemplate;

    @RequestMapping("/sendCode")
    public Result sendCode(String phone) {

        try {

            if (PhoneFormatCheckUtils.isPhoneLegal(phone)) {
                userService.sendCode(phone);
                return new Result(true, "发送验证码成功");
            } else {
                return new Result(false, "请输入正确的手机号");
            }

        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "发送验证码失败");
        }
    }

    /**
     * 返回全部列表
     *
     * @return
     */
    @RequestMapping("/findAll")
    public List<TbUser> findAll() {
        return userService.findAll();
    }


    /**
     * 返回全部列表
     *
     * @return
     */
    @RequestMapping("/findPage")
    public PageResult findPage(int page, int rows) {
        return userService.findPage(page, rows);
    }

    /**
     * 增加
     *
     * @param user
     * @return
     */
    @RequestMapping("/add")
    public Result add(@RequestBody TbUser user, String code) {
        try {

            String redis_code = (String) redisTemplate.boundHashOps("codeList").get(user.getPhone());
            if (!code.equals(redis_code)) {
                return new Result(false, "验证码输入错误");
            }
            //md5加密
            user.setPassword(DigestUtils.md5Hex(user.getPassword()));


            userService.add(user);
            redisTemplate.boundHashOps("codeList").delete(user.getPhone());
            return new Result(true, "增加成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "增加失败");
        }
    }

    /**
     * 修改
     *
     * @param user
     * @return
     */
    @RequestMapping("/update")
    public Result update(@RequestBody TbUser user) {
        try {
            userService.update(user);
            return new Result(true, "修改成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "修改失败");
        }
    }

    /**
     * 获取实体
     *
     * @param id
     * @return
     */
    @RequestMapping("/findOne")
    public TbUser findOne(Long id) {
        return userService.findOne(id);
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
            userService.delete(ids);
            return new Result(true, "删除成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "删除失败");
        }
    }

    /**
     * //     * 查询+分页
     * //     *
     * //     * @param brand
     * //     * @param page
     * //     * @param rows
     * //     * @return
     * //
     */
    @RequestMapping("/search")
    public PageResult search(@RequestBody TbUser user, int page, int rows) {
        return userService.findPage(user, page, rows);
    }

}
