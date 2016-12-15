package main.java.edu.wisc.my.rssToJson.model;

import java.util.ArrayList;

public class RssItemParent {

	private String status = "ok";
	private RssItemDetail feed;
	private ArrayList<RssItem> items = new ArrayList<RssItem>();

	public String getStatus() {
		return status;
	}

	public RssItemDetail getFeed() {
		return feed;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public ArrayList<RssItem> getItems() {
		return items;
	}

	public void setFeed(RssItemDetail feed) {
		this.feed = feed;
	}


	public void setItems(ArrayList<RssItem> items) {
		this.items = items;
	}

	public void addItem(RssItem item){
		items.add(item);
	}

}
