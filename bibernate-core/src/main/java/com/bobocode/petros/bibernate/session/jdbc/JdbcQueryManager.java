package com.bobocode.petros.bibernate.session.jdbc;

import com.bobocode.petros.bibernate.session.query.condition.Restriction;
import com.bobocode.petros.bibernate.transaction.Transaction;

import java.util.Collection;
import java.util.List;

public interface JdbcQueryManager {

    <T> T persist(T entity);

    <T, C extends Collection<T>> Collection<T> find(Class<T> type, List<Restriction> restrictions);

    <T> T update(T entity);

    <ID> void deleteById(ID id);

    <T, ID> void delete(Class<T> type, ID id);

    Transaction getTransaction();

}
