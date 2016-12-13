package main.java.edu.wisc.my.rssToJson.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;

public class RssItem {
	
	private String title;
	private String link;
	private String description;
	private boolean channel;
	

	public RssItem(){
		
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = cleanString(title);
	}
	public String getLink() {
		return link;
	}
	public void setLink(String link) {
		this.link = cleanString(link);
	}
	public String getDescription() {
		return description !=null ? description : "";
	}
	public void setDescription(String description) {
		this.description = cleanString(description);
	}
	
	@JsonIgnore
	public boolean isChannel() {
		return channel;
	}
	public void setChannel(boolean channel) {
		this.channel = channel;
	}
    
	
    private String cleanString(String jsonValueIn){
    	
    	char backslash = "\\".toCharArray()[0];
    	char[] formatting = jsonValueIn.toCharArray();
    	StringBuffer cleanedItem = new StringBuffer("");
    	boolean formatCleaner = false;
    	for (char c : formatting) {

    		if (c == backslash && !formatCleaner) {
    			formatCleaner = true;
    		}

    		if (!formatCleaner) {
    			cleanedItem.append(c);
    		} else {
    			if (c != backslash) {
    				formatCleaner = false;
    			}
    		}

    	}
    	String cleaned = cleanedItem.toString().trim();
    	
    	cleaned.replaceAll("null", "");
    	cleaned.replaceAll(":\"\\s", ":\"");

    	return cleaned;
    }
	
    

	
}
