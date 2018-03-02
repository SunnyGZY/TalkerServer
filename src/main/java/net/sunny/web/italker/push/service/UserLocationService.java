package net.sunny.web.italker.push.service;

import net.sunny.web.italker.push.bean.api.base.ResponseModel;
import net.sunny.web.italker.push.bean.api.user.UserLocationModel;
import net.sunny.web.italker.push.bean.card.NearbyPersonCard;
import net.sunny.web.italker.push.bean.card.UserCard;
import net.sunny.web.italker.push.bean.card.UserLocationCard;
import net.sunny.web.italker.push.bean.db.User;
import net.sunny.web.italker.push.bean.db.UserLocation;
import net.sunny.web.italker.push.factory.UserLocationFactory;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.List;

@Path("/location")
public class UserLocationService extends BaseService {

    @POST
    @Path("/update")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseModel<UserLocationCard> putLocation(UserLocationModel model) {

        if (!UserLocationModel.check(model))
            return ResponseModel.buildParameterError();

        User self = getSelf();

        UserLocation userLocation = UserLocationFactory.update(self, model);
        if (userLocation != null) {
            return ResponseModel.buildOk(new UserLocationCard(userLocation));
        } else {
            return ResponseModel.buildServiceError();
        }
    }

    @GET
    @Path("/nearby_person/longitude={longitude:(.*)?}&latitude={latitude:(.*)?}&distance={distance:(.*)}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseModel<List<NearbyPersonCard>> nearbyPersons(@PathParam("longitude") @DefaultValue("0") double longitude,
                                                               @PathParam("latitude") @DefaultValue("0") double latitude,
                                                               @PathParam("distance") @DefaultValue("500") double distance) {

        if (longitude == 0 || latitude == 0)
            return ResponseModel.buildParameterError();

        User self = getSelf();

        List<UserCard> userCardList = UserLocationFactory.getNearbyPerson(longitude, latitude, distance, self);
        List<NearbyPersonCard> nearPersonCardList = new ArrayList<>();
        for (UserCard userCard : userCardList) {
            double dis = ((int) userCard.getDistance()) / 100 * 100 + 100;

            nearPersonCardList.add(new NearbyPersonCard(userCard.getId(), userCard.getPortrait(),
                    userCard.getName(), userCard.getSex(), dis));
        }

        return ResponseModel.buildOk(nearPersonCardList);
    }
}
