package com.example.demo.service;
import com.example.demo.entity.TaskConfigVO;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 定时任务调用接口
 * @author linzhiqiang
 * @date 2018/6/6
 */
public interface TaskConfigService {
    /**
     * 查后定时任务配置信息
     *
     * @return
     * @throws Exception
     */
    void startQuartzTask(String jobName, Map<String, Object> param);

    /**
     * 修改定时任务配置信息
     * @param taskConfigVO
     */
    void updateQuartzTask(TaskConfigVO taskConfigVO);

    /**
     * 删除定时任务
     * @param name
     */
    void deleteQuartzTask(String name);


    /**
     * 查询正在执行的定时任务信息
     * @return
     */
    List<String> getStartTaskJob();

    /**
     * 通过开始时间和结束时间开启定时任务
     * @param jobName  定时任务名称
     * @param beginDate  定时任务开始时间
     * @param endDate  定时任务结束时间
     * @param repeatInterval  隔多少秒执行一次
     */
    void startTaskJobByTime(String jobName, Date beginDate, Date endDate, int repeatInterval, Map<String, Object> map);

    /**
     * 修改执行频率
     * @param jobName  任务名称
     * @param startTime  开始时间
     * @param endTime  结束时间
     * @param time  每隔多少秒执行一次
     */
    void updateRepeatInterval(String jobName, Date startTime, Date endTime, long time);
}
