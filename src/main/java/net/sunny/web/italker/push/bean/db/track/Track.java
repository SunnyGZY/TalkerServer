package net.sunny.web.italker.push.bean.db.track;

import net.sunny.web.italker.push.bean.api.track.PhotoModel;
import net.sunny.web.italker.push.bean.api.track.TrackCreateModel;
import net.sunny.web.italker.push.bean.api.track.TrackUpdateModel;
import net.sunny.web.italker.push.bean.db.User;
import org.hibernate.annotations.*;

import javax.persistence.CascadeType;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by sunny on 17-8-3.
 * 好友动态
 */

@Entity
@Table(name = "TB_TRACK")
public class Track {

    public static int IN_SCHOOL = 0x01;
    public static int IN_FRIEND = 0x02;

    public static int UPLOADING = 0x01;
    public static int UPLOADED = 0x02;

    @Id
    @PrimaryKeyJoinColumn
    //Id由客户端负责生成
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @Column(updatable = false, nullable = false)
    private String id;

    @JoinColumn(name = "publisherId")
    @ManyToOne(optional = false)
    private User publisher;
    @Column(nullable = false, updatable = false, insertable = false)
    private String publisherId;

    // 文本
    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    // 照片
    @JoinColumn(name = "trackId")
    @LazyCollection(LazyCollectionOption.EXTRA)
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Photo> photos = new HashSet<>();

    // TODO: 17-8-8 视频暂时未做

    @CreationTimestamp
    @Column(nullable = false)
    private LocalDateTime createAt = LocalDateTime.now();

    @Column
    private int type;

    // 动态显示的权限
    @Column
    private int jurisdiction;

    public Track() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public User getPublisher() {
        return publisher;
    }

    public void setPublisher(User publisher) {
        this.publisher = publisher;
    }

    public String getPublisherId() {
        return publisherId;
    }

    public void setPublisherId(String publisherId) {
        this.publisherId = publisherId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Set<Photo> getPhotos() {
        return photos;
    }

    public void setPhotos(Set<Photo> photos) {
        this.photos = photos;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getJurisdiction() {
        return jurisdiction;
    }

    public void setJurisdiction(int range) {
        this.jurisdiction = range;
    }

    public LocalDateTime getCreateAt() {
        return createAt;
    }

    public Track(User user, TrackCreateModel model) {
        this.id = model.getId();
        this.publisher = user;
        this.publisherId = user.getId();
        this.content = model.getContent();
        this.type = model.getType();
        this.jurisdiction = model.getJurisdiction();
        for (PhotoModel photoModel : model.getPhotos()) {
            this.photos.add(new Photo(user, this, photoModel));
        }
    }

    public Track(User user, TrackUpdateModel model) {
        this.id = model.getId();
        this.publisher = user;
        this.publisherId = user.getId();
        this.content = model.getContent();
        this.type = model.getType();
        this.jurisdiction = model.getJurisdiction();
        for (PhotoModel photoModel : model.getPhotos()) {
            this.photos.add(new Photo(user, this, photoModel));
        }
    }
}
