package edu.wisc.my.personalizedredirection.dao;

import java.util.ArrayList;

/*
 * Holder class for metadata contained in src/main/resources/dataSources.json
 */
public class UrlDataSourceList {
	private ArrayList<UrlDataSource> dataSources;

	public ArrayList<UrlDataSource> getDataSources() {
		return dataSources;
	}

	public void setDataSources(ArrayList<UrlDataSource> dataSources) {
		this.dataSources = dataSources;
	}

	public UrlDataSource findByAppName(String appName) {
		for (UrlDataSource dataSource : dataSources) {
			if (dataSource.getAppName().equalsIgnoreCase(appName)) {
				return dataSource;
			}
		}

		return null;
	}

}
