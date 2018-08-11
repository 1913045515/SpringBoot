package com.example.demo.controller;

import com.example.demo.entity.TaskConfigVO;
import com.example.demo.service.TaskConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author qiang220316
 * @date 2018/8/9
 */

@RestController
public class QuartzController {
    @Autowired
    private TaskConfigService taskConfigService;

    /**
     * 查后定时任务配置信息
     *
     * @return
     * @throws Exception
     */
    @RequestMapping("startQuartzTask")
    public void startQuartzTask(String jobName, String cronValue, String paramId) {
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("cronValue", cronValue);
        param.put("testId", paramId);
        taskConfigService.startQuartzTask(jobName, param);
    }

    /**
     * 修改定时任务配置信息
     *
     * @param jobName
     * @param jobName
     */
    @RequestMapping("updateQuartzTask")
    public void updateQuartzTask(String jobName, String cronValue) {
        TaskConfigVO taskConfigVO = new TaskConfigVO();
        taskConfigVO.setName(jobName);
        taskConfigVO.setCronValue(cronValue);
        taskConfigService.updateQuartzTask(taskConfigVO);
    }

    /**
     * 删除定时任务
     *
     * @param name
     */
    @RequestMapping("deleteQuartzTask")
    public void deleteQuartzTask(String name) {
        taskConfigService.deleteQuartzTask(name);
    }


    /**
     * 查询正在执行的定时任务信息
     *
     * @return
     */
    @RequestMapping("getStartTaskJob")
    public List<String> getStartTaskJob() {
        return taskConfigService.getStartTaskJob();
    }

    /**
     * 通过开始时间和结束时间开启定时任务
     *
     * @param jobName        定时任务名称
     * @param beginDate      定时任务开始时间
     * @param endDate        定时任务结束时间
     * @param repeatInterval 隔多少秒执行一次
     */
    @RequestMapping("startTaskJobByTime")
    public void startTaskJobByTime(String jobName, Date beginDate, Date endDate, int repeatInterval, String paramId) {
        Map<String, Object> map = new HashMap<>();
        map.put("testId", paramId);
        taskConfigService.startTaskJobByTime(jobName, beginDate, endDate, repeatInterval, map);
    }

    /**
     * 修改执行频率
     *
     * @param jobName   任务名称
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @param time      每隔多少秒执行一次
     */
    @RequestMapping("updateRepeatInterval")
    public void updateRepeatInterval(String jobName, Date startTime, Date endTime, long time) {
        taskConfigService.updateRepeatInterval(jobName, startTime, endTime, time);
    }
}
