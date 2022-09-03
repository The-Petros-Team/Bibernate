package com.bobocode.petros.bibernate.persondemo;

import com.bobocode.petros.bibernate.annotations.Column;
import com.bobocode.petros.bibernate.annotations.Entity;
import com.bobocode.petros.bibernate.annotations.Id;
import com.bobocode.petros.bibernate.annotations.Table;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "person", schema = "vromantsev")
public class Person {
    
    @Id
    private Long id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}
