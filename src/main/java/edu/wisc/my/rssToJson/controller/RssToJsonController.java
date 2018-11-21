package edu.wisc.my.rssToJson.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.beans.factory.BeanFactory;

import edu.wisc.my.rssToJson.filter.XmlFilter;
import edu.wisc.my.rssToJson.filter.iFilter;

import edu.wisc.my.rssToJson.service.RssToJsonService;

@Controller
public class RssToJsonController {

    protected final Logger logger = LoggerFactory.getLogger(getClass());
    private RssToJsonService rssToJsonService;

    @Autowired
    public void setRSSToJSONService(RssToJsonService rssToJsonService) {
        this.rssToJsonService = rssToJsonService;
    }

    @RequestMapping(value="/rssTransform/{feed}/xml")
    public @ResponseBody void getJsonifiedXMLUrl(HttpServletRequest request, HttpServletResponse response,
            @PathVariable String feed) {

        JSONObject jsonFromFeed = rssToJsonService.getJsonifiedXMLUrl(feed);
        if (jsonFromFeed == null) {
            logger.error("No feed for endpoint {}", feed);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        } else {
            /* The filter is a custom class based on the name of your endpoint.
             *  For example, if your feed is called "sports", then you should
            // have a class in the filter package called "SportsFilter".
            // All filter classes should implement the iFilter interface.
            // 
            // The static method XmlFilter.getXmlFilter(feed) will use
            // reflection to return a filter with your custom business logic. 
            */
            iFilter filter = XmlFilter.getXmlFilter(feed);

            JSONObject jsonToReturn = filter.getFilteredJSON(jsonFromFeed);
            response.setContentType("application/json");
            try {
                response.getWriter().write(jsonToReturn.toString());
                response.setStatus(HttpServletResponse.SC_OK);
            } catch (IOException e) {
                logger.warn(e.getMessage());
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            }
        }
    }
    
    @RequestMapping(value="/rssTransform/{feed}")
    public @ResponseBody void getJsonifiedRssUrl(HttpServletRequest request,
            HttpServletResponse response, @PathVariable String feed) {
        
        logger.debug("Attempting to retrieve feed for endpoint {}", feed);
        JSONObject jsonToReturn = rssToJsonService.getJsonFromURL(feed);
        if(jsonToReturn == null){
            logger.error("No feed for endpoint {}", feed);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }else{
            response.setContentType("application/json");
            try{
                response.getWriter().write(jsonToReturn.toString());
                response.setStatus(HttpServletResponse.SC_OK);
            }catch(IOException e){
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            }
        }
    }


    @RequestMapping("/")
    public @ResponseBody
    void index(HttpServletResponse response) {
        try {
            JSONObject responseObj = new JSONObject();
            responseObj.put("status", "up");
            response.getWriter().write(responseObj.toString());
            response.setContentType("application/json");
            response.setStatus(HttpServletResponse.SC_OK);
        } catch (IOException e) {
            logger.error("Issues happened while trying to write Status", e);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

}
