public class UserStatics {
    private int taskAdded;
    private int completed;

    public UserStatics(int taskAdded, int completed) {
        this.taskAdded = taskAdded;
        this.completed = completed;
    }

    public int getTaskAdded() {
        return taskAdded;
    }

    public int getCompleted() {
        return completed;
    }

    public void setTaskAdded(int taskAdded) {
        this.taskAdded = taskAdded;
    }

    public void setCompleted(int completed) {
        this.completed = completed;
    }

    @Override
    public String toString() {
        return "UserStatics{" +
                "taskAdded=" + taskAdded +
                ", completed=" + completed +
                '}';
    }
}