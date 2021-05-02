package ru.job4j.hibernate.hql;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.query.Query;

public class HbmRun {
    public static void main(String[] args) {
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure().build();
        try {
            SessionFactory sf = new MetadataSources(registry).buildMetadata().buildSessionFactory();
            Session session = sf.openSession();
            session.beginTransaction();

            Candidate ivan = new Candidate("Ivan", 5, 2070.5f);
            Candidate vadim = new Candidate("Vadim", 7, 3010.1f);
            Candidate john = new Candidate("John", 2, 7565.0f);

            session.save(ivan);
            session.save(vadim);
            session.save(john);

            Query selAllCandidates = session.createQuery("from Candidate ");
            selAllCandidates.list().forEach(System.out::println);

            Query selOneCandidate = session.createQuery("from Candidate c where c.id = :cId");
            selOneCandidate.setParameter("cId", 2);
            System.out.println(">>>selected by id: " + selOneCandidate.getSingleResult());

            selOneCandidate = session.createQuery("from Candidate c where c.name = :cNm");
            selOneCandidate.setParameter("cNm", "John");
            System.out.println(">>>selected by name: " + selOneCandidate.getSingleResult());

            Query updOneCandidate = session.createQuery(
                    "update Candidate c set c.name = :cNm,"
                            + " c.experience = :cExp,"
                            + "c.salary = :cSl where c.id = :cId");
            updOneCandidate.setParameter("cNm", "New Ivan");
            updOneCandidate.setParameter("cExp", 10);
            updOneCandidate.setParameter("cSl", 10000.4f);
            updOneCandidate.setParameter("cId", 1);
            int rsl = updOneCandidate.executeUpdate();
            session.getTransaction().commit();
            System.out.println(">>>candidates updated: " + rsl);

            selOneCandidate = session.createQuery("from Candidate c where c.id = :cId");
            selOneCandidate.setParameter("cId", 1);
            Candidate updatedCandidate = (Candidate) selOneCandidate.getSingleResult();
            session.refresh(updatedCandidate);
            System.out.println(">>>updated candidate: " + updatedCandidate);

            session.getTransaction().begin();
            Query delOneCandidate = session.createQuery(
                    "delete from Candidate c where c.id = :cId"
            );
            delOneCandidate.setParameter("cId", 3);
            delOneCandidate.executeUpdate();

            selOneCandidate = session.createQuery("from Candidate c where c.id = :cId");
            selOneCandidate.setParameter("cId", 3);
            System.out.println(">>>selected candidates after delete: "
                    + selOneCandidate.list().size());

            session.getTransaction().commit();
            session.close();
        }  catch (Exception e) {
            e.printStackTrace();
        } finally {
            StandardServiceRegistryBuilder.destroy(registry);
        }
    }
}
