import java.util.*;
import java.util.stream.Collectors;

public class TODOApplication {

    private Map<String, Map<String, TODOTask>> userToTasks;
    private Map<String, List<UserActivity>> userToActivityLogs;
    private Map<String, UserStatics> userToStatics;
    private int nextTaskId = 0;

    public TODOApplication() {
        this.userToTasks = new HashMap<String, Map<String, TODOTask>>();
        this.userToActivityLogs = new HashMap<String, List<UserActivity>>();
        this.userToStatics = new HashMap<String, UserStatics>();
    }

    private void removeTaskIfCompleted(String userId, TODOTask task) {
        if (task.getStatus().equals(TaskStatus.COMPLETED)) {
            removeTask(userId, task);
            userToStatics.get(userId).setCompleted(userToStatics.get(userId).getCompleted() + 1);
        }
    }

    public void addTask(String userId, TODOTask task) {
        if (!userToTasks.containsKey(userId)) {
            //new user
            userToTasks.put(userId, new HashMap<String, TODOTask>());
            userToActivityLogs.put(userId, new ArrayList<UserActivity>());
            userToStatics.put(userId, new UserStatics(0, 0));
        }
        userToTasks.get(userId).put(task.getId(), task);
        userToActivityLogs.get(userId).add(new UserActivity("Task Added: " + task.toString(), new Date()));
        userToStatics.get(userId).setTaskAdded(userToStatics.get(userId).getTaskAdded() + 1);
        removeTaskIfCompleted(userId, task);
    }

    public TODOTask getTask(String userId, String taskId) {
        if (userToTasks.containsKey(userId) &&
                userToTasks.get(userId).containsKey(taskId)) {
            return userToTasks.get(userId).get(taskId);
        }
        return null;
    }

    public void modifyTask(String userId, TODOTask task) {
        if (userToTasks.containsKey(userId)) {
            userToTasks.get(userId).put(task.getId(), task);
            userToActivityLogs.get(userId).add(new UserActivity("Task Modified: " + task.toString(), new Date()));
            removeTaskIfCompleted(userId, task);
        }
    }

    public void removeTask(String userId, TODOTask task) {
        if (userToTasks.containsKey(userId) && userToTasks.get(userId).containsKey(task.getId())) {
            userToTasks.get(userId).remove(task.getId());
            userToActivityLogs.get(userId).add(new UserActivity("Task Removed: " + task.toString(), new Date()));
        }
    }

    public List<TODOTask> listTask(String userId, final TaskFilter filter, TaskOrder order) {
        if (userToTasks.containsKey(userId)) {
            List<TODOTask> tasks = new ArrayList<TODOTask>(userToTasks.get(userId).values());

            //filter
            if (filter != null) {
                tasks = tasks.stream()
                        .filter(task-> {
                            if (filter.getStatus() != null) {
                                return task.getStatus() == filter.getStatus();
                            }
                            return true;
                        })
                        .filter(task-> {
                            if (filter.getTag() != null) {
                                return task.getTags().contains(filter.getTag());
                            }
                            return true;
                        })
                        .collect(Collectors.toList());
            }

            //sort
            if (order != null) {
                if (order.equals(TaskOrder.OLD_TO_NEW)) {
                    tasks.sort(Comparator.comparing(TODOTask::getDeadline));
                } else if (order.equals(TaskOrder.NEW_TO_OLD)) {
                    tasks.sort((task1, task2) -> task2.getDeadline().compareTo(task1.getDeadline()));
                }
            }
            return tasks;
        }
        return null;
    }

    public UserStatics getStatics(String userId) {
        if (userToStatics.containsKey(userId)) {
            return userToStatics.get(userId);
        }
        return null;
    }

    public List<UserActivity> getActivityLogs(String userId) {
        if (userToActivityLogs.containsKey(userId)) {
            return userToActivityLogs.get(userId);
        }
        return null;
    }


    public TODOTask generateNewTask(String title, List<String> tags, Date deadline) {
        nextTaskId++;
        return new TODOTask(String.valueOf(nextTaskId), title, tags, deadline, TaskStatus.PENDING);
    }


}
