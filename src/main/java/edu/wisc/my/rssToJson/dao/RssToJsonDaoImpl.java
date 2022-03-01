package edu.wisc.my.rssToJson.dao;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import com.rometools.rome.io.FeedException;
import edu.wisc.my.rssToJson.exception.FeedIdentifierUndefinedException;
import edu.wisc.my.rssToJson.exception.ResponseTooBigException;
import org.apache.commons.io.input.BOMInputStream;
import org.apache.http.Header;
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
        logger.trace("Fetching feed for {} ", feedEndpoint);
        //see if property file has corresponding url for requested endpoint
        String endpointURL = env.getProperty(feedEndpoint);
        if (endpointURL == null){
          logger.warn("No corresponding feed url for requested endpoint {}",
                  feedEndpoint);
          throw new FeedIdentifierUndefinedException(feedEndpoint);
        }
        SyndFeed feed = null;
        try{

            try {
              feed = urlToSyndFeedAsCharset(endpointURL, StandardCharsets.UTF_8);
            } catch (Exception e) {
              // try again as US-ASCII
              feed = urlToSyndFeedAsCharset(endpointURL, StandardCharsets.US_ASCII);
            }

            logger.trace("CONTENT OF FEED " + endpointURL);
            logger.trace(feed.toString());

        } catch(Exception ex){
            logger.error("Error while fetching xml from {}", endpointURL, ex);
            throw new RuntimeException("Problem converting " + endpointURL + " to a SyndFeed.", ex);
        }
        return feed;
    }


  /**
   * Given a URL and a desired charset, attempt to parse the response from that URL as the given charset.
   * @param url
   * @param charset UTF-8 or US-ASCII
   * @throws IOException on IO failures retrieving the URL
   * @throws ResponseTooBigException if response from URL is too big
   * @return
   */
  protected SyndFeed urlToSyndFeedAsCharset(String url, Charset charset) throws IOException, FeedException {

    try {
      HttpClient client = HttpClientBuilder.create().build();
      HttpGet request = new HttpGet(url);
      request.setHeader(HttpHeaders.USER_AGENT, "rss-to-json service");
      request.setHeader(HttpHeaders.CONTENT_ENCODING, charset.name());
      HttpResponse response = client.execute(request);
      SyndFeedInput input = new SyndFeedInput();

      HttpEntity responseEntity = response.getEntity();
      long contentLengthInBytes = responseEntity.getContentLength();

      logger.trace("Read " + contentLengthInBytes + " bytes from " + url);

      // before reading the response, verify it is small
      // does not check for size -1,
      // because in practice that was meaning "unknown" not "unreasonably large"
      if (contentLengthInBytes > MAX_RESPONSE_BYTES) {
        throw new ResponseTooBigException(url, contentLengthInBytes, MAX_RESPONSE_BYTES);
      }

      InputStream responseStream = responseEntity.getContent();

      // wrap in ByteOrderMarker-aware input stream to filter out a leading byte order marker if present
      BOMInputStream bomInputStream = new BOMInputStream(responseStream);
      SyndFeed feed = input.build(new InputStreamReader(bomInputStream, charset));
      feed.setFeedType(charset.name());
      return feed;
    } catch (Exception e) {
      logger.warn("Failed to read {} as {} to build a SyndFeed.", url, charset.name(), e);
      throw new RuntimeException("Problem reading from " + url + " using endcoding " + charset.name(), e);
    }
  }
}
