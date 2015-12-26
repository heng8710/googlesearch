package service;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.URLEncoder;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

import com.google.common.collect.BoundType;
import com.google.common.collect.Maps;
import com.google.common.collect.Range;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateExceptionHandler;
import jersey.repackaged.com.google.common.collect.Lists;
import my.json.JsonGetter;
import servlet.MyAppContextListener;

public class HtmlRenderervice {
	
	static byte[] render(final String htmlFile, final Map<String, Object> map) throws Exception{
		final Configuration cfg = new Configuration(Configuration.VERSION_2_3_22);
		cfg.setDirectoryForTemplateLoading(new File(Paths.get(MyAppContextListener.Classpath(), "ftl").toAbsolutePath().toString()));
		cfg.setDefaultEncoding("UTF-8");
		cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
		final Template temp = cfg.getTemplate(htmlFile);
		final ByteArrayOutputStream o = new ByteArrayOutputStream();
		final Writer out = new OutputStreamWriter(o);
		temp.process(map, out);
		return o.toByteArray();
	}
	
	
	public static byte[] index() throws Exception{
		return render("index.html", Maps.newHashMapWithExpectedSize(0));
	}
	
	
	
	public static byte[] search(final String searchText, final int pageNum, final Map<String, Object> lm) throws Exception{
		final String encodedSearchText;
		try {
			encodedSearchText = URLEncoder.encode(searchText, "utf-8");
		} catch (UnsupportedEncodingException e) {
			throw new IllegalArgumentException("searchText encoding error", e);
		}
		
		final Map<String, Object> root = Maps.newHashMap();
		root.put("searchText", searchText);
		final List<Map<String, Object>> items = (List<Map<String, Object>>)JsonGetter.getByPath(lm, "responseData.results");
		root.put("items", items);
		final long resultCount = Long.parseLong(JsonGetter.getStringByPath(lm, "responseData.cursor.estimatedResultCount"));
		root.put("resultCount", resultCount);
		
		
		
		root.put("currentPage", pageNum);
		
		final Range<Long> pagingRange = paging(resultCount, pageNum);
		final List<Map<String, Object>> pagingStrList = Lists.newArrayList();
		for(long i=pagingRange.lowerEndpoint(); i<=pagingRange.upperEndpoint(); i++){
			Map<String, Object> item = Maps.newHashMapWithExpectedSize(2);
			item.put("pageNum", i);
			item.put("url", String.format("/search?p=%s&s=%s", i, encodedSearchText));
			pagingStrList.add(item);
		}
		root.put("pagingList", pagingStrList);
		return render("search.html", root);
	}
	
	
	private static Range<Long> paging(final long resultCount, final long currentPage){
		final long maxPage = (resultCount/SearchGoogleService.NUM_PER_PAGE);
		if(maxPage <= 10){
			//1-maxpage
			return Range.range(1L, BoundType.CLOSED, maxPage, BoundType.CLOSED);
		}
		
		if(currentPage <=6 ){
			//1-10
			return Range.range(1L, BoundType.CLOSED, 10L, BoundType.CLOSED);
		}
		
		
		if(currentPage >= maxPage - 4){
			return Range.range(maxPage-9, BoundType.CLOSED, maxPage, BoundType.CLOSED);
		}
		
		return Range.range(currentPage -5, BoundType.CLOSED, currentPage + 4, BoundType.CLOSED);
	}
}
