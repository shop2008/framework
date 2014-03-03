/**
 * 
 */
package com.wxxr.mobile.core.tools;

import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;

import com.sun.source.util.Trees;
import com.sun.tools.javac.util.Context;

/**
 * @author neillin
 *
 */
public interface ICodeGenerationContext {
	ProcessingEnvironment getProcessingEnvironment();
	RoundEnvironment getRoundEnvironment();
	ITemplateRenderer getTemplateRenderer();
	Trees getTrees();
	Context getJavacContext();
}
