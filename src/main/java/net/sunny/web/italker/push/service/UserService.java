package net.sunny.web.italker.push.service;

import com.google.common.base.Strings;
import net.sunny.web.italker.push.bean.api.base.ResponseModel;
import net.sunny.web.italker.push.bean.api.user.UpdateInfoModel;
import net.sunny.web.italker.push.bean.card.UserCard;
import net.sunny.web.italker.push.bean.db.User;
import net.sunny.web.italker.push.factory.PushFactory;
import net.sunny.web.italker.push.factory.UserFactory;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Sunny on 2017/5/24.
 * Email：670453367@qq.com
 * Description:用户更新信息,拉取联系人,关注别人,拉取某人的信息,搜索他人
 */

@Path("/user")
public class UserService extends BaseService {

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseModel<UserCard> update(UpdateInfoModel model) {

        if (!UpdateInfoModel.check(model)) {
            return ResponseModel.buildParameterError();
        }

        User self = getSelf();

        self = model.updateToUser(self);
        self = UserFactory.update(self);
        UserCard card = new UserCard(self, User.YES_FOLLOW);
        return ResponseModel.buildOk(card);
    }

    /**
     * 拉取联系人
     *
     * @return
     */
    @GET
    @Path("/contact")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseModel<List<UserCard>> contact() {
        User self = getSelf();
        List<User> users = UserFactory.contacts(self);

        List<UserCard> userCards = users.stream()
                .map(user -> new UserCard(user, User.YES_FOLLOW)) // map操作
                .collect(Collectors.toList());

        return ResponseModel.buildOk(userCards);
    }

    /**
     * 关注别人
     *
     * @param followId 关注人的Id
     * @return 关注人的UserCard
     */
    @PUT
    @Path("/follow/{followId}")
    public ResponseModel<UserCard> follow(@PathParam("followId") String followId) {
        User self = getSelf();

        // 如果关注人的Id是自己或者为空,直接返回参数错误
        if (self.getId().equalsIgnoreCase(followId)
                || Strings.isNullOrEmpty(followId)) {
            return ResponseModel.buildParameterError();
        }

        User followedUser = UserFactory.findById(followId);
        if (followedUser == null) {
            return ResponseModel.buildNotFoundUserError(null);
        }

        // 通知我想关注的人
        PushFactory.pushFollow(followedUser, new UserCard(self), User.WAIT_FOLLOW_RECEIVE);

        return ResponseModel.buildOk(new UserCard(followedUser, User.WAIT_FOLLOW_SEND));
    }

    @PUT
    @Path("/accept/{acceptId}")
    public ResponseModel<UserCard> accept(@PathParam("acceptId") String acceptId) {
        User self = getSelf();

        // 如果接收的人Id是自己或者为空,直接返回参数错误
        if (self.getId().equalsIgnoreCase(acceptId)
                || Strings.isNullOrEmpty(acceptId)) {
            return ResponseModel.buildParameterError();
        }

        User acceptedUser = UserFactory.findById(acceptId);
        if (acceptedUser == null) {
            return ResponseModel.buildNotFoundUserError(null);
        }

        // 存入数据库
        acceptedUser = UserFactory.follow(self, acceptedUser, null);
        if (acceptedUser == null) {
            return ResponseModel.buildServiceError();
        }

        // 通知
        PushFactory.pushFollow(acceptedUser, new UserCard(self), User.YES_FOLLOW);
        return ResponseModel.buildOk(new UserCard(acceptedUser, User.YES_FOLLOW));
    }

    /**
     * 拉取某人的信息
     *
     * @param id
     * @return
     */
    @GET
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseModel<UserCard> getUser(@PathParam("id") String id) {
        if (Strings.isNullOrEmpty(id)) {
            return ResponseModel.buildParameterError();
        }

        User self = getSelf();
        if (self.getId().equalsIgnoreCase(id)) {
            return ResponseModel.buildOk(new UserCard(self, User.YES_FOLLOW));
        }

        User user = UserFactory.findById(id);
        if (user == null) {
            return ResponseModel.buildNotFoundUserError(null);
        }

        // 判断是否关注此人
        boolean isFollow = UserFactory.getUserFollow(self, user) != null;
        return ResponseModel.buildOk(new UserCard(user, isFollow ? User.YES_FOLLOW : User.NOT_FOLLOW));
    }

    /**
     * 搜索人的接口实现
     * 为了简化分页,每次返回20条数据
     */
    @GET
    @Path("/search/{name:(.*)?}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseModel<List<UserCard>> search(@DefaultValue("") @PathParam("name") String name) {
        User self = getSelf();
        List<User> searchUsers = UserFactory.search(name);
        final List<User> contacts = UserFactory.contacts(self);

        List<UserCard> userCards = searchUsers.stream()
                .map(user -> {
                    boolean isFollow = user.getId().equalsIgnoreCase(self.getId())
                            || contacts.stream().anyMatch(
                            contactUser -> contactUser.getId()
                                    .equalsIgnoreCase(user.getId()));

                    return new UserCard(user, isFollow ? User.YES_FOLLOW : User.NOT_FOLLOW);
                }).collect(Collectors.toList());
        return ResponseModel.buildOk(userCards);
    }
}
