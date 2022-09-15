package main.java.com.todo.taskmanager;

import main.java.com.todo.taskmanager.pojo.tasks.Tasks;
import main.java.com.todo.taskmanager.service.TaskManager;
import main.java.com.todo.taskmanager.service.impl.TaskManagerImp;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Driver {

    public static void main(String[] args) {
        Map<String, Tasks> listOfTask = new HashMap<>();
        TaskManager taskManager = new TaskManagerImp();
        Tasks newTask1 = taskManager.addTask(new Tasks("Book cab", new Date()));
        //Adding task one
        listOfTask.put(newTask1.getTaskId(),newTask1);
        // Get task by id
        Tasks getTask1 =  taskManager.getTasks(newTask1.getTaskId());
        // Update task
        getTask1.setTaskName("Booking cab");
        taskManager.modifyTask(getTask1);
        getTask1 =  taskManager.getTasks(newTask1.getTaskId());

    }
}
