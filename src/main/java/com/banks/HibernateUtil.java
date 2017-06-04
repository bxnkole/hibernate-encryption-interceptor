package com.banks;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

import javax.naming.NamingException;
import javax.swing.text.EditorKit;
import java.util.Set;

/**
 * Created by banks on 6/4/17.
 */
public class HibernateUtil {

    private static SessionFactory sessionFactory;

    private static SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            Configuration cfg = new Configuration();
            cfg.setInterceptor(new EncryptionInterceptor());

            StandardServiceRegistryBuilder ssrb = new StandardServiceRegistryBuilder().applySettings(cfg.configure().getProperties());
            sessionFactory = cfg.buildSessionFactory(ssrb.build());
        }
        return sessionFactory;
    }

    private Session getSession() throws HibernateException, NamingException {
        return getSessionFactory().openSession();
    }

    //<editor-fold defaultstate="collapsed" desc="Session and Transaction Cleanup">
    private void closeSession(Session sxn) {
        if (sxn != null) {
            sxn.close();
        }
    }

    private void rollback(Transaction tx) {
        if (tx != null) {
            tx.rollback();
        }
    }

    private void commit(Transaction tx) {
        if (tx != null) {
            tx.commit();
        }
    }
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="CRUD OPERATIONS">
    public <T> boolean create(T t) {
        Session sxn = null;
        Transaction tx = null;
        try {
            sxn = getSession();
            tx = sxn.beginTransaction();
            sxn.save(t);
            commit(tx);
            return true;
        } catch (Exception exception) {
            rollback(tx);
            exception.printStackTrace();
        } finally {
            closeSession(sxn);
        }
        return false;
    }

    public <T> void update(T t) {
        Session sxn = null;
        Transaction tx = null;
        try {
            sxn = getSession();
            tx = sxn.beginTransaction();
            sxn.saveOrUpdate(t);
            commit(tx);
        } catch (Exception exception) {
            rollback(tx);
            exception.printStackTrace();
        } finally {
            closeSession(sxn);
        }

    }

}
