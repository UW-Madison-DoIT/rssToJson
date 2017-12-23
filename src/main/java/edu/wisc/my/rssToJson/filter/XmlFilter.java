package edu.wisc.my.rssToJson.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public enum XmlFilter {
    WUD("wud");

    private String filterName;
    protected final Logger logger = LoggerFactory.getLogger(getClass());

    XmlFilter(String filterName){
        this.filterName = filterName;
    }

    public String getFilterName(){
        return this.filterName;
    }

    public static XmlFilter getXmlFilter(String filterName){
        filterName = toTitleCase(filterName);
        for (XmlFilter filter : XmlFilter.values()){
          if(filter.getFilterName().equalsIgnoreCase(filterName)){
              return filter;
          }
        }

        return null;
    }

    private static String toTitleCase(String filterNameIn){
        
        StringBuilder titleCase = new StringBuilder();
        boolean nextTitleCase = true;
        for (char c : filterNameIn.toCharArray()) {
             if (nextTitleCase) {
                 c = Character.toTitleCase(c);
                 nextTitleCase = false;
             }
     
             titleCase.append(c);
         }
         String filterNameOut = titleCase.toString().trim();
        return filterNameOut;
    }

    public iFilter getFilter(String filterName){
        int i = 0;
        try{ 
        
        i++; //1   
        String filterClass = XmlFilter.toTitleCase(filterName) + "Filter";
        i++;  //2
        String pkg = this.getClass().getPackage().getName();
        logger.error(this.getClass().getCanonicalName());
        logger.error(pkg + "." +filterClass);
        
        i++; //3
        iFilter filter = (iFilter) Class.forName(pkg + "." +filterClass).newInstance();
  
       
        i++; //4
        logger.error(filter.healthCheck());
        return filter;
       } catch (Exception e){
           logger.error("Step " + i);
           logger.error(e.getMessage());
           return null;
       }
    }

}
