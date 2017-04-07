package mydomain.model;

import java.util.ArrayList;
import java.util.List;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.PrimaryKey;

@PersistenceCapable(detachable = "true")
public class Contacts {
	@PrimaryKey
	Long id;

	List<Person> list;

	public Contacts(final long id) {
		this.id = id;
		this.list = new ArrayList<>();
	}

	public Long getId() {
		return this.id;
	}

	public List<Person> getList() {
		return this.list;
	}
}
