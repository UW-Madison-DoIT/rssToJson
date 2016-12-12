
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

//	private String demoString(BufferedInputStream inStream){
//		logger.trace("DEMO THE LOG THING");
//		
//		
//		    java.util.Scanner s = new java.util.Scanner(inStream).useDelimiter("\\A");
//		    String x = s.hasNext() ? s.next() : "";
//		    logger.warn(x);
//             return x;
//	}
//	
	
//	private InputStream getInputStream(String url) throws IOException {
//
//	try {
//	       String webServiceURL=url;
//	        URL rssUrl = new URL(webServiceURL);
//	        HttpURLConnection geoLocationDetailXMLURLConnection = (HttpURLConnection)rssUrl.openConnection();
//	        InputStream in = new BufferedInputStream(geoLocationDetailXMLURLConnection.getInputStream());
//	        logger.trace(in.toString());
//	        return in;
//	}catch(Exception e){
//		return null;
//	}
//	}

//	public String jsonifiedRssUrl(String url){
//		logger.warn("SHOULD NOT BE HERE");
//		try {
//			return jsonifiedRssUrl(url, getInputStream(url));
//		} catch (Exception e) {
//			return null;
//		}
//	}
	@Override
	public String testMe(){
		return "TEST TEST TEST TEST TEST TEST TEST TEST TEST TEST TEST TEST!!!!!!!!!!!!!";
	}
	
	@Override
	public String getJsonFromURL(String url, InputStream in) {
		logger.trace("STEP THREE: IN THE SERVICE");
        //demoString((BufferedInputStream) in);
        String retVal = "";
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
				
				String cleanedCurrentValue;
				String currentElement;
			
				String currentValue = "";
				final ObjectMapper om = new ObjectMapper();
				RssItem rssItem = new RssItem();
				StringBuffer output = new StringBuffer("");
				boolean isChannel = true;

				private void stringCleaner(String jsonItem) {
					logger.warn("STRING CLEANING " + jsonItem);
                    cleanedCurrentValue = "";
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
					cleanedCurrentValue = cleaned;
				}

				public void startElement(String uri, String localName, String qName, Attributes attributes)
						throws SAXException {

					logger.trace(qName);
					if (qName.equals(ITEM)) {

						try {
							System.out.println(om.writeValueAsString(rssItem));
							stringCleaner(om.writeValueAsString(rssItem));
							output.append(cleanedCurrentValue);
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
					logger.trace("THIS IS THE JSON OUTPUT " + output.toString());
					return this.output.toString();
				}
			}

			jsonHandler jh = new jsonHandler();
			saxParser.parse(in, jh);
			retVal =  jh.getJson();

		}catch(Exception e){
			logger.warn("THIS IS YOUR ERROR _ " + e.getMessage());
			e.printStackTrace();
		}
	        return retVal;
	}
}
