package com.bobocode.petros.bibernate;

import com.bobocode.petros.bibernate.entity.Person;
import com.bobocode.petros.bibernate.session.jdbc.DefaultJdbcQueryManager;
import com.bobocode.petros.bibernate.session.jdbc.JdbcQueryManager;
import com.bobocode.petros.bibernate.session.query.condition.Restrictions;
import com.mysql.cj.jdbc.MysqlDataSource;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

public class DemoApp {
    public static void main(String[] args) {


        MysqlDataSource ds = new MysqlDataSource();
        ds.setURL("jdbc:mysql://localhost:3306");
        ds.setUser("root");
        ds.setPassword("mysql");
        JdbcQueryManager qm = new DefaultJdbcQueryManager(ds);

        /*Person person = new Person();
        person.setFirstName("Vlad");
        person.setLastName("Romantsev");

        final Person persistedPerson = qm.persist(person);
        System.out.println(persistedPerson);*/

        final Collection<Person> vlad = qm.find(Person.class, Collections.singletonList(Restrictions.in("id", Arrays.asList(1,2))));
        System.out.println("Found: " + vlad);
    }
}
