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
        return getJSONCleaned(jsonToReturn);
    }

    private JSONObject getJSONCleaned(JSONObject jsonToClean) {

      String jsonString = jsonToClean.toString();
      String tellTaleHighBitIndicator = "\\u";

      if (jsonString.contains(tellTaleHighBitIndicator)) {
        try {
          Resource resource = new ClassPathResource("stringCleaner.json");
          // resource should be a valid json file containing an array of string replacement objects
          BufferedReader br = new BufferedReader(new InputStreamReader(resource.getInputStream()));
          StringBuilder stringBuilder = new StringBuilder();
          String line;
          while ((line = br.readLine()) != null) {
            stringBuilder.append(line).append(' ');
          }
          br.close();
          String json = stringBuilder.toString();
          JSONObject fileContents = new JSONObject(json);
          JSONArray replacements = fileContents.getJSONArray("replacements");
          // for each string replacement specified in the resource file,
          // we run a replace with the specified alternative.
          for (int i = 0; i < replacements.length(); ++i) {
              JSONObject replacement = replacements.getJSONObject(i);
              String replaceThis = Pattern.quote(replacement.getString("offender")) ;
              String withThat = replacement.getString("replacement");
              jsonString = jsonString.replaceAll(replaceThis, withThat);
            }
            return new JSONObject(jsonString);
        } catch (Exception e) {
          logger.error("Exception cleaning rss feed:: ", e);
          return jsonToClean;
        }
    }
        return jsonToClean;
  }
}
