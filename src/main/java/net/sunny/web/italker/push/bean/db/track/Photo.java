package net.sunny.web.italker.push.bean.db.track;

import net.sunny.web.italker.push.bean.api.track.PhotoModel;
import net.sunny.web.italker.push.bean.db.User;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * Created by sunny on 17-8-8.
 * 用户上传至朋友圈的照片
 */

@Entity
@Table(name = "TB_PHOTO")
public class Photo {

    @Id
    @PrimaryKeyJoinColumn
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @Column(updatable = false, nullable = false)
    private String id;

    // 用户ID
    @JoinColumn(name = "ownerId")
    @ManyToOne(optional = false)
    private User owner;
    @Column(nullable = false, updatable = false, insertable = false)
    private String ownerId;

    // 动态ID，标明这张照片属于那一条动态
    @JoinColumn(name = "trackId")
    @ManyToOne(optional = false, fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Track track;
    @Column(nullable = false, updatable = false, insertable = false)
    private String trackId;

    // 照片在这条动态显示时的顺序
    @Column
    private int position;

    // 照片的URL地址
    @Column
    private String photoUrl;

    public Photo() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
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

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public Photo(User user, Track track, PhotoModel model) {
        this.id = model.getId();
        this.owner = user;
        this.ownerId = user.getId();
        this.track = track;
        this.trackId = model.getTrackId();
        this.position = model.getPosition();
        this.photoUrl = model.getPhotoUrl();
    }
}
