package org.pentaho.metastore.persist;

import org.pentaho.metastore.api.exceptions.MetaStoreException;

import java.util.Collections;
import java.util.Map;

/**
 * @author nhudak
 */
public class ClassLoaderObjectFactory implements IMetaStoreObjectFactory {

  private ClassLoader classLoader;

  public ClassLoaderObjectFactory() {
    this( Thread.currentThread().getContextClassLoader() );
  }

  public ClassLoaderObjectFactory( ClassLoader classLoader ) {
    this.classLoader = classLoader;
  }

  @Override public Object instantiateClass( String className, Map<String, String> context ) throws MetaStoreException {
    try {
      return classLoader.loadClass( className ).newInstance();
    } catch ( ReflectiveOperationException e ) {
      throw new MetaStoreException( e );
    }
  }

  @Override public Map<String, String> getContext( Object pluginObject ) throws MetaStoreException {
    return Collections.emptyMap();
  }
}
