import net.sunny.web.italker.push.bean.db.User;

import net.sunny.web.italker.push.bean.db.track.Comment;
import net.sunny.web.italker.push.factory.TrackFactory;
import net.sunny.web.italker.push.utils.DateTimeUtil;
import net.sunny.web.italker.push.utils.Hib;
import org.junit.Test;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

/**
 * Created by sunny on 17-8-9.
 */
public class test {

    @Test
    public void testDistinct() {
        String hql = "select distinct u.sex from User u";
        List list = Hib.query(session
                -> session.createQuery(hql).list());

        for (Object o : list) {
            System.out.println(o);
        }
    }

    @Test
    public void testSelectClauseSelf() {
        String hql = "select new User(u.name, u.createAt) from User u";
        List<User> users = Hib.query(session
                -> session.createQuery(hql).list());

        for (User user : users) {
            System.out.println("name:" + user.getName());
            System.out.println("createAt:" + user.getCreateAt());
        }
    }

    @Test
    public void testSelectClauseMap() {
        String hql = "select new Map(u.name as name, u.createAt as createAt) from User u";
        List<Map> maps = Hib.query(session
                -> session.createQuery(hql).list());

        for (Map map : maps) {
            System.out.println("name:" + map.get("name"));
            System.out.println("createAt:" + map.get("createAt"));
        }
    }

    @Test
    public void testSelectClauseList() {
        String hql = "select new list(u.name, u.createAt) from User u";
        List<List> lists = Hib.query(session
                -> session.createQuery(hql).list());

        for (List list : lists) {
            System.out.println("name:" + list.get(0));
            System.out.println("createAt:" + list.get(1));
            System.out.println();
        }
    }

    @Test
    public void testSelectClauseObjectArray() {
        String hql = "select name from User";
        List<Object> list = Hib.query(session ->
                session.createQuery(hql).list());

        for (Object objects : list) {
            System.out.println("name:" + objects);
            System.out.println();
        }
    }

    @Test
    public void testHql() {
        String hql = "from User as user";
        List<User> list = Hib.query((Hib.Query<List>) session
                -> session.createQuery(hql).list());

        for (User user : list) {
            LocalDateTime localDateTime = user.getCreateAt();
            ZoneId zone = ZoneId.of("Asia/Shanghai");
            Instant instant = localDateTime.atZone(zone).toInstant();
            Date date = Date.from(instant);
            System.out.println(date);
        }
    }

    @Test
    public void test() {
        LocalDateTime date = LocalDateTime.now();
        System.out.println(date.toString());
        Long count = (Long) Hib.query(session -> (Long) session.createQuery("select count(*) from Track as T where T.createAt>:time")
                .setParameter("time", date).uniqueResult());

        System.out.println(String.valueOf(count));
    }

    @Test
    public void test2() {
        String strTime = "2017-10-09 15:55:13.600";

        Date date = DateTimeUtil.getIntactData(strTime);
        Instant instant = date.toInstant();
        ZoneId zone = ZoneId.systemDefault();

        LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, zone);

        Long count = Hib.query(session                  //注: T.createAt=2017-10-09T06:30:25.260  time=2017-10-09 06:30:25.260000
                -> (Long) session.createQuery("select count(*) from Track as T where T.createAt>:time and T.jurisdiction=1")
                .setParameter("time", localDateTime).uniqueResult());

        System.out.println("-------------- " + String.valueOf(count));
    }

    @Test
    public void test3() {
        int max = 100;
        int min = 0;
        int stepCount = 1000000;

        long count = 0;

        Random random = new Random();

        for (int i = stepCount; i > 0; i--) {
            int x = random.nextInt(max) % (max - min + 1) + min;
            int y = random.nextInt(max) % (max - min + 1) + min;

            double d = Math.sqrt((50 - x) * (50 - x) + (50 - y) * (50 - y)); //与圆心的距离
            if (d <= 50) {
                count++;
            }

            double area = 10000 * count / (stepCount - i);
            double pi = area / 2500;
            System.out.println("Pi = " + String.valueOf(pi));
            System.out.println("计算还剩" + i + "步");
            System.out.println();
        }

        double area = 10000 * count / stepCount;
        double pi = area / 2500;
        System.out.println(String.valueOf(pi));
        double pi_p = 3.1415926535898;
        double error = pi_p - pi;
        System.out.println("误差：" + error);
    }

    @Test
    public void test4() {
        List<Comment> commentList = TrackFactory.findTrackComment("851fb1a1-d3e3-4b22-b2ae-f4c38fc60e9a");
        for (Comment comment : commentList) {
            System.out.println(comment.toString());
        }
    }

    @Test
    public void test5() {
        List<Integer> list = new ArrayList();
        list.add(1);
        list.add(2);
        list.add(3);
        list.add(4);
        for (Integer o : list) {
            if (o == 2)
                list.remove(o);
        }

        for (Integer integer : list) {
            System.out.println(integer);
        }
    }
}
