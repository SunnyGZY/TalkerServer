package net.sunny.web.italker.push;

import net.sunny.web.italker.push.provider.AuthRequestFilter;
import net.sunny.web.italker.push.provider.GsonProvider;
import net.sunny.web.italker.push.service.AccountService;
import org.glassfish.jersey.server.ResourceConfig;

import java.util.logging.Logger;

/**
 * Created by Sunny on 2017/5/13.
 * Email：670453367@qq.com
 * Description: TOOD
 */
public class Application extends ResourceConfig{

    public Application() {
        // 初测逻辑处理的包名
        packages(AccountService.class.getPackage().getName());

        // 注册全局请求拦截器
        register(AuthRequestFilter.class);

        // 注册Json解析器
        register(GsonProvider.class);

        // 注册日志打印输出
        register(Logger.class);
    }
}
