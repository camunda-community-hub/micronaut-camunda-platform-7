package info.novatec.micronaut.camunda.bpm.feature.test

import io.micronaut.data.jdbc.annotation.JdbcRepository
import io.micronaut.data.model.query.builder.sql.Dialect
import io.micronaut.data.repository.CrudRepository

/**
 * Sample repository for micronaut-data.
 *
 * @author Tobias Schäfer
 */
@JdbcRepository(dialect = Dialect.H2) interface BookRepository : CrudRepository<Book, Long> {
    fun countByTitle(title: String): Long
}