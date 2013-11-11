package com.wxxr.el.codegen;
/*
 * @(#)IProcessor.java	 May 17, 2011
 *
 * Copyright 2004-2011 WXXR Network Technology Co. Ltd. 
 * All rights reserved.
 * 
 * WXXR PROPRIETARY/CONFIDENTIAL.
 */



public interface IProcessor {
   Object doProcess(IProcessingContext context) throws Exception ;
}
