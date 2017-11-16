package net.sunny.web.italker.push.factory;

import net.sunny.web.italker.push.bean.api.message.MessageCreateModel;
import net.sunny.web.italker.push.bean.db.Group;
import net.sunny.web.italker.push.bean.db.Message;
import net.sunny.web.italker.push.bean.db.User;
import net.sunny.web.italker.push.utils.Hib;

/**
 * Created by Sunny on 2017/6/14.
 * Email：670453367@qq.com
 * Description: TOOD
 */
public class MessageFactory {

    /**
     * 查询某一个消息
     *
     * @param id
     * @return
     */
    public static Message findById(String id) {
        return Hib.query(session -> session.get(Message.class, id));
    }

    /**
     * 添加一条普通消息
     *
     * @param sender
     * @param receiver
     * @param model
     * @return
     */
    public static Message add(User sender, User receiver, MessageCreateModel model) {
        Message message = new Message(sender, receiver, model);
        return save(message);
    }

    /**
     * 添加一条群消息
     *
     * @param sender
     * @param group
     * @param model
     * @return
     */
    public static Message add(User sender, Group group, MessageCreateModel model) {
        Message message = new Message(sender, group, model);
        return save(message);
    }

    private static Message save(Message message) {
        return Hib.query(session -> {
            session.save(message);
            // 写入到数据库
            session.flush();
            // 从数据库中查询出来
            session.refresh(message);
            return message;
        });
    }
}
