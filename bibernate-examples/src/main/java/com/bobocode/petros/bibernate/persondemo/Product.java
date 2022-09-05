package com.bobocode.petros.bibernate.persondemo;

import com.bobocode.petros.bibernate.annotations.Entity;
import com.bobocode.petros.bibernate.annotations.Id;
import com.bobocode.petros.bibernate.annotations.Table;

@Entity
@Table(name = "products")
public class Product {
    @Id
    private Integer id;

    private String names;
}
