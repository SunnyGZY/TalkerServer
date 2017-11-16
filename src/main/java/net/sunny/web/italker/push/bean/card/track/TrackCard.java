package net.sunny.web.italker.push.bean.card.track;

import com.google.gson.annotations.Expose;
import net.sunny.web.italker.push.bean.db.track.Photo;
import net.sunny.web.italker.push.bean.db.track.Track;
import net.sunny.web.italker.push.utils.Hib;

import java.time.LocalDateTime;
import java.util.*;

/**
 * 申请请求的Card, 用于推送一个申请请求
 *
 * @author qiujuer Email:qiujuer.live.cn
 */
public class TrackCard {
    // Id
    @Expose
    private String id;
    // 发送人Id
    @Expose
    private String ownerId;
    @Expose
    private String content;
    @Expose
    private Set<PhotoCard> photos = new HashSet<>();
    // 创建时间
    @Expose
    private LocalDateTime createAt;
    // 类型
    @Expose
    private int type;
    @Expose
    private int jurisdiction;
    @Expose
    private long tauntCount;
    @Expose
    private long complimentCount;
    @Expose
    private long commentCount;
    @Expose
    private boolean isCompliment;
    @Expose
    private boolean isTaunt;

    public TrackCard(Track track) {

        this.id = track.getId();
        this.content = track.getContent();
        this.jurisdiction = track.getJurisdiction();
        this.type = track.getType();

        Hib.queryOnly(session -> {
            session.load(track, track.getId());
            this.ownerId = track.getPublisherId();

            for (Photo photo : track.getPhotos()) {
                photos.add(new PhotoCard(photo));
            }
        });

        this.createAt = track.getCreateAt();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Set<PhotoCard> getPhotos() {
        return photos;
    }

    public void setPhotos(Set<PhotoCard> photos) {
        this.photos = photos;
    }

    public LocalDateTime getCreateAt() {
        return createAt;
    }

    public void setCreateAt(LocalDateTime createAt) {
        this.createAt = createAt;
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

    public void setJurisdiction(int jurisdiction) {
        this.jurisdiction = jurisdiction;
    }

    public long getTauntCount() {
        return tauntCount;
    }

    public void setTauntCount(long tauntCount) {
        this.tauntCount = tauntCount;
    }

    public long getComplimentCount() {
        return complimentCount;
    }

    public void setComplimentCount(long complimentCount) {
        this.complimentCount = complimentCount;
    }

    public long getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(long commentCount) {
        this.commentCount = commentCount;
    }

    public boolean isCompliment() {
        return isCompliment;
    }

    public void setCompliment(boolean compliment) {
        isCompliment = compliment;
    }

    public boolean isTaunt() {
        return isTaunt;
    }

    public void setTaunt(boolean taunt) {
        isTaunt = taunt;
    }
}
