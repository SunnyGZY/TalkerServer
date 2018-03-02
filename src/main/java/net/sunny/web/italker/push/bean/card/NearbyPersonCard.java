package net.sunny.web.italker.push.bean.card;

import com.google.gson.annotations.Expose;

/**
 * Created by 67045 on 2018/3/1.
 * 附近的人
 */
public class NearbyPersonCard {

    @Expose
    private String userId;
    @Expose
    private String portrait;
    @Expose
    private double distance;
    @Expose
    private String userName;
    @Expose
    private int sex;

    public NearbyPersonCard(String userId, String portrait, String userName, int sex, double distance) {
        this.userId = userId;
        this.portrait = portrait;
        this.userName = userName;
        this.sex = sex;
        this.distance = distance;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPortrait() {
        return portrait;
    }

    public void setPortrait(String portrait) {
        this.portrait = portrait;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    @Override
    public String toString() {
        return "NearbyPersonCard{" +
                "userId='" + userId + '\'' +
                ", portrait='" + portrait + '\'' +
                ", distance=" + distance +
                ", userName='" + userName + '\'' +
                ", sex=" + sex +
                '}';
    }
}
