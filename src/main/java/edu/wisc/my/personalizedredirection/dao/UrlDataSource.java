package edu.wisc.my.personalizedredirection.dao;

import org.springframework.stereotype.Repository;


/*
 * This class contains metadata about the list of attribute/URL pairs we'll be searching. 
 */
@Repository
public class UrlDataSource{

	private String appName;
    private String attributeName;
    private String dataSourceLocation;
    private String dataSourceType;


    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getAttributeName() {
        return attributeName;
    }

    public void setAttributeName(String attributeName) {
        this.attributeName = attributeName;
    }

    public String getDataSourceLocation() {
        return dataSourceLocation;
    }

    public void setDataSourceLocation(String dataSourceLocation) {
        this.dataSourceLocation = dataSourceLocation;
    }

	public String getDataSourceType() {
		
		return dataSourceType;
	}

	public void setDataSourceType(String dataSourceType) {
		this.dataSourceType = dataSourceType.trim().toUpperCase();
	}

    
}