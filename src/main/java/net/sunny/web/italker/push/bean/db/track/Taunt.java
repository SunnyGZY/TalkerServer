package net.sunny.web.italker.push.bean.db.track;

import net.sunny.web.italker.push.bean.db.User;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * 踩 数据库表
 */
@Entity
@Table(name = "TB_TAUNT")
public class Taunt {

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

    @JoinColumn(name = "taunterId")
    @ManyToOne(optional = false)
    private User taunter;
    @Column(nullable = false, updatable = false, insertable = false)
    private String taunterId;

    public Taunt() {
    }

    public Taunt(Track track, User taunter) {
        this.taunter = taunter;
        this.taunterId = taunter.getId();
        this.track = track;
        this.trackId = track.getId();
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

    public User getTaunter() {
        return taunter;
    }

    public void setTaunter(User taunter) {
        this.taunter = taunter;
    }

    public String getTaunterId() {
        return taunterId;
    }

    public void setTaunterId(String taunterId) {
        this.taunterId = taunterId;
    }
}
