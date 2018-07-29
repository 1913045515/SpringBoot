package com.example.demo.controller;
import com.alibaba.dubbo.config.annotation.Reference;
import com.example.demo.service.TestService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
/**
 * 测试的控制类
 * @author linzhiqiang
 * @date 2018/7/29
 */
@RestController
@RequestMapping("test")
public class TestController {
    @Reference
    private TestService testService;
    @RequestMapping("test1")
    public String test(){
        return testService.test("test");
    }
}
