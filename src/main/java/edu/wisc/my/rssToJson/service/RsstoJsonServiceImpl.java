package edu.wisc.my.rssToJson.service;

import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import edu.wisc.my.rssToJson.dao.RssToJsonDao;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.regex.Pattern;


@Service
public class RsstoJsonServiceImpl implements RssToJsonService {
    protected final Logger logger = LoggerFactory.getLogger(getClass());

    private RssToJsonDao rssToJsonDao;

    // JsonArray of unicode characters
    // offender is the unicode character we're replacing
    // replacement is the ASCII character we're subbing in.
    private String unicodeToClean = "{\"replacements\":[{" +
      "\"offender\":" + "\"\\\\" + "u2019" + "\"," + //right curly single quote
      "\"replacement\":" + "\"'\"" + // single quote
      "}, {" +
      "\"offender\":" + "\"\\\\" + "u2014" + "\"," + //en dash
      "\"replacement\":" + "\"-\"" + "}" + //minus sign
      "]}";

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
        //Clean undesirable unicode literals from the JsonObject
        JSONObject unicodeReplacements = new JSONObject(unicodeToClean);
        JSONArray replacements = unicodeReplacements.getJSONArray("replacements");
        String jsonStringToClean = jsonToReturn.toString();
        // for each string replacement in the resource file, we replace with the alternative.
        for (int i = 0; i < replacements.length(); ++i) {
          JSONObject replacement = replacements.getJSONObject(i);
          String replaceThis = Pattern.quote(replacement.getString("offender")) ;
          String withThat = replacement.getString("replacement");
          jsonStringToClean = jsonStringToClean.replaceAll(replaceThis, withThat);
        }
        //Reconstitute the JsonObject
        jsonToReturn = new JSONObject(jsonStringToClean);
        return jsonToReturn;
    }

}
