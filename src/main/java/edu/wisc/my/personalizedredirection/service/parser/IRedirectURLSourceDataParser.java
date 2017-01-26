package edu.wisc.my.personalizedredirection.service.parser;
import org.springframework.core.io.Resource;

import edu.wisc.my.personalizedredirection.dao.AttributeMapList;
import edu.wisc.my.personalizedredirection.exception.PersonalizedRedirectionException;

/**
 * The parser will take a resource containing attribute/url pairs and parse it into an AttributeMapList
 */
public interface IRedirectURLSourceDataParser {
    public AttributeMapList parseResource(Resource resource) throws PersonalizedRedirectionException;
}
