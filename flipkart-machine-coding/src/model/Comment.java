package model;

import utils.UUIDGenerator;

public class Comment {
    private String commentId;
    private String postId;
    private String userId;
    private String content;
    private int upVotes;
    private int downVotes;

    public Comment(String postId, String userId, String content) {
        this.postId = postId;
        this.userId = userId;
        this.content = content;
        commentId = UUIDGenerator.generateUUID();
        upVotes = 0;
        downVotes = 0;
    }

    public String getCommentId() {
        return commentId;
    }

    public void setCommentId(String commentId) {
        this.commentId = commentId;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getUpVotes() {
        return upVotes;
    }

    public void setUpVotes(int upVotes) {
        this.upVotes = upVotes;
    }

    public int getDownVotes() {
        return downVotes;
    }

    public void setDownVotes(int downVotes) {
        this.downVotes = downVotes;
    }
}
