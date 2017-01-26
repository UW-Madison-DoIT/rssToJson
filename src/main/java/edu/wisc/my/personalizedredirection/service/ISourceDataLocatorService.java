package edu.wisc.my.personalizedredirection.service;

import edu.wisc.my.personalizedredirection.dao.UrlDataSource;
import edu.wisc.my.personalizedredirection.exception.PersonalizedRedirectionException;

public interface ISourceDataLocatorService {
        public UrlDataSource getUrlDataSource(String appName) throws PersonalizedRedirectionException;
}
