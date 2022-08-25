package com.bobocode.petros.bibernate.session;

public record EntityKey<T>(Class<T> entityType, Object id) {
}
