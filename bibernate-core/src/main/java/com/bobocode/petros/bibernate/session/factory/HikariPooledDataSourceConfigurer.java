package com.bobocode.petros.bibernate.session.factory;

import com.bobocode.petros.bibernate.configuration.DataSourceConfiguration;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;
import java.util.Objects;

/**
 * Configurer class that is responsible for data source initialization. Data source configuration provided by the
 * user will be wrapped into {@link HikariDataSource} and can use available features, like thread pool etc.
 */
class HikariPooledDataSourceConfigurer {

    private final DataSource dataSource;

    HikariPooledDataSourceConfigurer(final DataSourceConfiguration configuration) {
        Objects.requireNonNull(configuration, "Parameter [configuration] must be provided!");
        this.dataSource = configure(configuration);
    }

    DataSource getDataSource() {
        Objects.requireNonNull(dataSource, "Data source should be initialized first!");
        return dataSource;
    }

    private DataSource configure(final DataSourceConfiguration configuration) {
        final HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setDriverClassName(configuration.getDriverClassName());
        hikariConfig.setJdbcUrl(configuration.getUrl());
        hikariConfig.setUsername(configuration.getUsername());
        hikariConfig.setPassword(configuration.getPassword());
        hikariConfig.setMaximumPoolSize(configuration.getPoolSize());
        return new HikariDataSource(hikariConfig);
    }
}
