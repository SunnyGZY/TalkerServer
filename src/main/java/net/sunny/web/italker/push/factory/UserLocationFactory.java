package net.sunny.web.italker.push.factory;

import net.sunny.web.italker.push.bean.api.user.UserLocationModel;
import net.sunny.web.italker.push.bean.db.User;
import net.sunny.web.italker.push.bean.db.UserLocation;
import net.sunny.web.italker.push.utils.Hib;

public class UserLocationFactory {

    public static UserLocation update(User self, UserLocationModel model) {

        UserLocation userLocation = new UserLocation(self, model);

        return Hib.save(session -> {
            session.save(userLocation);
            return userLocation;
        });
    }
}
