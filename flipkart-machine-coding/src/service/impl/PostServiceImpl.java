package service.impl;

import model.Comment;
import model.Post;
import model.User;
import service.CommentService;
import service.PostService;
import service.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PostServiceImpl implements PostService {
    private List<Post> postList;
    private CommentService commentService;
    private UserService userService;

    public PostServiceImpl(CommentService commentService, UserService userService) {
        this.commentService = commentService;
        postList = new ArrayList<>();
        this.userService = userService;
    }

    public List<Post> getPostList() {
        return postList;
    }

    public Post getPostByPostId(String postId){
        for(Post post : postList){
            if(Objects.equals(post.getPostId(), postId)){
                return post;
            }
        }
        return null;
    }

    public Post createPost(String content, String userId){
        Post post = new Post(userId, content);
        postList.add(post);
        return post;
    }

    public void addCommentToPost(String content, String postId, String userId){
        Post post = getPostByPostId(postId);
        User user = userService.getUserByUserId(userId);
        Comment comment = commentService.createComment(postId,userId,content );
        post.addComment(comment);
    }

    public void upVotePost(String postId){
        Post post = getPostByPostId(postId);
        post.addUpVote();
    }

    public void downVotePost(String postId){
        Post post = getPostByPostId(postId);
        post.addDownVote();
    }

}
