package main.java.edu.wisc.my.rssToJson.service;

import org.springframework.stereotype.Service;

@Service
public interface IRssToJsonService {
	public String jsonifiedRssUrl(String url);

}
