package edu.wisc.my.personalizedredirection.dao;

/*
 * Holder class for attribute/url pairs. Can be populated from any data source. 
 */
public class AttributeMap{
    private String attribute;
    private String url;

    public String getUrl(){
        return this.url;
    }

    public void setUrl(String url){
        this.url = url;
    }

    public String getAttribute(){
        return this.attribute;
    }

    public void setAttribute(String attribute){
        this.attribute = attribute;
    }
}