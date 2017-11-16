package net.sunny.web.italker.push.bean.card.track;

import com.google.gson.annotations.Expose;

/**
 * 申请请求的Card, 用于推送一个申请请求
 *
 * @author qiujuer Email:qiujuer.live.cn
 */
public class NewTrackCountCard {

    @Expose
    private int count;

    public NewTrackCountCard(int count) {
        this.count = count;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
