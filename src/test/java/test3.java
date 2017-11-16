import net.sunny.web.italker.push.bean.db.User;
import net.sunny.web.italker.push.utils.DateTimeUtil;
import net.sunny.web.italker.push.utils.Hib;
import org.junit.Test;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

/**
 * Created by sunny on 17-8-10.
 */
public class test3 {

    @Test
    public void testWhere1() {
        String hql = "from User u where u.following is empty";
        List<User> users = Hib.query(session ->
                session.createQuery(hql).list());

        for (User user : users) {
            System.out.println("name:" + user.getName());
//            System.out.println("following_size:" + user.getFollowing().size());
            System.out.println();
        }
    }

    @Test
    public void test() {
        // 2017-09-29T07:03:21.546

        Date date = DateTimeUtil.getIntactData("2017-09-29T07:03:21.546");

        Instant instant = date.toInstant();
        ZoneId zone = ZoneId.systemDefault();

        LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, zone);

        Long count = Hib.query(session
                -> (Long) session.createQuery("select count(*) from Track as T where T.createAt>:time and T.jurisdiction=1")
                .setParameter("time", localDateTime).uniqueResult());

        System.out.println(String.valueOf(count));
    }
}
