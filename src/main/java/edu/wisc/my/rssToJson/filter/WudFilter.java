package edu.wisc.my.rssToJson.filter;

import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class WudFilter implements iFilter{
    protected final Logger logger = LoggerFactory.getLogger(getClass());

    public WudFilter(){
    }

    public JSONObject getFilteredJSON(JSONObject rawJSON){
        ObjectMapper om = new ObjectMapper();
        JSONObject responseObj = new JSONObject();
        JSONObject feedInfo = new JSONObject();

        try{
            logger.error("try");
            JsonNode rootNode = om.readTree(rawJSON.toString());
            logger.error(rawJSON.toString());
             
            feedInfo.put("title", rootNode.findValue("title").asText());
            feedInfo.put("link", "https://union.wisc.edu/events-and-activities/event-calendar/");
            feedInfo.put("description", rootNode.findValue("description").asText());
            feedInfo.put("pubDate", rootNode.findValue("lastBuildDate").asText());
            
            
            JsonNode events = rootNode.findValue("event");
  
            Iterator<JsonNode> iter = events.elements();
             ArrayList<JSONObject> eventNodes = new ArrayList();
            
            while(iter.hasNext()){
                JSONObject item = new JSONObject();
                JsonNode anEvent = iter.next();
                item.put("title", anEvent.findValue("event_title").asText());
                item.put("link", anEvent.findValue("url").asText());
                item.put("description",anEvent.findValue("short_description").asText());
                JSONObject thisItem = new JSONObject();
                thisItem.put("item", item);
                eventNodes.add(thisItem);
            }

            JSONArray allEvents = new JSONArray(eventNodes);
            feedInfo.put("items",allEvents);

            
        }catch(Exception e){
            logger.error("NODES " + e.getMessage());
        };

        return feedInfo;
    }

    public String healthCheck(){
        return "WudFilter health check";
    }

    
}