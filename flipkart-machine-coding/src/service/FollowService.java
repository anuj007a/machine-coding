package service;

import java.util.List;

public interface FollowService {
    public List<String> getFollowers(String userId);
    public List<String> getFollowing(String userId);
    public void addFollower(String followedByUserId, String followingToUserId);
}
