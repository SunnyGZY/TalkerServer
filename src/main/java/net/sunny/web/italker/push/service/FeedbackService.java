package net.sunny.web.italker.push.service;

import net.sunny.web.italker.push.bean.api.base.ResponseModel;
import net.sunny.web.italker.push.bean.api.feedback.FeedbackModel;
import net.sunny.web.italker.push.bean.card.track.TrackCard;
import net.sunny.web.italker.push.bean.db.Feedback;
import net.sunny.web.italker.push.bean.db.User;
import net.sunny.web.italker.push.factory.FeedbackFactory;

import javax.ws.rs.Consumes;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/feedback")
public class FeedbackService extends BaseService {

    // 用户反馈
    @PUT
    @Path("/put")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseModel<String> feedBack(FeedbackModel model) {

        if (!FeedbackModel.check(model))
            return ResponseModel.buildParameterError();

        User self = getSelf();
        Feedback feedback = null;
        if (self != null) {
            feedback = FeedbackFactory.add(self, model);
        }

        if (feedback != null) {
            return ResponseModel.buildOk("OK");
        } else {
            return ResponseModel.buildOk("ERROR");
        }
    }
}
