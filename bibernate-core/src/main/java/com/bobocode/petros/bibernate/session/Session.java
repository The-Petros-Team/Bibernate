package com.bobocode.petros.bibernate.session;

import com.bobocode.petros.bibernate.transaction.Transaction;

import java.util.Collection;
import java.util.Optional;

public interface Session {

    <T> T persist(T entity);

    <T> Optional<T> findById(Class<T> type, Object id);

    <T> Collection<T> find(Class<T> type, String propertyName, Object value);

    <T> T update(T entity);

    <T> void deleteById(Class<T> type, Object id);

    <T> void delete(T entity);

    <T> Query<T> createQuery(String sql);

    <T> Query<T> createQuery(String sql, Class<T> type);

    Transaction getTransaction();

    void close();

}
