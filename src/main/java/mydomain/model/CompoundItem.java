package mydomain.model;

import java.io.Serializable;
import java.util.Objects;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import mydomain.model.CompoundItem.PK;

@PersistenceCapable(objectIdClass = PK.class)
public class CompoundItem {

	public static class PK implements Serializable {
		public PK() {}

		public PK(final String value) {
			final String[] split = value.split("[,]");
			this.owner = new CompoundContainer.PK(split[0]);
			this.id = split[1];
		}

		public CompoundContainer.PK owner;
		public String id;

		@Override
		public String toString() {
			return this.owner + "," + this.id;
		}

		@Override
		public int hashCode() {
			return Objects.hash(this.owner, this.id);
		}

		@Override
		public boolean equals(final Object obj) {
			if (obj != null && obj instanceof PK) {
				final PK other = (PK) obj;
				return this.owner.equals(other.owner) && this.id.equals(other.id);
			} else {
				return false;
			}
		}
	}

	public CompoundItem() {}

	@PrimaryKey
	@Persistent
	public CompoundContainer owner;

	@PrimaryKey
	@Persistent
	public String id;

}
