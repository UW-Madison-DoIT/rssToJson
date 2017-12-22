package edu.wisc.my.rssToJson.filter;

import org.json.JSONObject;

public interface iFilter{

    public JSONObject getFilteredJSON(JSONObject rawJSON);
    public String healthCheck();
}