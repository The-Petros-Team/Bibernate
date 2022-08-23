package com.bobocode.petros.bibernate.configuration;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class BasicDataSourceConfiguration implements DataSourceConfiguration {
    private String url;
    private String username;
    private String password;
    private int poolSize;
    private String driverClassName;
}
