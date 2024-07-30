package service;

import model.Post;

import java.util.List;

public interface PostService {
    public List<Post> getPostList();
    public Post getPostByPostId(String postId);
    public Post createPost(String content, String userId);
    public void addCommentToPost(String content, String postId, String userId);
    public void upVotePost(String postId);
    public void downVotePost(String postId);
}
