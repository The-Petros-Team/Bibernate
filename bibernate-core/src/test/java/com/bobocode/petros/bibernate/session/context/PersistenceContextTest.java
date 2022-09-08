package com.bobocode.petros.bibernate.session.context;

import com.bobocode.petros.bibernate.exceptions.NoEntityIdException;
import com.bobocode.petros.bibernate.session.EntityKey;
import com.bobocode.petros.bibernate.session.context.mocks.Order;
import com.bobocode.petros.bibernate.session.context.mocks.Product;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class PersistenceContextTest {

    private PersistenceContext persistenceContext;
    private Product milk = new Product();
    private Product banana = new Product();
    private Product porosiatko = new Product();
    private Order order = new Order();

    @BeforeEach
    void init() {
        persistenceContext = new PersistenceContext();

        milk.setId(1L);
        milk.setName("Moloko");
        milk.setPrice(BigDecimal.valueOf(2L));

        porosiatko.setId(3L);
        porosiatko.setName("Lusia");
        porosiatko.setPrice(BigDecimal.valueOf(100));

        order.setProvider("Home Farm");
        order.setProducts(List.of(milk, banana, porosiatko));
    }

    @Test
    void whenAddEntityToSnapshotThenItStoreInEntitySnapshotsMap() {
        persistenceContext.addSnapshot(milk);
        persistenceContext.addSnapshot(porosiatko);

        var milkKey = new EntityKey<>(milk.getClass(), milk.getId());
        var porosiatkoKey = new EntityKey<>(porosiatko.getClass(), porosiatko.getId());

        Map<EntityKey<?>, Object> entitySnapshots = getEntityKeyToObjectMap(persistenceContext, "entitySnapshots");

        assertAll(
                () -> assertNotNull(entitySnapshots.get(milkKey)),
                () -> assertNotNull(entitySnapshots.get(porosiatkoKey)),
                () -> assertEquals(milk, entitySnapshots.get(milkKey)),
                () -> assertEquals(porosiatko, entitySnapshots.get(porosiatkoKey)),
                () -> assertTrue(entitySnapshots.containsKey(milkKey)),
                () -> assertTrue(entitySnapshots.containsKey(porosiatkoKey)));
    }

    @Test
    void whenAddNullAsEntityToSnapshotThenThrowNullPointerException() {
        assertThrows(NullPointerException.class, () -> persistenceContext.addSnapshot(null));
    }

    @Test
    void whenAddObjectWithoutIdFieldToSnapshotThenThrowNoEntityIdException() {
        assertThrows(NoEntityIdException.class, () -> persistenceContext.addSnapshot(order));
    }

    @Test
    void whenAddEntityToCacheThenItStoreInCacheMap() {
        persistenceContext.addToCache(milk);
        persistenceContext.addToCache(porosiatko);

        var milkKey = new EntityKey<>(milk.getClass(), milk.getId());
        var porosiatkoKey = new EntityKey<>(porosiatko.getClass(), porosiatko.getId());

        Map<EntityKey<?>, Object> cache = getEntityKeyToObjectMap(persistenceContext, "cache");

        assertAll(
                () -> assertNotNull(cache.get(milkKey)),
                () -> assertNotNull(cache.get(porosiatkoKey)),
                () -> assertEquals(milk, cache.get(milkKey)),
                () -> assertEquals(porosiatko, cache.get(porosiatkoKey)),
                () -> assertTrue(cache.containsKey(milkKey)),
                () -> assertTrue(cache.containsKey(porosiatkoKey)));
    }

    @Test
    void whenAddNullAsEntityToCacheThenThrowNullPointerException() {
        assertThrows(NullPointerException.class, () -> persistenceContext.addToCache(null));
    }

    @Test
    void whenAddObjectWithoutIdFieldToCacheThenThrowNoEntityIdException(){
        assertThrows(NoEntityIdException.class, () -> persistenceContext.addToCache(order));

    }

    @Test
    void whenChangeEntityThenSnapshotReplicaWillNotChange() {
        persistenceContext.addSnapshot(milk);
        persistenceContext.addSnapshot(porosiatko);

        var milkKey = new EntityKey<>(milk.getClass(), milk.getId());
        var porosiatkoKey = new EntityKey<>(porosiatko.getClass(), porosiatko.getId());

        milk.setName("Not milk");
        porosiatko.setName("Not porosiatko");

        Map<EntityKey<?>, Object> entitySnapshots = getEntityKeyToObjectMap(persistenceContext, "entitySnapshots");

        assertAll(
                () -> assertNotNull(entitySnapshots.get(milkKey)),
                () -> assertNotNull(entitySnapshots.get(porosiatkoKey)),
                () -> assertNotEquals(milk, entitySnapshots.get(milkKey)),
                () -> assertNotEquals(porosiatko, entitySnapshots.get(porosiatkoKey)),
                () -> assertNotEquals("Not porosiatko", entitySnapshots.get(porosiatkoKey)),
                () -> assertNotEquals("Not milk", entitySnapshots.get(milkKey)),
                () -> assertTrue(entitySnapshots.containsKey(milkKey)),
                () -> assertTrue(entitySnapshots.containsKey(porosiatkoKey)));
    }

    @Test
    void whenCallGetChangedEntitiesThenReturnChangedEntities() {
        persistenceContext.addSnapshot(milk);
        persistenceContext.addSnapshot(porosiatko);
        persistenceContext.addToCache(milk);
        persistenceContext.addToCache(porosiatko);

        milk.setName("Not milk");

        var changedEntities = persistenceContext.getChangedEntities();
        assertAll(
                () -> assertNotNull(changedEntities),
                () -> assertTrue(changedEntities.size() > 0),
                () -> assertTrue(changedEntities.stream()
                        .anyMatch(entity -> entity.equals(milk))),
                () -> assertFalse(changedEntities.stream()
                        .anyMatch(entity -> entity.equals(porosiatko)))
        );
    }

    @Test
    void whenCallGetChangedEntitiesButCacheAndSnapshotMapsEmptyThenReturnEmptyList() {
        assertAll(
                () -> assertNotNull(persistenceContext.getChangedEntities()),
                () -> assertTrue(persistenceContext.getChangedEntities().isEmpty()));
    }

    @SneakyThrows
    private Map<EntityKey<?>, Object> getEntityKeyToObjectMap(PersistenceContext context, String mapName) {
        Field field = context.getClass().getDeclaredField(mapName);
        field.setAccessible(true);
        return (Map<EntityKey<?>, Object>) field.get(context);
    }
}
