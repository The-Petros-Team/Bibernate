package com.bobocode.petros.bibernate.persondemo;

import com.bobocode.petros.bibernate.session.Session;
import com.bobocode.petros.bibernate.session.factory.DefaultSessionFactory;
import com.bobocode.petros.bibernate.transaction.Transaction;
import org.postgresql.ds.PGSimpleDataSource;

import java.time.LocalDateTime;
import java.util.Set;

public class DemoApp {
    public static void main(String[] args) {
        /*MysqlDataSource mysql = new MysqlDataSource();
        mysql.setURL("jdbc:mysql://localhost:3306");
        mysql.setUser("root");
        mysql.setPassword("mysql");

        var qm = new DefaultJdbcQueryManager(mysql);

        Person person = new Person();
        person.setFirstName("Oleksii");
        person.setLastName("Talakh");
        person.setCreatedAt(LocalDateTime.now());

        //final Person oleksii = qm.persist(person);

        final Transaction transaction = qm.getTransaction();
        transaction.begin();

        Person p = new Person();
        p.setFirstName("Kirill");
        p.setLastName("Kotenok");
        p.setCreatedAt(LocalDateTime.now());

//        final Person kirill = qm.persist(p);

        transaction.commit();*/

        PGSimpleDataSource postgres = new PGSimpleDataSource();
        postgres.setURL("jdbc:postgresql://localhost:5432/postgres");
        postgres.setUser("postgres");
        postgres.setPassword("postgres");

        final DefaultSessionFactory sf = new DefaultSessionFactory(postgres, Set.of("com.bobocode.petros.bibernate"));
        final Session session = sf.openSession();

        Person person = new Person();
        person.setFirstName("Oleksii");
        person.setLastName("Talakh");
        person.setCreatedAt(LocalDateTime.now());

        // was an issue with authentication, solved by updating a password for user 'postgres'
        // ALTER USER postgres password 'postgres'; And that's it!
        // 1. simple persist works fine
        //final Person persistedEntity = session.persist(person);

        // 2. find by id works fine
        // there might be an issue with scheme, default scheme for postgres -> public
        //final Optional<Person> oleksii = session.findById(Person.class, 1L);

        // 3. find by criteria - works fine
        //session.find(Person.class, "first_name", "Oleksii");

        // 4. update - works fine
        /*person.setId(1L);
        person.setLastName("Talakh updated");
        session.update(person);*/

        // 5. delete - works fine as well as delete by id
        /*person.setId(1L);
        session.delete(person);*/

        // testing manual mode (simple operations)
        final Transaction transaction = session.getTransaction();

        transaction.begin();

        // 1. works correctly
        //final Person persistedEntity = session.persist(person);

        // 2. dirty checking - works fine
        /*final Collection<Person> people = session.find(Person.class, "last_name", "Talakh updated");
        people.iterator().next().setLastName("Talakh updated 2");
        session.flush();*/

        // 3. update
        /*person.setId(5L);
        person.setLastName("super updated");
        session.update(person);*/ // entity is not in the scope of persistence context

        // doesn't work with flush as well -> which is correct BTW -> entity is not in the scope of persistence context
        //session.flush();
        final Person oleksii = session.findById(Person.class, 5L).orElseThrow();
        oleksii.setLastName("super updated last name 123");
        session.update(oleksii); // didn't work without flush - added update action logic

        session.flush(); // flush was performed twice - on commit() phase and if called explicitly - added isFlushed flag
        transaction.commit();
        // on update: works correctly - nothing was saved to db
        //transaction.rollback();

        // on persist: this one works correctly and nothing is saved to db, but a new id was generated for discarded record,
        // is it a valid behavior?
        //transaction.rollback();

    }
}
