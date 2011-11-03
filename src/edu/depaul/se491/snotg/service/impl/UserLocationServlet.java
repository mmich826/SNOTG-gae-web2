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
import edu.depaul.se491.snotg.manager.UserLocationManager;
import edu.depaul.se491.snotg.manager.UserManager;
import edu.depaul.se491.snotg.manager.impl.UserLocationManagerImpl;
import edu.depaul.se491.snotg.manager.impl.UserManagerImpl;

public class UserLocationServlet extends HttpServlet {

	//TODO - create Factory to create concrete UserMgr
	UserLocationManager userLocMgr = new UserLocationManagerImpl(); 
		
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
              throws IOException {
       
		/*FOR AUTH com.google.appengine.api.users.UserService userService = UserServiceFactory.getUserService();
        User user = userService.getCurrentUser();*/

        List<UserLocation> userLocs = userLocMgr.getUserLocations();
        
        if (userLocs == null || userLocs.size() == 0) {
        	resp.getWriter().println("[]");
        	return;
        }
        
        JSONArray jList = new JSONArray();
        String jsonText = null;
        try {
			JSONObject obj;
			for(UserLocation l : userLocs) {
				obj = new JSONObject();
				obj.put("key", l.getKey().toString());
				obj.put("username", l.getUserName());
				obj.put("lastupdated", l.getLastUpdated());
				Loc loc = l.getLoc();
				if (loc != null) {
					obj.put("latitude", l.getLoc().getLatitude());
					obj.put("longitude", l.getLoc().getLongitude());
				}
				jList.put(obj);
			}
			StringWriter out = new StringWriter();
			jList.write(out);
			jsonText = out.toString();
		} catch (JSONException e) {
			e.printStackTrace();
		}
        
		resp.getWriter().println(jsonText);
    }
}
