package com.example;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.query.Query;

import java.util.List;

public class Main {

    private SessionFactory sessionFactory;

    private void doit() {
        createSession();
        Session session = sessionFactory.openSession();
        session.beginTransaction();

        List result = session.createQuery("select s from Student s").list();

        for(Student student : (List<Student>) result){
            System.out.println(student);
        }

        result = session.createQuery("select s from Student s where s.name like :name").setParameter("name","%ee%" ).list();

        for(Student student : (List<Student>) result){
            System.out.println(student);
        }


        result = session.createQuery("select s from Student s where s.id < :id").setParameter("id",5 ).list();

        for(Student student : (List<Student>) result){
            System.out.println(student);
        }

//        Student student = new Student();
//        student.setName("Zebra");
//        session.save(student);

        Student student = (Student) session.createQuery("select s from Student s where s.id = :id").setParameter("id",20).uniqueResult();

        System.out.println(student);

        student.setName("Cool Spot " + System.currentTimeMillis());
        session.update(student);

        student = (Student) session.createQuery("select s from Student s where s.id = :id").setParameter("id",20).uniqueResult();

        System.out.println(student);


        session.getTransaction().commit();
        session.close();
        endSession();

    }

    private void createSession() {
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder().configure("school.cfg.xml").build();
        try {
            sessionFactory = new MetadataSources(registry).buildMetadata().buildSessionFactory();
        } catch (Exception e) {
            System.out.println("#Error " + e);
            StandardServiceRegistryBuilder.destroy(registry);

        }
    }

    private void endSession(){
        sessionFactory.close();
    }

    public static void main(String[] args) {
        Main main = new Main();
        main.doit();
    }
}
