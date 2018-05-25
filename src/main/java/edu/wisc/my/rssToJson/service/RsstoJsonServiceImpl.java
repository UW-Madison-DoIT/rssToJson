package edu.wisc.my.rssToJson.service;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;

import edu.wisc.my.rssToJson.dao.RssToJsonDao;


@Service
public class RsstoJsonServiceImpl implements RssToJsonService {
    protected final Logger logger = LoggerFactory.getLogger(getClass());

    private RssToJsonDao rssToJsonDao;

    @Autowired
    void setRssToJsonDao(RssToJsonDao rssToJsonDao){
        this.rssToJsonDao = rssToJsonDao;
    }

    @Override
    public JSONObject getJsonFromURL(String endpoint) {
        SyndFeed feed = rssToJsonDao.getRssFeed(endpoint);
        if(feed == null){
            logger.warn("No feed returned for endpoint: {}", endpoint);
            return null;
        }
        JSONObject jsonToReturn = new JSONObject();

        JSONObject feedInfo = new JSONObject();
        feedInfo.put("title", feed.getTitle());
        feedInfo.put("link", feed.getLink());
        feedInfo.put("description", feed.getDescription());
        feedInfo.put("pubDate", feed.getPublishedDate());
        jsonToReturn.put("feed", feedInfo);
        JSONArray entries = new JSONArray();
        for(SyndEntry entry : feed.getEntries()){
            JSONObject feedItem = new JSONObject();
            feedItem.put("title", entry.getTitle());
            feedItem.put("link", entry.getLink());
            feedItem.put("description", entry.getDescription().getValue());
            feedItem.put("pubDate", entry.getPublishedDate());
            entries.put(feedItem);
        }
        jsonToReturn.put("items", entries);
        jsonToReturn.put("status", "ok");
        String stringToClean = jsonToReturn.toString();
        // replace right single curly quote with apostrophe
        stringToClean =  stringToClean.replaceAll("\\\u2019", "'");
        // replace en-dash with minus
        stringToClean = stringToClean.replaceAll("\\\u2014", "-");
        return new JSONObject(stringToClean);
    }
}
