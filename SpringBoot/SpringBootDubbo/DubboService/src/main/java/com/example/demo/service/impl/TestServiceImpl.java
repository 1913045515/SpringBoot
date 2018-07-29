package com.example.demo.service.impl;
import com.alibaba.dubbo.config.annotation.Service;
import com.example.demo.service.TestService;

/**
 * 接口提供者的实现类
 * @author linzhiqiang
 * @date 2018/7/29
 */
@Service
public class TestServiceImpl implements TestService{
    @Override
    public String test(String str) {
        System.out.println("客户端传过来的值是:"+str);
        return "服务端已经接收到值,接收的值为:"+str;
    }
}
