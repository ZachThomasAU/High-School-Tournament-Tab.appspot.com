package com.stdesco.swisstab.webapp;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;


public class datastoreConnecter {
  
DatastoreService datastore = 
        DatastoreServiceFactory.getDatastoreService();      
Entity entity;

/*
 * gets the value of a property within a datstore location
 * @args: kind= kind of data, key=unique key for data entry, 
 * property=property identifier string  
 * @returns: returns an the requested object from the datastore 
 * the return must be cast to the expected type.  
 */
public Object getProperty(String kind, Object keyName, String property) {
  
     Object retval;         
     //System.out.print("We getting data boyzzz\n");
     
     if (keyName instanceof String || keyName.equals((int) keyName)){             
       // Pull the global entity from Google Cloud Datastore
       try {
         entity = getEntity(kind, keyName);
       } catch(EntityNotFoundException e) {
         // TODO Handle this
         e.printStackTrace();
       }         
     } else {      
       throw new IllegalArgumentException("keyName is not of type String or int\n");      
     }
     
     retval = entity.getProperty(property);
  
     return retval;
}

/*
 * Generates the data-store key and entity from the kind and pkey
 * given to the function by the user. 
 */
private Entity getEntity(String kind, Object keyName) throws EntityNotFoundException { 
    Key key;
    
    if(keyName instanceof String) {
      System.out.print("your keyName is a String\n");
      key = KeyFactory.createKey(kind, (String) keyName);     
    } else {
      System.out.print("your keyName is not a String\n");
      key = KeyFactory.createKey(kind, (int) keyName);
    }    
    entity = datastore.get(key);
    return entity;
}

}
