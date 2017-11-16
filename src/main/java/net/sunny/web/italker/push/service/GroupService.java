package net.sunny.web.italker.push.service;

import com.google.common.base.Strings;
import net.sunny.web.italker.push.bean.api.base.ResponseModel;
import net.sunny.web.italker.push.bean.api.group.GroupCreateModel;
import net.sunny.web.italker.push.bean.api.group.GroupMemberAddModel;
import net.sunny.web.italker.push.bean.api.group.GroupUpdateModel;
import net.sunny.web.italker.push.bean.card.ApplyCard;
import net.sunny.web.italker.push.bean.card.GroupCard;
import net.sunny.web.italker.push.bean.card.GroupMemberCard;
import net.sunny.web.italker.push.bean.db.Group;
import net.sunny.web.italker.push.bean.db.GroupMember;
import net.sunny.web.italker.push.bean.db.User;
import net.sunny.web.italker.push.factory.GroupFactory;
import net.sunny.web.italker.push.factory.PushFactory;
import net.sunny.web.italker.push.factory.UserFactory;
import net.sunny.web.italker.push.provider.LocalDateTimeConverter;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by Sunny on 2017/6/20.
 * Email：670453367@qq.com
 * Description: TOOD
 */
@Path("/group")
public class GroupService extends BaseService {

    /**
     * 创建群
     *
     * @param model 群基本参数
     * @return 群信息
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseModel<GroupCard> create(GroupCreateModel model) {
        if (!GroupCreateModel.check(model)) {
            return ResponseModel.buildParameterError();
        }

        User creator = getSelf();
        model.getUsers().remove(creator.getId()); // 移除创建者
        if (model.getUsers().size() == 0)
            return ResponseModel.buildParameterError();

        if (GroupFactory.findByName(model.getName()) != null)
            return ResponseModel.buildParameterError();

        // 拿到Users数据
        List<User> users = new ArrayList<>();
        for (String s : model.getUsers()) {
            User user = UserFactory.findById(s);
            if (user == null)
                continue;
            users.add(user);
        }

        if (users.size() == 0)
            return ResponseModel.buildParameterError();

        Group group = GroupFactory.create(creator, model, users);
        if (group == null)
            return ResponseModel.buildServiceError();

        GroupMember member = GroupFactory.getMember(creator.getId(), group.getId());
        if (member == null)
            return ResponseModel.buildServiceError();

        // 推送消息给其他群成员，告诉他们被邀请至某某群
        Set<GroupMember> members = GroupFactory.getMembers(group);
        if (members == null)
            return ResponseModel.buildServiceError();

        members = members.stream()
                .filter(groupMember -> groupMember.getId().equalsIgnoreCase(member.getId()))
                .collect(Collectors.toSet());

        PushFactory.pushJoinGroup(members);

        return ResponseModel.buildOk(new GroupCard(member));
    }

    /**
     * 查找群，没有传递参数就是搜索最近所有的群
     *
     * @param name 搜索的参数
     * @return 群信息列表
     */
    @GET
    @Path("/search/{name:(.*)?}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseModel<List<GroupCard>> search(@PathParam("name") @DefaultValue("") String name) {
        User self = getSelf();
        List<Group> groups = GroupFactory.search(name);
        if (groups != null && groups.size() > 0) {
            List<GroupCard> groupCards = groups.stream()
                    .map(group -> {
                        GroupMember member = GroupFactory.getMember(self.getId(), group.getId());
                        return new GroupCard(group, member);
                    }).collect(Collectors.toList());
            return ResponseModel.buildOk(groupCards);
        }

        return ResponseModel.buildOk();
    }

    /**
     * 拉取自己当前的群的列表
     *
     * @param dateStr 时间字段，不传递，则会返回全部当前的群列表;有时间，则返回这个时间之后加入的群
     * @return 群信息列表
     */
    @GET
    @Path("/list/{date:(.*)?}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseModel<List<GroupCard>> list(@DefaultValue("") @PathParam("date") String dateStr) {
        User self = getSelf();
        LocalDateTime dateTime = null;
        if (!Strings.isNullOrEmpty(dateStr)) {
            try {
                dateTime = LocalDateTime.parse(dateStr, LocalDateTimeConverter.FORMATTER);
            } catch (Exception e) {
                dateTime = null;
            }
        }

        Set<GroupMember> members = GroupFactory.getMembers(self);
        if (members == null || members.size() == 0)
            return ResponseModel.buildOk();

        LocalDateTime finalDateTime = dateTime;
        List<GroupCard> groupCards = members.stream().
                filter(groupMember -> finalDateTime == null ||
                        groupMember.getUpdateAt().isAfter(finalDateTime))
                .map(GroupCard::new)
                .collect(Collectors.toList());

//        for (GroupMember groupMember : members) {
//            if (finalDateTime == null
//                    || groupMember.getUpdateAt().isAfter(finalDateTime)) {
//                GroupCard groupCard = new GroupCard(groupMember);
//                groupCards.add(groupCard);
//            }
//        }

        return ResponseModel.buildOk(groupCards);
    }

