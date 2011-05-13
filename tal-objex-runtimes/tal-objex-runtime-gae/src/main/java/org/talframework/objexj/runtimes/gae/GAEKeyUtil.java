package org.talframework.objexj.runtimes.gae;

import org.talframework.objexj.ObjexObj;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

/**
 * Contains various helper methods for dealing with Google
 * App Engine Keys
 *
 * @author Tom Spencer
 */
public class GAEKeyUtil {

    /**
     * Calculates a key for a specific entity
     * 
     * @param root The root key (key of the container)
     * @param obj The object to get key for
     * @return The key for this object
     */
    public static Key getKey(Key root, ObjexObj obj) {
        Object val = obj.getId().getId();
        if( val instanceof Long ) return KeyFactory.createKey(root, obj.getType(), ((Long)val).longValue());
        else return KeyFactory.createKey(root, obj.getType(), val.toString());
    }
}
