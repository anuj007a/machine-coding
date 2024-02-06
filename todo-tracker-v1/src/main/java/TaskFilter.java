public class TaskFilter {
    private TaskStatus status;
    private String tag;

    public TaskFilter(TaskStatus status, String tag) {
        this.status = status;
        this.tag = tag;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public String getTag() {
        return tag;
    }
}
