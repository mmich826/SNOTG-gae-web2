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
		pm = PMF.getFactory().getPersistenceManager();  
		boolean notFound = false;  
		try {  
			   pm.getObjectById(UserLocation.class, usrLocKey);  
			   fail("should have raised not found exception");  
		} catch (JDOObjectNotFoundException e) {  
		  notFound = true;  
		} finally {  
		 pm.close();  
		}  
		assertTrue(notFound);  
		
		// Now add the entity DB
		pm = PMF.getFactory().getPersistenceManager();  
		try {  
			  pm.makePersistent(usrLoc);  
		} finally {  
			pm.close();  
		}  
  
		// Get the entity to prove exists in DB
		pm = PMF.getFactory().getPersistenceManager(); 
		UserLocation foundUsrLoc = null;
		notFound = false;  
		try {  
			   foundUsrLoc = pm.getObjectById(UserLocation.class, usrLocKey);  
			   foundUsrLoc.getLoc(); // lazily loaded.  Must call embedded obj getters here to load before closing pm
		} catch (JDOObjectNotFoundException e) {  
			notFound = true;  
		} finally {  
		 pm.close();  
		}  
		assertFalse("UserLocation for mike was not found.", notFound);  

		assertEquals("mike", foundUsrLoc.getUserName());
		assertEquals(456L, foundUsrLoc.getKey().getId());
		assertNotNull(foundUsrLoc.getLoc());
		assertEquals(41.9249, foundUsrLoc.getLoc().getLatitude(), .01);
    }
}