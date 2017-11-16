package net.sunny.web.italker.push.service;

import net.sunny.web.italker.push.bean.api.base.ResponseModel;
import net.sunny.web.italker.push.bean.api.message.MessageCreateModel;
import net.sunny.web.italker.push.bean.card.MessageCard;
import net.sunny.web.italker.push.bean.db.Group;
import net.sunny.web.italker.push.bean.db.Message;
import net.sunny.web.italker.push.bean.db.User;
import net.sunny.web.italker.push.factory.GroupFactory;
import net.sunny.web.italker.push.factory.MessageFactory;
import net.sunny.web.italker.push.factory.PushFactory;
import net.sunny.web.italker.push.factory.UserFactory;
import sun.rmi.runtime.Log;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * Created by Sunny on 2017/6/14.
 * Email：670453367@qq.com
 * Description: TOOD
 */

@Path("/msg")
public class MessageService extends BaseService {

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseModel<MessageCard> pushMessage(MessageCreateModel model) {
        if (!MessageCreateModel.check(model))
            return ResponseModel.buildParameterError();

        User self = getSelf();

        Message message = MessageFactory.findById(model.getId());
        if (message != null) {
            return ResponseModel.buildOk(new MessageCard(message));
        }

        if (model.getReceiverType() == Message.RECEIVER_TYPE_GROUP) {
            return pushToGroup(self, model);
        } else {
            return pushToUser(self, model);
        }
    }

    private ResponseModel<MessageCard> pushToUser(User sender, MessageCreateModel model) {
        User receiver = UserFactory.findById(model.getReceiverId());
        if (receiver == null)
            // 没有找到接收者
            return ResponseModel.buildNotFoundUserError("Con't find receive user");

        if (receiver.getId().equalsIgnoreCase(sender.getId()))
            return ResponseModel.buildCreateError(ResponseModel.ERROR_CREATE_MESSAGE);

        // 存储到数据库
        Message message = MessageFactory.add(sender, receiver, model);

        return buildAndPushResponse(sender, message);
    }

    private ResponseModel<MessageCard> pushToGroup(User sender, MessageCreateModel model) {
        Group group = GroupFactory.findById(sender, model.getReceiverId());
        if (group == null)
            return ResponseModel.buildNotFoundUserError("Con't find receive group");

        // 存储到数据库
        Message message = MessageFactory.add(sender, group, model);

        return buildAndPushResponse(sender, message);
    }

    /**
     * 推送并构建一个返回信息
     *
     * @param sender
     * @param message
     * @return
     */
    private ResponseModel<MessageCard> buildAndPushResponse(User sender, Message message) {
        if (message == null)
            return ResponseModel.buildCreateError(ResponseModel.ERROR_CREATE_MESSAGE);

        // 进行推送
        PushFactory.pushNewMessage(sender, message);

        return ResponseModel.buildOk(new MessageCard(message));
    }
}
