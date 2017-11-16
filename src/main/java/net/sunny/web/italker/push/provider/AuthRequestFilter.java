package net.sunny.web.italker.push.provider;

import com.google.common.base.Strings;
import net.sunny.web.italker.push.bean.api.base.ResponseModel;
import net.sunny.web.italker.push.bean.db.User;
import net.sunny.web.italker.push.factory.UserFactory;
import org.glassfish.jersey.server.ContainerRequest;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.security.Principal;

/**
 * Created by Sunny on 2017/5/24.
 * Email：670453367@qq.com
 * Description: 用于所有请求的拦截器
 */
@Provider
public class AuthRequestFilter implements ContainerRequestFilter {

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {

        String relationPath = ((ContainerRequest) requestContext).getPath(false);
        if (relationPath.startsWith("account/login") || relationPath.startsWith("account/register")) {
            return;
        }

        String token = requestContext.getHeaders().getFirst("token");
        if (!Strings.isNullOrEmpty(token)) {
            final User self = UserFactory.findByToken(token);
            if (self != null) {
                // 给当前请求添加一个上下文
                requestContext.setSecurityContext(new SecurityContext() {
                    @Override
                    public Principal getUserPrincipal() {
                        return self;
                    }

                    @Override
                    public boolean isUserInRole(String role) {
                        return true;
                    }

                    @Override
                    public boolean isSecure() {
                        return false;
                    }

                    @Override
                    public String getAuthenticationScheme() {
                        return null;
                    }
                });
                return;
            }
        }

        ResponseModel model = ResponseModel.buildAccountError();
        Response response = Response.status(Response.Status.OK)
                .entity(model)
                .build();

        // 停止一个请求的继续下发
        requestContext.abortWith(response);
    }
}
