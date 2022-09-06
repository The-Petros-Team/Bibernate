package com.bobocode.petros.bibernate.session.context.mocks;

import com.bobocode.petros.bibernate.annotations.Entity;
import com.bobocode.petros.bibernate.annotations.Id;
import com.bobocode.petros.bibernate.annotations.Table;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@Data
public class Order {
    private Long id;
    private String provider;
    private List<Product> products = new ArrayList<>();
}
