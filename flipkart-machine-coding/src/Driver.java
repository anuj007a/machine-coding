import model.Post;
import model.User;
import newsFeedSorter.NewsFeedSorter;
import newsFeedSorter.ScoreSorter;
import service.*;
import service.impl.*;

import java.util.List;

public class Driver {
    public static void main(String[] args) throws Exception {
        SessionService sessionService = new SessionServiceImpl();
        UserService userService = new UserServiceImpl();
        CommentService commentService = new CommentServiceImpl();
        PostService postService = new PostServiceImpl(commentService, userService);
        FollowService followService = new FollowServiceImpl(userService);
        NewsFeedService newsFeedService = new NewsFeedServiceImpl(userService, postService, followService);

        User user1 = userService.signupUser("user1");
        User user2 = userService.signupUser("user2");
        User user3 = userService.signupUser("user3");
        sessionService.login(user1.getUserId());

        postService.createPost("I am going to be the darkest dark wizard of all time", user1.getUserId());

        postService.createPost("I am lord Voldemort btw", user1.getUserId());

        NewsFeedSorter scoreSorter = new ScoreSorter();

        List<Post> posts = newsFeedService.getNewsFeedByUserId(user1.getUserId(), scoreSorter);



        System.out.println("Hello world!");
    }
}