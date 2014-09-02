package org.pentaho.metastore.persist;

import org.pentaho.metastore.api.exceptions.MetaStoreException;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author nhudak
 */
public class DeferringObjectFactory implements IMetaStoreObjectFactory {
  public List<IMetaStoreObjectFactory> getObjectFactories() {
    return objectFactories;
  }

  List<IMetaStoreObjectFactory> objectFactories = new LinkedList<IMetaStoreObjectFactory>();

  @Override public Object instantiateClass( String className, Map<String, String> context ) throws MetaStoreException {
    Exception lastException = null;
    for ( IMetaStoreObjectFactory objectFactory : getObjectFactories() ) {
      try {
        Object object = objectFactory.instantiateClass( className, context );
        if ( object != null ) {
          return object;
        }
      } catch ( Exception e ) {
        // Store and keep trying
        lastException = e;
      }
    }

    String message = "Unable to load class " + className;
    if ( lastException != null ) {
      throw new MetaStoreException( message, lastException );
    } else {
      throw new MetaStoreException( message );
    }
  }

  @Override public Map<String, String> getContext( Object pluginObject ) throws MetaStoreException {
    HashMap<String, String> map = new HashMap<String, String>();

    for ( IMetaStoreObjectFactory objectFactory : getObjectFactories() ) {
      map.putAll( objectFactory.getContext( pluginObject ) );
    }

    return map;
  }
}
