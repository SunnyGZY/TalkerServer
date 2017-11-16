package net.sunny.web.italker.push.utils;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

/**
 * Created by qiujuer
 * on 2017/2/17.
 */
public class Hib {
    // 全局SessionFactory
    private static SessionFactory sessionFactory;

    static {
        // 静态初始化sessionFactory
        init();
    }

    private static void init() {
        // 从hibernate.cfg.xml文件初始化
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure() // configures settings from hibernate.cfg.xml
                .build();
        try {
            // build 一个sessionFactory
            sessionFactory = new MetadataSources(registry)
                    .buildMetadata()
                    .buildSessionFactory();
        } catch (Exception e) {
            e.printStackTrace();
            // 错误则打印输出，并销毁
            StandardServiceRegistryBuilder.destroy(registry);
        }
    }

    /**
     * 获取全局的SessionFactory
     *
     * @return SessionFactory
     */
    public static SessionFactory sessionFactory() {
        return sessionFactory;
    }

    /**
     * 从SessionFactory中得到一个Session会话
     *
     * @return Session
     */
    public static Session session() {
        return sessionFactory.getCurrentSession();
    }

    /**
     * 关闭sessionFactory
     */
    public static void closeFactory() {
        if (sessionFactory != null) {
            sessionFactory.close();
        }
    }

    public interface Query<T> {
        T query(Session session);
    }

    public interface QueryOnly {
        void query(Session session);
    }

    public interface Save<T> {
        T save(Session session);
    }

    public interface SaveOnly {
        void save(Session session);
    }

    public interface SaveOrUpdate {
        void saveOrUpdate(Session session);
    }

    /**
     * 查询并返回所查询的数据
     *
     * @param query 接口
     * @param <T>   泛型
     * @return 查询到的数据
     */
    public static <T> T query(Query<T> query) {
        // 重开一个Session
        Session session = sessionFactory.openSession();
        final Transaction transaction = session.beginTransaction();

        T t = null;
        try {
            t = query.query(session);
            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
            try {
                transaction.rollback();
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        } finally {
            session.close();
        }

        return t;
    }

    /**
     * 只查询，不返回数据
     *
     * @param query
     */
    public static void queryOnly(QueryOnly query) {
        // 重开一个Session
        Session session = sessionFactory.openSession();
        final Transaction transaction = session.beginTransaction();
        try {
            query.query(session);
            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
            try {
                transaction.rollback();
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        } finally {
            session.close();
        }
    }

    /**
     * 保存数据并返回保存的数据
     *
     * @param save 接口
     * @param <T>  泛型
     * @return 保存的数据
     */
    public static <T> T save(Save<T> save) {
        // 重开一个Session
        Session session = sessionFactory.openSession();
        final Transaction transaction = session.beginTransaction();

        T t = null;
        try {
            t = save.save(session);
            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
            try {
                transaction.rollback();
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        } finally {
            session.close();
        }

        return t;
    }

    /**
     * 保存数据
     *
     * @param saveOnly 接口
     */
    public static void saveOnly(SaveOnly saveOnly) {
        // 重开一个Session
        Session session = sessionFactory.openSession();
        final Transaction transaction = session.beginTransaction();

        try {
            saveOnly.save(session);
            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
            try {
                transaction.rollback();
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        } finally {
            session.close();
        }
    }

    /**
     * 保存或更新数据
     *
     * @param save 接口
     */
    public static void saveOrUpdate(SaveOrUpdate save) {
        // 重开一个Session
        Session session = sessionFactory.openSession();
        final Transaction transaction = session.beginTransaction();
        try {
            save.saveOrUpdate(session);
            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
            try {
                transaction.rollback();
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        } finally {
            // TODO: 17-8-18  
//            session.clear();
            session.close();
        }
    }
}
