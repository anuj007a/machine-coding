package main.java.com.todo.taskmanager.pojo.statistics;

import main.java.com.todo.taskmanager.enums.Actions;

import java.util.Date;

public class Statistics {
    private Integer taskId;
    private Actions actions;
    private Date createdTime;

    public Integer getTaskId() {
        return taskId;
    }

    public void setTaskId(Integer taskId) {
        this.taskId = taskId;
    }

    public Actions getActions() {
        return actions;
    }

    public void setActions(Actions actions) {
        this.actions = actions;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }
}
