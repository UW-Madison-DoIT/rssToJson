package main.java.edu.wisc.my.rssToJson.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import main.java.edu.wisc.my.rssToJson.service.IRSSToJSONService;

@Controller
public class RSSToJSONController {
	
	  protected final Logger logger = LoggerFactory.getLogger(getClass());
	  private IRSSToJSONService rssToJsonService;
	  private final String ERROR_MESSAGE = "{\"Your rss feed may be unavailable at this time. Please try again later.\"}";
	private Environment env;
	  
	  @Autowired
	    public void setEnv(Environment env) { this.env = env; }
	  
	  @Autowired
	    public void setRSSToJSONService(IRSSToJSONService rssToJsonService){
	        this.rssToJsonService = rssToJsonService;
	    }
	    
	  
	  private IRSSToJSONService getRssToJsonService() {
		return rssToJsonService;
	}
	  
	  @RequestMapping(value="/rssTransform/{url}")
	  public @ResponseBody void getJsonifiedRssUrl(HttpServletRequest request, HttpServletResponse response, @PathVariable String url){
	        
		  String returnBody = null;
		  try{
			 returnBody = getRssToJsonService().jsonifiedRssUrl(url);
			  
		  }catch(Exception e){
			  returnBody = ERROR_MESSAGE;
		  }
		  
	        try {
	            if(isJSONValid(returnBody)) {
	              //valid json, cool, write it
	              response.getWriter().write(returnBody);
	            }
	            else {
	              //if its not valid JSON (backwards compatible, wrap in a value object
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
