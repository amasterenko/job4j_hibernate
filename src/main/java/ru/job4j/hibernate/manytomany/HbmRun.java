package ru.job4j.hibernate.manytomany;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

public class HbmRun {
    public static void main(String[] args) {
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure().build();
        try {
            SessionFactory sf = new MetadataSources(registry).buildMetadata().buildSessionFactory();
            Session session = sf.openSession();
            session.beginTransaction();

            Book book1 = new Book("Head First Java");
            Book book2 = new Book("Effective Java");
            Book book3 = new Book("Java-The complete reference");
            Book book4 = new Book("Java Programming Cookbook");

            Author author1 = new Author("Kathy Sierra");
            Author author2 = new Author("Bert Bates");
            Author author3 = new Author("Joshua Bloch");
            Author author4 = new Author("Herbert Schildt");

            book1.addAuthor(author1);
            book1.addAuthor(author2);
            book2.addAuthor(author3);
            book3.addAuthor(author4);
            book4.addAuthor(author4);

            session.persist(book1);
            session.persist(book2);
            session.persist(book3);
            session.persist(book4);

            Book book = session.get(Book.class, 1);
            session.remove(book);
            book = session.get(Book.class, 3);
            session.remove(book);

            session.getTransaction().commit();
            session.close();

        }  catch (Exception e) {
            e.printStackTrace();
        } finally {
            StandardServiceRegistryBuilder.destroy(registry);
        }
    }
}
