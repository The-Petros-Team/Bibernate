package com.bobocode.petros.bibernate.session.context.mocks;

import com.bobocode.petros.bibernate.annotations.Entity;
import com.bobocode.petros.bibernate.annotations.Id;
import com.bobocode.petros.bibernate.annotations.Table;
import lombok.Data;

import java.math.BigDecimal;

@Entity
@Table(name = "products")
@Data
public class Product {
    @Id
    private Long id;
    private String name;
    private BigDecimal price;
}
