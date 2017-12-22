package edu.wisc.my.rssToJson.service;

import org.json.JSONObject;
import org.springframework.stereotype.Service;

@Service
public interface RssToJsonService {
    public JSONObject getJsonifiedXMLUrl(String feed);
	public JSONObject getJsonFromURL(String url);
}
