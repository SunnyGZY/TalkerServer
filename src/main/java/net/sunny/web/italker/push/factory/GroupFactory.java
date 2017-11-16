package net.sunny.web.italker.push.factory;

import com.google.common.base.Strings;
import net.sunny.web.italker.push.bean.api.group.GroupCreateModel;
import net.sunny.web.italker.push.bean.db.Group;
import net.sunny.web.italker.push.bean.db.GroupMember;
import net.sunny.web.italker.push.bean.db.User;
import net.sunny.web.italker.push.utils.Hib;
import org.hibernate.Session;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Sunny on 2017/6/15.
 * Email：670453367@qq.com
 * Description: 群数据库处理
 */
public class GroupFactory {
    /**
     * @param groupId
     * @return
     */
    public static Group findById(String groupId) {
        return Hib.query(session -> session.get(Group.class, groupId));
    }

    /**
     * 查询一个群，同时这个人必须是群成员
     *
     * @param user
     * @param groupId
     * @return
     */
    public static Group findById(User user, String groupId) {
        GroupMember member = getMember(user.getId(), groupId);
        if (member != null)
            return member.getGroup();

        return null;
    }

    /**
     * 获取一个群的所有成员
     *
     * @param group
     * @return
     */
    public static Set<GroupMember> getMembers(Group group) {
        return Hib.query(session -> {
            List<GroupMember> members = session.createQuery("from GroupMember where group=:group")
                    .setParameter("group", group)
                    .list();

            return new HashSet<>(members);
        });
    }

    public static Set<GroupMember> getMembers(User user) {
        return Hib.query(session -> {
            List<GroupMember> members = session.createQuery("from GroupMember where userId=:userId")
                    .setParameter("userId", user.getId())
                    .list();

            return new HashSet<>(members);
        });
    }

    public static Group findByName(String name) {
        return Hib.query(session -> (Group) session.createQuery("from Group where lower(name)=:name ")
                .setParameter("name", name.toLowerCase())
                .uniqueResult());
    }

    /**
     * 创建群
     *
     * @param creator
     * @param model
     * @param users
     * @return
     */
    public static Group create(final User creator, GroupCreateModel model, List<User> users) {
        return Hib.query(session -> {
            Group group = new Group(creator, model);
            session.save(group);

            GroupMember ownerMember = new GroupMember(creator, group);
            ownerMember.setPermissionType(GroupMember.PERMISSION_TYPE_ADMIN_SU);
            session.save(ownerMember);
            for (User user : users) {
                GroupMember member = new GroupMember(user, group);
                session.save(member);
            }
            return group;
        });
    }

    /**
     * 获取一个群的成员
     *
     * @param userId
     * @param groupId
     * @return
     */
    public static GroupMember getMember(String userId, String groupId) {
        return Hib.query(session ->
                (GroupMember) session.createQuery("from GroupMember where userId=:userId and groupId=:groupId")
                        .setParameter("userId", userId)
                        .setParameter("groupId", groupId)
                        .setMaxResults(1)
                        .uniqueResult());
    }

    /**
     * 查询
     *
     * @param name 群名称
     * @return 查询到的群集合
     */
    public static List<Group> search(String name) {
        if (Strings.isNullOrEmpty(name))
            name = "";
        final String searchName = "%" + name + "%";
        return Hib.query(session -> (List<Group>) session.createQuery("from Group where lower(name) like:name")
                .setParameter("name", searchName)
                .setMaxResults(20)
                .list());
    }

    /**
     * 群添加成员
     *
     * @param group       群
     * @param insertUsers 需要添加到群的用户集合
     * @return 添加到群的用户集合
     */
    public static Set<GroupMember> addMembers(Group group, List<User> insertUsers) {
        Set<GroupMember> members = new HashSet<>();

        return Hib.query(session -> {
            for (User user : insertUsers) {
                GroupMember member = new GroupMember(user, group);
                session.save(member);
                members.add(member);
            }
            return members;
        });
    }
}
