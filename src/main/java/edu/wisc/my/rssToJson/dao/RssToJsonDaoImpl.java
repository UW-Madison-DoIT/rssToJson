package edu.wisc.my.rssToJson.dao;

import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.JSONObject;
import org.json.XML;
import javax.json.JsonReader;  
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Repository;

import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.SyndFeedInput;

@PropertySource("classpath:endpoint.properties")
@Repository
public class RssToJsonDaoImpl implements RssToJsonDao{

    protected final Logger logger = LoggerFactory.getLogger(getClass());
    private Environment env;

    /**
     * @param env the env to set
     */
    @Autowired
    void setEnv(Environment env) {
      this.env = env;
    }

    @Override
    public JSONObject getXMLFeed(String feedEndpoint) {
       String endpointURL = getEndpointURL(feedEndpoint);
       JSONObject jsonObject = null;
       try {
           HttpClient client = HttpClientBuilder.create().build();
           HttpGet request = new HttpGet(endpointURL);
           request.setHeader(HttpHeaders.USER_AGENT, "rss-to-json service");
           HttpResponse response = client.execute(request);
           InputStreamReader isr = new InputStreamReader(response.getEntity().getContent());
           StringBuffer stringBuffer = new StringBuffer();
           int i;
           while ((i = isr.read()) != -1) {
               stringBuffer.append((char) i);
           }
           String xmlString = stringBuffer.toString();
           jsonObject = XML.toJSONObject(xmlString);
       } catch (Exception e) {
           logger.warn(e.getMessage());
           return null;
       }
       return jsonObject;
   }
   
   private String getEndpointURL(String feed) {
       String endpointURL = env.getProperty(feed);
       if (endpointURL == null) {
           logger.warn("No corresponding feed url for requested endpoint {}", feed);
           return null;
       }
       return endpointURL;
   }

    @Override
    @Cacheable(cacheNames="feeds", sync=true)
    public SyndFeed getRssFeed(String feedEndpoint) {
        logger.info("Fetching feed for {} ", feedEndpoint);
        //see if property file has corresponding url for requested endpoint
        String endpointURL = env.getProperty(feedEndpoint);
        if (endpointURL == null){
          logger.warn("No corresponding feed url for requested endpoint {}",
                  feedEndpoint);
          return null;
        }
        SyndFeed feed = null;
        try{
            HttpClient client = HttpClientBuilder.create().build();
            HttpGet request = new HttpGet(endpointURL);
            request.setHeader(HttpHeaders.USER_AGENT, "rss-to-json service");
            request.setHeader(HttpHeaders.CONTENT_ENCODING, "UTF-8");
            HttpResponse response = client.execute(request);
            SyndFeedInput input = new SyndFeedInput();
            feed = input.build(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
            feed.setFeedType("UTF-8");
            logger.debug("CONTENT OF FEED " + endpointURL);
            logger.debug(feed.toString());

        }catch(Exception ex){
            logger.error("Error while fetching xml from {}", endpointURL, ex);
        }
        return feed;
    }

}
