package com.example.demo.send;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 消息发送者
 *
 * @Author qiang220316
 * @Description 消息发送者
 */

@Service
public class TestQueueSend {

    private static final Logger LOGGER = LoggerFactory.getLogger(TestQueueSend.class);

    @Autowired
    private AmqpTemplate rabbitTemplate;

    /**
     * @param queueName 路由key（这里设置了路由key与队列名称一样了）
     * @param object 发送对象
     * @throws Exception
     */
    public void sendQueue(String queueName, Object object) throws Exception {
        rabbitTemplate.convertAndSend("mq-exchange",queueName, object);
        LOGGER.info("队列" + queueName + ",【消息提供者】发送成功!");
    }
}
