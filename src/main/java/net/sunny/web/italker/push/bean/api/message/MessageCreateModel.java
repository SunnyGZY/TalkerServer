package net.sunny.web.italker.push.bean.api.message;

import com.google.common.base.Strings;
import com.google.gson.annotations.Expose;
import net.sunny.web.italker.push.bean.db.Message;

/**
 * Created by Sunny on 2017/6/14.
 * Email：670453367@qq.com
 * Description: API请求的Model格式
 */
public class MessageCreateModel {

    // 客户端生成一个UUID
    @Expose
    private String id;
    @Expose
    private String content;
    // 附件
    @Expose
    private String attach;
    @Expose
    private int type = Message.TYPE_STR;
    @Expose
    private String receiverId;
    // 接收者类型，群或人
    @Expose
    private int receiverType;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAttach() {
        return attach;
    }

    public void setAttach(String attach) {
        this.attach = attach;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }

    public int getReceiverType() {
        return receiverType;
    }

    public void setReceiverType(int receiverType) {
        this.receiverType = receiverType;
    }

    public static boolean check(MessageCreateModel model) {
        return model != null
                && !(Strings.isNullOrEmpty(model.id)
                || Strings.isNullOrEmpty(model.receiverId)
                || Strings.isNullOrEmpty(model.content))

                && (model.receiverType == Message.RECEIVER_TYPE_NONE
                || model.receiverType == Message.RECEIVER_TYPE_GROUP)

                && (model.type == Message.TYPE_STR
                || model.type == Message.TYPE_AUDIO
                || model.type == Message.TYPE_FILE
                || model.type == Message.TYPE_PIC);
    }
}
