package com.bobocode.petros.bibernate.transaction;

public interface Transaction {

    void begin();

    void commit();

    void rollback();

}
