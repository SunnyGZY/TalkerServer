package net.sunny.web.italker.push.bean.card;

import com.google.gson.annotations.Expose;
import net.sunny.web.italker.push.bean.db.User;
import net.sunny.web.italker.push.utils.Hib;

import java.time.LocalDateTime;

/**
 * Created by Sunny on 2017/5/23.
 * Email：670453367@qq.com
 * Description: 用户信息卡片
 */
public class UserCard {

    @Expose
    private String id;
    @Expose
    private String name;
    @Expose
    private String phone;
    @Expose
    private String portrait;
    @Expose
    private String desc;
    @Expose
    private int sex = 0;
    @Expose
    private int follows;//用户关注人的数量
    @Expose
    private int following;//用户的粉丝
    @Expose
    private int followState;
    @Expose
    private LocalDateTime modifyAt = LocalDateTime.now();
    @Expose
    private double longitude;
    @Expose
    private double latitude;
    @Expose
    private boolean isPubLoca;

    // 计算距离时临时存储变量
    private double distance;

    public UserCard(final User user) {
        this(user, User.NOT_FOLLOW);
    }

    public UserCard(final User user, int followState) {
        this.id = user.getId();
        this.name = user.getName();
        this.phone = user.getPhone();
        this.portrait = user.getPortrait();
        this.desc = user.getDescription();
        this.sex = user.getSex();
        this.modifyAt = user.getUpdateAt();
        this.followState = followState;
        this.isPubLoca = user.getIsPubLoca() != 0;

        // TODO 得到关注人和粉丝的数量
        // user.getFollowers().size(); // 懒加载会报错
        Hib.queryOnly(session -> {
            session.load(user, user.getId());
            this.follows = user.getFollowers().size();
            this.following = user.getFollowing().size();
        });
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPortrait() {
        return portrait;
    }

    public void setPortrait(String portrait) {
        this.portrait = portrait;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public int getFollows() {
        return follows;
    }

    public void setFollows(int follows) {
        this.follows = follows;
    }

    public int getFollowing() {
        return following;
    }

    public void setFollowing(int following) {
        this.following = following;
    }

    public int getFollowState() {
        return followState;
    }

    public void setFollowState(int followState) {
        this.followState = followState;
    }

    public LocalDateTime getModifyAt() {
        return modifyAt;
    }

    public void setModifyAt(LocalDateTime modifyAt) {
        this.modifyAt = modifyAt;
    }

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

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public boolean isPubLoca() {
        return isPubLoca;
    }

    public void setPubLoca(boolean pubLoca) {
        isPubLoca = pubLoca;
    }
}
