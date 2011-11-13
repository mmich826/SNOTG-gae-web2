package edu.depaul.se491.snotg.service.impl;

import static edu.depaul.se491.snotg.SnotgConstants.INVALID_REQUEST_PARAM_MSG;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import com.google.appengine.repackaged.org.json.JSONArray;
import com.google.appengine.repackaged.org.json.JSONException;
import com.google.appengine.repackaged.org.json.JSONObject;

import edu.depaul.se491.snotg.UserLocation;
import edu.depaul.se491.snotg.UserLocation.Loc;
import edu.depaul.se491.snotg.UserLocationJson;
import edu.depaul.se491.snotg.manager.UserLocationManager;
import edu.depaul.se491.snotg.manager.impl.UserLocationManagerImpl;

/**
 * This class represents a restful service.  The service contracts are:
 * 
 * Requests are returned as json strings
 * @author mmichalak
 *
 */
public class UserLocationServlet extends HttpServlet {

	//TODO - create Factory to create concrete UserMgr
	UserLocationManager userLocMgr = new UserLocationManagerImpl(); 
		
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
              throws IOException {
		String jsonResponse = null;
       
		if (req.getParameter("get_user_locs") != null)
			jsonResponse = getUserLocations(req);
		else if (req.getParameter("user_heartbeat") != null)
			jsonResponse = handleHeartbeat(req);
		else
			jsonResponse = INVALID_REQUEST_PARAM_MSG;
		
		resp.getWriter().println(jsonResponse);
    }
	
	/**
	 * user_locations?get_user_locs
	 * Retrieves list of active user geo locations
	 * @return String
	 */
	private String getUserLocations(HttpServletRequest req) {
		/*FOR AUTH com.google.appengine.api.users.UserService userService = UserServiceFactory.getUserService();
        User user = userService.getCurrentUser();*/
        
		String jsonText = null;
        
        List<UserLocation> userLocs = userLocMgr.getUserLocations();
        
        if (userLocs == null || userLocs.size() == 0) {
        	return "[]";
        }
        
        JSONArray jList = new JSONArray();
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
		
		return jsonText;
	}
	
	/*
	 * user_locations?heartbeat 
	 * This takes and save the current users location and returns
	 * the list of active user geo locations (as with get_user_locs call).
	 */
	private String handleHeartbeat(HttpServletRequest req) {
		UserLocation userLoc = new UserLocation();
		
		String reqJson = req.getParameter("request_json");
		//TODO check for null; Return error
		
		try {
			ObjectMapper mapper = new ObjectMapper();
			UserLocationJson userJson = mapper.readValue(reqJson.getBytes(), UserLocationJson.class);			
			//mapper.writeValue(System.out, user); // where 'dst' can be File, OutputStream or Writer
			// Translate values from Json obj to backend object
			userLoc.setUserName(userJson.getUserName());
			UserLocation.Loc loc = new UserLocation.Loc(userJson.getLatit().doubleValue(), userJson.getLongit().doubleValue());
			userLoc.setLoc(loc);
			userLoc.setLastUpdated(new Date());
		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}     
		/*userLoc = new UserLocation();
		userLoc.setUserName("mike");
		UserLocation.Loc loc = new UserLocation.Loc(123.1234, 555.4444);
		userLoc.setLoc(loc);*/
		
		userLocMgr.updateUserLocation(userLoc);
		
		String jsonText = getUserLocations(req);
		
		return jsonText;
	}
}
