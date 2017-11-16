package net.sunny.web.italker.push.bean.db.track;

import net.sunny.web.italker.push.bean.api.track.CommentModel;
import net.sunny.web.italker.push.bean.card.track.CommentCard;
import net.sunny.web.italker.push.bean.db.User;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * Created by sunny on 17-8-8.
 * 好友动态评论
 */
@Entity
@Table(name = "TB_COMMENT")
public class Comment {

//    private String commenterName;
//    private String receiverName;
//    private String portrait;

    @Id
    @PrimaryKeyJoinColumn
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @Column(updatable = false, nullable = false)
    private String id; // 主键

    // 标明此条评论属于那一条动态
    @JoinColumn(name = "trackId")
    @ManyToOne(optional = false)
    private Track track;
    @Column(nullable = false, updatable = false, insertable = false)
    private String trackId;

    // 发表评论的好友ID
    @JoinColumn(name = "commenterId")
    @ManyToOne(optional = false)
    private User commenter;
    @Column(nullable = false, updatable = false, insertable = false)
    private String commenterId;

    // 评论接收者，好友和好友之间可以在自己的动态下聊天，
    // 可以为空，即只发送给动态的发表者
    @JoinColumn(name = "receiverId")
    @ManyToOne
    private User receiver;
    @Column(updatable = false, insertable = false)
    private String receiverId;

    @Column
    private String commentId; // 回复评论的ID

    // 评论内容
    @Column
    private String content;

    // 发表此条评论的时间
    @CreationTimestamp
    @Column(nullable = false)
    private LocalDateTime createAt = LocalDateTime.now();

    public Comment() {
    }

    public Comment(CommentModel model, User commenter, Track track, User receiver) {
        this.track = track;
        this.trackId = model.getTrackId();
        this.commenter = commenter;
        this.commenterId = model.getCommenterId();
        this.receiver = receiver;
        this.receiverId = model.getReceiverId();
        this.content = model.getContent();
        this.commentId = model.getCommentId();
    }

    public Comment(CommentModel model, User commenter, Track track) {
        this.track = track;
        this.trackId = model.getTrackId();
        this.commenter = commenter;
        this.commenterId = model.getCommenterId();
        this.receiverId = model.getReceiverId();
        this.content = model.getContent();
    }

    public Track getTrack() {
        return track;
    }

    public void setTrack(Track track) {
        this.track = track;
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

    public User getCommenter() {
        return commenter;
    }

    public void setCommenter(User commenter) {
        this.commenter = commenter;
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

    public User getReceiver() {
        return receiver;
    }

    public void setReceiver(User receiver) {
        this.receiver = receiver;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }

    public LocalDateTime getCreateAt() {
        return createAt;
    }

    public String getCommentId() {
        return commentId;
    }

    public void setCommentId(String commentId) {
        this.commentId = commentId;
    }

    public void setCreateAt(LocalDateTime createAt) {
        this.createAt = createAt;
    }


    @Override
    public String toString() {
        return "Comment{" +
                "id='" + id + '\'' +
                ", track=" + track +
                ", trackId='" + trackId + '\'' +
                ", commenter=" + commenter +
                ", commenterId='" + commenterId + '\'' +
                ", receiver=" + receiver +
                ", receiverId='" + receiverId + '\'' +
                ", content='" + content + '\'' +
                ", createAt=" + createAt +
                '}';
    }

//    public CommentCard build(User commenter) {
//        CommentCard commentCard = new CommentCard();
//        commentCard.setId(id);
//        commentCard.setTrackId(trackId);
//        commentCard.setCommenterId(commenterId);
//        commentCard.setReceiverId(receiverId);
//        commentCard.setContent(content);
//        commentCard.setDate(createAt);
//        commentCard.setCommentId(commentId);
//        commentCard.setPortrait(commenter.getPortrait());
//        commentCard.setCommenterName(commenter.getName());
//        return commentCard;
//    }

    public CommentCard build() {
        CommentCard commentCard = new CommentCard();
        commentCard.setId(id);
        commentCard.setTrackId(trackId);
        commentCard.setCommenterId(commenterId);
        commentCard.setReceiverId(receiverId);
        commentCard.setContent(content);
        commentCard.setDate(createAt);
        commentCard.setCommentId(commentId);
        commentCard.setPortrait(commenter.getPortrait());
        commentCard.setCommenterName(commenter.getName());

        if (receiver != null)
            commentCard.setReceiverName(receiver.getName());

        return commentCard;
    }
}


















