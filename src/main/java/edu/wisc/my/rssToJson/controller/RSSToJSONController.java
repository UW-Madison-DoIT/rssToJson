package main.java.edu.wisc.my.rssToJson.controller;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import main.java.edu.wisc.my.rssToJson.service.IRssToJsonService;

@Controller
public class RSSToJSONController {
	

	
	  protected final Logger logger = LoggerFactory.getLogger(getClass());
	  private IRssToJsonService rssToJsonService;
	  private String ERROR_MESSAGE = "Your rss feed may be unavailable at this time. Please try again later.";
	  private int errStep = 0;
	  private Environment env;
	  
	  
	  
	  @Autowired
	    public void setEnv(Environment env) { this.env = env; }
	  
	  @Autowired
	    public void setRSSToJSONService(IRssToJsonService rssToJsonService){
	        this.rssToJsonService = rssToJsonService;
	    }
	  
	  /**
	     * Status page
	     * @param response the thing to write to
	     */
	    @RequestMapping("/rssTransform")
	    public @ResponseBody void index(HttpServletResponse response){
	        
	    	try {
	          JSONObject responseObj = new JSONObject();
	          responseObj.put("status", "up");

	          response.getWriter().write(responseObj.toString());
	          response.setContentType("application/json");
	          response.setStatus(HttpServletResponse.SC_OK);
	        } catch (IOException e) {
	            logger.error("Issues happened while trying to write Status", e);
	            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
	        }
	    }
	    
	  @RequestMapping(value="/rssTransform/demo/football")
	  public @ResponseBody void getDemoJson(HttpServletRequest request, HttpServletResponse response){
           logger.warn("STEP ONE I AM IN THE CONTROLLER");
	       String paramaterizedURL = env.getRequiredProperty("demo.url");
	       if(!StringUtils.isEmpty(paramaterizedURL)){
	    	   getJsonifiedRssUrl(request, response, paramaterizedURL);
	       }
	       
	  }
	  
	  private String getErrorString(){
		  return ERROR_MESSAGE + " =" + errStep + " " + env.getProperty("demo.url");
	  }
	  
	  @RequestMapping(value="/rssTransform/{url}")
	  public @ResponseBody void getJsonifiedRssUrl(HttpServletRequest request, HttpServletResponse response, @PathVariable String url){
	   
		logger.warn("STEP TWO: I AM IN THE REQUIRED CONTROLLER METHOD FOR " + url);  
	    
		String returnBody = "";
		try {
			logger.warn("3. Here?");  
			URL rssUrl = new URL(url);
			logger.warn("4. Here?"); 
			HttpURLConnection urlConnection = (HttpURLConnection) rssUrl
					.openConnection();
			logger.warn("5. Here?"); 
			InputStream in = new BufferedInputStream(urlConnection.getInputStream());
			
			logger.warn("6. Here? " + getRssToJsonService().toString()); 
			logger.warn(getRssToJsonService().testMe());
			logger.warn("7. Here?"); 
			returnBody = getRssToJsonService().getJsonFromURL(url, in);
			
		
		} catch (Exception e) {
			e.printStackTrace();
			logger.warn(e.getMessage());
			returnBody = getErrorString();
		}
		logger.warn("RETURN " + returnBody);
		
		try {
			if (isJSONValid(returnBody)) {
				response.getWriter().write(returnBody);
				response.setContentType("application/json");
				response.setStatus(HttpServletResponse.SC_OK);
			} else {
				JSONObject responseObj = new JSONObject();
				responseObj.put("returnBody", returnBody);
				response.getWriter().write(responseObj.toString());
			}
			response.setContentType("application/json");
			response.setStatus(HttpServletResponse.SC_OK);
		} catch (IOException e) {
			logger.error("Issues happened while trying to write json", e);
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
	}
	    
	  private IRssToJsonService getRssToJsonService() {
		return this.rssToJsonService;
	}

	private boolean isJSONValid(String test) {
	      try {
	          new JSONObject(test);
	      } catch (JSONException ex) {
	          try {
	              new JSONArray(test);
	          } catch (JSONException ex1) {
	              return false;
	          }
	      } 
	      return true;
	  }
	  
	  

}
