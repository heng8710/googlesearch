package service;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.core.Response;

import com.fasterxml.jackson.databind.ObjectMapper;

import my.json.JsonGetter;

public class SearchGoogleService {
	
	static final int NUM_PER_PAGE = 8;

	public static Map<String, Object> search(final String searchText, final int pageNum) throws Exception{
		final String encodedSearchText;
		try {
			encodedSearchText = URLEncoder.encode(searchText, "utf-8");
		} catch (UnsupportedEncodingException e) {
			throw new IllegalArgumentException("searchText encoding error", e);
		}
		final int start = pageNum> 1? (pageNum -1)* NUM_PER_PAGE:  0;
		final String url = String.format("https://ajax.googleapis.com/ajax/services/search/web?v=1.0&rsz=%S&start=%s&q=%s", NUM_PER_PAGE ,start, encodedSearchText);
		return queryGoogle(url);
		
	}
	
	private static Builder newClient(final String url){
		return ClientBuilder.newClient().target(url).request()
			.header("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/47.0.2526.73 Safari/537.36 OPR/34.0.2036.42")
			;
	}
	
	private static Map<String, Object> queryGoogle(final String url) throws Exception{
		//只试x次，不成功，就返回异常了
		for(int i=0;i < 3; i++){
			final Builder b = newClient(url);
			final Response resp;
			try {
				resp = b.get();
				if(resp.getStatus() != 200){
					throw new IllegalStateException(String.format("google api查询出错了, resp.header=%s, resp.body=%s", resp.getStringHeaders(), resp.readEntity(String.class)));
				}
			} catch (Exception e) {
				Logger.getGlobal().log(Level.WARNING, "SearchGoogleService 查询googleapi出错", e);
				continue;
			}
			final byte[] r = resp.readEntity(byte[].class);
			return outOfRangStart(r);
		}
		throw new IllegalStateException();
	}
	
	
	//{"responseData": null, "responseDetails": "out of range start", "responseStatus": 400}
	private static Map<String, Object> outOfRangStart(final byte[] r) throws Exception{
		final Map<String, Object> lm = new ObjectMapper().readValue(r, Map.class);
		final int status = JsonGetter.getIntegerByPath(lm, "responseStatus");
		if(status == 200){
			return lm;
		}
		
		if(status == 400 && "out of range start".equals(JsonGetter.getStringByPath(lm, "responseDetails").toLowerCase())){
			throw new IllegalStateException("search error, google search api return 400 status");
		}
		
		throw new IllegalStateException("search error, google search api status not expected");
	}
	
	
	public static void main(String...strings) throws Exception{
		search("刘飞儿", 2);
	}
}
