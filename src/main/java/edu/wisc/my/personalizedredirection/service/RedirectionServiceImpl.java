package edu.wisc.my.personalizedredirection.service;

import javax.servlet.http.HttpServletRequest;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import edu.wisc.my.personalizedredirection.dao.AttributeMapList;
import edu.wisc.my.personalizedredirection.dao.UrlDataSource;
import edu.wisc.my.personalizedredirection.exception.PersonalizedRedirectionException;
import edu.wisc.my.personalizedredirection.service.parser.IRedirectURLSourceDataParser;
import edu.wisc.my.personalizedredirection.service.parser.RedirectUrlCSVParser;


@Service
public class RedirectionServiceImpl implements IRedirectionService {

	public String getUrl(HttpServletRequest request, UrlDataSource dataSource) throws PersonalizedRedirectionException {

		// Find search key in the header
		String attributeToSearchFor = dataSource.getAttributeName();
		String toFind = request.getHeader(attributeToSearchFor);

		if (toFind == null || toFind.length() == 0) {
			throw new PersonalizedRedirectionException("Attribute " + attributeToSearchFor + " not found.");
		}

		// Grab the resource which contains your key/url pairs.
		String locale = dataSource.getDataSourceLocation();
		Resource resource = new ClassPathResource(locale);
		AttributeMapList mapList;

		if (resource.exists()) {
			IRedirectURLSourceDataParser parser = getParser(dataSource);
			mapList = parser.parseResource(resource);
		} else {
			throw new PersonalizedRedirectionException("Location " + locale + " not found.");
		}

		String retVal = mapList.find(toFind);
		return retVal;
	}

	
	
	/*
	 * This service is currently set up to handle a two-column CSV file. 
	 * To implement other file types, write new implementations of edu.wisc.my.personalizedredirection.service.parser.IRedirectURLSourceDataParser
	 */
	private IRedirectURLSourceDataParser getParser(UrlDataSource dataSource) throws PersonalizedRedirectionException {
		IRedirectURLSourceDataParser parser = null;

		// Add new implementations of IRedirectUrlSourceDataParser as demanded.
		
		// Currently, there is one implementation which handles a two-column
		// CSV.
		if (dataSource.getDataSourceType().equalsIgnoreCase("CSV")) {
			parser = new RedirectUrlCSVParser();
		}

		if (parser == null) {
			throw new PersonalizedRedirectionException("Invalid data source type");
		}
		
		return parser;
	}
}
