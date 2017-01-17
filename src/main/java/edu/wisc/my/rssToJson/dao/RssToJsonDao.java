package edu.wisc.my.rssToJson.dao;

import com.rometools.rome.feed.synd.SyndFeed;

public interface RssToJsonDao{
    
    public SyndFeed getRssFeed(String feedEndpoint);
    
}
