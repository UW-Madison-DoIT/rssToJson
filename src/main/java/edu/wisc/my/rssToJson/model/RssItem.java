package main.java.edu.wisc.my.rssToJson.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

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
		this.title = title;
	}
	public String getLink() {
		return link;
	}
	public void setLink(String link) {
		this.link = link;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	@JsonIgnore
	public boolean isChannel() {
		return channel;
	}
	public void setChannel(boolean channel) {
		this.channel = channel;
	}

	
}
