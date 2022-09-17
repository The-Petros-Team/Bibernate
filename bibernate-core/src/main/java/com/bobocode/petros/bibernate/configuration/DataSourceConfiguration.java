package com.bobocode.petros.bibernate.configuration;

/**
 * Interface has accessors and mutators for the basic parameters that are mandatory for data source configuration.
 */
public interface DataSourceConfiguration {

    /**
     * Returns URL that points to a database.
     *
     * @return database url
     */
    String getUrl();

    /**
     * Allows to set database url.
     *
     * @param url database url
     */
    void setUrl(String url);

    /**
     * Returns a valid username.
     *
     * @return username
     */
    String getUsername();

    /**
     * Allows to set a username.
     *
     * @param username username
     */
    void setUsername(String username);

    /**
     * Returns a password that is required for database authentication.
     *
     * @return password
     */
    String getPassword();

    /**
     * Allows to set a password for database authentication.
     *
     * @param password password
     */
    void setPassword(String password);

    /**
     * Returns data source pool size. Default pool size = 10.
     *
     * @return pool size
     */
    int getPoolSize();

    /**
     * Allows to set a pool size.
     *
     * @param poolSize pool size
     */
    void setPoolSize(int poolSize);

    /**
     * Returns a database driver class name.
     *
     * @return database driver class name
     */
    String getDriverClassName();

    /**
     * Allows to set a database driver class name.
     *
     * @param driverClassName database driver class name
     */
    void setDriverClassName(String driverClassName);

}
