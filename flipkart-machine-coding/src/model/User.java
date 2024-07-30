package model;

import utils.UUIDGenerator;

import java.util.ArrayList;
import java.util.List;

public class User {
    private String userId;
    private String name;
    List<String> followerUsers;
    List<String> followingUsers;
    List<Post> posts;

    public User(String name) {
        this.name = name;
        userId = UUIDGenerator.generateUUID();
        followerUsers = new ArrayList<>();
        followingUsers = new ArrayList<>();
        posts = new ArrayList<>();
    }

    public String getUserId() {
        return userId;
    }

    public void addFollower(String userId){
        followerUsers.add(userId);
    }

    public void addFollowing(String userId){
        followingUsers.add(userId);
    }

    public String getName() {
        return name;
    }

    public List<String> getFollowerUsers() {
        return followerUsers;
    }

    public List<String> getFollowingUsers() {
        return followingUsers;
    }

    public List<Post> getPosts() {
        return posts;
    }
}
