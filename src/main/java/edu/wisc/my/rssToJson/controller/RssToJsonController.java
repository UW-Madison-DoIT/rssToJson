package edu.wisc.my.rssToJson.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.wisc.my.rssToJson.exception.FeedIdentifierUndefinedException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import edu.wisc.my.rssToJson.service.RssToJsonService;

@Controller
public class RssToJsonController {

    protected final Logger logger = LoggerFactory.getLogger(getClass());
    private RssToJsonService rssToJsonService;

    @Autowired
    public void setRSSToJSONService(RssToJsonService rssToJsonService) {
        this.rssToJsonService = rssToJsonService;
    }

    @RequestMapping(value="/rssTransform/{feed}")
    public @ResponseBody void getJsonifiedRssUrl(HttpServletRequest request,
            HttpServletResponse response, @PathVariable String feed) {
      try {
        logger.debug("Attempting to retrieve feed for endpoint {}", feed);
        JSONObject jsonToReturn = rssToJsonService.getJsonFromURL(feed);
        response.setContentType("application/json");
        response.getWriter().write(jsonToReturn.toString());
        response.setStatus(HttpServletResponse.SC_OK);

      } catch (FeedIdentifierUndefinedException notFoundException) {
        logger.error("No feed defined for id {}; is endpoint.properties configured for this feed?", feed, notFoundException);
        response.setStatus(HttpServletResponse.SC_NOT_FOUND); // 404 NOT FOUND

      } catch (Exception e) {
        logger.warn("Problem retrieving feed with id {}", feed, e);
        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
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
