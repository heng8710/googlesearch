package app;

import javax.ws.rs.ApplicationPath;

import org.glassfish.jersey.server.ResourceConfig;


//@ApplicationPath("")
public class RootApp extends ResourceConfig{
	public RootApp() {
        packages("res4index");
        
        //referer filter
        
        //cache headers
        
    }
}
