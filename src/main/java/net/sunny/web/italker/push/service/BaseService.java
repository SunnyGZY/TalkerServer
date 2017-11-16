package net.sunny.web.italker.push.service;

import net.sunny.web.italker.push.bean.db.User;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.SecurityContext;

/**
 * Created by Sunny on 2017/5/24.
 * Email：670453367@qq.com
 * Description: BaseService
 */
public class BaseService {

    @Context
    protected SecurityContext securityContext;

    /**
     * 从上下文中获取User
     *
     * @return UserSelf
     */
    User getSelf() {
        return (User) securityContext.getUserPrincipal();
    }
}
