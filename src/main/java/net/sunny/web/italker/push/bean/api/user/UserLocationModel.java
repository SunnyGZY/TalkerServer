package net.sunny.web.italker.push.bean.api.user;

import com.google.gson.annotations.Expose;

public class UserLocationModel {

    @Expose
    private double longitude; // 经度

    @Expose
    private double latitude; // 维度

    @Expose
    private String locationDsc; // 地址描述

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
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
        return true;
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
