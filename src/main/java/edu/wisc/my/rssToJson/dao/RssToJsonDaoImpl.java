package edu.wisc.my.rssToJson.dao;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.JSONObject;
import org.json.XML;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils; 
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
    private String httpResponseAsString(String url) throws IOException {
        logger.error("HTTP Response method " + url);
        CloseableHttpClient httpclient = HttpClients.createDefault();
        try {
            HttpGet httpget = new HttpGet(url);
            logger.error(httpget.toString());

            // Create a custom response handler
            ResponseHandler<String> responseHandler = new ResponseHandler<String>() {

                @Override
                public String handleResponse(
                        final HttpResponse response) throws ClientProtocolException, IOException {
                     logger.error("TWO " + response.toString());        
                    int status = response.getStatusLine().getStatusCode();
                    logger.debug(status + " response code ");
                    if (status >= 200 && status < 300) {
                        HttpEntity entity = response.getEntity();
                        return entity != null ? EntityUtils.toString(entity) : null;
                    } else {
                        throw new ClientProtocolException("Unexpected response status: " + status);
                    }
                }
            };
            String responseBody = httpclient.execute(httpget, responseHandler);
            logger.error("ONE POINT SIX " + responseBody);
            return responseBody;
        } finally {
            httpclient.close();
        }
    }


    @Override
    public JSONObject getXMLFeed(String feedEndpoint) {
       String endpointURL = getEndpointURL(feedEndpoint);
       JSONObject jsonObject = null;
       try {
         String xmlString = httpResponseAsString(endpointURL);
         jsonObject = XML.toJSONObject(xmlString);
       } catch (Exception e) {
           logger.warn(e.getMessage());
           return null;
       }
       return jsonObject;
   }
   
   public String getEndpointURL(String feed) {
       logger.error("GETTING THE ENDPOINT FOR " + feed);
       String endpointURL = env.getProperty(feed);
       logger.error(endpointURL);
       if (endpointURL == null) {
           logger.warn("No corresponding feed url for requested endpoint {}", feed);
           return null;
       }
       return endpointURL;
   }

    @Override
    @Cacheable(cacheNames="feeds", sync=true)
    public SyndFeed getRssFeed(String feedEndpoint) {
      try{  
        String result = httpResponseAsString(feedEndpoint);
        SyndFeedInput input = new SyndFeedInput();
        InputStream stream = new ByteArrayInputStream(result.getBytes("UTF-8"));
        SyndFeed feed = input.build(new InputStreamReader(stream, "UTF-8"));
        logger.debug("CONTENT OF FEED " + feedEndpoint);
        logger.debug(feed.toString());
        return feed;
      } catch (Exception e) {
          logger.warn("Could not get feed " + feedEndpoint + " " + e.getMessage());
      } 

      return null;
    }

}
