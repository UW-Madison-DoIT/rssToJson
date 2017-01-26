package edu.wisc.my.personalizedredirection.service.parser;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.springframework.core.io.Resource;

import edu.wisc.my.personalizedredirection.dao.AttributeMap;
import edu.wisc.my.personalizedredirection.dao.AttributeMapList;
import edu.wisc.my.personalizedredirection.exception.PersonalizedRedirectionException;

/**
 * Implementation of IRedirectURLSourceDataParser which will parse a two-column
 * csv file.
 */
public class RedirectUrlCSVParser implements IRedirectURLSourceDataParser {

	public AttributeMapList parseResource(Resource resource) throws PersonalizedRedirectionException {
		AttributeMapList mapList = new AttributeMapList();
		try {
			// The resource should be a two-column csv file.
			// We have already verified its existence.
			BufferedReader br = new BufferedReader(new InputStreamReader(resource.getInputStream()));
			String line;
			String csvSplitBy = ",";
			while ((line = br.readLine()) != null) {
				String[] data = line.split(csvSplitBy);
				AttributeMap map = new AttributeMap();
				map.setAttribute(data[0].trim());
				map.setUrl(data[1].trim());
				mapList.addAttributeMap(map);
			}
			br.close();

		} catch (Exception e) {
			throw new PersonalizedRedirectionException(e.getMessage());
		}

		return mapList;
	}
}
