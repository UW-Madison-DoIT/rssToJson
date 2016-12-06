
package main.java.edu.wisc.my.rssToJson.service;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.springframework.stereotype.Service;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.springframework.context.annotation.Bean;
import org.xml.sax.helpers.DefaultHandler;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import main.java.edu.wisc.my.rssToJson.model.RssItem;

@Service
class RSStoJsonServiceImpl implements IRSSToJSONService {

public RSStoJsonServiceImpl(){
	
}
	
public String jsonifiedRssUrl(String url){
         //1. Parse the url
		 SAXParserFactory factoryS = SAXParserFactory.newInstance();
		 SAXParser saxParser = null;
		 
		 final ObjectMapper om = new ObjectMapper();
		 JsonNode root = om.reader().createObjectNode();
		 
		 try {
			saxParser = factoryS.newSAXParser();
		} catch (Exception e) {
			//TODO: Fail Gracefully
		}
 
 class jsonHandler extends DefaultHandler{
		
		private static final String ITEM = "item";
		private static final String TITLE = "title";
		private static final String DESCRIPTION = "description";
		private static final String LINK = "link";
				
		String currentElement;
		String currentValue;
		
		RssItem rssItem = new RssItem();
		StringBuffer output = new StringBuffer("");
		boolean isChannel = true;
		
		public void startElement(String uri, String localName,
	            String qName, Attributes attributes)
	            		throws SAXException {
		
			
			if(qName.equals(ITEM) ){
				
				try {
					output.append(om.writeValueAsString(rssItem));
					
				} catch (Exception e) {
					// TODO Fail gracefully
				}
				currentValue = "";
				isChannel = false;
				rssItem = new RssItem();
			}

			currentElement = qName;
		}
	
	public void characters(char ch[], int start, int length) throws SAXException {
			        currentValue = currentValue +  new String(ch, start, length);
			}
		
		public void endElement(String uri, String localName,
				String qName) throws SAXException {

                if(currentElement.equals(TITLE)){
                	rssItem.setTitle(currentValue);
                }
                
                if(currentElement.equals(LINK)){
                	rssItem.setLink(currentValue);
                }
                
                if(currentElement.equals(DESCRIPTION)){
                	rssItem.setDescription(currentValue);
                }
                currentValue = "";
                rssItem.setChannel(isChannel);
                }
		
		public String getJson(){
			return this.output.toString();
		}
	}
	try {
		jsonHandler jh = new jsonHandler();
		saxParser.parse(url, jh);
		return jh.getJson();
		
	} catch (Exception e) {
		// TODO Fail gracefully
		return null;
	}
}}




