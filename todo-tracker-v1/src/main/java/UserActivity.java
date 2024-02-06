import java.util.Date;

public class UserActivity {
    private String activity;
    private Date activityTime;

    public UserActivity(String activity, Date activityTime) {
        this.activity = activity;
        this.activityTime = activityTime;
    }

    public String getActivity() {
        return activity;
    }

    public Date getActivityTime() {
        return activityTime;
    }

    @Override
    public String toString() {
        return "UserActivity{" +
                "activity='" + activity + '\'' +
                ", activityTime=" + activityTime +
                '}';
    }
}
