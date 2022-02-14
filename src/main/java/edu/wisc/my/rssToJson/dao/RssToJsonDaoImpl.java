package edu.wisc.my.rssToJson.dao;

import java.io.InputStream;
import java.io.InputStreamReader;

import edu.wisc.my.rssToJson.exception.FeedIdentifierUndefinedException;
import org.apache.commons.io.input.BOMInputStream;
import org.apache.http.HttpEntity;
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

  public static final long MAX_RESPONSE_BYTES = 1000000; // one million bytes

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
          throw new FeedIdentifierUndefinedException(feedEndpoint);
        }
        SyndFeed feed = null;
        try{
            HttpClient client = HttpClientBuilder.create().build();
            HttpGet request = new HttpGet(endpointURL);
            request.setHeader(HttpHeaders.USER_AGENT, "rss-to-json service");
            request.setHeader(HttpHeaders.CONTENT_ENCODING, "UTF-8");
            HttpResponse response = client.execute(request);
            SyndFeedInput input = new SyndFeedInput();

            HttpEntity responseEntity = response.getEntity();
            long contentLengthInBytes = responseEntity.getContentLength();

            // before reading the response into a String, verify it is small
            if (contentLengthInBytes < 0 || contentLengthInBytes > MAX_RESPONSE_BYTES) {
              throw new RuntimeException("Response from " + feedEndpoint + " was " +
                contentLengthInBytes + " bytes which exceeded maximum size " + MAX_RESPONSE_BYTES + " bytes.");
            }

            InputStream responseStream = responseEntity.getContent();

            // wrap in ByteOrderMarker-aware input stream to filter out a leading byte order marker if present
            BOMInputStream bomInputStream = new BOMInputStream(responseStream);

            feed = input.build(new InputStreamReader(bomInputStream, "UTF-8"));
            feed.setFeedType("UTF-8");
            logger.debug("CONTENT OF FEED " + endpointURL);
            logger.debug(feed.toString());

        }catch(Exception ex){
            logger.error("Error while fetching xml from {}", endpointURL, ex);
        }
        return feed;
    }

}
