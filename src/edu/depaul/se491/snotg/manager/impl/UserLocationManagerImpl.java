package edu.depaul.se491.snotg.manager.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

import edu.depaul.se491.snotg.UserLocation;
import edu.depaul.se491.snotg.UserLocation.Loc;
import edu.depaul.se491.snotg.manager.UserLocationManager;

public class UserLocationManagerImpl implements UserLocationManager {

	@Override
	public void updateUserLocation(UserLocation userLog) {
		// TODO Auto-generated method stub

	}

	@Override
	public List<UserLocation> getUserLocations() {
		ArrayList<UserLocation> usrLocList = new ArrayList<UserLocation>();
		// TEMP until data access is working
		long t = new Timestamp(new Date().getTime()).getTime();
    	long m = 2*60*1000;
    	
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
		
		return usrLocList;
	}

}
