package com.bobocode.petros.bibernate.configuration;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class JavaConnectionConfiguration implements ConnectionConfiguration {
    private String url;
    private String username;
    private String password;
    private int poolSize;
    private String driverClassName;
}
