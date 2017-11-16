package net.sunny.web.italker.push.factory;

import net.sunny.web.italker.push.bean.db.track.Taunt;
import net.sunny.web.italker.push.utils.Hib;

public class TauntFactory {
    public static Taunt findByIds(String trackId, String userId) {
        return Hib.query(session ->
                (Taunt) session.createQuery("from Taunt as T where T.trackId=:trackId and T.taunterId=:userId")
                        .setParameter("trackId", trackId)
                        .setParameter("userId", userId)
                        .uniqueResult());
    }
}
