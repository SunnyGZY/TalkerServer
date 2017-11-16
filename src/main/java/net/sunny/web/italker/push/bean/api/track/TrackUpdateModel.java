package net.sunny.web.italker.push.bean.api.track;

import com.google.common.base.Strings;
import com.google.gson.annotations.Expose;
import net.sunny.web.italker.push.bean.db.track.Track;

import java.util.Set;

/**
 * Created by sunny on 17-8-16.
 */
public class TrackUpdateModel {

    @Expose
    private String id;

    @Expose
    private String content;

    @Expose
    private Set<PhotoModel> photos;

    @Expose
    private int type;

    @Expose
    private int jurisdiction;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Set<PhotoModel> getPhotos() {
        return photos;
    }

    public void setPhotos(Set<PhotoModel> photos) {
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

    public void setJurisdiction(int jurisdiction) {
        this.jurisdiction = jurisdiction;
    }

    public static boolean check(TrackUpdateModel model) {
        return !(model == null || model.photos == null
                && Strings.isNullOrEmpty(model.id)
                || Strings.isNullOrEmpty(model.content)
                || Strings.isNullOrEmpty(model.getId())
                && model.jurisdiction == Track.IN_FRIEND
                || model.jurisdiction == Track.IN_SCHOOL);
    }

    @Override
    public String toString() {
        return "TrackCreateModel{" +
                "id='" + id + '\'' +
                ", content='" + content + '\'' +
                ", photos=" + photos +
                ", type=" + type +
                ", jurisdiction=" + jurisdiction +
                '}';
    }
}
