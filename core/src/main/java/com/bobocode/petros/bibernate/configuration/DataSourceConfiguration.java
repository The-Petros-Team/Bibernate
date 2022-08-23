package com.bobocode.petros.bibernate.configuration;

public interface DataSourceConfiguration {

    String getUrl();

    void setUrl(String url);

    String getUsername();

    void setUsername(String username);

    String getPassword();

    void setPassword(String password);

    int getPoolSize();

    void setPoolSize(int poolSize);

    String getDriverClassName();

    void setDriverClassName(String driverClassName);

}
