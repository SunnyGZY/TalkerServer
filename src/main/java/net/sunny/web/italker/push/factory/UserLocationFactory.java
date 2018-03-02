package net.sunny.web.italker.push.factory;

import net.sunny.web.italker.push.bean.api.user.UserLocationModel;
import net.sunny.web.italker.push.bean.card.UserCard;
import net.sunny.web.italker.push.bean.db.User;
import net.sunny.web.italker.push.bean.db.UserLocation;
import net.sunny.web.italker.push.utils.Hib;
import net.sunny.web.italker.push.utils.LocationCalculateUtil;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class UserLocationFactory {

    public static UserLocation update(User self, UserLocationModel model) {

        UserLocation userLocation = new UserLocation(self, model);

        return Hib.save(session -> {
            session.save(userLocation);
            return userLocation;
        });
    }

    public static List<UserCard> getNearbyPerson(double longitude, double latitude, double distance, User self) {


        List<UserLocation> locationList = Hib.query(session ->
                session.createQuery("from UserLocation where userId!=:selfId order by updateTime desc")
                        .setParameter("selfId", self.getId())
                        .list());

        Set<UserLocation> userLocationSet = new HashSet<>();
        Set<String> userIds = new HashSet<>();

        for (UserLocation userLocation : locationList) {
            if (!userIds.contains(userLocation.getUserId())) {
                double dis = LocationCalculateUtil.getDistance(longitude, latitude, userLocation.getLongitude(), userLocation.getLatitude());
                if (dis <= distance) {
                    userLocation.setDistance(dis);
                    userLocationSet.add(userLocation);
                }
                userIds.add(userLocation.getUserId());
            }
        }

        List<UserCard> userCardList = new ArrayList<>();
        for (UserLocation userLocation : userLocationSet) {
            if (userCardList.size() == 0) {
                User user = UserFactory.findById(userLocation.getUserId());
                UserCard userCard = new UserCard(user);
                userCard.setLongitude(userLocation.getLongitude());
                userCard.setLatitude(userLocation.getLatitude());
                userCard.setDistance(userLocation.getDistance());
                userCardList.add(userCard);
            } else {
                for (int i = 0; i <= userCardList.size(); i++) {
                    if (userLocation.getDistance() <= userCardList.get(i).getDistance()) {
                        User user = UserFactory.findById(userLocation.getUserId());
                        UserCard userCard = new UserCard(user);
                        userCard.setLongitude(userLocation.getLongitude());
                        userCard.setLatitude(userLocation.getLatitude());
                        userCard.setDistance(userLocation.getDistance());
                        userCardList.add(i, userCard);
                        break;
                    }
                }
            }
        }

        return userCardList;
    }
}
