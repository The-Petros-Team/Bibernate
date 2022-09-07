CREATE TABLE persons
(
    id            BIGINT       NOT NULL AUTO_INCREMENT,
    first_name    VARCHAR(255) NOT NULL,
    last_name     VARCHAR(255) NOT NULL,
    email         VARCHAR(255),
    date_of_birth DATE         NOT NULL,
    salary        DECIMAL(10, 2),
    created_at    TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id)
);

INSERT INTO persons (first_name, last_name, email, date_of_birth, salary)
VALUES ('testName', 'testSurname-1', 'test1@email.com', '2022-04-09', 100500),
       ('testName', 'testSurname-2', 'test2@email.com', '2022-04-09', 500.50);
