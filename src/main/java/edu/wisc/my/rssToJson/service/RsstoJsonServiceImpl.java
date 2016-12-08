
package main.java.edu.wisc.my.rssToJson.service;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.fasterxml.jackson.databind.ObjectMapper;

import main.java.edu.wisc.my.rssToJson.model.RssItem;

@Service
public class RsstoJsonServiceImpl  implements IRssToJsonService {
	protected final Logger logger = LoggerFactory.getLogger(getClass());
	private String retVal;

	public RsstoJsonServiceImpl() {

	}

	private InputStream getInputStream(String url) throws IOException {

	try {
	       String webServiceURL=url;
	        URL geoLocationDetailXMLURL = new URL(webServiceURL);
	        HttpURLConnection geoLocationDetailXMLURLConnection = (HttpURLConnection)geoLocationDetailXMLURL.openConnection();
	        InputStream in = new BufferedInputStream(geoLocationDetailXMLURLConnection.getInputStream());
	        return in;
	}catch(Exception e){
		return null;
	}
	}
	
	@Override
	public String jsonifiedRssUrl(String url){
		try {
			return jsonifiedRssUrl(url, getInputStream(url));
		} catch (Exception e) {
			return null;
		}
	}
	
	@SuppressWarnings("finally")
	public String jsonifiedRssUrl(String url, InputStream in) {
		logger.warn("IN SERVICE");
		System.out.println("SERVICE METHOD");
		try {

			// 1. Parse the url
			SAXParserFactory factoryS = SAXParserFactory.newInstance();
			SAXParser saxParser = null;
			saxParser = factoryS.newSAXParser();

			class jsonHandler extends DefaultHandler {

				private static final String ITEM = "item";
				private static final String TITLE = "title";
				private static final String DESCRIPTION = "description";
				private static final String LINK = "link";
				
				
				String currentElement;
			
				String currentValue = "";
				final ObjectMapper om = new ObjectMapper();
				RssItem rssItem = new RssItem();
				StringBuffer output = new StringBuffer("");
				boolean isChannel = true;

				private String stringCleaner(String jsonItem) {

					char backslash = "\\".toCharArray()[0];
					char[] formatting = jsonItem.toCharArray();
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
					cleaned.replaceAll("null ", "");
					return cleaned;
				}

				public void startElement(String uri, String localName, String qName, Attributes attributes)
						throws SAXException {

					if (qName.equals(ITEM)) {

						try {
							String cleanedString = stringCleaner(om.writeValueAsString(rssItem));
							output.append(cleanedString);
						} catch (Exception e) {
							// ContinueProcessing
						}

						currentValue = "";
						isChannel = false;
						rssItem = new RssItem();

					}

					currentElement = qName;
				}

				public void characters(char ch[], int start, int length) throws SAXException {

					currentValue = currentValue + new String(ch, start, length);
				}

				public void endElement(String uri, String localName, String qName) throws SAXException {

					if (currentElement.equals(TITLE)) {
						rssItem.setTitle(currentValue);
					}

					if (currentElement.equals(LINK)) {
						rssItem.setLink(currentValue);
					}

					if (currentElement.equals(DESCRIPTION)) {
						rssItem.setDescription(currentValue);
					}
					currentValue = "";
					rssItem.setChannel(isChannel);
				}

				public String getJson() {
					return this.output.toString();
				}
			}

			jsonHandler jh = new jsonHandler();
			saxParser.parse(url, jh);
			retVal = jh.getJson();

		} finally {
			return retVal;
		}
	}
}
