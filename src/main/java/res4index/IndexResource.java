package res4index;

import java.net.URI;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import service.HtmlRenderervice;

/**
 * 根路径，转向index.html
 */
@Path("")
public class IndexResource {

	
	@GET
	@Produces("text/html")
	public Response doGet() throws Exception{
//		return Response.seeOther(URI.create("/movie/recommend/random")).build();
		return query();
	}
	
	
	@POST
	@Produces("text/html")
	public Response doPost() throws Exception{
//		return Response.seeOther(URI.create("/movie/recommend/random")).build();
		return query();
	}
	
	
	Response query(){
		try {
			return Response.ok(HtmlRenderervice.index()).build();
		} catch (Exception e) {
			Logger.getGlobal().log(Level.SEVERE, "SearchHtmlService渲染index.html出错", e);
			return Response.seeOther(URI.create("/index.html")).build();
		}
	}
	
	
}
