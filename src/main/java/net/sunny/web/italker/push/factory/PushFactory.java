package net.sunny.web.italker.push.factory;

import com.google.common.base.Strings;
import net.sunny.web.italker.push.bean.api.base.PushModel;
import net.sunny.web.italker.push.bean.card.GroupMemberCard;
import net.sunny.web.italker.push.bean.card.MessageCard;
import net.sunny.web.italker.push.bean.card.UserCard;
import net.sunny.web.italker.push.bean.db.*;
import net.sunny.web.italker.push.utils.Hib;
import net.sunny.web.italker.push.utils.PushDispatcher;
import net.sunny.web.italker.push.utils.TextUtil;
import org.hibernate.Session;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by Sunny on 2017/6/14.
 * Email：670453367@qq.com
 * Description: TOOD
 */
public class PushFactory {

    /**
     * 发送一条消息，并在当前的发送历史记录中存储记录
     *
     * @param sender
     * @param message
     */
    public static void pushNewMessage(User sender, Message message) {
        if (sender == null || message == null)
            return;

        MessageCard card = new MessageCard(message);
        String entity = TextUtil.toJson(card);

        PushDispatcher dispatcher = new PushDispatcher();

        if (message.getGroup() == null && Strings.isNullOrEmpty(message.getGroupId())) {
            User receiver = UserFactory.findById(message.getReceiverId());
            if (receiver == null)
                return;
            // 历史记录表建立
            PushHistory history = new PushHistory();
            history.setEntityType(PushModel.ENTITY_TYPE_MESSAGE);
            history.setEntity(entity);
            history.setReceiver(receiver);
            history.setReceiverId(receiver.getPushId());

            PushModel pushModel = new PushModel();
            pushModel.add(history.getEntityType(), history.getEntity());

            dispatcher.add(receiver, pushModel);
            Hib.save(session -> session.save(history));
        } else {
            Group group = message.getGroup();
            // 因为延迟加载情况可能为null，需要通过Id查询
            if (group == null)
                group = GroupFactory.findById(message.getGroupId());

            if (group == null)
                return;

            Set<GroupMember> members = GroupFactory.getMembers(group);
            if (members == null || members.size() == 0)
                return;

            // 过滤我自己
            members = members
                    .stream()
                    .filter(groupMember -> !groupMember.getUserId().equalsIgnoreCase(sender.getId()))
                    .collect(Collectors.toSet());

            if (members.size() == 0)
                return;

            List<PushHistory> histories = new ArrayList<>();
            addGroupMembersPushModel(dispatcher, // 消息的发送者
                    histories, // 数据库存储的列表
                    members, // 所有的成员
                    entity, // 要发送的数据
                    PushModel.ENTITY_TYPE_MESSAGE); // 发送的类型

            Hib.saveOnly(session -> {
                for (PushHistory history : histories) {
                    session.save(history);
                }
            });
        }

        dispatcher.submit();
    }

    /**
     * 给群成员构建一个消息
     * 把消息存储到数据库的历史记录中，每个人，每条消息都是一个记录
     *
     * @param dispatcher
     * @param histories
     * @param members
     * @param entity
     * @param entityTypeMessage
     */
    private static void addGroupMembersPushModel(PushDispatcher dispatcher, List<PushHistory> histories, Set<GroupMember> members, String entity, int entityTypeMessage) {
        for (GroupMember member : members) {
            User receiver = member.getUser();
            if (receiver == null)
                return;

            PushHistory history = new PushHistory();
            history.setEntityType(entityTypeMessage);
            history.setEntity(entity);
            history.setReceiver(receiver);
            history.setReceiverPushId(receiver.getPushId());
            histories.add(history);

            PushModel pushModel = new PushModel();
            pushModel.add(history.getEntityType(), history.getEntity());

            dispatcher.add(receiver, pushModel);
        }
    }

    /**
     * 通知新加入的群成员
     *
     * @param members 被加入群的成员
     */
    public static void pushJoinGroup(Set<GroupMember> members) {
        PushDispatcher dispatcher = new PushDispatcher();
        List<PushHistory> histories = new ArrayList<>();
        for (GroupMember member : members) {
            User receiver = member.getUser();
            if (receiver == null)
                return;

            GroupMemberCard memberCard = new GroupMemberCard(member);
            String entity = TextUtil.toJson(memberCard);

            PushHistory history = new PushHistory();
            history.setEntityType(PushModel.ENTITY_TYPE_ADD_GROUP);
            history.setEntity(entity);
            history.setReceiver(receiver);
            history.setReceiverPushId(receiver.getPushId());
            histories.add(history);

            PushModel pushModel = new PushModel().add(history.getEntityType(), history.getEntity());

            dispatcher.add(receiver, pushModel);
            histories.add(history);
        }

        Hib.saveOnly(session -> {
            for (PushHistory history : histories) {
                session.save(history);
            }
        });
        dispatcher.submit();
    }

    /**
     * 通知老群成员有一系列新的成员加入某个群
     *
     * @param oldMembers  老成员
     * @param insertCards 新成员
     */
    public static void pushGroupMemberAdd(Set<GroupMember> oldMembers, List<GroupMemberCard> insertCards) {
        PushDispatcher dispatcher = new PushDispatcher();
        List<PushHistory> histories = new ArrayList<>();

        String entity = TextUtil.toJson(insertCards);

        addGroupMembersPushModel(dispatcher, histories, oldMembers, entity, PushModel.ENTITY_TYPE_ADD_GROUP_MEMBERS);

        Hib.saveOrUpdate(session -> {
            for (PushHistory history : histories) {
                session.saveOrUpdate(history);
            }
        });

        dispatcher.submit();
    }

    /**
     * 推送账户退出命令
     *
     * @param receiver 退出软件的用户
     * @param pushId   这个时候的接收者设备Id
     */
    public static void pushLogout(User receiver, String pushId) {
        PushHistory history = new PushHistory();
        history.setEntityType(PushModel.ENTITY_TYPE_LOGOUT);
        history.setEntity("Account logout!");
        history.setReceiver(receiver);
        history.setReceiverPushId(pushId);

        Hib.save(session -> session.save(history));

        PushDispatcher dispatcher = new PushDispatcher();
        PushModel pushModel = new PushModel()
                .add(history.getEntityType(), history.getEntity());

        dispatcher.add(receiver, pushModel);
        dispatcher.submit();
    }

    public static void pushFollow(User receiver, UserCard userCard, int state) {

//        TrackCreateModel track = new TrackCreateModel();
//        track.setId("1");
//        track.setContent("123");
//
//        Hib.queryOnly(session1 -> session1.save(track));

        userCard.setFollowState(state);
        String entity = TextUtil.toJson(userCard);

        PushHistory history = new PushHistory();
        history.setEntityType(PushModel.ENTITY_TYPE_ADD_FRIEND);
        history.setEntity(entity);
        history.setReceiver(receiver);
        history.setReceiverPushId(receiver.getPushId());


        Hib.saveOnly(session -> session.save(history));

        PushDispatcher dispatcher = new PushDispatcher();
        PushModel pushModel = new PushModel()
                .add(history.getEntityType(), history.getEntity());
        dispatcher.add(receiver, pushModel);
        dispatcher.submit();
    }
}
