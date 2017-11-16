package net.sunny.web.italker.push.bean.db.track;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * Track的点赞数，评论数等额外数据
 */
@Entity
@Table(name = "TB_TRACK_OTHER_INF")
public class TrackOtherInf {

    @Id
    @PrimaryKeyJoinColumn
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @Column(updatable = false, nullable = false)
    private String id;

    // 动态ID，标明这张照片属于那一条动态
    @JoinColumn(name = "trackId")
    @OneToOne(optional = false, fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Track track;
    @Column(nullable = false, updatable = false, insertable = false)
    private String trackId;

    @Column
    private long greatCount;

    @Column
    private long hateCount;

    @Column
    private long commentCount;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getGreatCount() {
        return greatCount;
    }

    public void setGreatCount(long greatCount) {
        this.greatCount = greatCount;
    }

    public long getHateCount() {
        return hateCount;
    }

    public void setHateCount(long hateCount) {
        this.hateCount = hateCount;
    }

    public long getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(long commentCount) {
        this.commentCount = commentCount;
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
}
