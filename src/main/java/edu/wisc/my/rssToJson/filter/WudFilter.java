package edu.wisc.my.rssToJson.filter;

import java.util.Iterator;

import javax.json.JsonObject;

import org.json.JSONArray;
import org.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class WudFilter implements iFilter{
    protected final Logger logger = LoggerFactory.getLogger(getClass());

    public WudFilter(){
        logger.error("WudFilter Constructor");
    }

    public JSONObject getFilteredJSON(JSONObject rawJSON){
        ObjectMapper om = new ObjectMapper();
        JSONObject responseObj = new JSONObject();
        JSONObject feedInfo = new JSONObject();
        
        try{
            logger.error(rawJSON.toString());
            JsonNode rootNode = om.readTree(rawJSON.toString());
            logger.error(rootNode.findValue("title").asText());

            feedInfo.put("title", rootNode.findValue("title").asText());
            feedInfo.put("link", "https://union.wisc.edu/events-and-activities/event-calendar/");
            feedInfo.put("description", rootNode.findValue("description").asText());
            feedInfo.put("pubDate", rootNode.findValue("lastBuildDate").asText());
            responseObj.
            JsonNode eventsArray = rootNode.path("event");
            logger.error(eventsArray.toString());
        }catch(Exception e){
            logger.error("NODES " + e.getMessage());
        };
/*
        
        try{
        //logger.error(rawJSON.toString());
        int e = 0;
        logger.error("IN THE WUDFILTER METHOD");
        e++; //1
        

        JSONArray events = rawJSON.getJSONArray("event");
        e++; //2
        for(int i = 0; i < events.length(); i++){
            e++; 
           JSONObject event = events.getJSONObject(i);
           
           responseObj.put("short_description", event.getString("short_description"));
        }
        
    }catch(Exception e){
        logger.error("step " + e + " in WudFilter");
    }
    
    */
    
        return responseObj;
    }

    public String healthCheck(){
        return "Splendiferous WUD";
    }

    
}