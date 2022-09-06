package com.bobocode.petros.bibernate.session.validation;

import com.bobocode.petros.bibernate.exceptions.JdbcOperationException;
import com.bobocode.petros.bibernate.scanner.EntityScanner;
import com.bobocode.petros.bibernate.utils.EntityUtils;
import lombok.experimental.UtilityClass;

import javax.sql.DataSource;
import java.lang.reflect.Field;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

import static com.bobocode.petros.bibernate.exceptions.ExceptionMessages.COLUMNS_METADATA_MESSAGE;
import static com.bobocode.petros.bibernate.exceptions.ExceptionMessages.FIELD_DOESNT_EXIST_MSG;
import static com.bobocode.petros.bibernate.exceptions.ExceptionMessages.MAPPING_VALIDATION_ERROR_MSG;
import static com.bobocode.petros.bibernate.exceptions.ExceptionMessages.TABLES_METADATA_MESSAGE;
import static com.bobocode.petros.bibernate.exceptions.ExceptionMessages.TABLE_DOESNT_EXIST_MSG;

@UtilityClass
public class EntityMappingValidator {
    private static final String TABLE_NAME_LABEL = "TABLE_NAME";
    private static final String COLUMN_NAME_LABEL = "COLUMN_NAME";

    /**
     * Validates that entity table and columns names are the same as in DB.
     *
     * @param dataSource datasource to DB
     * @param entityPackages packages to scan entities
     * @return set of violations
     */
    public Set<MappingViolationResult> validate(DataSource dataSource, Set<String> entityPackages) {
        Set<MappingViolationResult> mappingViolationResults = new LinkedHashSet<>();
        var entities = EntityScanner.scan(entityPackages);

        try (var connection = dataSource.getConnection()) {
            var metadata = connection.getMetaData();
            String databaseName = connection.getCatalog();
            String schema = connection.getSchema();

            var tables = getTableNames(metadata, databaseName, schema);
            for (Class<?> entity : entities) {
                var tableName = EntityUtils.getTableName(entity);
                if (tables.contains(tableName)) {
                    var tableFields = getTableFields(databaseName, schema, tableName, metadata);
                    var entityFields = entity.getDeclaredFields();
                    getUnknownFields(entityFields, tableFields)
                            .forEach(f -> mappingViolationResults.add(
                                    new MappingViolationResult()
                                            .setClassName(entity.getName())
                                            .setErrorMessage(FIELD_DOESNT_EXIST_MSG.formatted(f, tableName))));
                } else {
                    mappingViolationResults.add(
                            new MappingViolationResult()
                                    .setClassName(entity.getName())
                                    .setErrorMessage(TABLE_DOESNT_EXIST_MSG.formatted(tableName)));
                }
            }
        } catch (SQLException e) {
            throw new JdbcOperationException(MAPPING_VALIDATION_ERROR_MSG.formatted(e.getMessage()), e);
        }
        return mappingViolationResults;
    }

    private static List<String> getUnknownFields(Field[] entityFields, List<String> tableFields) {
        return Arrays.stream(entityFields)
                .map(Field::getName)
                .filter(Predicate.not(tableFields::contains))
                .toList();
    }

    private List<String> getTableFields(String database, String schema, String table, DatabaseMetaData metaData) {
        List<String> fields = new ArrayList<>();
        try (ResultSet tableColumnsResultSet = metaData.getColumns(database, schema, table, null)) {
            while (tableColumnsResultSet.next()) {
                String colName = tableColumnsResultSet.getString(COLUMN_NAME_LABEL);
                fields.add(colName);
            }
        } catch (SQLException e) {
            throw new JdbcOperationException(COLUMNS_METADATA_MESSAGE.formatted(e.getMessage()), e);
        }
        return fields;
    }

    private Set<String> getTableNames(DatabaseMetaData metadata, String database, String schema) {
        Set<String> tables = new HashSet<>();
        try (ResultSet tablesResultSet = getTablesResultSet(metadata, database, schema)) {
            while (tablesResultSet.next()) {
                tables.add(tablesResultSet.getString(TABLE_NAME_LABEL));
            }
        } catch (SQLException e) {
            throw new JdbcOperationException(TABLES_METADATA_MESSAGE.formatted(e.getMessage()), e);
        }
        return tables;
    }

    private ResultSet getTablesResultSet(DatabaseMetaData metaData, String database, String schema) throws
            SQLException {
        return metaData.getTables(database, schema, null, new String[]{"TABLE"});
    }
}
