package com.bobocode.petros.bibernate.scanner;

import com.bobocode.petros.bibernate.annotations.Entity;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import org.reflections.Reflections;
import org.reflections.util.ConfigurationBuilder;

import java.util.Set;

@UtilityClass
public class EntityScanner {

    @SneakyThrows
    public Set<Class<?>> scan(Set<String> packages) {
        var configBuilder = new ConfigurationBuilder()
                .forPackages(packages.toArray(String[]::new));

        return new Reflections(configBuilder)
                .getTypesAnnotatedWith(Entity.class);
    }
}
