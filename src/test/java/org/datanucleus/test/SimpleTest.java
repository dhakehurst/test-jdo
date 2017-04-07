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

	@Test
	public void simpleTest() {
		NucleusLogger.GENERAL.info(">> test START");

		final Path path = Paths.get("db/neo4j");
		if (path.toFile().exists()) {
			this.delete(path.toFile());
		}

		final String connectionDriver = "org.neo4j.jdbc.Driver";
		final String connectionUrl = "neo4j:db/neo4j";

		final Map<String, Object> props = new HashMap<>();
		props.put("javax.jdo.PersistenceManagerFactoryClass", "org.datanucleus.api.jdo.JDOPersistenceManagerFactory");
		props.put("javax.jdo.option.ConnectionDriverName", connectionDriver);
		props.put("javax.jdo.option.ConnectionURL", connectionUrl);
		props.put("datanucleus.schema.autoCreateAll", "true");

		final PersistenceManagerFactory pmf = JDOHelper.getPersistenceManagerFactory(props);

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
			Assert.assertTrue(isEmpty2);

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
