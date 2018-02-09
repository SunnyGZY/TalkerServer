package net.sunny.web.italker.push.factory;

import net.sunny.web.italker.push.bean.api.track.PhotoModel;
import net.sunny.web.italker.push.bean.api.track.TrackCreateModel;
import net.sunny.web.italker.push.bean.api.track.TrackUpdateModel;
import net.sunny.web.italker.push.bean.db.User;
import net.sunny.web.italker.push.bean.db.track.*;
import net.sunny.web.italker.push.utils.Hib;
import org.hibernate.Session;

import java.time.*;
import java.util.*;

/**
 * Created by sunny on 17-8-16.
 * TrackFactory
 */
public class TrackFactory {
    /**
     * 根据Id查询Track
     *
     * @param id Track的Id
     * @return Track
     */
    public static Track findById(String id) {
        return Hib.query(session -> session.get(Track.class, id));
    }

    public static Track add(User self, TrackCreateModel model) {
        Track track = new Track(self, model);

        return save(track);
    }

    public static Track update(User self, Track track, TrackUpdateModel model) {

        Hib.saveOrUpdate(session -> {
            session.load(track, model.getId());
            track.setContent(model.getContent());
            track.setJurisdiction(model.getJurisdiction());
            track.setType(model.getType());

            session.saveOrUpdate(track);
        });

        Set<Photo> photoList = new HashSet<>();

        for (PhotoModel photoModel : model.getPhotos()) {
            Photo photo = new Photo(self, track, photoModel);

            Photo p = Hib.save(session -> {
                session.save(photo);
                session.flush();
                session.refresh(photo);
                return photo;
            });
            photoList.add(p);
        }

        track.setPhotos(photoList);

        return track;
    }

    private static Track save(Track track) {
        return Hib.save(session -> {
            session.save(track);
            session.flush();
            session.refresh(track);
            return track;
        });
    }


    public static List<Track> findSchoolTrack(int pageNo, int pageSize, LocalDateTime dateTime) {
        return Hib.query(session -> session.createQuery("from Track as T where T.createAt<:time order by T.createAt desc")
                .setParameter("time", dateTime)
                .setFirstResult(pageNo * pageSize)
                .setMaxResults(pageSize)
                .list());
    }

    public static List<Track> findFriendTrack(User self, int pageNo, int pageSize, LocalDateTime dateTime) {
        return Hib.query(session -> session.createQuery("from Track as T where T.createAt<:time and T.publisherId " +
                "in(select U.targetId from UserFollow as U where U.originId=:selfId) order by T.createAt desc")
                .setParameter("time", dateTime)
                .setParameter("selfId", self.getId())
                .setFirstResult(pageNo * pageSize)
                .setMaxResults(pageSize)
                .list());
    }

    public static List<Comment> findTrackComment(String trackId) {
        return Hib.query(session -> (List<Comment>) session.createQuery("from Comment as C where C.trackId=:trackId")
                .setParameter("trackId", trackId)
                .list());
    }

    public static int getAfterTimeSchoolTrackCount(LocalDateTime date) {

        Long count = Hib.query(session
                -> (Long) session.createQuery("select count(*) from Track as T where T.createAt>:time and T.jurisdiction=1")
                .setParameter("time", date).uniqueResult());

        return Math.toIntExact(count);
    }

    public static Integer getAfterTimeFriendTrackCount(LocalDateTime date, User self) {
        Long count = Hib.query(session
                -> (Long) session.createQuery("select count(*) from Track as T where T.createAt>:time " +
                "and T.publisherId in(select U.targetId from UserFollow as U where U.originId=:selfId)")
                .setParameter("selfId", self.getId())
                .setParameter("time", date)
                .uniqueResult());

        return Math.toIntExact(count);
    }

    public static Comment saveComment(Comment comment) {
        return Hib.save(session -> {
            session.save(comment);
            session.flush();
            session.refresh(comment);
            return comment;
        });
    }

    public static Compliment saveCompliment(Compliment compliment) {

        return Hib.save(session -> {
            session.save(compliment);
            session.flush();
            session.refresh(compliment);
            return compliment;
        });
    }

    public static Taunt saveTaunt(Taunt taunt) {
        return Hib.save(session -> {
            session.save(taunt);
            session.flush();
            session.refresh(taunt);
            return taunt;
        });
    }

    public static long getComplimentCount(String trackId) {
        return Hib.query(session -> (long) session.createQuery("select count(*) from Compliment as T where T.trackId=:trackId")
                .setParameter("trackId", trackId).uniqueResult());
    }

    public static long getTauntCount(String trackId) {
        return Hib.query(session
                -> (long) session.createQuery("select count(*) from Taunt as T where T.trackId=:trackId")
                .setParameter("trackId", trackId).uniqueResult());
    }

    public static long getCommentCount(String trackId) {
        return Hib.query(session
                -> (long) session.createQuery("select count(*) from Comment as T where T.trackId=:trackId")
                .setParameter("trackId", trackId).uniqueResult());
    }
}
