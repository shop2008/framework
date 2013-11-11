/*
 * @(#)JavaClosureDomainResolver.java	 May 16, 2011
 *
 * Copyright 2004-2011 WXXR Network Technology Co. Ltd. 
 * All rights reserved.
 * 
 * WXXR PROPRIETARY/CONFIDENTIAL.
 */

package com.wxxr.el.codegen;

/**
 * @class desc A JavaClosureDomainResolver.
 * 
 * @author Neil
 * @version v1.0 
 * @created time May 16, 2011  11:35:39 AM
 */
public interface JavaClosureDomainResolver extends JavaDomainResolver {
   String getClosureIdentifier();
}
