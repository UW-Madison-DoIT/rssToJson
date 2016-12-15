
package main.java.edu.wisc.my.rssToJson.service;

import java.io.InputStream;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import main.java.edu.wisc.my.rssToJson.model.RssItem;
import main.java.edu.wisc.my.rssToJson.model.RssItemDetail;
import main.java.edu.wisc.my.rssToJson.model.RssItemParent;

@Service
public class RsstoJsonServiceImpl  implements IRssToJsonService {
	protected final Logger logger = LoggerFactory.getLogger(getClass());
	public RsstoJsonServiceImpl() {

	}


	@Override
	public String getJsonFromURL(String url, InputStream in) {
        String retVal = "";
		try {
			SAXParserFactory factoryS = SAXParserFactory.newInstance();
			SAXParser saxParser = null;
			saxParser = factoryS.newSAXParser();

			class jsonHandler extends DefaultHandler {

				private static final String ITEM = "item";
				private static final String TITLE = "title";
				private static final String DESCRIPTION = "description";
				private static final String LINK = "link";

				String currentElement;

				RssItemParent parent = new RssItemParent();
				String currentValue = "";
				final ObjectMapper om = new ObjectMapper();
				RssItem rssItem;
				RssItemDetail rssItemDetail = new RssItemDetail();
				boolean isChannel = true;

				public void startElement(String uri, String localName, String qName, Attributes attributes)
						throws SAXException {

					logger.trace(qName);

					if (qName.equals(ITEM)) {
						if(isChannel){
							parent.setFeed(rssItemDetail);
							rssItem = new RssItem();
						}else{
						    parent.addItem(rssItem);
						}

						currentValue = "";
						isChannel = false;
						rssItem = new RssItem();
						rssItem.setItem(new RssItemDetail());
						rssItemDetail = rssItem.getItem();
					}

					currentElement = qName;
				}

				public void characters(char ch[], int start, int length) throws SAXException {

					currentValue = currentValue + new String(ch, start, length);
				}

				public void endElement(String uri, String localName, String qName) throws SAXException {

					if (currentElement.equals(TITLE)) {
						rssItemDetail.setTitle(currentValue);
					}
					if (currentElement.equals(LINK)) {
						rssItemDetail.setLink(currentValue);
					}
					if (currentElement.equals(DESCRIPTION)) {
						String descriptionSoFar = rssItemDetail.getDescription() + " " + currentValue;
						rssItemDetail.setDescription(descriptionSoFar);
					}
					currentValue = "";
					rssItemDetail.setChannel(isChannel);
				}

				public String getJson() {
					    String retVal;
						try {
							retVal = om.writeValueAsString(parent);
							return retVal;
						} catch (JsonProcessingException e) {
							logger.error(e.getMessage());
							retVal = "}}>< invalid json.";
						}
						return retVal;
				}
			}

			jsonHandler jh = new jsonHandler();
			saxParser.parse(in, jh);
			retVal =  jh.getJson();

		}catch(Exception e){
			logger.error("THIS IS YOUR ERROR _ " + e.getMessage());
		}
	        return retVal;
	}
}
