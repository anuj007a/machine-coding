package model;

import utils.UUIDGenerator;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Post {
    private String postId;
    private String userId;
    private String content;
    private int upVotes;
    private int downVotes;
    private List<Comment> commentList;
    private Date createdAt;

    public Post(String userId, String content) {
        this.userId = userId;
        this.content = content;
        postId = UUIDGenerator.generateUUID();
        upVotes = 0;
        downVotes = 0;
        commentList = new ArrayList<>();
        createdAt = new Date();
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

    public void addUpVote() {
        upVotes++;
    }

    public int getDownVotes() {
        return downVotes;
    }

    public void addDownVote() {
        downVotes++;
    }

    public List<Comment> getCommentList() {
        return commentList;
    }

    public void addComment(Comment comment) {
        commentList.add(comment);
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public int getCommentCount(){
        return commentList.size();
    }
}
