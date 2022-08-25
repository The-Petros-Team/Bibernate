package com.bobocode.petros.bibernate.session.factory;

import com.bobocode.petros.bibernate.configuration.DataSourceConfiguration;
import com.bobocode.petros.bibernate.exceptions.MappingViolationException;
import com.bobocode.petros.bibernate.session.CacheableSession;
import com.bobocode.petros.bibernate.session.Session;
import com.bobocode.petros.bibernate.session.jdbc.DefaultJdbcQueryManager;
import com.bobocode.petros.bibernate.session.jdbc.JdbcQueryManager;
import com.bobocode.petros.bibernate.session.validation.EntityMappingValidator;
import com.bobocode.petros.bibernate.session.validation.MappingViolationResult;

import javax.sql.DataSource;
import java.util.Objects;
import java.util.Set;

import static com.bobocode.petros.bibernate.exceptions.ExceptionMessages.MAPPING_VIOLATION_MESSAGE;

public class DefaultSessionFactory implements SessionFactory {

    private final DataSource dataSource;
    private final JdbcQueryManager jdbcQueryManager;

    public DefaultSessionFactory(DataSource dataSource, Set<String> entityPackages) {
        Objects.requireNonNull(dataSource, "Parameter [dataSource] must be provided!");
        Objects.requireNonNull(entityPackages, "Parameter [entityPackages] must be provided!");
        this.dataSource = dataSource;
        final Set<MappingViolationResult> violations = EntityMappingValidator.validate(dataSource, entityPackages);
        if (!violations.isEmpty()) {
            throw new MappingViolationException(String.format(MAPPING_VIOLATION_MESSAGE, violations.size(), violations));
        }
        this.jdbcQueryManager = new DefaultJdbcQueryManager(dataSource);
    }

    public DefaultSessionFactory(DataSourceConfiguration configuration, Set<String> entityPackages) {
        this(new HikariPooledDataSourceConfigurer(configuration).getDataSource(), entityPackages);
    }

    @Override
    public Session openSession() {
        return new CacheableSession(dataSource, jdbcQueryManager);
    }
}
