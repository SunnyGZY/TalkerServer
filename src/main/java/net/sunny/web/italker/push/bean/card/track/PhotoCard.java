package net.sunny.web.italker.push.bean.card.track;

import com.google.gson.annotations.Expose;
import net.sunny.web.italker.push.bean.db.track.Photo;

/**
 * Created by sunny on 17-8-16.
 * 照片Card
 */
public class PhotoCard {

    @Expose
    private String id;

    @Expose
    private String ownerId;

    @Expose
    private String trackId;

    @Expose
    private int position;

    @Expose
    private String photoUrl;

    PhotoCard(Photo photo) {
        this.id = photo.getId();
        this.ownerId = photo.getOwnerId();
        this.trackId = photo.getTrackId();
        this.position = photo.getPosition();
        this.photoUrl = photo.getPhotoUrl();
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
}
