package service.impl;

import model.User;
import service.FollowService;
import service.UserService;

import java.util.List;

public class FollowServiceImpl implements FollowService {

    private UserService userService;

    public FollowServiceImpl(UserService userService) {
        this.userService = userService;
    }

    public List<String> getFollowers(String userId){
        User user = userService.getUserByUserId(userId);
        return user.getFollowerUsers();
    }

    public List<String> getFollowing(String userId){
        User user = userService.getUserByUserId(userId);
        return user.getFollowingUsers();
    }

    public void addFollower(String followedByUserId, String followingToUserId){
        User followedByUser = userService.getUserByUserId(followedByUserId);
        User followingToUser = userService.getUserByUserId(followingToUserId);
        followingToUser.addFollower(followedByUserId);
        followedByUser.addFollowing(followingToUserId);
    }

}
