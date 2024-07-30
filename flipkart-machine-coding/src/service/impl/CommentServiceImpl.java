package service.impl;

import model.Comment;
import service.CommentService;

import java.util.ArrayList;
import java.util.List;

public class CommentServiceImpl implements CommentService {

    private List<Comment> commentList;

    public CommentServiceImpl() {
        commentList = new ArrayList<>();
    }

    public Comment createComment(String postId, String userId, String content){
        Comment comment = new Comment(postId, userId, content);
        commentList.add(comment);
        return comment;
    }

}
