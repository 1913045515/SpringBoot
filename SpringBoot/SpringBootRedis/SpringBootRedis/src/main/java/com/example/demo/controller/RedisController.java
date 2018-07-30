package com.example.demo.controller;
import com.example.demo.util.RedisClusterUtil;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;


/**
 * 测试的控制类
 * @author linzhiqiang
 * @date 2018/7/29
 */
@RestController
@RequestMapping("redis")
public class RedisController {
    @Resource
    private RedisClusterUtil redisUtil;

    @RequestMapping("setKey")
    public boolean setKey(){
        return redisUtil.setObject("test","I am Test");
    }

    @RequestMapping("getKey")
    public Object getKey(){
        return redisUtil.getObject("test");
    }
}
