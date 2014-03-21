/**
 * 
 */
package com.wxxr.mobile.stock.app.common;

import java.util.List;

import com.wxxr.mobile.core.command.api.ICommand;

/**
 * @author neillin
 *
 */
public interface IEntityLoaderRegistry {
	/**
	 * K : key object type
	 * V : cache value object type
	 * T : DTO object type
	 * C : loading command object type
	 * @param entityName
	 * @return
	 */
	<K,V,T, C extends ICommand<List<T>>> IEntityLoader<K,V,T,C> getEntityLoader(String entityName);
	
	<K,V,T, C extends ICommand<List<T>>> IEntityLoaderRegistry registerEntityLoader(String entityName, IEntityLoader<K,V,T,C> loader);
	<K,V,T, C extends ICommand<List<T>>> IEntityLoaderRegistry unregisterEntityLoader(String entityName, IEntityLoader<K,V,T,C> loader);

}
