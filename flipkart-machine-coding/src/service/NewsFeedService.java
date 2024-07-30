package service;

import model.Post;
import newsFeedSorter.NewsFeedSorter;

import java.util.List;

public interface NewsFeedService {
    public List<Post> getNewsFeedByUserId(String userId, NewsFeedSorter newsFeedSorter);
}
