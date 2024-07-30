package newsFeedSorter;

import model.Post;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ScoreSorter implements NewsFeedSorter{

    @Override
    public List<Post> sortPosts(List<Post> postList) {
        postList.sort(new ScoreComparator());
        return postList;
    }

    private static class ScoreComparator implements Comparator<Post> {
        @Override
        public int compare(Post post1, Post post2) {
            int score1 = post1.getUpVotes() - post1.getDownVotes();
            int score2 = post2.getUpVotes() - post2.getDownVotes();
            return Integer.compare(score2, score1);
        }
    }

}
