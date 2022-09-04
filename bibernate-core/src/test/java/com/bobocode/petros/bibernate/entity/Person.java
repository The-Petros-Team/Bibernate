package com.bobocode.petros.bibernate.entity;

import com.bobocode.petros.bibernate.annotations.Column;
import com.bobocode.petros.bibernate.annotations.Entity;
import com.bobocode.petros.bibernate.annotations.Id;
import com.bobocode.petros.bibernate.annotations.Table;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
@Entity
@Table(name = "persons")
public class Person {
    @Id
    private Long id;
    @Column(name = "first_name")
    private String firstName;
    @Column(name = "last_name")
    private String lastName;
    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;
    private BigDecimal salary;
    @Column(name = "created_at")
    private LocalDateTime createdAt;
}
