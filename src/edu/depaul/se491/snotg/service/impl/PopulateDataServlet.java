package edu.depaul.se491.snotg.service.impl;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Date;
import java.util.List;

import javax.servlet.http.*;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.appengine.repackaged.org.json.JSONArray;
import com.google.appengine.repackaged.org.json.JSONException;
import com.google.appengine.repackaged.org.json.JSONObject;

import edu.depaul.se491.snotg.UserLocation;
import edu.depaul.se491.snotg.UserLocation.Loc;
import edu.depaul.se491.snotg.UserProfile;
import edu.depaul.se491.snotg.dao.PopulateData;
import edu.depaul.se491.snotg.manager.UserLocationManager;
import edu.depaul.se491.snotg.manager.UserManager;
import edu.depaul.se491.snotg.manager.impl.UserLocationManagerImpl;
import edu.depaul.se491.snotg.manager.impl.UserManagerImpl;

public class PopulateDataServlet extends HttpServlet {

	
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
              throws IOException {
       
        PopulateData dataSeeder = new PopulateData();
        dataSeeder.populateUserLocation();
        
		resp.getWriter().println(new Date().toString());
    }
}
