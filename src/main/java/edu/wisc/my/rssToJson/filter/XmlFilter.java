package edu.wisc.my.rssToJson.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class XmlFilter {

    protected final Logger logger = LoggerFactory.getLogger(getClass());


    public static iFilter getXmlFilter(String filterName){
        try{ 
          String filterClass = XmlFilter.toTitleCase(filterName) + "Filter";
          String pkg = new CurrentClassGetter().getPackageName();
          iFilter filter = (iFilter) Class.forName(pkg + "." +filterClass).newInstance();
          return filter;
       } catch (Exception e){
           return null;
       }
    }

   public static class CurrentClassGetter extends SecurityManager {
    public String getPackageName() {
      return getClassContext()[1].getPackage().getName(); 
    }
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

}
