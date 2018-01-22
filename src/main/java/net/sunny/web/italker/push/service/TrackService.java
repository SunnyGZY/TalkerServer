package net.sunny.web.italker.push.service;

import net.sunny.web.italker.push.bean.api.base.ResponseModel;
import net.sunny.web.italker.push.bean.api.track.CommentModel;
import net.sunny.web.italker.push.bean.api.track.TrackCreateModel;
import net.sunny.web.italker.push.bean.api.track.TrackUpdateModel;
import net.sunny.web.italker.push.bean.card.track.CommentCard;
import net.sunny.web.italker.push.bean.card.track.TrackCard;
import net.sunny.web.italker.push.bean.db.User;
import net.sunny.web.italker.push.bean.db.track.Comment;
import net.sunny.web.italker.push.bean.db.track.Compliment;
import net.sunny.web.italker.push.bean.db.track.Taunt;
import net.sunny.web.italker.push.bean.db.track.Track;
import net.sunny.web.italker.push.factory.ComplimentFactory;
import net.sunny.web.italker.push.factory.TauntFactory;
import net.sunny.web.italker.push.factory.TrackFactory;
import net.sunny.web.italker.push.factory.UserFactory;
import net.sunny.web.italker.push.utils.DateTimeUtil;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

/**
 * Created by sunny on 17-8-16.
 * 好友动态
 */
@Path("/track")
public class TrackService extends BaseService {

    // 发布动态
    @PUT
    @Path("/put")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseModel<TrackCard> putTrack(TrackCreateModel model) {

        if (TrackCreateModel.check(model))
            return ResponseModel.buildParameterError();

        User self = getSelf();

        Track track = TrackFactory.findById(model.getId());
        if (track != null)
            return ResponseModel.buildParameterError();

        track = TrackFactory.add(self, model);
        TrackCard trackCard = new TrackCard(track);
        trackCard.setState(Track.UPLOADED);
        return ResponseModel.buildOk(new TrackCard(track));
    }

    // 更新某条动态
    @PUT
    @Path("/update")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseModel<TrackCard> updateTrack(TrackUpdateModel model) {

        if (!TrackUpdateModel.check(model))
            return ResponseModel.buildParameterError();

        Track track = TrackFactory.findById(model.getId());
        if (track != null) {
            User self = getSelf();

            track = TrackFactory.update(self, track, model);
            return ResponseModel.buildOk(new TrackCard(track));
        } else {
            return ResponseModel.buildParameterError();
        }
    }


    // 删除某条动态
    @PUT
    @Path("/delete")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseModel<TrackCard> deleteTrack(TrackUpdateModel model) {

        return null;
    }


    // 获取校内动态
    @GET
    @Path("/school/pageNo={pageNo:(.*)?}&pageSize={pageSize:(.*)?}&lastTime={lastTime:(.*)?}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseModel<List<TrackCard>> getSchoolTrack(@PathParam("pageNo") @DefaultValue("0") int pageNo,
                                                         @PathParam("pageSize") @DefaultValue("0") int pageSize,
                                                         @PathParam("lastTime") @DefaultValue("") String strTime) {

        if (strTime.isEmpty())
            return ResponseModel.buildParameterError();

        User self = getSelf();

        Date date = DateTimeUtil.getIntactData(strTime);
        Instant instant = date.toInstant();
        ZoneId zone = ZoneId.systemDefault();
        LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, zone);

        List<Track> tracks = TrackFactory.findSchoolTrack(pageNo, pageSize, localDateTime);
        List<TrackCard> trackCards = new ArrayList<>();

        for (Track track : tracks) {
            TrackCard trackCard = new TrackCard(track);
            // 取动态的额外信息（点赞数等）
            long complimentCount = TrackFactory.getComplimentCount(track.getId());
            long tauntCount = TrackFactory.getTauntCount(track.getId());
            long commentCount = TrackFactory.getCommentCount(track.getId());
            trackCard.setComplimentCount(complimentCount);
            trackCard.setTauntCount(tauntCount);
            trackCard.setCommentCount(commentCount);

            // 判断用户是否已经对这条动态点过赞/踩
            Compliment compliment = ComplimentFactory.findByIds(track.getId(), self.getId());
            Taunt taunt = TauntFactory.findByIds(track.getId(), self.getId());
            trackCard.setType(track.getType());
            trackCard.setCompliment(compliment != null);
            trackCard.setTaunt(taunt != null);
            trackCard.setState(Track.UPLOADED);
            trackCards.add(trackCard);
        }

