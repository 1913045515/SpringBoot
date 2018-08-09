package com.example.demo.receiver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * 消息队列监听器
 * @author qiang220316
 */
@Component
public class TestQueueListener {
    private static final Logger logger = LoggerFactory.getLogger(TestQueueListener.class);

    @RabbitHandler
    @RabbitListener(queues = "test-queue")
    public void process(Message message) {
        logger.info("MQ消息接收成功");
    }
}
