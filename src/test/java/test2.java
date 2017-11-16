import net.sunny.web.italker.push.bean.db.User;
import net.sunny.web.italker.push.utils.Hib;
import org.hibernate.Session;
import org.junit.Test;

import java.util.List;

/**
 * Created by sunny on 17-8-10.
 */
public class test2 {

    @Test
    public void testOrderBy() {
        // desc 降序 asc 升序
        String hql = "from User order by name asc, createAt asc";
        List<User> users = Hib.query(session ->
                session.createQuery(hql).list());

        for (User user : users) {
            System.out.println("name:" + user.getName());
            System.out.println("createAt:" + user.getCreateAt());
        }
    }

    @Test
    public void testWhere7() {
        String hql = "from User u where u.name = '44'";
        User user = Hib.query(session ->
                (User) session.createQuery(hql).uniqueResult());

        System.out.println("name:" + user.getName());
        System.out.println("token:" + user.getToken());
    }

    // TODO: 17-8-10 原课程代码
//    @Test
//    public void testWhere6() {
//        String hql = "from Commodity c where c.price*5 > 3000";
//
//        List<Commodity> commodities = Hib.query(session ->
//                session.createQuery(hql).list());
//
//        for (Commodity commodity : commodities) {
//            System.out.println("name:" + commodity.getName());
//            System.out.println("sex:" + commodity.getPrice() * 5);
//            System.out.println();
//        }
//    }

    @Test
    public void testWhere5() {
        String hql = "from User u where u.name like '%1' and u.sex = 1 or u.sex = 0";
        List<User> users = Hib.query(session ->
                session.createQuery(hql).list());

        for (User user : users) {
            System.out.println("name:" + user.getName());
            System.out.println("sex:" + user.getSex());
            System.out.println();
        }
    }

    @Test
    public void testWhere4() {
        String hql = "from User u where u.name like '%3%'";
        List<User> list = Hib.query(session ->
                session.createQuery(hql).list());

        for (User user : list) {
            System.out.println("name:" + user.getName());
        }
    }

    @Test
    public void testWhere3() {
        String hql = "from User u where u.sex not between 1 and 2";
//        String hql = "from User u where u.sex between 1 and 2";
//        String hql = "from User u where u.sex not in(1, 0)";
//        String hql = "from User u where u.sex in(1, 0)";
        List<User> users = Hib.query(session ->
                session.createQuery(hql).list());

        for (User user : users) {
            System.out.println(user.getSex());
        }
    }

    @Test
    public void testWhere2() {
        String hql = "from User u where u.pushId is not null";
        // 等同于下面这句,即 is null 等同于 = null，反之亦然
//        String hql = "from User u where u.pushId != null";
        List<User> users = Hib.query(session ->
                session.createQuery(hql).list());

        for (User user : users) {
            System.out.println("name:" + user.getPushId());
        }
    }

    @Test
    public void testWhere1() {
        String hql = "from User u where u.token='YTBkNDQ1MGEtZGQwYi00ZTliLWE3MWMtYmUyMTgyOTdlNjk5'";
        List<User> users = Hib.query(session ->
                session.createQuery(hql).list());

        for (User user : users) {
            System.out.println("name:" + user.getName());
        }
    }
}
