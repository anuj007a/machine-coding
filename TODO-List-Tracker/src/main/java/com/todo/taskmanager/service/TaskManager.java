package main.java.com.todo.taskmanager.service;

import main.java.com.todo.taskmanager.filters.Filters;
import main.java.com.todo.taskmanager.pojo.tasks.Tasks;

import java.util.List;

public interface TaskManager {

    public Tasks addTask(Tasks tasks);
    public Tasks getTasks(String taskId);
    public boolean modifyTask(Tasks tasks);
    public Tasks removeTask(int taskId);
    public List<Tasks> listTasks(Filters filters);

    public List<Tasks> getStatistics(Filters filters);
    public List<Tasks> getActivityLog(Filters filters);
}
