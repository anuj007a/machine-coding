import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Driver {

    public static void main(String[] args) throws InterruptedException {
        TODOApplication app = new TODOApplication();
        app.addTask("anuj", app.generateNewTask("task1", new ArrayList<String>(){{add("tag1");}}, new Date()));
        Thread.sleep(1000);
        app.addTask("anuj", app.generateNewTask("task2", new ArrayList<String>(){{add("tag2");}}, new Date()));
        Thread.sleep(1000);
        app.addTask("anuj", app.generateNewTask("task3", new ArrayList<String>(){{add("tag3");}}, new Date()));

        List<TODOTask> anujTasks = app.listTask("anuj", null, null);
        System.out.println("All Tasks" + anujTasks);

        TODOTask anujTask1 = app.getTask("anuj", anujTasks.get(0).getId());
        System.out.println(anujTask1);

        anujTask1.setTitle("task1 modified");
        app.modifyTask("anuj", anujTask1);

        anujTasks = app.listTask("anuj", null, null);
        System.out.println("All Tasks" + anujTasks);

        anujTask1.setStatus(TaskStatus.COMPLETED);
        app.modifyTask("anuj", anujTask1);

        UserStatics statics = app.getStatics("anuj");
        System.out.println(statics);

        List<UserActivity> anujActivityLogs = app.getActivityLogs("anuj");
        System.out.println(anujActivityLogs);

        anujTasks = app.listTask("anuj", new TaskFilter(null, "tag2"), null);
        System.out.println("Filtered Tasks: " + anujTasks);

        anujTasks = app.listTask("anuj", null, TaskOrder.OLD_TO_NEW);
        System.out.println("Soreted Tasks: " + anujTasks);

        anujTasks = app.listTask("anuj", null, TaskOrder.NEW_TO_OLD);
        System.out.println("Soreted Tasks: " + anujTasks);

    }

}
