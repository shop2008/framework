/**
 * 
 */
package com.wxxr.mobile.core.tools;

import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;

/**
 * @author neillin
 *
 */
public interface ICodeGenerationContext {
	ProcessingEnvironment getProcessingEnvironment();
	RoundEnvironment getRoundEnvironment();
	ITemplateRenderer getTemplateRenderer();
}
