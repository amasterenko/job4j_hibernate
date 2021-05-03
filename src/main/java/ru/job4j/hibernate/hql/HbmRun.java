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
        try (SessionFactory sf = new MetadataSources(registry)
                .buildMetadata().buildSessionFactory();
            Session session = sf.openSession()) {

            session.beginTransaction();

            Vacancy vac1 = new Vacancy("Java trainee");
            Vacancy vac2 = new Vacancy("Java junior");
            Vacancy vac3 = new Vacancy("JS junior");
            session.save(vac1);
            session.save(vac2);
            session.save(vac3);

            VacBase base1 = new VacBase("Java vacancies");
            VacBase base2 = new VacBase("JS vacancies");
            base1.addVacancy(vac1);
            base1.addVacancy(vac2);
            base2.addVacancy(vac3);
            session.save(base1);
            session.save(base2);

            Candidate ivan = new Candidate("Ivan", 5, 2070.5f);
            ivan.setVacBase(base1);
            Candidate vadim = new Candidate("Vadim", 7, 3010.1f);
            vadim.setVacBase(base2);
            session.save(ivan);
            session.save(vadim);

            Query selCandidate = session.createQuery("select distinct c from Candidate c "
                    + "join fetch c.vacbase b join fetch b.vacancies "
                    + "where c.id = :cId", Candidate.class);
            selCandidate.setParameter("cId", 1);
            System.out.println(selCandidate.getSingleResult());

            session.getTransaction().commit();
        }  catch (Exception e) {
            e.printStackTrace();
        } finally {
            StandardServiceRegistryBuilder.destroy(registry);
        }
    }
}
