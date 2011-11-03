package edu.depaul.se491.snotg.dao;

import java.util.ArrayList;
import java.util.List;
import javax.jdo.Query;
import javax.jdo.PersistenceManager;


import edu.depaul.se491.snotg.PMF;
import edu.depaul.se491.snotg.UserLocation;

public class UserLocationDao {

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
}
