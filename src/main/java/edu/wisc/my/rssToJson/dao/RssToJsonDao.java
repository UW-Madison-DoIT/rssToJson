package edu.wisc.my.rssToJson.dao;

import com.rometools.rome.feed.synd.SyndFeed;
import org.json.JSONObject;

public interface RssToJsonDao{
    public JSONObject getXMLFeed(String feedEndpoint);
    public SyndFeed getRssFeed(String feedEndpoint);
    public String getEndpointURL(String feedEndpoint);
    
}
