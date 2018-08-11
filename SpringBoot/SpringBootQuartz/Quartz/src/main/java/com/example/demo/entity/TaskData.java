package com.example.demo.entity;

import java.util.Map;

/**
 *  任务接口，接口具体实现由调用者来实现
 * @author linzhiqiang
 * @date 2018/6/5
 */
public interface TaskData {
    /**
     * 任务具体的执行方法
     */
    public void execute(Map<String, Object> param);
}
