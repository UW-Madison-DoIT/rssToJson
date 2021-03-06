package edu.wisc.my.rssToJson.dao;

import java.io.InputStreamReader;

import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
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
