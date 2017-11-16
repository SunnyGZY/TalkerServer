package net.sunny.web.italker.push.bean.api.group;

import com.google.gson.annotations.Expose;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Sunny on 2017/6/20.
 * Email：670453367@qq.com
 * Description: 群添加用户model
 */
public class GroupMemberAddModel {

    @Expose
    private Set<String> users = new HashSet<>();

    public Set<String> getUsers() {
        return users;
    }

    public void setUsers(Set<String> users) {
        this.users = users;
    }

    public static boolean check(GroupMemberAddModel model) {
        return !(model.users == null
                || model.users.size() == 0);
    }
}
