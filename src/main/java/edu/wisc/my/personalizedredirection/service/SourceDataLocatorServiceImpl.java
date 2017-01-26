package edu.wisc.my.personalizedredirection.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;

import edu.wisc.my.personalizedredirection.dao.UrlDataSource;
import edu.wisc.my.personalizedredirection.dao.UrlDataSourceList;
import edu.wisc.my.personalizedredirection.exception.PersonalizedRedirectionException;

@Service
public class SourceDataLocatorServiceImpl implements ISourceDataLocatorService {
	protected final Logger logger = LoggerFactory.getLogger(getClass());

	// hardcoded path to map containing the map of application name to the
	// location of that application's redirect data
	public final String DATA_LOCATION = "dataSources.json";

	@Override
	public UrlDataSource getUrlDataSource(String appName) throws PersonalizedRedirectionException {
		UrlDataSource retVal = new UrlDataSource();

		try {
			Resource resource = new ClassPathResource(DATA_LOCATION);
			if (resource.exists()) {
				
				// resource should be a valid json file containing an array of data source objects.
				
				BufferedReader br = new BufferedReader(new InputStreamReader(resource.getInputStream()));
				StringBuilder stringBuilder = new StringBuilder();
				String line;
				while ((line = br.readLine()) != null) {
					stringBuilder.append(line).append(' ');
				}
				br.close();
				String json = stringBuilder.toString();

				if (isValidJSON(json)) {
					retVal.setAttributeName("Resource = json");
					ObjectMapper objectMapper = new ObjectMapper();
					UrlDataSourceList sourceList = objectMapper.readValue(json, UrlDataSourceList.class);
					retVal = sourceList.findByAppName(appName);
				}
			}

		} catch (Exception e) {
			throw new PersonalizedRedirectionException("Error fetching application metadata.");
		}

		return retVal;
	}
    
	/*
	 * Insurance that the file we've read is actually json.
	 */
	private boolean isValidJSON(final String json) {
		boolean valid = false;
		try {
			final JsonParser parser = new ObjectMapper().getFactory().createParser(json);
			while (parser.nextToken() != null) {
			}
			valid = true;
		} catch (Exception jpe) {
			// eat error
			valid = false;
		}

		return valid;
	}

}
