package newsFeedSorter;

import model.Post;

import java.util.List;

public interface NewsFeedSorter {
    public List<Post> sortPosts(List<Post> postList);
}
