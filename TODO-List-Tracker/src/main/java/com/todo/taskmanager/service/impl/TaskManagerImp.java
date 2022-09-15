package main.java.com.todo.taskmanager.service.impl;

import main.java.com.todo.taskmanager.filters.Filters;
import main.java.com.todo.taskmanager.service.TaskManager;
import main.java.com.todo.taskmanager.pojo.tasks.Tasks;
import main.java.com.todo.taskmanager.storage.InMemoryStore;
import main.java.com.todo.taskmanager.storage.StorageService;

import java.util.ArrayList;
import java.util.List;

public class TaskManagerImp implements TaskManager {

    private StorageService storageService;

    public TaskManagerImp(){
        this.storageService= InMemoryStore.getInstance();
    }

    @Override
    public Tasks addTask(Tasks task) {
        return storageService.addTask(task);
    }

    @Override
    public Tasks getTasks(String taskId) {
        return storageService.getTask(taskId);
    }

    @Override
    public boolean modifyTask(Tasks tasks) {
        return storageService.modifyTask(tasks);
    }

    @Override

    public Tasks removeTask(int taskId) {
        return new Tasks();
    }

    @Override
    public List<Tasks> listTasks(Filters filters) {
        List<Tasks> tasks = new ArrayList<>();
        return tasks;
    }

    @Override
    public List<Tasks> getStatistics(Filters filters) {
        List<Tasks> tasks = new ArrayList<>();
        return tasks;
    }

    @Override
    public List<Tasks> getActivityLog(Filters filters) {
        List<Tasks> tasks = new ArrayList<>();
        return tasks;
    }

}
