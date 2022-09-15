import java.util.Date;
import java.util.List;

public class TODOTask {
    private String id;
    private String title;
    private List<String> tags;
    private Date deadline;
    private TaskStatus status;

    public TODOTask(String id, String title, List<String> tags, Date deadline, TaskStatus status) {
        this.id = id;
        this.title = title;
        this.tags = tags;
        this.deadline = deadline;
        this.status = status;
    }

    @Override
    public String toString() {
        return "TODOTask{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", tags=" + tags +
                ", deadline=" + deadline +
                ", status=" + status +
                '}';
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public List<String> getTags() {
        return tags;
    }

    public Date getDeadline() {
        return deadline;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public void setDeadline(Date deadline) {
        this.deadline = deadline;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }
}
