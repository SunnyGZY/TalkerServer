package net.sunny.web.italker.push.factory;

import com.google.common.base.Strings;
import net.sunny.web.italker.push.bean.db.User;
import net.sunny.web.italker.push.bean.db.UserFollow;
import net.sunny.web.italker.push.utils.Hib;
import net.sunny.web.italker.push.utils.TextUtil;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Created by Sunny on 2017/5/23.
 * Email：670453367@qq.com
 * Description: TOOD
 */
public class UserFactory {

    public static User findByToken(String token) {
        return Hib.query(session -> (User) session
                .createQuery("from User where token=:token")
                .setParameter("token", token)
                .uniqueResult());
    }

    public static User findByPhone(String phone) {
        return Hib.query(session -> (User) session.createQuery("from User where phone=:inPhone")
                .setParameter("inPhone", phone)
                .uniqueResult());
    }

    public static User findByName(String name) {
        return Hib.query(session -> (User) session.createQuery("from User where name=:name")
                .setParameter("name", name)
                .uniqueResult());
    }

    public static User findById(String id) {
        return Hib.query(session -> session.get(User.class, id));
    }

    // 更新到数据库
    public static User update(User user) {
        return Hib.query(session -> {
            session.saveOrUpdate(user);
            return user;
        });
    }

    public static User bindPushId(User user, String pushId) {
        if (Strings.isNullOrEmpty(pushId))
            return null;

        Hib.queryOnly(session -> {
            @SuppressWarnings("unchecked")
            List<User> userList = (List<User>) session
                    .createQuery("from User where lower(pushId)=:pushId and id!=:userId")
                    .setParameter("pushId", pushId)
                    .setParameter("userId", user.getId())
                    .list();

            for (User u : userList) {
                u.setPushId(null);
                session.saveOrUpdate(u);
            }
        });

        if (pushId.equalsIgnoreCase(user.getPushId())) {
            return user;
        } else {
            if (Strings.isNullOrEmpty(user.getPushId())) {
                //推送一个退出消息
                PushFactory.pushLogout(user, user.getPushId());
            }

            user.setPushId(pushId);
            return update(user);
        }
    }

    public static User login(String account, String password) {
        final String accountStr = account.trim();
        final String encodePassword = encodePassword(password);
        User user = Hib.query(session -> (User) session.createQuery("from User where phone=:phone and password=:password")
                .setParameter("phone", accountStr)
                .setParameter("password", encodePassword)
                .uniqueResult());

        if (user != null) {
            user = login(user);
        }
        return user;
    }

    /**
     * 用户注册
     *
     * @param account  账户
     * @param password 密码
     * @param name     用户名
     * @return User
     */
    public static User register(String account, String password, String name) {
        account = account.trim();
        password = encodePassword(password);

        User user = createUser(account, password, name);

        if (user != null) {
            user = login(user);
        }
        return user;
    }

    /**
     * 对用户的密码进行加密
     *
     * @param password 用户原本的明文密码
     * @return 加密后的用户密码
     */
    private static String encodePassword(String password) {
        password = password.trim();
        // MD5非对称加密
        password = TextUtil.getMD5(password);
        // 再进行一次对称的Base64加密
        return TextUtil.encodeBase64(password);
    }

    private static User createUser(String account, String password, String name) {
        User user = new User();
        user.setName(name);
        user.setPassword(password);
        user.setPhone(account);

        return Hib.query(session -> {
            session.save(user);
            return user;
        });
    }

    /**
     * 生成新的token,并存储到服务器数据库中
     */
    private static User login(User user) {
        String newToken = UUID.randomUUID().toString();
        newToken = TextUtil.encodeBase64(newToken);
        user.setToken(newToken);

        return update(user);
    }

    /**
     * 获取用户联系人的列表
     */
    public static List<User> contacts(User self) {
        return Hib.query(session -> {
            session.load(self, self.getId());
            Set<UserFollow> flows = self.getFollowing();

            return flows.stream()
                    .map(userFollow -> userFollow.getTarget())
                    .collect(Collectors.toList());
        });
    }

    /**
     * 关注人的操作
     *
     * @param origin 发起者
     * @param target 被关注的人
     * @param alias  备注名
     * @return 被关注人的信息
     */
    public static User follow(final User origin, final User target, final String alias) {
        UserFollow follow = getUserFollow(origin, target);
        // 若已关注,直接返回
        if (follow != null) {
            return follow.getTarget();
        }

        return Hib.query(session -> {
            session.load(origin, origin.getId());
            session.load(target, target.getId());

            // 在 tb_user_follow 表中存储关注信息
            UserFollow originFollow = new UserFollow();
            originFollow.setOrigin(origin);
            originFollow.setTarget(target);
            originFollow.setAlias(alias);

            // 在 tb_user_follow 表中添加一条反关注的信息
            UserFollow targetFollow = new UserFollow();
            targetFollow.setOrigin(target);
            targetFollow.setTarget(origin);

            session.save(originFollow);
            session.save(targetFollow);

            return target;
        });
    }

    /**
     * 根据关注人,被关注人在 tb_user_follow 表中查找数据
     *
     * @param origin 关注人
     * @param target 被关注人
     * @return tb_user_follow 表中关注关系的数据
     */
    public static UserFollow getUserFollow(final User origin, final User target) {
        return Hib.query(session -> (UserFollow) session
                .createQuery("from UserFollow where originId=:originId and targetId=:targetId")
                .setParameter("originId", origin.getId())
                .setParameter("targetId", target.getId())
                .setMaxResults(1)
                .uniqueResult());
    }

    /**
     * 搜索其他人的实现
     *
     * @param name 查询的name, 允许为空
     * @return 查询到的用户集合, 如果name为空, 则返回最近的用户
     */
    public static List<User> search(String name) {
        if (Strings.isNullOrEmpty(name))
            name = "";
        final String searchName = "%" + name + "%";

        // 根据 name 值模糊查找其他人,头像或者描述为空的的用户不返回
        return Hib.query(session -> (List<User>) session.createQuery("from User where lower(name) " +
                "like :name and portrait is not null and description is not null ")
                .setParameter("name", searchName)
                .setMaxResults(20)
                .list());
    }
}
