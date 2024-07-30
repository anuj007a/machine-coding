package service;

import model.Comment;

public interface CommentService {
    public Comment createComment(String postId, String userId, String content);
}
