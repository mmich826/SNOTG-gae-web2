package edu.depaul.se491.snotg.manager.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

import edu.depaul.se491.snotg.UserLocation;
import edu.depaul.se491.snotg.UserLocation.Loc;
import edu.depaul.se491.snotg.dao.UserLocationDao;
import edu.depaul.se491.snotg.manager.UserLocationManager;

public class UserLocationManagerImpl implements UserLocationManager {

	UserLocationDao userLocDao;
	
	public UserLocationManagerImpl() {
		userLocDao = new UserLocationDao();
	}
	
	@Override
	public void updateUserLocation(UserLocation userLoc) {
		userLocDao.updateUserLocation(userLoc);

	}

	@Override
	public List<UserLocation> getUserLocations() {
		List<UserLocation> usrLocList;
		usrLocList = userLocDao.getUserLocations();
		
		return usrLocList;
	}

}
