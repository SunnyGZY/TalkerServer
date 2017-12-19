package net.sunny.web.italker.push.bean.api.feedback;

import com.google.common.base.Strings;
import com.google.gson.annotations.Expose;
import net.sunny.web.italker.push.bean.api.track.PhotoModel;
import net.sunny.web.italker.push.bean.db.track.Track;

import java.util.Set;

/**
 * Created by sunny on 17-8-16.
 */
public class FeedbackModel {

    @Expose
    private String id;

    @Expose
    private String content;

    @Expose
    private String appVersion;

    @Expose
    private String userPhoneNum;

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

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    public String getUserPhoneNum() {
        return userPhoneNum;
    }

    public void setUserPhoneNum(String userPhoneNum) {
        this.userPhoneNum = userPhoneNum;
    }

    public static boolean check(FeedbackModel model) {
        return true;
    }

}
