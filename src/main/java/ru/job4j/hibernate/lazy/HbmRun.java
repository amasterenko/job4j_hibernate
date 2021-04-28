package ru.job4j.hibernate.lazy;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import java.util.ArrayList;
import java.util.List;

public class HbmRun {
    private final static StandardServiceRegistry REGISTRY = new StandardServiceRegistryBuilder()
            .configure().build();
    private final static SessionFactory SF = new MetadataSources(REGISTRY)
            .buildMetadata().buildSessionFactory();

    public static void main(String[] args) {
        createMakesAndModels();
        showModelsWithInTheSession();
        showModelsWithJoinFetchQuery();
    }

    public static void createMakesAndModels() {
        try (Session session = SF.openSession()) {
            session.beginTransaction();
            Make ford = new Make("Ford");
            Make bmw = new Make("BMW");

            Model model1 = new Model("Mustang");
            model1.setMake(ford);

            Model model2 = new Model("Explorer");
            model2.setMake(ford);

            Model model3 = new Model("Z4");
            model3.setMake(bmw);

            session.persist(ford);
            session.persist(bmw);

            session.persist(model1);
            session.persist(model2);
            session.persist(model3);
            session.getTransaction().commit();
        }  catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void showModelsWithInTheSession() {
        try (Session session = SF.openSession()) {
            session.beginTransaction();
            List<Make> makes = session.createQuery("from Make").list();
            for (Make make : makes) {
                System.out.println(">>>Models of " + make + " :");
                for (Model model : make.getModels()) {
                    System.out.println(">" + model);
                }
            }
            session.getTransaction().commit();
        }  catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void showModelsWithJoinFetchQuery() {
        List<Make> makes = new ArrayList<>();
        try (Session session = SF.openSession()) {
            session.beginTransaction();
            makes = session.createQuery(
                    "select distinct c from Make c join fetch c.models"
            ).list();

            session.getTransaction().commit();
        }  catch (Exception e) {
            e.printStackTrace();
        }
        for (Make make : makes) {
            System.out.println(">>>Models of " + make + " :");
            for (Model model : make.getModels()) {
                System.out.println(">" + model);
            }
        }
    }
}
