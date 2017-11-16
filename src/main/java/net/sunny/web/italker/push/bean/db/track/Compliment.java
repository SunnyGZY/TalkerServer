package net.sunny.web.italker.push.bean.db.track;

import net.sunny.web.italker.push.bean.db.User;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * 点赞 数据库表
 */
@Entity
@Table(name = "TB_COMPLIMENT")
public class Compliment {

    @Id
    @PrimaryKeyJoinColumn
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @Column(updatable = false, nullable = false)
    private String id;

    @JoinColumn(name = "trackId")
    @ManyToOne(optional = false, fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Track track;
    @Column(nullable = false, updatable = false, insertable = false)
    private String trackId;

    @JoinColumn(name = "complimenterId")
    @ManyToOne(optional = false)
    private User complimenter;
    @Column(nullable = false, updatable = false, insertable = false)
    private String complimenterId;

    public Compliment(Track track, User complimenter) {
        this.track = track;
        this.trackId = track.getId();
        this.complimenter = complimenter;
        this.complimenterId = complimenter.getId();
    }

    public Compliment() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public User getComplimenter() {
        return complimenter;
    }

    public void setComplimenter(User complimenter) {
        this.complimenter = complimenter;
    }

    public String getComplimenterId() {
        return complimenterId;
    }

    public void setComplimenterId(String complimenterId) {
        this.complimenterId = complimenterId;
    }
}
