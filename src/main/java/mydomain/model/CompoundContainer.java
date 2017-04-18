package mydomain.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import mydomain.model.CompoundContainer.PK;

@PersistenceCapable(objectIdClass = PK.class)
public class CompoundContainer {
	public static class PK implements Serializable {
		public PK() {}

		public PK(final String value) {
			this.id = value;
		}

		public String id;

		@Override
		public String toString() {
			return this.id;
		}

		@Override
		public int hashCode() {
			return Objects.hash(this.id);
		}

		@Override
		public boolean equals(final Object obj) {
			if (obj != null && obj instanceof PK) {
				final PK other = (PK) obj;
				return this.id.equals(other.id);
			} else {
				return false;
			}
		}
	}

	public CompoundContainer() {
		this.items = new ArrayList<>();
	}

	@PrimaryKey
	@Persistent
	public String id;

	private List<CompoundItem> items;

	@Persistent
	public List<CompoundItem> getItems() {
		return this.items;
	}

	public void setItems(final List<CompoundItem> value) {
		this.items = value;
	}
}
