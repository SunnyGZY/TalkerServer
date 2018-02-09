package net.sunny.web.italker.push.bean.card;

import com.google.gson.annotations.Expose;
import net.sunny.web.italker.push.bean.db.UserLocation;

import java.time.LocalDateTime;

public class UserLocationCard {

    @Expose
    private boolean isUpdateSuccess;

    @Expose
    private LocalDateTime updateAt;

    public UserLocationCard(UserLocation userLocation) {
        this.isUpdateSuccess = true;
        this.updateAt = userLocation.getUpdateTime();
    }

    public boolean isUpdateSuccess() {
        return isUpdateSuccess;
    }

    public void setUpdateSuccess(boolean updateSuccess) {
        isUpdateSuccess = updateSuccess;
    }

    public LocalDateTime getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(LocalDateTime updateAt) {
        this.updateAt = updateAt;
    }
}
