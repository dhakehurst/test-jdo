package org.datanucleus.test;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import javax.jdo.Query;
import javax.jdo.Transaction;

import org.datanucleus.util.NucleusLogger;
import org.junit.Assert;
import org.junit.Test;

import mydomain.model.CompoundContainer;
import mydomain.model.CompoundItem;
import mydomain.model.Contacts;
import mydomain.model.Person;

public class SimpleTest {

	private void delete(final File f) {
		if (f.isDirectory()) {
			for (final File f2 : f.listFiles()) {
				this.delete(f2);
			}
		}

		f.delete();

	}

	private PersistenceManagerFactory connectDb(final String connectionUrl) {
		final Map<String, Object> props = new HashMap<>();
		props.put("javax.jdo.PersistenceManagerFactoryClass", "org.datanucleus.api.jdo.JDOPersistenceManagerFactory");
		props.put("javax.jdo.option.ConnectionURL", connectionUrl);
		props.put("datanucleus.schema.autoCreateAll", "true");
		final PersistenceManagerFactory pmf = JDOHelper.getPersistenceManagerFactory(props);
		return pmf;
	}

	private void disconnectDb(final PersistenceManagerFactory pmf) {
		pmf.close();
	}

	@Test
	public void LazyLoadQueryResultExceptionIfResultIsEmpty() {
		NucleusLogger.GENERAL.info(">> test START");

		final String connectionUrl = "neo4j:db/LazyLoadQueryResultExceptionIfResultIsEmpty";
		final Path path = Paths.get("db/LazyLoadQueryResultExceptionIfResultIsEmpty");
		if (path.toFile().exists()) {
			this.delete(path.toFile());
		}

		final PersistenceManagerFactory pmf = this.connectDb(connectionUrl);
		final PersistenceManager pm = pmf.getPersistenceManager();
		final Transaction tx = pm.currentTransaction();
		try {
			tx.begin();

			final Person p = new Person(1, "Adam Ant");

			pm.makePersistent(p);

			tx.commit();

			tx.begin();

			final Query<Contacts> query2 = pm.newQuery(Contacts.class);
			final List<Contacts> result2 = query2.executeList();

			final boolean isEmpty2 = result2.isEmpty();
			Assert.assertFalse(isEmpty2);

			tx.commit();

		} catch (final Throwable thr) {
			NucleusLogger.GENERAL.error(">> Exception in test", thr);
			Assert.fail("Failed test : " + thr.getMessage());
		} finally {
			if (tx.isActive()) {
				tx.rollback();
			}
			pm.close();
		}

		pmf.close();
		NucleusLogger.GENERAL.info(">> test END");
	}

	@Test
	public void RelatedObjectNotPersisted() {
		NucleusLogger.GENERAL.info(">> test START");

		final String connectionUrl = "neo4j:db/RelatedObjectNotPersisted";
		final Path path = Paths.get("db/RelatedObjectNotPersisted");
		if (path.toFile().exists()) {
			this.delete(path.toFile());
		}

		try {
			// Create container
			{
				final PersistenceManagerFactory pmf = this.connectDb(connectionUrl);
				final PersistenceManager pm = pmf.getPersistenceManager();
				final Transaction tx = pm.currentTransaction();

				try {
					tx.begin();
					final Contacts c1 = new Contacts(1);
					pm.makePersistent(c1);
					tx.commit();
				} finally {
					if (tx.isActive()) {
						tx.rollback();
					}
					this.disconnectDb(pmf);
				}

			}

			// create contained item
			{
				final PersistenceManagerFactory pmf = this.connectDb(connectionUrl);
				final PersistenceManager pm = pmf.getPersistenceManager();
				final Transaction tx = pm.currentTransaction();

				try {
					tx.begin();
					final Query<Contacts> q = pm.newQuery(Contacts.class);
					final List<Contacts> l = q.executeList();
					final Contacts c1 = l.get(0);
					final Person p = new Person(0, "Adam Ant");
					pm.makePersistent(p);
					c1.getList1().add(p);
					c1.getList2().add(p);
					tx.commit();
				} finally {
					if (tx.isActive()) {
						tx.rollback();
					}
					this.disconnectDb(pmf);
				}

			}

			// check for containment relationship
			{
				final PersistenceManagerFactory pmf = this.connectDb(connectionUrl);
				final PersistenceManager pm = pmf.getPersistenceManager();
				final Transaction tx = pm.currentTransaction();

				try {
					tx.begin();
					final Query<Contacts> q = pm.newQuery(Contacts.class);
					final List<Contacts> l = q.executeList();
					final Contacts c1 = l.get(0);
					Assert.assertEquals(1, c1.getList1().size());
					Assert.assertEquals(1, c1.getList2().size());
					tx.commit();
				} finally {
					if (tx.isActive()) {
						tx.rollback();
					}
					this.disconnectDb(pmf);
				}

			}

		} catch (final Throwable thr) {
			NucleusLogger.GENERAL.error(">> Exception in test", thr);
			Assert.fail("Failed test : " + thr.getMessage());
		}

		NucleusLogger.GENERAL.info(">> test END");
	}

	@Test
	public void CompoundIdentity() {
		NucleusLogger.GENERAL.info(">> test START");

		final String connectionUrl = "neo4j:db/CompoundIdentity";
		final Path path = Paths.get("db/CompoundIdentity");
		if (path.toFile().exists()) {
			this.delete(path.toFile());
		}

		final PersistenceManagerFactory pmf = this.connectDb(connectionUrl);
		final PersistenceManager pm = pmf.getPersistenceManager();
		final Transaction tx = pm.currentTransaction();
		try {
			tx.begin();
			final CompoundContainer container = new CompoundContainer();
			container.id = "cont1";
			pm.makePersistent(container);
			tx.commit();

			tx.begin();
			final Query<CompoundContainer> query2 = pm.newQuery(CompoundContainer.class);
			final List<CompoundContainer> result2 = query2.executeList();
			final CompoundContainer cont = result2.get(0);
			final CompoundItem item = new CompoundItem();
			item.owner = cont;
			item.id = "item1";
			cont.getItems().add(item);
			tx.commit();

		} catch (final Throwable thr) {
			NucleusLogger.GENERAL.error(">> Exception in test", thr);
			Assert.fail("Failed test : " + thr.getMessage());
		} finally {
			if (tx.isActive()) {
				tx.rollback();
			}
			pm.close();
		}

		pmf.close();
		NucleusLogger.GENERAL.info(">> test END");
	}
}
