package edu.depaul.se491.snotg.dao;

import java.sql.Timestamp;
import java.util.ArrayList;
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
import com.google.appengine.repackaged.org.json.JSONArray;
import com.google.appengine.repackaged.org.json.JSONException;
import com.google.appengine.repackaged.org.json.JSONObject;
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

public class PopulateData {

    private LocalServiceTestHelper helper;

    @Before
    public void setUp() {
        helper.setUp();
    }

    @After
    public void tearDown() {
        helper.tearDown();
    }

 
    public PopulateData() {
    	LocalDatastoreServiceTestConfig config = new LocalDatastoreServiceTestConfig();
    	config.setBackingStoreLocation("C:/git_sandbox/SNOTG-gae-web2/war/WEB-INF/appengine-generated");
    	config.setNoStorage(false);
    	helper = new LocalServiceTestHelper(config);
    }

    @Test
    public void addUserProfileTest() {
    	DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
        
		Loc loc = new Loc(41.9249, -87.6550);
		Key usrLocKey = KeyFactory.createKey("UserLocation", 456L);
		UserLocation usrLoc = new UserLocation(usrLocKey, "mike", loc);	  
		
		save(usrLoc);

    }

    @Test
    public void junk() {
    	long t = new Timestamp(new Date().getTime()).getTime();
    	long m = 2*60*1000;
    	
		ArrayList<UserLocation> usrLocList = new ArrayList<UserLocation>();
		Loc loc = new Loc(41.9249, -87.6550);
		Key usrLocKey = KeyFactory.createKey("UserLocation", 456L);
		UserLocation usrLoc = new UserLocation(usrLocKey, "mike", loc);	 
		usrLoc.setLastUpdated( new Timestamp(t+m));
		usrLocList.add(usrLoc);
		
		Loc loc1 = new Loc(41.9333, -87.6550);
		Key usrLocKey1 = KeyFactory.createKey("UserLocation", 123L);
		UserLocation usrLoc1 = new UserLocation(usrLocKey1, "daveyjones", loc1);	 
		usrLoc1.setLastUpdated( new Timestamp(t+m*2));
		usrLocList.add(usrLoc1);
		
		Loc loc2 = new Loc(41.92343, -88.00123);
		Key usrLocKey2 = KeyFactory.createKey("UserLocation", 333L);
		UserLocation usrLoc2 = new UserLocation(usrLocKey2, "freddie Merc", loc2);	 
		usrLoc2.setLastUpdated( new Timestamp(t+m*3));
		usrLocList.add(usrLoc2);
		
		Loc loc3 = new Loc(41.9333, -87.6550);
		Key usrLocKey3 = KeyFactory.createKey("UserLocation", 789L);
		UserLocation usrLoc3 = new UserLocation(usrLocKey3, "WillieNelson", loc3);	 
		usrLoc3.setLastUpdated( new Timestamp(t+m*4));
		usrLocList.add(usrLoc3);
		

	    String jsonText = null;
	    try {
			JSONObject obj;
			JSONArray jList = new JSONArray();
			/*obj.put("msg","Response from GAE User Service - ");
			obj.put("ts",new Date().toString());
			StringWriter out = new StringWriter();
			obj.write(out);
			jsonText = out.toString()usrLocList*/
			for(UserLocation l : usrLocList) {
				obj = new JSONObject();
				obj.put("key", l.getKey().toString());
				obj.put("username", l.getUserName());
				obj.put("lastupdated", l.getLastUpdated());
				loc = l.getLoc();
				if (loc != null) {
					obj.put("latitude", l.getLoc().getLatitude());
					obj.put("longitude", l.getLoc().getLongitude());
				}
				jList.put(obj);
			}
			//System.out.println(usrLocList.toString());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
		}

	
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