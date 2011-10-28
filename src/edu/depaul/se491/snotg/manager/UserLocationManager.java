package edu.depaul.se491.snotg.manager;

import java.util.List;

import edu.depaul.se491.snotg.UserLocation;
import edu.depaul.se491.snotg.UserProfile;

public interface UserLocationManager {
	
	public void updateUserLocation(UserLocation userLog);
	public List<UserLocation> getUserLocations();
	
}
