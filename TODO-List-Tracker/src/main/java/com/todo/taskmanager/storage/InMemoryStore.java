package main.java.com.todo.taskmanager.storage;

import main.java.com.todo.taskmanager.Utils.IdGenerator;
import main.java.com.todo.taskmanager.enums.Actions;
import main.java.com.todo.taskmanager.pojo.tasks.Tasks;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class InMemoryStore implements StorageService {
    private Map<String, Tasks> taskList;
    private Map<String, Tasks> completedTaskList;

    private Map<String, Actions> activityLog;

    private static InMemoryStore instance;

    private InMemoryStore() {
        this.taskList = new HashMap<>();
        this.completedTaskList = new HashMap<>();
        this.activityLog = new HashMap<>();
    }

    public static InMemoryStore getInstance() {
        if (instance == null) {
            instance = new InMemoryStore();
        }
        return instance;
    }

    @Override
    public Tasks addTask(Tasks tasks) {
        tasks.setTaskId(new IdGenerator().getId());
        taskList.put(tasks.getTaskId(), tasks);
        activityLog.put(tasks.getTaskId(), Actions.ADD);
        return tasks;
    }

    @Override
    public Tasks getTask(String taskId){
        if(taskList.containsKey(taskId)){
            return taskList.get(taskId);
        }else{
            return null;
        }
    }

    @Override
    public boolean modifyTask(Tasks tasks){
        if(taskList.containsKey(tasks.getTaskId())){
            tasks.setUpdatedOn(new Date());
            taskList.put(tasks.getTaskId(), tasks);
            return true;
        }
        return false;
    }
}