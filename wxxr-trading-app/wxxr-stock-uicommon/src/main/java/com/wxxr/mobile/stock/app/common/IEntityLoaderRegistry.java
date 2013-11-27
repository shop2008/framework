/**
 * 
 */
package com.wxxr.mobile.stock.app.common;

/**
 * @author neillin
 *
 */
public interface IEntityLoaderRegistry {
	IEntityLoader<?,?,?> getEntityLoader(String entityName);
	
	IEntityLoaderRegistry registerEntityLoader(String entityName, IEntityLoader<?,?,?> loader);
	IEntityLoaderRegistry unregisterEntityLoader(String entityName, IEntityLoader<?,?,?> loader);

}
