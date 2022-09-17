## <span style="color:green">Petros Bibernate Framework</span>

Bibernate ORM is a library providing Object/Relational Mapping (ORM) support to applications, libraries, and frameworks.

Implemented features:

1. Mapping validation during the start of the framework. In case of invalid mapping a MappingViolationException will be thrown with description of violations
2. Datasource configuration. Under the hood HikariDataSource is used to manage connection pool
3. First level cache for session. All managed entities are stored in internal cache
4. Action Queue. Query to database will be sent only after flush or close method calls
5. Transactions. It is possible to open transaction and process managed entities within separate transaction.
6. Dirty checking mechanism. On session close or flush this mechanism will provide checking of the state of the managed entities and their state in db and
   provide update operations if there were any differences
7. Crud operations for managed entities*

* currently entities relations are not supported by the framework :(

Description of annotation used by framework:

1. <span style="color:green">_**@Entity**_</span> - declares the class as an entity (i.e. a persistent POJO class).
2. <span style="color:green">_**@Table**_</span> - @Table is set at the class level; it allows you to define the table, catalog, and schema names for your
   entity mapping.
3. <span style="color:green">_**@Id**_</span> - declares the identifier property of this entity
4. <span style="color:green">_**@Column**_</span> - used for a property name mapping. In case if it is not declared explicitly then field name is taken as a
   column name

To start using Bibernate Framework please proceed with the following steps:

1. Add the following configuration to your pom.xml:

```
<dependency>
    <groupId>com.bobocode.petros</groupId>
    <artifactId>bibernate-core</artifactId>
    <version>1.0</version>
</dependency>
```

2. Copy the following configuration to your settings.xml file:

```
<servers>
    <server>
        <username>vladyslav.romantsev@gmail.com</username>
        <password>AKCp8mZwKha3HA2UogNLWenwvGKXzJvdsbeResFM53Pv4toTe8DN2pRZfEpKqdtCdwmSZWtFE</password>
        <id>bibernate-central</id>
    </server>
    <server>
        <username>vladyslav.romantsev@gmail.com</username>
        <password>AKCp8mZwKha3HA2UogNLWenwvGKXzJvdsbeResFM53Pv4toTe8DN2pRZfEpKqdtCdwmSZWtFE</password>
        <id>bibernate-snapshots</id>
    </server>
</servers>
<profiles>
    <profile>
        <repositories>
            <repository>
                <snapshots>
                    <enabled>false</enabled>
                </snapshots>
                <id>bibernate-central</id>
                <name>petros-bibernate-libs-release</name>
                <url>https://petros.jfrog.io/artifactory/petros-bibernate-libs-release</url>
            </repository>
            <repository>
                <snapshots />
                <id>bibernate-snapshots</id>
                <name>petros-bibernate-libs-snapshot</name>
                <url>https://petros.jfrog.io/artifactory/petros-bibernate-libs-snapshot</url>
            </repository>
        </repositories>
        <pluginRepositories>
            <pluginRepository>
                <snapshots>
                    <enabled>false</enabled>
                </snapshots>
                <id>bibernate-central</id>
                <name>petros-bibernate-libs-release</name>
                <url>https://petros.jfrog.io/artifactory/petros-bibernate-libs-release</url>
            </pluginRepository>
            <pluginRepository>
                <snapshots />
                <id>bibernate-snapshots</id>
                <name>petros-bibernate-libs-snapshot</name>
                <url>https://petros.jfrog.io/artifactory/petros-bibernate-libs-snapshot</url>
            </pluginRepository>
        </pluginRepositories>
        <id>artifactory</id>
    </profile>
</profiles>
<activeProfiles>
    <activeProfile>artifactory</activeProfile>
</activeProfiles>
```

3. Please ask Petros Team for username and password as the jar file is hosted in private repository

When all the configuration is ready run _**mvn clean install**_ command to download the dependencies. And now you are ready to start!

Snippet to create session

```java

SessionFactory sessionFactory=new DefaultSessionFactory(BasicDataSourceConfiguration.builder()
        .driverClassName(genericContainer.getDriverClassName())
        .url(genericContainer.getJdbcUrl())
        .username(genericContainer.getUsername())
        .password(genericContainer.getPassword())
        .poolSize(2) //since we need minimum 2 connections to test transactional functionality
        .build(),Set.of("com.bobocode.petros.bibernate.entity"));

        Session session = sessionFactory.openSession();
```

To declare your class as an Entity mapped on table in configured datasource you could use one of the following code snippet:

```java

@Entity
@Table(name = "table_name")
public class MyBeautifulEntity {
    @Id
    private Long id;
    @Column(name = "column_name")
    private String columnName;
//getters and setters are omitted
}
```

Example of using session functionality:

```java

/**
 * Persist example
 */
session.getTransaction().begin();
        session.persist(oleksii);
        session.persist(kirill);
        session.getTransaction().commit();

/**
 * Remove by entity example
 */
        session.getTransaction().begin();
        session.delete(kirill);
        session.getTransaction().commit();


/**
 * Remove by entity type and id example
 */
        session.getTransaction().begin();
        session.deleteById(Person.class, 1L);
        session.getTransaction().commit();

/**
 * Find by entity type and parameter example
 */
        session.getTransaction().begin();
        Collection<Person> personsById = session.find(Person.class, "id", 1L);
        Collection<Person> personsByName = session.find(Person.class, "name", "kirill");
        session.getTransaction().commit();

/**
 * Find entity by id example
 */
        session.getTransaction().begin();
        Optional<Person> byId = session.findById(Person.class, 1L);
        session.getTransaction().commit();

        session.getTransaction().begin();
        Person person = session.findById(Person.class, 1L).get();
        person.setFirstName("New name");
        session.update(person);
        session.getTransaction().commit();
        }
```

In case transaction start is omitted the session will handle all operations in autocommit mode

Don't hesitate to help us with improving of our project. It's opensource and free to use. We're waiting for your contribution 😜

https://github.com/The-Petros-Team/Bibernate