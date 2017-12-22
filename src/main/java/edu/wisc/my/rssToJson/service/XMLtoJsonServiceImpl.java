    package edu.wisc.my.rssToJson.service;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;

import edu.wisc.my.rssToJson.dao.RssToJsonDao;

@Service
public class XMLtoJsonServiceImpl implements RssToJsonService {


     @Override
    public JSONObject getJsonFromURL(String endpoint) {
        JSONObject json = new JSONObject();
        json.put ("working", "yes");
        return json;
    }
}