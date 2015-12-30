package app;

import javax.ws.rs.ApplicationPath;

import org.glassfish.jersey.server.ResourceConfig;


@ApplicationPath("search")
public class SearchApp extends ResourceConfig{
	public SearchApp() {
        packages("resource");
        
        //referer filter
        
        //cache headers
        
    }
}
