/**
 * 
 */
package com.wxxr.mobile.core.command.annotation;

/**
 * @author neillin
 *
 */
public class SecurityConstraintLiteral extends ConstraintLiteral{
	private String[] allowRoles;
	
	public SecurityConstraintLiteral(){
		
	}
	
	public SecurityConstraintLiteral(String[] roles){
		this.allowRoles = roles;
	}

	/**
	 * @return the allowRoles
	 */
	public String[] getAllowRoles() {
		return allowRoles;
	}

	/**
	 * @param allowRoles the allowRoles to set
	 */
	public void setAllowRoles(String[] allowRoles) {
		this.allowRoles = allowRoles;
	}
	
	public static SecurityConstraintLiteral fromAnnotation(SecurityConstraint constraint){
		return new SecurityConstraintLiteral(constraint.allowRoles());
	}
	
}
