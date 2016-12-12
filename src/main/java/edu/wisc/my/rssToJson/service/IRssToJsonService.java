package main.java.edu.wisc.my.rssToJson.service;

import java.io.InputStream;

import org.springframework.stereotype.Service;

@Service
public interface IRssToJsonService {
	public String getJsonFromURL(String url, InputStream in);

	String testMe();

}
