package com.bobocode.petros.bibernate.scanner;

import com.bobocode.petros.bibernate.annotations.Entity;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.reflections.Reflections;
import org.reflections.util.ConfigurationBuilder;

import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@UtilityClass
public class EntityScanner {

    /**
     * Collects classes annotated with {@link Entity} from the given packages.
     *
     * @param packages – packages to scan
     * @return set of classes from the scanned packages
     */
    public Set<Class<?>> scan(Set<String> packages) {
        log.trace("Start scanning Bibernate entities in packages: {}.", packages);
        var configBuilder = new ConfigurationBuilder()
                .forPackages(packages.toArray(String[]::new));

        var entities = new Reflections(configBuilder)
                .getTypesAnnotatedWith(Entity.class)
                .stream()
                .filter(c -> isInPackage(packages, c))
                .collect(Collectors.toSet());
        log.trace("Found {} entities.", entities.size());
        return entities;
    }

    private static boolean isInPackage(Set<String> packages, Class<?> c) {
        return packages.stream()
                .anyMatch(p -> p.contains(c.getPackage().getName()));
    }
}
