package edu.depaul.se491.snotg.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.jdo.Query;
import javax.jdo.PersistenceManager;


import edu.depaul.se491.snotg.PMF;
import edu.depaul.se491.snotg.UserLocation;

public class UserLocationDao {

	private final static Logger LOGGER = Logger.getLogger("UserLocationDao");
	
	/**
	 * 
	 * @return List - NOTE cannot return an ArrayList.  Result cannot be cast to ArrayList
	 */
	public List<UserLocation> getUserLocations() {
		PersistenceManager pm = PMF.getFactory().getPersistenceManager();
		
		Query query = pm.newQuery(UserLocation.class);
		query.setOrdering("lastUpdated desc");
		
		List<UserLocation> results = null;
		try {
			results = (List<UserLocation>) query.execute();
		} finally {
		    query.closeAll();
		}
		return results;		
	}
	
	public boolean updateUserLocation(UserLocation userLoc) {
		PersistenceManager pm = PMF.getFactory().getPersistenceManager();
		
		List<UserLocation> results = null;
		Query query = null;
		try {
			query = pm.newQuery(UserLocation.class, 
						"userName == userNameParam");
			query.declareParameters("String userNameParam");
			
			results = (List<UserLocation>) query.execute(userLoc.getUserName());
			if (results != null && results.size()==1) {
				UserLocation userLocPersist = results.get(0);
				userLocPersist.setLastUpdated(new Date());
				userLocPersist.setLoc(userLoc.getLoc());
			}
			else {
				LOGGER.log(Level.WARNING, "Unable to fine user:  " + userLoc.getUserName());
			}
			
		}
		catch(Exception e) {
			LOGGER.log(Level.WARNING, "Error trying to find user:  " + userLoc.getUserName());
			e.printStackTrace();
		}
		finally {
		    query.closeAll();
		    pm.close();
		}
		return false;
	}
}
