package main.java.com.todo.taskmanager.pojo.tasks;

import main.java.com.todo.taskmanager.Utils.IdGenerator;
import main.java.com.todo.taskmanager.enums.TaskStatus;

import java.util.Date;

public class Tasks {

    public Tasks(){}

    private String taskId;

    private String taskName;
    private TaskStatus status;
    private Date createdOn;
    private Date updatedOn;

    private Date deadLine;

    public String getTaskId() {
        return taskId;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public Date getDeadLine() {
        return deadLine;
    }

    public void setDeadLine(Date deadLine) {
        this.deadLine = deadLine;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

    public Date getUpdatedOn() {
        return updatedOn;
    }

    public void setUpdatedOn(Date updatedOn) {
        this.updatedOn = updatedOn;
    }

    public Tasks(String taskName, Date deadLine){
        this.setTaskName( taskName);
        this.setCreatedOn(new Date());
        this.setDeadLine(deadLine);
        this.setStatus(TaskStatus.NEW);
    }
}
