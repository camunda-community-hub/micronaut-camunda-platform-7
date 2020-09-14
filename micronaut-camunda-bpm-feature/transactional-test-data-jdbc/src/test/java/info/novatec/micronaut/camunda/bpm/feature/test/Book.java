package info.novatec.micronaut.camunda.bpm.feature.test;

import io.micronaut.data.annotation.GeneratedValue;
import io.micronaut.data.annotation.Id;
import io.micronaut.data.annotation.MappedEntity;

/**
 * Sample entity for micronaut-data.
 *
 * @author Lukasz Frankowski
 * @author Tobias Schäfer
 */
@MappedEntity
public class Book {

	@Id
	@GeneratedValue
	private Long id;
	private String title;

	public Book() {
	}

	public Book(String title) {
		this.title = title;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}
}
