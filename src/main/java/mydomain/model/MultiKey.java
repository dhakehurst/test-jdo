package mydomain.model;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

@PersistenceCapable
public class MultiKey {

	public MultiKey(final long id1, final long id2) {
		this.id1 = id1;
		this.id2 = id2;
	}

	private Long id1;
	private Long id2;

	@PrimaryKey
	@Persistent
	public Long getId1() {
		return this.id1;
	}

	public void setId1(final Long value) {
		this.id1 = value;
	}

	@PrimaryKey
	@Persistent
	public Long getId2() {
		return this.id2;
	}

	public void setId2(final Long value) {
		this.id2 = value;
	}

}
