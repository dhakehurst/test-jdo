package mydomain.model;

import java.util.ArrayList;
import java.util.List;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

@PersistenceCapable(detachable = "true")
public class Contacts {

	private Long id;

	private List<Person> list1;
	private List<Person> list2;

	public Contacts(final long id) {
		this.id = id;
		this.list1 = new ArrayList<>();
		this.list2 = new ArrayList<>();
	}

	@PrimaryKey
	@Persistent
	public Long getId() {
		return this.id;
	}

	public void setId(final Long value) {
		this.id = value;
	}

	@Persistent
	public List<Person> getList1() {
		return this.list1;
	}

	public void setList1(final List<Person> value) {
		this.list1 = value;
	}

	@Persistent(defaultFetchGroup = "true")
	public List<Person> getList2() {
		return this.list2;
	}

	public void setList2(final List<Person> value) {
		this.list2 = value;
	}
}
