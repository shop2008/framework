package com.wxxr.mobile.core.command.api;

/**
 * @author neillin
 *
 * @param <T>
 */
public interface ICommand<T>{
      
	String getCommandName();
   
   /**
    * return result object's class
    * @return
    */
   Class<T> getResultType();
   
   /**
    * validate command arguments, throw IllegalArgumentException if arguments is invalid
    */
   void validate();
}