        return ResponseModel.buildOk(trackCards);
    }

    // 获取好友动态
    @GET
    @Path("/friend/pageNo={pageNo:(.*)?}&pageSize={pageSize:(.*)?}&lastTime={lastTime:(.*)?}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseModel<List<TrackCard>> getFriendTrack(@PathParam("pageNo") @DefaultValue("0") int pageNo,
                                                         @PathParam("pageSize") @DefaultValue("0") int pageSize,
                                                         @PathParam("lastTime") @DefaultValue("") String strTime) {

        if (strTime.isEmpty())
            return ResponseModel.buildParameterError();

        User self = getSelf();

        Date date = DateTimeUtil.getIntactData(strTime);
        Instant instant = date.toInstant();
        ZoneId zone = ZoneId.systemDefault();
        LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, zone);

        List<Track> tracks = TrackFactory.findFriendTrack(self, pageNo, pageSize, localDateTime);
        List<TrackCard> trackCards = new ArrayList<>();

        for (Track track : tracks) {
            TrackCard trackCard = new TrackCard(track);
            long complimentCount = TrackFactory.getComplimentCount(track.getId());
            long tauntCount = TrackFactory.getTauntCount(track.getId());
            long commentCount = TrackFactory.getCommentCount(track.getId());
            trackCard.setComplimentCount(complimentCount);
            trackCard.setTauntCount(tauntCount);
            trackCard.setCommentCount(commentCount);

            Compliment compliment = ComplimentFactory.findByIds(track.getId(), self.getId());
            Taunt taunt = TauntFactory.findByIds(track.getId(), self.getId());
            trackCard.setCompliment(compliment != null);
            trackCard.setTaunt(taunt != null);

            trackCards.add(trackCard);
        }

        return ResponseModel.buildOk(trackCards);
    }


    /**
     * 客户端第一次打开向服务器发送此请求，获得所有新动态的数量，以此来提醒用户是否接收查看
     */
    @GET
    @Path("/school/newTrackCount/lastTime={lastTime:(.*)?}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseModel<Integer> getNewSchoolTrackCount(@PathParam("lastTime") @DefaultValue("") String strTime) {

        if (strTime.isEmpty())
            return ResponseModel.buildParameterError();

        Date date = DateTimeUtil.getIntactData(strTime);
        Instant instant = date.toInstant();
        ZoneId zone = ZoneId.systemDefault();

        LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, zone);

        Integer count = TrackFactory.getAfterTimeSchoolTrackCount(localDateTime);

        return ResponseModel.buildOk(count);
    }

    /**
     * 客户端第一次打开向服务器发送此请求，获得所有新动态的数量，以此来提醒用户是否接收查看
     */
    @GET
    @Path("/friend/newTrackCount/lastTime={lastTime:(.*)?}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseModel<Integer> getNewFriendTrackCount(@PathParam("lastTime") @DefaultValue("") String strTime) {

        if (strTime.isEmpty())
            return ResponseModel.buildParameterError();

        User self = getSelf();

        Date date = DateTimeUtil.getIntactData(strTime);
        Instant instant = date.toInstant();
        ZoneId zone = ZoneId.systemDefault();

        LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, zone);

        Integer count = TrackFactory.getAfterTimeFriendTrackCount(localDateTime, self);

        return ResponseModel.buildOk(count);
    }

    /**
     * 点赞接口
     *
     * @param trackId        trackId
     * @param complimenterId userId
     * @return TrackId
     */
    @GET
    @Path("/great/trackId={trackId:(.*)?}&complimenterId={complimenterId:(.*)?}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseModel<String> giveCompliment(@PathParam("trackId") @DefaultValue("") String trackId,
                                                @PathParam("complimenterId") @DefaultValue("") String complimenterId) {

        if (trackId.equals("") || complimenterId.equals(""))
            return ResponseModel.buildParameterError();

        User complimenter = getSelf();
        Track track = TrackFactory.findById(trackId);

        if (complimenter == null || track == null)
            return ResponseModel.buildParameterError();

        Compliment compliment = new Compliment(track, complimenter);
        compliment = TrackFactory.saveCompliment(compliment);

        if (compliment != null) {
            return ResponseModel.buildOk();
        }

        return ResponseModel.buildServiceError();
    }

    /**
     * 点踩接口
     *
     * @param trackId   trackId
     * @param taunterId userId
     * @return TrackId
     */
    @GET
    @Path("/hate/trackId={trackId:(.*)?}&taunterId={taunterId:(.*)?}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseModel<String> giveTaunt(@PathParam("trackId") @DefaultValue("") String trackId,
                                           @PathParam("taunterId") @DefaultValue("") String taunterId) {

        if (trackId.equals("") || taunterId.equals(""))
            return ResponseModel.buildParameterError();

        User taunter = getSelf();
        Track track = TrackFactory.findById(trackId);

        if (taunter == null || track == null)
            return ResponseModel.buildParameterError();

        Taunt taunt = new Taunt(track, taunter);
        taunt = TrackFactory.saveTaunt(taunt);

        if (taunt != null) {
            return ResponseModel.buildOk();
        }

        return ResponseModel.buildServiceError();
    }

    /**
     * 评论接口
     *
     * @param model CommentModel
     * @return CommentCard
     */
    @PUT
    @Path("/comment")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseModel<CommentCard> commentTrack(CommentModel model) {

        if (!CommentModel.check(model))
            return ResponseModel.buildParameterError();

        User commenter = getSelf();
        Track track = TrackFactory.findById(model.getTrackId());

        if (commenter == null || track == null)
            return ResponseModel.buildParameterError();

        Comment comment;
        String receiverName = null;
        if (model.getReceiverId() != null && !model.getReceiverId().equals("")) {
            User receiver = UserFactory.findById(model.getReceiverId());
            comment = new Comment(model, commenter, track, receiver);
            receiverName = receiver.getName();
        } else {
            comment = new Comment(model, commenter, track);
        }

        comment = TrackFactory.saveComment(comment);
        CommentCard commentCard = new CommentCard(comment);
        commentCard.setCommenterName(commenter.getName());
        commentCard.setPortrait(commenter.getPortrait());

        if (receiverName != null) {
            commentCard.setReceiverName(receiverName);
        }
        return ResponseModel.buildOk(commentCard);
    }

    // 拉取动态的所有评论
    @GET
    @Path("/comments/trackId={trackId:(.*)?}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseModel<List<List<CommentCard>>> getCommentList(@PathParam("trackId") @DefaultValue("") String trackId) {

        if (trackId.equals(""))
            return ResponseModel.buildParameterError();

        Track track = TrackFactory.findById(trackId);
        if (track == null)
            return ResponseModel.buildParameterError();

        List<Comment> commentList = TrackFactory.findTrackComment(trackId);
        List<CommentCard> commentCardListNoReceiver = new ArrayList<>(); // 没有额外的接收者
        List<List<CommentCard>> responseData = new ArrayList<>(); // 最后返回的数据
        Map<String, List<CommentCard>> commentIds = new HashMap<>();

        // 将 List<Comment> 转变成 List<CommentCard>
        for (Comment comment : commentList) {
            if (comment.getReceiverId() == null) {
                CommentCard commentCard = comment.build();
                commentCardListNoReceiver.add(commentCard);
            } else {
                CommentCard commentCard = comment.build();
                if (!commentIds.containsKey(commentCard.getCommentId())) {
                    List<CommentCard> list = new ArrayList<>();
                    list.add(commentCard);
                    commentIds.put(commentCard.getCommentId(), list);
                } else {
                    List<CommentCard> list = commentIds.get(commentCard.getCommentId());
                    list.add(commentCard);
                    commentIds.put(commentCard.getCommentId(), list);
                }
            }
        }

        for (CommentCard commentCard : commentCardListNoReceiver) {
            List<CommentCard> commentCards = new ArrayList<>();
            commentCards.add(commentCard);
            responseData.add(commentCards);
        }


        while (commentIds.size() != 0) {
            boolean shouldPass = false;
            for (List<CommentCard> responseDatum : responseData) {
                for (int i = 0; i < responseDatum.size(); i++) {
                    List<CommentCard> list = commentIds.get(responseDatum.get(i).getId());
                    if (list != null) {
                        responseData.remove(responseDatum);
                        commentIds.remove(responseDatum.get(i).getId());
                        responseDatum.addAll(i + 1, list);
                        responseData.add(responseDatum);
                        shouldPass = true;
                    }
                }
                if (shouldPass)
                    break;
            }
        }

        return ResponseModel.buildOk(responseData);
    }

}
