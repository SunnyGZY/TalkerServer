package net.sunny.web.italker.push.bean.card.track;

import com.google.gson.annotations.Expose;
import net.sunny.web.italker.push.bean.db.User;
import net.sunny.web.italker.push.bean.db.track.Comment;
import net.sunny.web.italker.push.bean.db.track.Track;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * Created by sunny on 17-8-8.
 * 评论
 */
public class CommentCard {

    @Expose
    private String id; // 主键

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

    // 发表此条评论的时间
    @Expose
    private LocalDateTime date;

    @Expose
    private String commentId;

    @Expose
    private String commenterName;

    @Expose
    private String receiverName;

    @Expose
    private String portrait;

    @Expose
    private String time;

    public CommentCard(){
    }

    public CommentCard(Comment comment) {
        this.id = comment.getId();
        this.trackId = comment.getTrackId();
        this.commenterId = comment.getId();
        this.receiverId = comment.getReceiverId();
        this.content = comment.getContent();
        this.date = comment.getCreateAt();
        this.commentId = comment.getCommentId();
    }

    public String getTrackId() {
        return trackId;
    }

    public void setTrackId(String trackId) {
        this.trackId = trackId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
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

    public String getCommenterName() {
        return commenterName;
    }

    public void setCommenterName(String commenterName) {
        this.commenterName = commenterName;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public String getPortrait() {
        return portrait;
    }

    public void setPortrait(String portrait) {
        this.portrait = portrait;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
