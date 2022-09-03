package com.bobocode.petros.bibernate.persondemo;

import com.bobocode.petros.bibernate.session.jdbc.DefaultJdbcQueryManager;
import com.bobocode.petros.bibernate.transaction.Transaction;
import com.mysql.cj.jdbc.MysqlDataSource;

import java.time.LocalDateTime;

public class DemoApp {
    public static void main(String[] args) {
        MysqlDataSource mysql = new MysqlDataSource();
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

        final Person kirill = qm.persist(p);

        transaction.commit();


    }
}
