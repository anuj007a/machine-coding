package service.impl;

import model.Post;
import model.User;
import newsFeedSorter.*;
import service.FollowService;
import service.NewsFeedService;
import service.PostService;
import service.UserService;

import java.util.ArrayList;
import java.util.List;

public class NewsFeedServiceImpl implements NewsFeedService {
    private List<Post> newsFeedPostList ;
    private UserService userService;
    private PostService postService;
    private FollowService followService;

    public NewsFeedServiceImpl(UserService userService, PostService postService, FollowService followService) {
        this.userService = userService;
        this.postService = postService;
        this.followService = followService;
        newsFeedPostList = new ArrayList<>();
    }

    public List<Post> getNewsFeedByUserId(String userId, NewsFeedSorter newsFeedSorter){
        User user = userService.getUserByUserId(userId);
        List<Post> allPosts = postService.getPostList();
        List<Post> followingPosts = FollowedUsersSorter.sortPosts(allPosts, userService, userId, followService);
        List<Post> notFollowedPosts = new ArrayList<>();
        //        NewsFeedSorter scoreSorter = new ScoreSorter();
//        NewsFeedSorter commentSorter = new CommentSorter();
//        NewsFeedSorter timestampSorter = new TimestampSorter();
        //        posts=commentSorter.sortPosts(posts);
//        posts = timestampSorter.sortPosts(posts);
        List<Post> posts = newsFeedSorter.sortPosts(followingPosts);
        List<Post> resultPosts = new ArrayList<>(posts);
        for(Post post : allPosts){
            if(!followingPosts.contains(post)){
                resultPosts.add(post);
            }
        }
        return resultPosts;
        //TODO: fix this
    }
}
