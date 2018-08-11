package com.example.demo.entity;
import java.io.Serializable;
import java.util.Date;
/**
 * 定时任务配置vo类
 *
 * @author linzhiqiang
 * @date 2018-6-6
 */
public class TaskConfigVO implements Serializable {
    private static final long serialVersionUID = 1L;

    //定时任务状态：关闭
    public static final int TASK_CLOSE = 0;
    //定时任务状态：开启
    public static final int TASK_START = 1;

    /**
     * 主键
     */
    private String id;

    /**
     * 任务名称
     */
    private String name;

    /**
     * 任务详细描述
     */
    private String description;

    /**
     * 任务创建时间
     */
    private Date createTime;

    /**
     * 任务修改时间
     */
    private Date updateTime;

    /**
     * 用户id
     */
    private String userId;

    /**
     * cron表达式
     */
    private String cronValue;

    /**
     * 对应的启动器表达式
     */
    private String expression;

    /**
     * 定时任务启动类型   0：手动启动   1：容器启动时启动
     */
    private int startType = 0;

    /**
     * 定时任务是否启动  0 ：未启动  1：已启动
     */
    private int isStart = 0;

    public String getExpression() {
        return expression;
    }

    public void setExpression(String expression) {
        this.expression = expression;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCronValue() {
        return cronValue;
    }

    @Override
    public String toString() {
        return "TaskConfigVO{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", createTime='" + createTime + '\'' +
                ", updateTime='" + updateTime + '\'' +
                ", userId='" + userId + '\'' +
                ", cronValue='" + cronValue + '\'' +
                '}';
    }

    public void setCronValue(String cronValue) {
        this.cronValue = cronValue;
    }

    public int getStartType() {
        return startType;
    }

    public void setStartType(int startType) {
        this.startType = startType;
    }

    public int getIsStart() {
        return isStart;
    }

    public void setIsStart(int isStart) {
        this.isStart = isStart;
    }
}
