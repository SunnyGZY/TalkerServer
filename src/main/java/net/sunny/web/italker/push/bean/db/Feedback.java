package net.sunny.web.italker.push.bean.db;

import net.sunny.web.italker.push.bean.api.feedback.FeedbackModel;
import net.sunny.web.italker.push.bean.api.track.PhotoModel;
import net.sunny.web.italker.push.bean.api.track.TrackCreateModel;
import net.sunny.web.italker.push.bean.api.track.TrackUpdateModel;
import net.sunny.web.italker.push.bean.db.track.Photo;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by sunny on 17-8-3.
 * 好友动态
 */

@Entity
@Table(name = "TB_FEEDBACK")
public class Feedback {

    @Id
    @PrimaryKeyJoinColumn
    //Id由客户端负责生成
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @Column(updatable = false, nullable = false)
    private String id;

    @JoinColumn(name = "userId")
    @ManyToOne(optional = false)
    private User feedbacker;
    @Column(nullable = false, updatable = false, insertable = false)
    private String userId;

    //
    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    //
    @Column(nullable = false, columnDefinition = "TEXT")
    private String userPhoneNum;

    @CreationTimestamp
    @Column(nullable = false)
    private LocalDateTime createAt = LocalDateTime.now();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public User getFeedbacker() {
        return feedbacker;
    }

    public void setFeedbacker(User feedbacker) {
        this.feedbacker = feedbacker;
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

    public String getUserPhoneNum() {
        return userPhoneNum;
    }

    public void setUserPhoneNum(String userPhoneNum) {
        this.userPhoneNum = userPhoneNum;
    }

    public LocalDateTime getCreateAt() {
        return createAt;
    }

    public void setCreateAt(LocalDateTime createAt) {
        this.createAt = createAt;
    }

    public Feedback() {
    }


    public Feedback(User user, FeedbackModel model) {
        this.id = model.getId();
        this.feedbacker = user;
        this.userId = user.getId();
        this.content = model.getContent();
        this.userPhoneNum = model.getUserPhoneNum();
    }
}
