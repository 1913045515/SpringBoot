package com.example.demo.service.impl;
import com.example.demo.utils.Constants;
import com.example.demo.entity.TaskConfigVO;
import com.example.demo.entity.TaskData;
import com.example.demo.service.QuartzService;
import com.example.demo.service.TaskConfigService;
import com.example.demo.utils.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author linzhiqiang
 * @date 2018/6/6
 */
@Service
public class TaskConfigServiceImpl implements TaskConfigService, ApplicationListener<ContextRefreshedEvent> {
    @Resource
    private QuartzService quartzService;

    @Autowired
    private ApplicationContext ctx;

    /**
     * 定时任务执行service
     *
     * @return
     * @throws Exception
     */
    @Override
    public void startQuartzTask(String jobName, Map<String, Object> param) {
        TaskConfigVO taskConfigVO = new TaskConfigVO();
        taskConfigVO.setName(jobName);
        try {
            List<String> startTaskJob = quartzService.getStartTaskJob();
            //判断定时任务是否已经启动
            if (!startTaskJob.contains(jobName)) {
                // 默认的cron表达式，如果数据库没有对应数据的话
                String cronStr = Constants.DEFAULT_CRON_VALUE;
                if (param != null && param.get("cronValue") != null) {
                    //如果这个有值就取这个（页面传过来的）
                    cronStr = param.get("cronValue").toString();
                }
                Class<?> taskDataClz = Class.forName("com.example.demo.task.job.TestJob");
                TaskData task = (TaskData) ctx.getBean(taskDataClz);
                quartzService.startTask(jobName, cronStr, task, param);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 修改定时任务，同时更新定时任务配置信息
     * @param taskConfigVO
     */
    @Override
    public void updateQuartzTask(TaskConfigVO taskConfigVO) {
        //如果定时任务时启动的，修改定时任务表达式
        quartzService.updateTaskTime(taskConfigVO.getName(), taskConfigVO.getCronValue());
    }

    /**
     * 删除定时任务
     * @param name 定时任务name
     */
    @Override
    public void deleteQuartzTask(String name) {
        quartzService.removeTask(name);
    }

    /**
     * 查询正在执行的定时任务信息
     *
     * @return
     */
    @Override
    public List<String> getStartTaskJob() {
        return quartzService.getStartTaskJob();
    }

    /**
     * 通过开始时间和结束时间开启定时任务
     * @param jobName  定时任务名称
     * @param beginDate  定时任务开始时间
     * @param endDate  定时任务结束时间
     * @param repeatInterval  隔多少秒执行一次
     * @param map
     */
    @Override
    public void startTaskJobByTime(String jobName, Date beginDate, Date endDate, int repeatInterval, Map<String, Object> map) {
        TaskConfigVO taskConfigVO = new TaskConfigVO();
        taskConfigVO.setName(jobName);
        Class<?> taskDataClz = null;
        try {
            List<String> startTaskJob = quartzService.getStartTaskJob();
            //判断定时任务是否已经启动
            if (!startTaskJob.contains(jobName)) {
                taskDataClz = Class.forName("com.example.demo.task.job.TestJob");
                TaskData task = (TaskData) ctx.getBean(taskDataClz);
                //将结束时间延迟一个小时
                long time = endDate.getTime() + 1000 * 60 * 60;
                quartzService.startTaskJobByTime(jobName, beginDate, DateUtil.getDateByLong(time), repeatInterval, jobName, task, map);
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * 修改执行频率
     * @param jobName  任务名称
     * @param startTime  开始时间
     * @param endTime  结束时间
     * @param time  每隔多少秒执行一次
     */
    @Override
    public void updateRepeatInterval(String jobName, Date startTime, Date endTime, long time) {
        long tmpEndTime = endTime.getTime() + 1000 * 60 * 60;
        quartzService.updateRepeatInterval(jobName, jobName, startTime, DateUtil.getDateByLong(tmpEndTime), time);
    }

    /**
     * 初始化任务
     * @param contextRefreshedEvent
     */
    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        System.out.println("我是容器一启动就执行的。。。。。");
        if (contextRefreshedEvent.getApplicationContext().getParent() == null) {
            Class<?> taskDataClz = null;
            try {
                taskDataClz = Class.forName("com.example.demo.task.job.TestJob");
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
//            TaskData task = (TaskData) ctx.getBean(taskDataClz);
//            quartzService.startTask("testTask", "0/4 * * * * ? ", task, null);
        }
    }
}
