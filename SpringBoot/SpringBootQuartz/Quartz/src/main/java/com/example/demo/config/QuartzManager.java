package com.example.demo.config;
import com.example.demo.entity.TaskData;
import org.quartz.*;
import org.quartz.impl.matchers.GroupMatcher;
import org.quartz.impl.triggers.SimpleTriggerImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.*;
import static org.quartz.TriggerBuilder.newTrigger;

/**
 * 定时任务配置管理中心
 *
 * @author linzhiqiang
 * @date 2018/6/5
 */
@Component
public class QuartzManager {

    @Autowired
    private Scheduler scheduler;

    /**
     * @param jobName          任务名
     * @param jobGroupName     任务组名
     * @param triggerName      触发器名
     * @param triggerGroupName 触发器组名
     * @param jobClass         任务
     * @param cron             时间设置，参考quartz说明文档
     * @param quartzTask       task参数
     * @Description: 添加一个定时任务
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public void addJob(String jobName, String jobGroupName, String triggerName, String triggerGroupName,
                       Class jobClass, String cron, TaskData quartzTask, Map<String, Object> param) {
        try {
            JobDataMap jobMap = new JobDataMap();
            jobMap.put("task", quartzTask);
            jobMap.put("param", param);
            // 任务名，任务组，任务执行类
            JobDetail jobDetail = JobBuilder.newJob(jobClass).withIdentity(jobName, jobGroupName)
                    .usingJobData(jobMap)
                    .build();
            // 触发器
            TriggerBuilder<Trigger> triggerBuilder = newTrigger();
            // 触发器名,触发器组
            triggerBuilder.withIdentity(triggerName, triggerGroupName);
            triggerBuilder.startNow();
            // 触发器时间设定
            triggerBuilder.withSchedule(CronScheduleBuilder.cronSchedule(cron));
            // 创建Trigger对象
            CronTrigger trigger = (CronTrigger) triggerBuilder.build();
            // 调度容器设置JobDetail和Trigger
            scheduler.scheduleJob(jobDetail, trigger);
            // 启动
            if (!scheduler.isShutdown()) {
                scheduler.start();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @param jobName
     * @param jobGroupName
     * @param triggerName      触发器名
     * @param triggerGroupName 触发器组名
     * @param cron             时间设置，参考quartz说明文档
     * @Description: 修改一个任务的触发时间
     */
    public void modifyJobTime(String jobName,
                              String jobGroupName, String triggerName, String triggerGroupName, String cron) {
        try {
            TriggerKey triggerKey = TriggerKey.triggerKey(triggerName, triggerGroupName);
            CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
            if (trigger == null) {
                return;
            }
            String oldTime = trigger.getCronExpression();
            if (!oldTime.equalsIgnoreCase(cron)) {
                // 触发器
                TriggerBuilder<Trigger> triggerBuilder = newTrigger();
                // 触发器名,触发器组
                triggerBuilder.withIdentity(triggerName, triggerGroupName);
                triggerBuilder.startNow();
                // 触发器时间设定
                triggerBuilder.withSchedule(CronScheduleBuilder.cronSchedule(cron));
                // 创建Trigger对象
                trigger = (CronTrigger) triggerBuilder.build();
                // 方式一 ：修改一个任务的触发时间
                scheduler.rescheduleJob(triggerKey, trigger);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @param jobName
     * @param jobGroupName
     * @param triggerName
     * @param triggerGroupName
     * @Description: 移除一个任务
     */
    public void removeJob(String jobName, String jobGroupName,
                          String triggerName, String triggerGroupName) {
        try {
            TriggerKey triggerKey = TriggerKey.triggerKey(triggerName, triggerGroupName);
            // 停止触发器
            scheduler.pauseTrigger(triggerKey);
            // 移除触发器
            scheduler.unscheduleJob(triggerKey);
            // 删除任务
            scheduler.deleteJob(JobKey.jobKey(jobName, jobGroupName));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @Description:启动所有定时任务
     */
    public void startJobs() {
        try {
            scheduler.start();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @Description:关闭所有定时任务
     */
    public void shutdownJobs() {
        try {
            if (!scheduler.isShutdown()) {
                scheduler.shutdown();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Scheduler getScheduler() {
        return scheduler;
    }

    public void setScheduler(Scheduler scheduler) {
        this.scheduler = scheduler;
    }

    /**
     * 获取正在执行的任务信息
     *
     * @return
     * @throws SchedulerException
     */
    public List<String> getStartTaskJob() {
        List<String> jobNameList = new ArrayList<>();
        try {
            for (String groupName : scheduler.getJobGroupNames()) {
                for (JobKey jobKey : scheduler.getJobKeys(GroupMatcher.jobGroupEquals(groupName))) {
                    String jobName = jobKey.getName();
                    jobNameList.add(jobName);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jobNameList;
    }

    /**
     * 在startTime时执行调试，endTime结束执行调度，重复执行repeatCount次，每隔repeatInterval秒执行一次
     *
     * @param name           Quartz SimpleTrigger 名称
     * @param startTime      调度开始时间
     * @param endTime        调度结束时间
     * @param repeatCount    重复执行次数
     * @param repeatInterval 执行时间隔间，单位：秒
     */
    public void startTaskJobByTime(String name, Date startTime, Date endTime, int repeatCount,
                                   int repeatInterval, String group, TaskData taskData, Map<String, Object> param, Class jobClass) {
        if (this.isValidExpression(startTime)) {
            if (name == null || name.trim().equals("")) {
                name = UUID.randomUUID().toString();
            }
            JobDataMap jobMap = new JobDataMap();
            jobMap.put("task", taskData);
            jobMap.put("param", param);
            // 任务名，任务组，任务执行类
            JobDetail jobDetail = JobBuilder.newJob(jobClass).withIdentity(name, group).usingJobData(jobMap).build();
            SimpleTrigger trigger = (SimpleTrigger) newTrigger().withIdentity(name, group)
                    .startAt(startTime)
                    .endAt(endTime)
                    .withSchedule(
                            SimpleScheduleBuilder.simpleSchedule()
                                    .withIntervalInSeconds(repeatInterval)
                                    .repeatForever()
                    )
                    .build();
            // 注册并进行调度
            try {
                Date ft = scheduler.scheduleJob(jobDetail, trigger);
                // 调度启动
                scheduler.start();
            } catch (SchedulerException e) {
                e.printStackTrace();
            }
        }
    }

    private boolean isValidExpression(final Date startTime) {
        SimpleTriggerImpl trigger = new SimpleTriggerImpl();
        trigger.setStartTime(startTime);
        Date date = trigger.computeFirstFireTime(null);
        return date != null && date.after(new Date());
    }

    /**
     * 修改执行频率
     *
     * @param jobName 任务名称
     * @param time    每隔多少秒执行一次
     */
    public void restJob(String jobName, String groupName, Date startTime, Date endTime, long time) {
        TriggerKey triggerKey = new TriggerKey(jobName, groupName);
        SimpleTriggerImpl simpleTrigger = null;
        try {
            simpleTrigger = (SimpleTriggerImpl) scheduler.getTrigger(triggerKey);
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
        if (startTime != null) {
            simpleTrigger.setStartTime(startTime);
        }
        if (endTime != null) {
            simpleTrigger.setEndTime(endTime);
        }
        //将秒转为毫秒
        simpleTrigger.setRepeatInterval(time * 1000);
        try {
            scheduler.rescheduleJob(triggerKey, simpleTrigger);
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }
}
