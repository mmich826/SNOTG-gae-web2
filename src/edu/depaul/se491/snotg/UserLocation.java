package edu.depaul.se491.snotg;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Blob;
import com.google.appengine.api.datastore.Key;

@PersistenceCapable
public class UserLocation 
{
    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Key key;

    @Persistent
    private String userName;
    
    @Persistent
    private Location loc;
    
    public UserLocation() { super();}
    
    public UserLocation(Key key, String usrName)
    {
    	this.key = key;
    	this.userName = usrName;
    }

    public UserLocation(String usrName)
    {
    	this.userName = usrName;
    }
    
    public UserLocation(Key key, String usrName, Location loc)
    {
    	this.key = key;
    	this.userName = usrName;
    	this.loc = loc;
    }
    
    
    
    public Key getKey() {
        return key;
    }
    
	public void setKey(Key key) {
		this.key = key;
	}

    public void setUserName(String user)
    {
    	this.userName = user;
    }
    
    public String getUserName()
    {
    	return userName;
    }

	public Location getLoc() {
		return loc;
	}

	public void setLoc(Location loc) {
		this.loc = loc;
	}
}