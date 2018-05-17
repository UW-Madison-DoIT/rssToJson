package edu.wisc.my.rssToJson.dao;

import org.junit.jupiter.api.Test;

import edu.wisc.my.rssToJson.service.RssToJsonService;
import org.springframework.core.env.Environment;

import static org.mockito.Mockito.*;

import org.hibernate.validator.constraints.ModCheck;

public class RssToJsonDaoImplTest {
  
   RssToJsonDaoImpl rssToJsonDaoImpl;

   @Mock 
   RssToJsonService mockRssToJsonService;

   @Mock 
  Environment mockEnv;
  
   @Before
  public void setupTests() {
      rssToJsonDaoImpl = new RssToJsonDaoImpl();
  }

    @Test 
  public void getRssFeedTest() {
    String uwrf = "https://www.uwrf.edu/News/Index.cfm?xml=UWRF%20News,RSS2.0full";
    when(mockEnv.getProperty(anyString())).thenReturn(uwrf);
    rssToJsonDaoImpl.setEnv(mockEnv);
        
  }

}