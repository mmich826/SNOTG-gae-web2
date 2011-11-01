package edu.depaul.se491.snotg.dao;

import java.util.Date;

import javax.jdo.JDOObjectNotFoundException;
import javax.jdo.PersistenceManager;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

import static com.google.appengine.api.datastore.FetchOptions.Builder.withLimit;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;

import edu.depaul.se491.snotg.Location;
import edu.depaul.se491.snotg.PMF;
import edu.depaul.se491.snotg.UserLocation;
import edu.depaul.se491.snotg.UserLocation.Loc;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class UserLocationTest {

    private final LocalServiceTestHelper helper =
        new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig());

    @Before
    public void setUp() {
        helper.setUp();
    }

    @After
    public void tearDown() {
        helper.tearDown();
    }

 

    @Test
    public void addUserProfileTest() {
    	DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
        assertEquals(0, ds.prepare(new Query("UserLocation")).countEntities(withLimit(10)));

        Key uLocKey = KeyFactory.createKey("snotg", "user_location");
        Entity usrProf = new Entity("UserLocation", uLocKey);
        usrProf.setProperty("userName", "mike");
        ds.put(usrProf);
        
        assertEquals(1, ds.prepare(new Query("UserLocation")).countEntities(withLimit(10)));
    }

	@Test
    public void createAndGetTest() {
    	PersistenceManager pm;  
        
		Loc loc = new Loc(41.9249, -87.6550);
		Key usrLocKey = KeyFactory.createKey("UserLocation", 456L);
		UserLocation usrLoc = new UserLocation(usrLocKey, "mike", loc);	  
		
		// prove entity doesn't yet exist in DB
		UserLocation foundUsrLoc = find(usrLocKey);  
		assertNull(foundUsrLoc);  
		
		// Now add the entity DB
		save(usrLoc);
  
		// Get the entity to prove exists in DB
		foundUsrLoc = find(usrLocKey);  
		assertNotNull("UserLocation for mike was not found.", foundUsrLoc);

		assertEquals("mike", foundUsrLoc.getUserName());
		assertEquals(456L, foundUsrLoc.getKey().getId());
		assertNotNull(foundUsrLoc.getLoc());
		assertEquals(41.9249, foundUsrLoc.getLoc().getLatitude(), .01);
    }
	
	@Test
    public void deleteTest() {
    	PersistenceManager pm;  
        
		Loc loc = new Loc(41.9249, -87.6550);
		Key usrLocKey = KeyFactory.createKey("UserLocation", 456L);
		UserLocation usrLoc = new UserLocation(usrLocKey, "mike", loc);	  
		
		// Now add the entity DB
		save(usrLoc);
  
		// Get the entity to prove exists in DB
		UserLocation foundUsrLoc = find(usrLocKey);  
		assertNotNull("UserLocation for mike was not found.", foundUsrLoc);

		assertEquals("mike", foundUsrLoc.getUserName());
		assertEquals(456L, foundUsrLoc.getKey().getId());
		assertNotNull(foundUsrLoc.getLoc());
		assertEquals(41.9249, foundUsrLoc.getLoc().getLatitude(), .01);
		
		// Now delete
		pm = PMF.getFactory().getPersistenceManager();  
		try {  
			  UserLocation loadedEntity = pm.getObjectById(UserLocation.class, usrLocKey);  
			  pm.deletePersistent( loadedEntity ); 
		} finally {  
			pm.close();  
		}  
		
		// prove entity doesn't  exist in DB anymore
		foundUsrLoc = find(usrLocKey);  
		assertNull("UserLocation for mike not deleted.", foundUsrLoc);
    }
	
	/**
	 * This is not using detachable option to enable updating once PM is closed.
	 * 
	 */
	@Test
	public void updateTest() 
	{
		PersistenceManager pm;  

		Loc loc = new Loc(41.9249, -87.6550);
		Key usrLocKey = KeyFactory.createKey("UserLocation", 456L);
		UserLocation usrLoc = new UserLocation(usrLocKey, "mike", loc);	  
		
		save(usrLoc);
		
		pm = PMF.getFactory().getPersistenceManager(); 
		UserLocation foundUsrLoc = null;
		boolean notFound = false;  
		try {  
			   foundUsrLoc = pm.getObjectById(UserLocation.class, usrLocKey);  
			   foundUsrLoc.getLoc(); // lazily loaded.  Must call embedded obj getters here to load before closing pm
			   
				// Modify and update
				foundUsrLoc.setUserName("mmich826");
				foundUsrLoc.getLoc().setLatitude(99.9999d);
		} catch (JDOObjectNotFoundException e) {  
			notFound = true;  
		} finally {  
		 pm.close();  
		}  
		assertFalse("UserLocation for mike was not found.", notFound);  

		assertEquals("mmich826", foundUsrLoc.getUserName());
		assertEquals(456L, foundUsrLoc.getKey().getId());
		assertNotNull(foundUsrLoc.getLoc());
		assertEquals(99.999, foundUsrLoc.getLoc().getLatitude(), .01);
	}
	
	/**
	 * detachable update test
	 * 
	 */
	@Test
	public void detachableUpdateTest() 
	{
		PersistenceManager pm;  

		Loc loc = new Loc(41.9249, -87.6550);
		Key usrLocKey = KeyFactory.createKey("UserLocation", 456L);
		UserLocation usrLoc = new UserLocation(usrLocKey, "mike", loc);	  
		UserLocation detachedUsrLoc = null, foundUsrLoc = null;
		
		save(usrLoc);
		
		foundUsrLoc = find(usrLocKey);  
		assertNotNull("UserLocation for mike was not found.", foundUsrLoc);

		assertEquals("mike", foundUsrLoc.getUserName());
		assertEquals(456L, foundUsrLoc.getKey().getId());
		assertNotNull(foundUsrLoc.getLoc());
		assertEquals(41.9249, foundUsrLoc.getLoc().getLatitude(), .01);

		
		pm = PMF.getFactory().getPersistenceManager(); 
		foundUsrLoc = null;
		boolean notFound = false;  
		try {  
			   foundUsrLoc = pm.getObjectById(UserLocation.class, usrLocKey);  
			   foundUsrLoc.getLoc(); // lazily loaded.  Must call embedded obj getters here to load before closing pm
			   detachedUsrLoc  = pm.detachCopy(foundUsrLoc);

		} catch (JDOObjectNotFoundException e) {  
			notFound = true;  
		} finally {  
		 pm.close();  
		}  
		
		// Now update via detachable
		detachedUsrLoc.setUserName("mmich826");
		detachedUsrLoc.getLoc().setLatitude(99.9999d);
		
		save(detachedUsrLoc);
		
		// Now verify chgs
		foundUsrLoc = find(usrLocKey);  
		assertNotNull("UserLocation for mike was not found.", foundUsrLoc);

		assertEquals("mmich826", foundUsrLoc.getUserName());
		assertEquals(456L, foundUsrLoc.getKey().getId());
		assertNotNull(foundUsrLoc.getLoc());
		assertEquals(99.999, foundUsrLoc.getLoc().getLatitude(), .01);
	}
	
	///////////////////////////////////
	private UserLocation find(Key usrLocKey)
	{
		PersistenceManager pm = PMF.getFactory().getPersistenceManager(); 
		UserLocation foundUsrLoc = null;
		boolean notFound = false;  
		try {  
			   foundUsrLoc = pm.getObjectById(UserLocation.class, usrLocKey);  
			   foundUsrLoc.getLoc(); // lazily loaded.  Must call embedded obj getters here to load before closing pm

		} catch (JDOObjectNotFoundException e) {  
			notFound = true;  
		} finally {  
		 pm.close();  
		}  
		return foundUsrLoc;
	}
	
	private void save(UserLocation usrLoc) {
		PersistenceManager pm = PMF.getFactory().getPersistenceManager();  
		try {  
			  pm.makePersistent(usrLoc);  
		} finally {  
			pm.close();  
		} 
	}
}