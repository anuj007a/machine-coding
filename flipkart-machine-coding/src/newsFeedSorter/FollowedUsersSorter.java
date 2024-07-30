package newsFeedSorter;

import model.Post;
import service.FollowService;
import service.UserService;

import java.util.ArrayList;
import java.util.List;

public class FollowedUsersSorter {

    public static List<Post> sortPosts(List<Post> postList, UserService userService, String userId, FollowService followService) {
        List<Post> resultPosts = new ArrayList<>();
        for(Post post: postList){
            String currentUserId = post.getUserId();
            List<String> followingUsers = followService.getFollowing(userId);
            if(followingUsers.contains(currentUserId)){
                resultPosts.add(post);
            }
        }
        return resultPosts;
    }
}
