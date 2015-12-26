package resource;

import java.net.URI;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import com.google.common.base.Strings;

import service.HtmlRenderervice;
import service.SearchGoogleService;

@Path("")
public class SearchResource {

	
	@GET
	@Produces("text/html")
	public Response doGet(@QueryParam("s")final String searchText, @QueryParam("p")final String pageNum) throws Exception{
//		return Response.seeOther(URI.create("/movie/recommend/random")).build();
		int page = 0;
		if(!Strings.isNullOrEmpty(pageNum)){
			try {
				page = Integer.parseInt(pageNum);
			} catch (Exception e) {
				Logger.getGlobal().log(Level.SEVERE, String.format("SearchResource 请求查询的pageNum=[%s]格式不正确", pageNum), e);
				page = 0;
			}
		}
		return query(searchText, page >= 1? page: 1);
	}
	
	
	@POST
	@Produces("text/html")
	public Response doPost(@FormParam("s")final String searchText) throws Exception{
//		return Response.seeOther(URI.create("/movie/recommend/random")).build();
		return query(searchText, 1);
	}
	
	
	Response query(final String searchText, final int pageNum){
		
		if(Strings.isNullOrEmpty(searchText)){
			return Response.seeOther(URI.create("/")).build();
		}
		final Map<String, Object> r;
		try {
			r = SearchGoogleService.search(searchText, pageNum);
		} catch (Exception e) {
			Logger.getGlobal().log(Level.SEVERE, "SearchGoogleService 查询 google search api 出错", e);
			return Response.seeOther(URI.create("/")).build();
		}
		try {
			return Response.ok(HtmlRenderervice.search(searchText, pageNum, r)).build();
		} catch (Exception e) {
			Logger.getGlobal().log(Level.SEVERE, "SearchHtmlService 渲染 search.html 出错", e);
			return Response.seeOther(URI.create("/")).build();
		}
	}
}
