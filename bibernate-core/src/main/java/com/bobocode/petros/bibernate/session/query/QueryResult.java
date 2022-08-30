package com.bobocode.petros.bibernate.session.query;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
@Setter
@NoArgsConstructor
public class QueryResult {

    private List<Object> results = new ArrayList<>();

    public <T> T getSingleResult() {
        @SuppressWarnings("unchecked") final T result = (T) Optional.ofNullable(results.iterator().next()).orElseThrow();
        return result;
    }

    public <T, C extends Collection<T>> C getResultList() {
        @SuppressWarnings("unchecked") final C result = (C) results;
        return result;
    }

    public <T, C extends Collection<T>> C wrap(final T entity) {
        @SuppressWarnings("unchecked") final C result = (C) Stream.of(entity).collect(Collectors.toList());
        return result;
    }
}
