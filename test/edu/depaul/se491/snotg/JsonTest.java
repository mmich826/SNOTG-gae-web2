package edu.depaul.se491.snotg;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Map;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import edu.depaul.se491.snotg.UserProfile;
import edu.depaul.se491.snotg.manager.UserManager;
import edu.depaul.se491.snotg.manager.impl.UserManagerImpl;


public class JsonTest {


    
	@Test
	public void testParseUserLocationJson() {
		String jsonString = "{\"userName\":\"mike\",\"latit\":\"123.1234\",\"longit\":\"555.4444\"}";
		//String jsonString = "{\"userName\":\"mike\"}";
		
		try {
			ObjectMapper mapper = new ObjectMapper();
			UserLocationJson user = mapper.readValue(jsonString.getBytes(), UserLocationJson.class);
			
			mapper.writeValue(System.out, user); // where 'dst' can be File, OutputStream or Writer
			//Object root = mapper.readValue(src, Object.class);
			//Map<?,?> rootAsMap = mapper.readValue(src, Map.class);
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
	}

}
