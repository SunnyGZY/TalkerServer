package net.sunny.web.italker.push.bean.db;

import net.sunny.web.italker.push.bean.api.message.MessageCreateModel;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * Created by Sunny on 2017/5/21.
 * Email：670453367@qq.com
 * Description: TOOD
 */

@Entity
@Table(name = "TB_MESSAGE")
public class Message {

    // 发送给人
    public static final int RECEIVER_TYPE_NONE = 0x01;
    public static final int RECEIVER_TYPE_GROUP = 0x02;
    public static final int TYPE_STR = 0x01;
    public static final int TYPE_PIC = 0x02;
    public static final int TYPE_FILE = 0x03;
    public static final int TYPE_AUDIO = 0x04;

    @Id
    @PrimaryKeyJoinColumn
    //这里不自动生成UUID,Id由代码写入,由客户端负责生成
    //避免复杂的映射关系
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @Column(updatable = false, nullable = false)
    private String id;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    // 附件
    @Column
    private String attach;

    @Column(nullable = false)
    private int type;

    @CreationTimestamp
    @Column(nullable = false)
    private LocalDateTime createAt = LocalDateTime.now();

    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updateAt = LocalDateTime.now();

    @JoinColumn(name = "senderId")
    @ManyToOne(optional = false)
    private User sender;
    @Column(nullable = false, updatable = false, insertable = false)
    private String senderId;

    @ManyToOne
    @JoinColumn(name = "receiverId")
    private User receiver;
    @Column(updatable = false, insertable = false)
    private String receiverId;

    @ManyToOne
    @JoinColumn(name = "groupId")
    private Group group;
    @Column(updatable = false, insertable = false)
    private String groupId;

    public Message() {
    }

    public Message(User sender, User receiver, MessageCreateModel model) {
        this.id = model.getId();
        this.content = model.getContent();
        this.attach = model.getAttach();
        this.type = model.getType();
        this.sender = sender;
        this.receiver = receiver;
    }

    public Message(User sender, Group group, MessageCreateModel model) {
        this.id = model.getId();
        this.content = model.getContent();
        this.attach = model.getAttach();
        this.type = model.getType();
        this.sender = sender;
        this.group = group;
    }

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

    public LocalDateTime getCreateAt() {
        return createAt;
    }

    public void setCreateAt(LocalDateTime createAt) {
        this.createAt = createAt;
    }

    public LocalDateTime getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(LocalDateTime updateAt) {
        this.updateAt = updateAt;
    }

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public User getReceiver() {
        return receiver;
    }

    public void setReceiver(User receiver) {
        this.receiver = receiver;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }

    public static int getTypeStr() {
        return TYPE_STR;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }
}