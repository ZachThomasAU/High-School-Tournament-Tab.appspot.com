package com.stdesco.swisstab.webapp;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

public class DatastoreConnecter {

	DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
	Entity entity;

	/**
	 * Gets the value of a property within a datastore location.
	 * 
	 * Property is returned of class Object, so ensure the returned object is
	 * cast to its appropriate type.
	 * 
	 * @param kind     	Entity kind of Entity the Property is located in.
	 * @param key      	Key for Entity the Property is located in.
	 * @param property 	Property identifier string
	 * @returns 		The requested property from the datastore
	 */
	public Object getProperty(String kind, Object keyName, String property) {
		Object retval;
		// System.out.print("We getting data boyzzz\n");

		if (keyName instanceof String || keyName.equals((int) keyName)) {
			// Pull the global entity from Google Cloud Datastore
			try {
				entity = getEntity(kind, keyName);
			} catch (EntityNotFoundException e) {
				// TODO Handle this
				e.printStackTrace();
				return 0;
			}
		} else {
			throw new IllegalArgumentException(
					"keyName is not of type String or " + "int\n");
		}
		retval = entity.getProperty(property);
		return retval;
	}

	/*
	 * Generates the data-store key and entity from the kind and pkey given to
	 * the function by the user.
	 */
	private Entity getEntity(String kind, Object keyName)
			throws EntityNotFoundException {
		Key key;
		if (keyName instanceof String) {
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