    /**
     * 获取一个群的信息,请求者必须是这个群的成员
     *
     * @param groupId 群的Id
     * @return 群的信息
     */
    @GET
    @Path("/{groupId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseModel<GroupCard> getGroup(@PathParam("groupId") String groupId) {
        if (Strings.isNullOrEmpty(groupId))
            return ResponseModel.buildParameterError();

        User self = getSelf();
        GroupMember member = GroupFactory.getMember(self.getId(), groupId);
        if (member == null)
            return ResponseModel.buildNotFoundGroupError(null);

        return ResponseModel.buildOk(new GroupCard(member));
    }

    /**
     * 拉取一个群的所有成员,用户必须是该群成员之一
     *
     * @param groupId 群Id
     * @return 群成员列表
     */
    @GET
    @Path("/{groupId}/members")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseModel<List<GroupMemberCard>> members(@PathParam("groupId") String groupId) {
        User self = getSelf();
        Group group = GroupFactory.findById(groupId);
        if (group == null)
            return ResponseModel.buildNotFoundGroupError(null);

        GroupMember selfMember = GroupFactory.getMember(self.getId(), groupId);
        if (selfMember == null)
            ResponseModel.buildNoPermissionError();

        Set<GroupMember> members = GroupFactory.getMembers(group);
        if (members == null)
            return ResponseModel.buildServiceError();

        List<GroupMemberCard> memberCards = members
                .stream()
                .map(GroupMemberCard::new).collect(Collectors.toList());
        return ResponseModel.buildOk(memberCards);
    }

    /**
     * 给群添加成员的接口
     *
     * @param groupId 群Id，用户必须是这个群的管理者之一
     * @param model   添加成员的model
     * @return 添加的成员列表
     */
    @POST
    @Path("/{groupId}/member")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseModel<List<GroupMemberCard>> memberAdd(@PathParam("groupId") String groupId, GroupMemberAddModel model) {
        if (Strings.isNullOrEmpty(groupId) || !GroupMemberAddModel.check(model))
            ResponseModel.buildParameterError();

        User self = getSelf();
        model.getUsers().remove(self.getId());
        if (model.getUsers().size() == 0)
            return ResponseModel.buildParameterError();

        Group group = GroupFactory.findById(groupId);
        if (group == null)
            return ResponseModel.buildNotFoundGroupError(null);

        GroupMember selfMember = GroupFactory.getMember(self.getId(), groupId);
        if (selfMember == null || selfMember.getPermissionType() == GroupMember.PERMISSION_TYPE_NONE)
            return ResponseModel.buildNoPermissionError();

        Set<GroupMember> oldMembers = GroupFactory.getMembers(group);
        Set<String> oldMemberUserIds = oldMembers.stream()
                .map(GroupMember::getUserId)
                .collect(Collectors.toSet());

        List<User> insertUsers = new ArrayList<>();
        for (String s : model.getUsers()) {
            User user = UserFactory.findById(s);
            if (user == null)
                continue;
            if (oldMemberUserIds.contains(user.getId()))
                continue;

            insertUsers.add(user);
        }

        if (insertUsers.size() == 0)
            return ResponseModel.buildParameterError();

        Set<GroupMember> insertMembers = GroupFactory.addMembers(group, insertUsers);
        if (insertMembers == null)
            return ResponseModel.buildServiceError();

        List<GroupMemberCard> insertCards = insertMembers.stream()
                .map(GroupMemberCard::new)
                .collect(Collectors.toList());

        // 通知新增的成员，你被加入了XXX群
        PushFactory.pushJoinGroup(insertMembers);

        // 通知群中老成员，有XXX加入了群
        PushFactory.pushGroupMemberAdd(oldMembers, insertCards);

        return ResponseModel.buildOk(insertCards);
    }

    /**
     * 更改成员信息，请求的人要么是管理员要么是成员本人
     *
     * @param memberId 成员Id，可以查询对应的群和人
     * @param model    修改的Model
     * @return 当前成员的信息
     */
    @PUT
    @Path("/member/{memberId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseModel<GroupCard> modifyMember(@PathParam("memberId") String memberId, GroupUpdateModel model) {
        return null;
    }

    /**
     * 用户申请加入一个群
     * 此时会创建一个加入的申请，并写入表，然后会给管理员发送消息
     *
     * @param groupId 群Id
     * @return 申请的信息
     */
    @POST
    @Path("/applyJoin/{groupId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseModel<ApplyCard> join(@PathParam("groupId") String groupId) {
        return null;
    }
}
