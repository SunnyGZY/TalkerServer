package net.sunny.web.italker.push.service;

import net.sunny.web.italker.push.bean.api.base.ResponseModel;
import net.sunny.web.italker.push.bean.api.user.UserLocationModel;
import net.sunny.web.italker.push.bean.card.UserCard;
import net.sunny.web.italker.push.bean.card.UserLocationCard;
import net.sunny.web.italker.push.bean.db.User;
import net.sunny.web.italker.push.bean.db.UserLocation;
import net.sunny.web.italker.push.factory.UserLocationFactory;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/location")
public class UserLocationService extends BaseService {

    @POST
    @Path("/update")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseModel<UserLocationCard> putTrack(UserLocationModel model) {

        if (!UserLocationModel.check(model))
            return ResponseModel.buildAccountError();

        User self = getSelf();

        UserLocation userLocation = UserLocationFactory.update(self, model);
        if (userLocation != null) {
            return ResponseModel.buildOk(new UserLocationCard(userLocation));
        } else {
            return ResponseModel.buildServiceError();
        }
    }

    @GET
    @Path("/nearby_person/longitude={longitude:(.*)?}&latitude={latitude:(.*)?}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseModel<List<UserCard>> members(@PathParam("longitude") @DefaultValue("0x00") String longitude,
                                                 @PathParam("latitude") @DefaultValue("0x00") String latitude) {

        User self = getSelf();

        return ResponseModel.buildOk();
    }
}
