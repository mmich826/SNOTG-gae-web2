package edu.depaul.se491.snotg;

import java.util.Properties;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;

public final class PMF
{
	private static final PersistenceManagerFactory mPmfInstance; // = JDOHelper.getPersistenceManagerFactory( "transactions-optional" );

	static {
		Properties newProperties = new Properties();
	      newProperties
	          .put(
	              "javax.jdo.PersistenceManagerFactoryClass",
	              "org.datanucleus.store.appengine.jdo.DatastoreJDOPersistenceManagerFactory");
	      newProperties.put("javax.jdo.option.ConnectionURL",
	          "appengine");
	      newProperties.put(
	          "javax.jdo.option.NontransactionalRead", "true");
	      newProperties.put(
	          "javax.jdo.option.NontransactionalWrite", "true");
	      newProperties.put("javax.jdo.option.RetainValues",
	          "true");
	      newProperties.put(
	          "datanucleus.appengine.autoCreateDatastoreTxns",
	          "true");
	      newProperties.put(
	          "datanucleus.appengine.autoCreateDatastoreTxns",
	          "true");
	      mPmfInstance = JDOHelper
	              .getPersistenceManagerFactory(newProperties);
	}
	
	
	private PMF() { }

	public static PersistenceManagerFactory getFactory()
	{
		return mPmfInstance;
	}
	
	public static PersistenceManager getPM()
	{
		return mPmfInstance.getPersistenceManager();
	}
}
