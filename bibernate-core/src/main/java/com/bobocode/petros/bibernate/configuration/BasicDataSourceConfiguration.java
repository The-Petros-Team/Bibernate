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
    @Builder.Default
    private int poolSize = 10;
    private String driverClassName;
}
