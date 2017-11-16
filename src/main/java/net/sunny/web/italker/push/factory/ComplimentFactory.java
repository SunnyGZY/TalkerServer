package net.sunny.web.italker.push.factory;

import net.sunny.web.italker.push.bean.db.track.Compliment;
import net.sunny.web.italker.push.utils.Hib;

public class ComplimentFactory {
    public static Compliment findByIds(String trackId, String userId) {
        return Hib.query(session ->
                (Compliment) session.createQuery("from Compliment as C where C.trackId=:trackId and C.complimenterId=:userId")
                        .setParameter("trackId", trackId)
                        .setParameter("userId", userId)
                        .uniqueResult());
    }
}
