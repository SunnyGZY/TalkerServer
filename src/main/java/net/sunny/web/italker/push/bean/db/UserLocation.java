package net.sunny.web.italker.push.bean.db;

import net.sunny.web.italker.push.bean.api.user.UserLocationModel;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "TB_USER_LOCATION")
public class UserLocation {

    @Id
    @PrimaryKeyJoinColumn
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @Column(updatable = false, nullable = false)
    private String id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "userId")
    private User user;
    @Column(nullable = false, updatable = false, insertable = false)
    private String userId;

    @Column(nullable = false)
    private String longitude; // 经度

    @Column(nullable = false)
    private String latitude; // 维度

    @Column
    private String locationDsc; // 地址描述

    @CreationTimestamp
    @Column(nullable = false)
    private LocalDateTime updateTime = LocalDateTime.now();

    public UserLocation() {
    }

    public UserLocation(User user, UserLocationModel model) {
        this.user = user;
        this.userId = user.getId();
        this.longitude = model.getLongitude();
        this.latitude = model.getLatitude();
        this.locationDsc = model.getLocationDsc();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

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

    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(LocalDateTime time) {
        this.updateTime = time;
    }


}
