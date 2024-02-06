package main.java.com.todo.taskmanager.storage;

import main.java.com.todo.taskmanager.pojo.tasks.Tasks;

public interface StorageService {

    public Tasks addTask(Tasks tasks);

    public Tasks getTask(String taskId);

    public boolean modifyTask(Tasks tasks);



}
