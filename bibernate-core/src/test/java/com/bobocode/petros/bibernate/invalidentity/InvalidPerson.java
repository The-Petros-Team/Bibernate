package com.bobocode.petros.bibernate.invalidentity;

import com.bobocode.petros.bibernate.annotations.Entity;
import com.bobocode.petros.bibernate.annotations.Id;
import com.bobocode.petros.bibernate.annotations.Table;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/*
This entity has no @Column annotation to resolve difference in fields naming of Entity and a Table that it's mapped on
* */
@Data
@Accessors(chain = true)
@Entity
@Table(name = "persons")
public class InvalidPerson {
    @Id
    private Long id;
    private String firstName;
    private String lastName;
    private LocalDate dateOfBirth;
    private BigDecimal salary;
    private LocalDateTime createdAt;
}
