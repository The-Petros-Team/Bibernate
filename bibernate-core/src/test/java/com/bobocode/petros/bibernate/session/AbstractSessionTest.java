package com.bobocode.petros.bibernate.session;

import com.bobocode.petros.bibernate.configuration.BasicDataSourceConfiguration;
import com.bobocode.petros.bibernate.entity.Person;
import com.bobocode.petros.bibernate.exceptions.MappingViolationException;
import com.bobocode.petros.bibernate.session.factory.DefaultSessionFactory;
import com.bobocode.petros.bibernate.session.factory.SessionFactory;
import org.junit.jupiter.api.*;
import org.testcontainers.containers.JdbcDatabaseContainer;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.Collection;
import java.util.Objects;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public abstract class AbstractSessionTest {
    protected static JdbcDatabaseContainer<?> genericContainer;
    private static SessionFactory sessionFactory;

    abstract void setJdbcDatabaseContainer();

    @BeforeEach
    void setUp() {
        if (genericContainer == null) {
            setJdbcDatabaseContainer();
            genericContainer.start();
            sessionFactory = new DefaultSessionFactory(BasicDataSourceConfiguration.builder()
                    .driverClassName(genericContainer.getDriverClassName())
                    .url(genericContainer.getJdbcUrl())
                    .username(genericContainer.getUsername())
                    .password(genericContainer.getPassword())
                    .poolSize(2) //since we need minimum 2 connections to test transactional functionality
                    .build(), Set.of("com.bobocode.petros.bibernate.entity"));
        }
    }

    @AfterAll
    static void tearDown() {
        genericContainer.close();
        genericContainer = null;
    }

    @Test
    @Order(1)
    void whenNoTransactionFindByPropertyNameMatchesMultipleRowsThenReturnCollectionWithEntities() {
        var session = sessionFactory.openSession();
        assertNotNull(session);
        var persons = session.find(Person.class, "first_name", "testName");
        assertAll(
                () -> assertEquals(2, persons.size()),
                () -> assertTrue(persons.stream()
                        .allMatch(this::isAllFieldsNotNull))
        );
    }

    @Test
    @Order(2)
    void whenNoTransactionFindByIdExistsThenReturnOptionalWithEntity() {
        var session = sessionFactory.openSession();
        assertNotNull(session);
        var person = session.findById(Person.class, 1L).orElseThrow();
        assertAll(
                () -> assertEquals(1, person.getId()),
                () -> assertEquals("testName", person.getFirstName()),
                () -> assertEquals("testSurname-1", person.getLastName()),
                () -> assertEquals(LocalDate.of(2022, 4, 9), person.getDateOfBirth()),
                () -> assertEquals(new BigDecimal("100500.00"), person.getSalary())
        );
    }

    @Test
    @Order(3)
    void whenTransactionUpdateExistsWithoutCommitThenNoUpdate() {
        var session = sessionFactory.openSession();
        var createdAt = LocalDateTime.of(2022, 1, 1, 0, 0, 0);
        var personForUpdate = new Person()
                .setId(1L)
                .setFirstName("UpdatedName")
                .setLastName("UpdatedLastName")
                .setSalary(new BigDecimal("777.77"))
                .setDateOfBirth(LocalDate.of(2000, 1, 1))
                .setCreatedAt(createdAt);
        session.getTransaction().begin();
        session.update(personForUpdate);
        assertFalse(session.getTransaction().isClosed());
        var newSession = sessionFactory.openSession();
        var person = newSession.findById(Person.class, 1L).orElseThrow();
        assertEquals("testName", person.getFirstName());
        session.getTransaction().rollback();
    }

    @Test
    @Order(4)
    void whenNoTransactionUpdateExistsThenUpdateSuccess() {
        var session = sessionFactory.openSession();
        var createdAt = LocalDateTime.of(2022, 1, 1, 0, 0, 0);
        var personForUpdate = new Person()
                .setId(1L)
                .setFirstName("UpdatedName")
                .setLastName("UpdatedLastName")
                .setSalary(new BigDecimal("777.77"))
                .setDateOfBirth(LocalDate.of(2000, 1, 1))
                .setCreatedAt(createdAt);
        session.update(personForUpdate);
        var newSession = sessionFactory.openSession();
        var person = newSession.findById(Person.class, 1L).orElseThrow();
        assertEquals(personForUpdate, person);
    }

    @Test
    @Order(5)
    void whenTransactionRemoveExistsWithoutCommitThenNotRemoved() {
        var session = sessionFactory.openSession();
        session.getTransaction().begin();
        var person = session.findById(Person.class, 1L).orElseThrow();
        session.delete(person);
        assertFalse(session.getTransaction().isClosed());
        var newSession = sessionFactory.openSession();
        var notDeletedPerson = newSession.findById(Person.class, 1L);
        assertFalse(notDeletedPerson.isEmpty());
        session.getTransaction().rollback();
        var doubleCheckSession = sessionFactory.openSession();
        var stillNotDeletedPerson = doubleCheckSession.findById(Person.class, 1L);
        assertFalse(stillNotDeletedPerson.isEmpty());
    }

    @Test
    @Order(6)
    void whenTransactionRemoveExistsWithCommitThenEntityRemoved() {
        var session = sessionFactory.openSession();
        session.getTransaction().begin();
        var person = session.findById(Person.class, 1L).orElseThrow();
        session.delete(person);
        assertFalse(session.getTransaction().isClosed());
        var newSession = sessionFactory.openSession();
        var notDeletedPerson = newSession.findById(Person.class, 1L);
        assertFalse(notDeletedPerson.isEmpty());
        session.getTransaction().commit();
        var doubleCheckSession = sessionFactory.openSession();
        var notFoundPerson = doubleCheckSession.findById(Person.class, 1L);
        assertTrue(notFoundPerson.isEmpty());
    }

    @Test
    @Order(7)
    void whenPersistWithThenEntitySaved() {
        var session = sessionFactory.openSession();
        var createdAt = LocalDateTime.of(2022, 1, 1, 0, 0, 0);
        var newPerson = new Person()
                .setId(1L)
                .setFirstName("newFirstName")
                .setLastName("newLastName")
                .setSalary(new BigDecimal("555.00"))
                .setDateOfBirth(LocalDate.of(2000, 1, 1))
                .setCreatedAt(createdAt);
        session.persist(newPerson);
        var newSession = sessionFactory.openSession();
        var persons = newSession.find(Person.class, "first_name", "newFirstName");
        assertEquals(1, persons.size());
        assertEquals(newPerson, persons.iterator().next());
    }

    @Test
    void whenInvalidEntityMappingThenThrowException() {
        assertThrows(MappingViolationException.class, () -> new DefaultSessionFactory(BasicDataSourceConfiguration.builder()
                .driverClassName(genericContainer.getDriverClassName())
                .url(genericContainer.getJdbcUrl())
                .username(genericContainer.getUsername())
                .password(genericContainer.getPassword())
                .build(), Set.of("com.bobocode.petros.bibernate.invalidentity")));
    }

    @Test
    @Order(8)
    void whenCallFindOperationWithDifferentlySpelledPropertyNamesThenReturnEntities() {
        // given
        var columnName = "last_name";
        var entityFieldName = "lastName";
        final Session session = sessionFactory.openSession();
        final Person tarasShevchenko = session.persist(new Person()
                .setFirstName("Taras")
                .setLastName("Shevchenko")
                .setDateOfBirth(LocalDate.of(1814, Month.MARCH, 9))
                .setCreatedAt(LocalDateTime.now())
        );

        // assertions & verifications
        final Collection<Person> persons = session.find(Person.class, columnName, tarasShevchenko.getLastName());
        assertAll(
                () -> assertNotNull(persons),
                () -> assertEquals(1, persons.size()),
                () -> assertEquals(tarasShevchenko.getLastName(), persons.iterator().next().getLastName())
        );
        final Collection<Person> theSamePeople = session.find(Person.class, entityFieldName, tarasShevchenko.getLastName());
        assertAll(
                () -> assertNotNull(theSamePeople),
                () -> assertEquals(1, theSamePeople.size()),
                () -> assertEquals(tarasShevchenko.getLastName(), theSamePeople.iterator().next().getLastName())
        );
        assertEquals(persons, theSamePeople);
    }

    private boolean isAllFieldsNotNull(Person person) {
        return Objects.nonNull(person.getId())
                && Objects.nonNull(person.getFirstName())
                && Objects.nonNull(person.getLastName())
                && Objects.nonNull(person.getDateOfBirth())
                && Objects.nonNull(person.getSalary())
                && Objects.nonNull(person.getCreatedAt());
    }
}
