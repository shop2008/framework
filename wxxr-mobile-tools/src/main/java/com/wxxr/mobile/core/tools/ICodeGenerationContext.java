/**
 * 
 */
package com.wxxr.mobile.core.tools;

import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import com.sun.source.util.Trees;

/**
 * @author neillin
 *
 */
@SuppressWarnings("restriction")
public interface ICodeGenerationContext {
	ProcessingEnvironment getProcessingEnvironment();
	RoundEnvironment getRoundEnvironment();
	ITemplateRenderer getTemplateRenderer();
	Trees getTrees();
}
