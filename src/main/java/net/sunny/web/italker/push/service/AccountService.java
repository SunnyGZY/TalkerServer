package net.sunny.web.italker.push.service;

import com.google.common.base.Strings;
import net.sunny.web.italker.push.bean.api.account.AccountRspModel;
import net.sunny.web.italker.push.bean.api.account.LoginModel;
import net.sunny.web.italker.push.bean.api.account.RegisterModel;
import net.sunny.web.italker.push.bean.api.base.ResponseModel;
import net.sunny.web.italker.push.bean.db.User;
import net.sunny.web.italker.push.bean.db.track.Photo;
import net.sunny.web.italker.push.bean.db.track.Track;
import net.sunny.web.italker.push.factory.UserFactory;
import net.sunny.web.italker.push.utils.Hib;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

/**
 * Created by Sunny on 2017/5/13.
 * Email：670453367@qq.com
 * Description: 用户账户的注册登录以及pushId绑定
 */

@Path("/account")
public class AccountService extends BaseService {

    @POST
    @Path("/register")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseModel<AccountRspModel> register(RegisterModel model) {

        if (!RegisterModel.check(model)) {
            return ResponseModel.buildParameterError();
        }

        User user = UserFactory.findByPhone(model.getAccount().trim());
        if (user != null) {
            // 已有账户
            return ResponseModel.buildHaveAccountError();
        }

        user = UserFactory.findByName(model.getName().trim());
        if (user != null) {
            // 已有名字
            return ResponseModel.buildHaveNameError();
        }

        // 开始注册逻辑
        user = UserFactory.register(model.getAccount(),
                model.getPassword(), model.getName());
        if (user != null) {
            if (!Strings.isNullOrEmpty(model.getPushId())) {
                return bind(user, model.getPushId());
            }
            AccountRspModel repModel = new AccountRspModel(user);
            return ResponseModel.buildOk(repModel);
        } else {
            return ResponseModel.buildRegisterError();
        }
    }

    @POST
    @Path("/login")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseModel<AccountRspModel> login(LoginModel model) {

        if (!LoginModel.check(model)) {
            return ResponseModel.buildParameterError();
        }

        User user = UserFactory.login(model.getAccount(), model.getPassword());
        if (user != null) {
            if (!Strings.isNullOrEmpty(model.getPushId())) {
                return bind(user, model.getPushId());
            }
            AccountRspModel rspModel = new AccountRspModel(user);
            return ResponseModel.buildOk(rspModel);
        } else {
            return ResponseModel.buildLoginError();
        }
    }

    @POST
    @Path("/bind/{pushId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseModel<AccountRspModel> bind(@PathParam("pushId") String pushId) {

        if (Strings.isNullOrEmpty(pushId)) {
            return ResponseModel.buildParameterError();
        }

        User self = getSelf();
        return bind(self, pushId);
    }

    private ResponseModel<AccountRspModel> bind(User self, String pushId) {

        User user = UserFactory.bindPushId(self, pushId);

        if (user == null) {
            return ResponseModel.buildServiceError();
        }

        AccountRspModel rspModel = new AccountRspModel(user, true);
        return ResponseModel.buildOk(rspModel);
    }
}