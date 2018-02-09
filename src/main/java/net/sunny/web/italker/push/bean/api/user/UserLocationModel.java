package net.sunny.web.italker.push.bean.api.user;

import com.google.gson.annotations.Expose;

public class UserLocationModel {

    @Expose
    private String longitude; // 经度

    @Expose
    private String latitude; // 维度

    @Expose
    private String locationDsc; // 地址描述

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLocationDsc() {
        return locationDsc;
    }

    public void setLocationDsc(String locationDsc) {
        this.locationDsc = locationDsc;
    }

    /**
     * 判断数据是否为空
     * 用户Id，经纬度不能为扩能
     *
     * @param model UserLocationModel
     * @return false 数据不符合
     */
    public static boolean check(UserLocationModel model) {
        return !model.getLatitude().isEmpty()
                || !model.getLocationDsc().isEmpty();
    }

    @Override
    public String toString() {
        return "UserLocationModel{" +
                "longitude='" + longitude + '\'' +
                ", latitude='" + latitude + '\'' +
                ", locationDsc='" + locationDsc + '\'' +
                '}';
    }
}
