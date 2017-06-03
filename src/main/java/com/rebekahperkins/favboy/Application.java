package com.rebekahperkins.favboy;

import com.rebekahperkins.favboy.model.Boy;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.service.ServiceRegistry;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import java.util.List;

public class Application {
    private static final SessionFactory sessionFactory = buildSessionFactory();

    private static SessionFactory buildSessionFactory() {
      final ServiceRegistry registry = new StandardServiceRegistryBuilder().configure().build();
      return new MetadataSources(registry).buildMetadata().buildSessionFactory();
    }

    public static void main(String[] args) {
      Boy boy = new Boy("Louie",6);
      int id = save(boy);

      System.out.println("Before update");
      fetchBoys().stream().forEach(System.out::println);

      Boy b = findById(id);
      b.setRank(8);
      update(b);

      System.out.println("After update");
      fetchBoys().stream().forEach(System.out::println);
    }

    //    @SuppressWarnings("unchecked")
    private static List<Boy> fetchBoys(){
        Session session = sessionFactory.openSession();

        //deprecated in Hibernate 5.2
        //        Criteria c = session.createCriteria(Boy.class);
        //        List<Boy> boys = c.list();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<Boy> cq = cb.createQuery(Boy.class);
        cq.from(Boy.class);
        List<Boy> boys = session.createQuery(cq).getResultList();

        session.close();
        return boys;
    }

    private static int save(Boy boy){
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        int id = (int)session.save(boy);
        session.getTransaction().commit();
        session.close();
        return id;
    }

    private static Boy findById(int id){
        Session session = sessionFactory.openSession();
        Boy boy = session.get(Boy.class, id);
        session.close();
        return boy;
    }

    private static void update(Boy boy){
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        session.update(boy);
        session.getTransaction().commit();
        session.close();
    }

    private static void delete(Boy boy){
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        session.delete(boy);
        session.getTransaction().commit();
        session.close();
    }
}
