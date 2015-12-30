package servlet;

import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;


@WebListener
public class MyAppContextListener implements ServletContextListener {

	private static volatile String webRoot;
	private static volatile String webInfo;
	private static volatile String classpath;
	
	
	@Override
	public void contextInitialized(ServletContextEvent sce) {
		webRoot = sce.getServletContext().getRealPath("/");
		webInfo = Paths.get(webRoot, "WEB-INF").toAbsolutePath().toString();
		classpath = Paths.get(webRoot, "WEB-INF", "classes").toAbsolutePath().toString();
		Logger.getGlobal().log(Level.INFO, String.format("servlet启动，webroot=[%s], web-info=[%s], classpath=[%s]", webRoot, webInfo, classpath));
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		Logger.getGlobal().log(Level.INFO, String.format("servlet关闭，webroot=[%s], web-info=[%s], classpath=[%s]", webRoot, webInfo, classpath));
	}

	/**WebRoot在文件系统中的路径
	 * @return
	 */
	public static String WebRoot(){
		return webRoot;
	}
	
	
	/**
	 * WebInfo在文件系统中的路径
	 * @return
	 */
	public static String WebInfo(){
		return webInfo;
	}
	
	/**
	 * Classpath在文件系统中的路径
	 * @return
	 */
	public static String Classpath(){
		return classpath;
	}
}
