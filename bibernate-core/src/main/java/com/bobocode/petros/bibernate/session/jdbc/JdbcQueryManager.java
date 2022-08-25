package com.bobocode.petros.bibernate.session.jdbc;

import com.bobocode.petros.bibernate.transaction.Transaction;

import java.util.Map;

public interface JdbcQueryManager {

    <T> T persist(T entity);

    <T> T find(Class<T> type, Map<String, Object> parameters);

    <T> T update(T entity);

    <ID> void deleteById(ID id);

    <T, ID> void delete(Class<T> type, ID id);

    Transaction getTransaction();

}
