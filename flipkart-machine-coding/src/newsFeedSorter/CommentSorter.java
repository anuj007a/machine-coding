package newsFeedSorter;

import model.Post;

import java.util.Comparator;
import java.util.List;

public class CommentSorter implements NewsFeedSorter{
    @Override
    public List<Post> sortPosts(List<Post> postList) {
        postList.sort(Comparator.comparing(Post:: getCommentCount).reversed());
        return postList;
    }
}
