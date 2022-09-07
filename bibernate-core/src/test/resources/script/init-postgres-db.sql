CREATE SEQUENCE persons_id_seq;

CREATE TABLE persons
(
    id            BIGINT    NOT NULL DEFAULT nextval('persons_id_seq'),
    first_name    TEXT      NOT NULL,
    last_name     TEXT      NOT NULL,
    email         TEXT,
    date_of_birth DATE      not null,
    salary        DECIMAL(10, 2),
    created_at    TIMESTAMP NOT NULL DEFAULT now(),
    CONSTRAINT person_PK PRIMARY KEY (id),
    CONSTRAINT person_email_UQ UNIQUE (email)
);

INSERT INTO persons (first_name, last_name, email, date_of_birth, salary)
VALUES ('testName', 'testSurname-1', 'test1@email.com', '2022-04-09', 100500),
       ('testName', 'testSurname-2', 'test2@email.com', '2022-04-09', 500.50);
