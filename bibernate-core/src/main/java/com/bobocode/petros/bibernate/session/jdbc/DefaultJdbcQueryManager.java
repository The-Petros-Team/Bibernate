package com.bobocode.petros.bibernate.session.jdbc;

import com.bobocode.petros.bibernate.session.query.condition.Restriction;
import com.bobocode.petros.bibernate.transaction.Transaction;

import javax.sql.DataSource;
import java.util.List;
import java.util.Objects;

public class DefaultJdbcQueryManager implements JdbcQueryManager {

    private DataSource dataSource;

    public DefaultJdbcQueryManager(DataSource dataSource) {
        Objects.requireNonNull(dataSource, "Parameter [dataSource] must be provided!");
        this.dataSource = dataSource;
    }

    @Override
    public <T> T persist(T entity) {
        return null;
    }

    @Override
    public <T> T find(Class<T> type, List<Restriction> restrictions) {
        return null;
    }

    @Override
    public <T> T update(T entity) {
        return null;
    }

    @Override
    public <ID> void deleteById(ID id) {

    }

    @Override
    public <T, ID> void delete(Class<T> type, ID id) {

    }

    @Override
    public Transaction getTransaction() {
        return null;
    }
}
