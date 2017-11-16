package net.sunny.web.italker.push.utils;

import com.gexin.rp.sdk.base.IBatch;
import com.gexin.rp.sdk.base.IPushResult;
import com.gexin.rp.sdk.base.impl.SingleMessage;
import com.gexin.rp.sdk.base.impl.Target;
import com.gexin.rp.sdk.http.IGtPush;
import com.gexin.rp.sdk.template.TransmissionTemplate;
import com.google.common.base.Strings;
import net.sunny.web.italker.push.bean.api.base.PushModel;
import net.sunny.web.italker.push.bean.db.User;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Sunny on 2017/6/13.
 * Email：670453367@qq.com
 * Description: 消息推送共工具类
 */
public class PushDispatcher {
    private static final String appId = "WCriwFNA4UAOjbxViOAtp5";
    private static final String appKey = "unx4sg2om96wmLJlaUjG44";
    private static final String masterSecret = "PKNOtGfAaP8qJwmhqQwf83";
    private static final String host = "http://sdk.open.api.igexin.com/apiex.htm";

    private final IGtPush pusher;
    private List<BatchBean> beans = new ArrayList<>();

    public PushDispatcher() {
        pusher = new IGtPush(host, appKey, masterSecret);
    }

    /**
     * 将PushModel添加到BatchBean中
     *
     * @param receiver 接收者
     * @param model 发送的消息
     * @return true 添加成功，false 添加失败
     */
    public boolean add(User receiver, PushModel model) {
        if (receiver == null || model == null || Strings.isNullOrEmpty(receiver.getPushId()))
            return false;

        String pushString = model.getPushString();
        if (Strings.isNullOrEmpty(pushString))
            return false;

        BatchBean bean = buildMessage(receiver.getPushId(), pushString);
        beans.add(bean);
        return true;
    }

    /**
     * 对要发送的数据进行格式化封装
     *
     * @param clientId 接收者的设备Id
     * @param text     要接收的数据
     * @return BatchBean
     */
    private BatchBean buildMessage(String clientId, String text) {
        TransmissionTemplate template = new TransmissionTemplate();
        template.setAppId(appId);
        template.setAppkey(appKey);
        template.setTransmissionContent(text);
        template.setTransmissionType(0);
        SingleMessage message = new SingleMessage();
        message.setData(template);
        message.setOffline(true);
        message.setOfflineExpireTime(24 * 3600 * 1000);

        Target target = new Target();
        target.setAppId(appId);
        target.setClientId(clientId);

        return new BatchBean(message, target);
    }

    /**
     * 进行消息最终发送
     */
    public boolean submit() {
        IBatch batch = pusher.getBatch();
        boolean haveData = false;
        for (BatchBean bean : beans) {
            try {
                batch.add(bean.message, bean.target);
                haveData = true;
            } catch (Exception e) {
                e.printStackTrace();
                try {
                    batch.retry();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }

        if (!haveData)
            return false;
        IPushResult result = null;
        try {
            result = batch.submit();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (result != null) {
            try {
                Logger.getLogger("PushDispatcher")
                        .log(Level.INFO, (String) result.getResponse().get("result"));
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        Logger.getLogger("PushDispatcher")
                .log(Level.WARNING, "推送服务器响应异常");
        return false;
    }

    /**
     * 给每个人发送消息的一个Bean封装
     */
    private static class BatchBean {
        SingleMessage message;
        Target target;

        public BatchBean(SingleMessage message, Target target) {
            this.message = message;
            this.target = target;
        }
    }
}
