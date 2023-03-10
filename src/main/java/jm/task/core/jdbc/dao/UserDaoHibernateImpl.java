package jm.task.core.jdbc.dao;

import com.mysql.cj.jdbc.DatabaseMetaData;
import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class UserDaoHibernateImpl implements UserDao {

    @Override
    public void createUsersTable() {
        Transaction transaction = null;
        try (Session session = Util.getSessionFactory().openSession()){
            transaction = session.beginTransaction();
            String sql = "CREATE TABLE IF NOT EXISTS users" +
                    "(id BIGINT PRIMARY KEY AUTO_INCREMENT, name VARCHAR(50), lastName VARCHAR(50), age INT)";
            session.createSQLQuery(sql).executeUpdate();
            transaction.commit();
            System.out.println("Таблица создана");
        } catch (HibernateException e) {
            e.printStackTrace();
            if (transaction != null) {
                transaction.rollback();
                System.out.println("Не удалось создать таблицу");
            }
        }

    }

    @Override
    public void dropUsersTable(String nameTable) {
        Transaction transaction = null;
        try (Session session = Util.getSessionFactory().openSession()){
            transaction = session.beginTransaction();
            String sql = "DROP TABLE IF EXISTS "+nameTable+"";
            session.createSQLQuery(sql).executeUpdate();
            transaction.commit();
            System.out.println("Таблица удалена");
        } catch (HibernateException e) {
            e.printStackTrace();
            if (transaction != null) {
                transaction.rollback();
                System.out.println("Таблицу удалить не удалось");
            }
        }
    }

    @Override
    public void saveUser(String name, String lastName, byte age) {
        Transaction transaction = null;
        try (Session session = Util.getSessionFactory().openSession()){
            transaction = session.beginTransaction();
            String sql = "INSERT INTO users (name, lastName, age) VALUES ('"+name+"','"+lastName+"','"+age+"')";
            session.createSQLQuery(sql).executeUpdate();
            transaction.commit();
            System.out.println("User с именем – " + name + " добавлен в базу данных");
        } catch (HibernateException e) {
            e.printStackTrace();
            if (transaction != null) {
                transaction.rollback();
                System.out.println("Не удалось добавить в базу User с именем – " + name);
            }
        }
    }

    @Override
    public void removeUserById(long id) {
        Transaction transaction;
        try (Session session = Util.getSessionFactory().openSession()){
            transaction = session.beginTransaction();
            String sql = "DELETE FROM users WHERE id = "+id+"";
            session.createSQLQuery(sql).executeUpdate();
            transaction.commit();
            System.out.println("User удален");
        } catch (HibernateException e) {
            e.printStackTrace();
            System.out.println("Не удалось удалить User");
        }
    }

    @Override
    public List<User> getAllUsers() {
        List<User> userList = new ArrayList<>();
        Transaction transaction5;
        try (Session session = Util.getSessionFactory().openSession()) {
            transaction5 = session.beginTransaction();
            userList = session.createQuery("FROM User").list();
            transaction5.commit();
            System.out.println("Список User-ов успешно получен.");
            return userList;
        } catch (HibernateException e) {
            throw new HibernateException("Не удалось получить User-ов", e);
        }
    }

    @Override
    public void cleanUsersTable() {
        Transaction transaction = null;
        try (Session session = Util.getSessionFactory().openSession()){
            transaction = session.beginTransaction();
            String sql = "TRUNCATE TABLE users";
            session.createSQLQuery(sql).executeUpdate();
            transaction.commit();
            System.out.println("Таблица очищена");
        } catch (HibernateException e) {
            e.printStackTrace();
            if (transaction != null) {
                transaction.rollback();
                System.out.println("Таблица не очищена");
            }
        }
    }
}
