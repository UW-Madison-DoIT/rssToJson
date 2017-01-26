package edu.wisc.my.personalizedredirection.dao;

import java.util.HashMap;

/*
 * Wrapper class for edu.wisc.my.personalizedredirection.dao.AttributeMap.
 * Contains helper methods for map management. 
 */
public class AttributeMapList {

	private HashMap<String, String> mappedAttributes = new HashMap<String, String>();

	public void addAttributeMap(AttributeMap am) {
		mappedAttributes.put(am.getAttribute(), am.getUrl());
	}

	public String find(String key) {
		String retVal = mappedAttributes.get(key);
		return retVal;
	}

}