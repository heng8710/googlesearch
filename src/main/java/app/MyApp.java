package app;

import javax.ws.rs.ApplicationPath;

import org.glassfish.jersey.server.ResourceConfig;


@ApplicationPath("search")
public class MyApp extends ResourceConfig{
	public MyApp() {
        packages("resource");
        
        //referer filter
        
        //cache headers
        
    }
}
