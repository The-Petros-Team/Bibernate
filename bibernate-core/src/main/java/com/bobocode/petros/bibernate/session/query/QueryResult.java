package com.bobocode.petros.bibernate.session.query;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.*;

/**
 * Wraps a result received from a call to database.
 */
@Getter
@Setter
@NoArgsConstructor
public class QueryResult {

    /**
     * Holds a list of results.
     */
    private List<Object> results = new ArrayList<>();

    /**
     * Returns single result.
     *
     * @return query result
     * @param <T> generic type
     */
    public <T> T getSingleResult() {
        @SuppressWarnings("unchecked") final T result = (T) Optional.ofNullable(results.iterator().next()).orElseThrow();
        return result;
    }

    /**
     * Returns results collection.
     *
     * @return results collection
     * @param <T> generic type of element
     * @param <C> collection of type <T>
     */
    public <T, C extends Collection<T>> C getResultList() {
        @SuppressWarnings("unchecked") final C result = (C) results;
        return result;
    }

    /**
     * Adds an entity to a collection.
     *
     * @param entity entity
     * @return collection with a single entity
     * @param <T> generic type
     * @param <C> collection of type <T>
     */
    public <T, C extends Collection<T>> C wrap(final T entity) {
        @SuppressWarnings("unchecked") final C result = (C) Collections.singletonList(entity);
        return result;
    }

    /**
     * Checks whether results collection has exactly one element.
     *
     * @return true if collection has exactly one element or false otherwise
     */
    public boolean isSingle() {
        return this.results.size() == 1;
    }

    /**
     * Checks whether results collection has 2 or more elements.
     *
     * @return true if collection has 2 or more elements or false otherwise
     */
    public boolean isMulti() {
        return this.results.size() >= 2;
    }
}
