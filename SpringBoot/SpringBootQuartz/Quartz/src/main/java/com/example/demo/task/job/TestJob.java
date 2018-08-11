package com.example.demo.task.job;

import com.example.demo.entity.TaskData;
import com.example.demo.task.QuartzTask;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 *
 * @author qiang220316
 * @Description 定时任务
 * @date 2018/8/11
 */

@Component
public class TestJob implements TaskData{

    @Override
    public void execute(Map<String, Object> param) {
        if(param != null) {
            String actId = (String) param.get("testId");
            System.out.println("TestJob定时任务执行了,参数是："+actId);
        }else {
            System.out.println("TestJob定时任务执行了,没有参数");
        }
    }
}
