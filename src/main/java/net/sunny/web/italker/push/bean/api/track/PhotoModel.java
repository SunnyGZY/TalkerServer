package net.sunny.web.italker.push.bean.api.track;

import com.google.gson.annotations.Expose;

/**
 * Created by sunny on 17-8-16.
 */
public class PhotoModel {

    @Expose
    private String id;

    @Expose
    private String trackId;

    @Expose
    private int position;

    @Expose
    private String photoUrl;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    @Override
    public String toString() {
        return "PhotoModel{" +
                "id='" + id + '\'' +
                ", trackId='" + trackId + '\'' +
                ", position=" + position +
                ", photoUrl='" + photoUrl + '\'' +
                '}';
    }
}
