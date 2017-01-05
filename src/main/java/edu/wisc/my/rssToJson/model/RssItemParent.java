package main.java.edu.wisc.my.rssToJson.model;

import java.util.ArrayList;

public class RssItemParent {

	private String status = "ok";
	private RssItemDetail feed;
	private ArrayList<RssItemDetail> items = new ArrayList<RssItemDetail>();

	public String getStatus() {
		return status;
	}

	public RssItemDetail getFeed() {
		return feed;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public ArrayList<RssItemDetail> getItems() {
		return items;
	}

	public void setFeed(RssItemDetail feed) {
		this.feed = feed;
	}


	public void setItems(ArrayList<RssItemDetail> items) {
		this.items = items;
	}

	public void addItem(RssItemDetail item){
		items.add(item);
	}

}
