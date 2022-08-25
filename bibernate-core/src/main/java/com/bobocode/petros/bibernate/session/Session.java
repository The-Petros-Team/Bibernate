package com.bobocode.petros.bibernate.session;

import com.bobocode.petros.bibernate.transaction.Transaction;

public interface Session {

    <T> T persist(T entity);

    <T, ID> T findById(Class<T> type, ID id);

    <T> T update(T entity);

    <ID> void deleteById(ID id);

    <T> void delete(T entity);

    <T> Query<T> createQuery(String sql);

    <T> Query<T> createQuery(String sql, Class<T> type);

    Transaction getTransaction();

    void close();

}
