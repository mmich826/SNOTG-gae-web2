package edu.depaul.se491.snotg.service.impl;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Date;

import javax.servlet.http.*;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.appengine.repackaged.org.json.JSONException;
import com.google.appengine.repackaged.org.json.JSONObject;

import edu.depaul.se491.snotg.UserProfile;
import edu.depaul.se491.snotg.manager.UserManager;
import edu.depaul.se491.snotg.manager.impl.UserManagerImpl;

public class UserServlet extends HttpServlet {
//TODO - create Factory to create concrete UserMgr
	UserManager userMgr = new UserManagerImpl(); 
		
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
              throws IOException {
       
		com.google.appengine.api.users.UserService userService = UserServiceFactory.getUserService();
        User user = userService.getCurrentUser();

/*        if (user != null) {
            resp.setContentType("text/plain");
            resp.getWriter().println("Hello, " + user.getNickname());
        } else {
            resp.sendRedirect(userService.createLoginURL(req.getRequestURI()));
        }*/
        
        UserProfile userProf = userMgr.findUserProfile(12345L);
        
        String jsonText = null;
        try {
			JSONObject obj=new JSONObject();
			obj.put("msg","Response from GAE User Service - ");
			obj.put("ts",new Date().toString());
			//obj.put("num",new Integer(100));
			//obj.put("balance",new Double(1000.21));
			//obj.put("is_vip",new Boolean(true));
			//obj.put("nickname",null);
			StringWriter out = new StringWriter();
			obj.write(out);
			jsonText = out.toString();
			System.out.print(jsonText);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
		resp.getWriter().println(jsonText);
    }
}
