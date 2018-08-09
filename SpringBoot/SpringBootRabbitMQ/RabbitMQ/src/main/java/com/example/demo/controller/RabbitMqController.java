package com.example.demo.controller;

import com.example.demo.send.TestQueueSend;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author qiang220316
 * @date 2018/8/9
 */

@RestController
public class RabbitMqController {

    @Autowired
    private TestQueueSend testQueueSend;

    @RequestMapping("rabbit")
    public String sendMQ(){
        try {
            testQueueSend.sendQueue("test-queue","我是消息");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "控制类开始发送消息";
    }
}
