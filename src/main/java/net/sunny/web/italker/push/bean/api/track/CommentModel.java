package net.sunny.web.italker.push.bean.api.track;

import com.google.gson.annotations.Expose;

import javax.persistence.Column;
import java.time.LocalDateTime;

/**
 * Created by sunny on 17-8-8.
 * 评论
 */
public class CommentModel {

    // 标明此条评论属于那一条动态
    @Expose
    private String trackId;

    // 发表评论的好友ID
    @Expose
    private String commenterId;

    // 评论接收者，好友和好友之间可以在自己的动态下聊天，
    // 可以为空，即只发送给动态的发表者
    @Expose
    private String receiverId;

    // 评论内容
    @Expose
    private String content;

    @Expose
    private String commentId;

    public String getTrackId() {
        return trackId;
    }

    public void setTrackId(String trackId) {
        this.trackId = trackId;
    }

    public String getCommenterId() {
        return commenterId;
    }

    public void setCommenterId(String commenterId) {
        this.commenterId = commenterId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }

    public String getCommentId() {
        return commentId;
    }

    public void setCommentId(String commentId) {
        this.commentId = commentId;
    }

    public static boolean check(CommentModel model) {
        return model.getTrackId() != null
                && model.getCommenterId() != null
                && model.getContent() != null;
    }
}
