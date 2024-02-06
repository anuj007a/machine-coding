package main.java.com.todo.taskmanager.filters;

import main.java.com.todo.taskmanager.enums.TaskStatus;

import java.util.Date;

public class Filters {

    private Date startTime;
    private Date endTime;
    private Date createdTime;
    private TaskStatus status;

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }
}
