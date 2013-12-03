/**
 * 
 */
package com.wxxr.mobile.core.command.annotation;

/**
 * @author neillin
 *
 */
public class NetworkConstraintLiteral extends ConstraintLiteral{
	private NetworkConnectionType[] allowConnectionTypes;
	
	public NetworkConstraintLiteral(){
		
	}
	
	public NetworkConstraintLiteral(NetworkConnectionType[] types){
		if(types == null){
			throw new IllegalArgumentException("Invalid connection types : NULL");
		}
		this.allowConnectionTypes = types;
	}
	
	public NetworkConstraintLiteral(String[] types){
		if(types == null){
			throw new IllegalArgumentException("Invalid connection types : NULL");
		}
		this.allowConnectionTypes = new NetworkConnectionType[types.length];
		for (int i=0 ; i < types.length; i++) {
			this.allowConnectionTypes[i] = NetworkConnectionType.valueOf(types[i]);
		}
	}

	/**
	 * @return the allowConnectionTypes
	 */
	public NetworkConnectionType[] getAllowConnectionTypes() {
		return allowConnectionTypes;
	}

	/**
	 * @param allowConnectionTypes the allowConnectionTypes to set
	 */
	public void setAllowConnectionTypes(NetworkConnectionType[] allowConnectionTypes) {
		this.allowConnectionTypes = allowConnectionTypes;
	}
	
	public static NetworkConstraintLiteral fromAnnotation(NetworkConstraint ann){
		return new NetworkConstraintLiteral(ann.allowConnectionTypes());
	}
	
}
