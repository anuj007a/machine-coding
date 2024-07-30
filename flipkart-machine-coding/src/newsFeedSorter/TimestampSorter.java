package newsFeedSorter;

import model.Post;

import java.util.Comparator;
import java.util.List;

public class TimestampSorter implements NewsFeedSorter{
    @Override
    public List<Post> sortPosts(List<Post> postList) {
        postList.sort(Comparator.comparing(Post:: getCreatedAt).reversed());
        return postList;
    }
}
