package info.novatec.micronaut.camunda.bpm.feature.test

import io.micronaut.data.annotation.GeneratedValue
import io.micronaut.data.annotation.Id
import io.micronaut.data.annotation.MappedEntity

/**
 * Sample entity for micronaut-data.
 *
 * @author Lukasz Frankowski
 * @author Tobias Schäfer
 */
@MappedEntity
data class Book(val title: String) {
    @Id
    @GeneratedValue
    var id: Long? = null
}