package edu.wisc.my.rssToJson.service;

import static org.mockito.Mockito.*;

import com.rometools.rome.io.SyndFeedInput;
import edu.wisc.my.rssToJson.dao.RssToJsonDaoImpl;
import org.springframework.core.env.Environment;
import com.rometools.rome.feed.synd.SyndFeed;
import java.io.InputStream;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import org.junit.*;
import java.io.UnsupportedEncodingException;


//import static org.mockito.ArgumentMatchers.anyString;
// import org.apache.commons.lang3.StringEscapeUtils;
public class RssToJsonServiceImplTest {

    RssToJsonDaoImpl rssToJsonDaoImpl;
    RsstoJsonServiceImpl rssToJsonServiceImpl;

    @Test
    public void getRssFeedTest() {
      Environment mockEnv = mock(Environment.class);
      rssToJsonDaoImpl = new RssToJsonDaoImpl();
      //String uwrfURL = "https://docs.google.com/document/d/1EjnGglyBbE5xtcHs-bgvc4Yh1u0eml7DHWJmm4thQRw/edit?usp=sharing";
      String uwrfURL = "http://www.uwbadgers.com/rss.aspx";
      String messyRss = "<rss xmlns:atom=\"http://www.w3.org/2005/Atom\" version=\"2.0\"> " +
      "<channel> <title>University of Wisconsin</title> <link>http://uwbadgers.com/rss.aspx</link> " +
      "<description>Latest news items for University of Wisconsin</description> " +
      "<atom:link href=\"http://uwbadgers.com/rss.aspx\" rel=\"self\" type=\"application/rss+xml\"/> " +
       "<language>en-us</language>"  +
      "<ttl>60</ttl><item><title>Curtis’ career ends at NCAA Championships</title><link>" +
      "http://uwbadgers.com/news/2018/5/21/womens-golf-curtis-career-ends-at-ncaa-championships.aspx" +
      "</link><guid>http://uwbadgers.com/news/2018/5/21/womens-golf-curtis-career-ends-at-ncaa-championships.aspx" +
      "</guid><category>Women's Golf</category><description><![CDATA[" +
      "<img src=\"http://uwbadgers.com/common/controls/image_handler.aspx?thumb_prefix=rp_primary&image_path=/images/2018/5/21/Curtis_NCAA_headline.jpg\"" +
      "/><br /><br />Finishes with second-best career scoring average in Wisconsin history.]]></description>"+
      "<pubDate>Mon, 21 May 2018 10:43:00 GMT</pubDate></item></channel></rss>";
        try {
          InputStream is = new ByteArrayInputStream(messyRss.getBytes("UTF-8"));
          SyndFeedInput sfi = new SyndFeedInput();
          SyndFeed feed = sfi.build(new InputStreamReader(is));
          rssToJsonServiceImpl = new RsstoJsonServiceImpl();
          rssToJsonDaoImpl = mock(RssToJsonDaoImpl.class);
          when(rssToJsonDaoImpl.getRssFeed(anyString())).thenReturn(feed);
          rssToJsonServiceImpl.setRssToJsonDao(rssToJsonDaoImpl);
          rssToJsonServiceImpl.getJsonFromURL("mocked");
          System.out.println("HI");
        } catch (Exception e) {
          System.out.println(e);
        //fuck off
        }

      System.out.println("HEY DOOFUS THIS IS YOUR TEST");

    }

  }
