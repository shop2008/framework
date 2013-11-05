/**
 * 
 */
package com.wxxr.mobile.core.tools;

import java.util.Set;

import javax.lang.model.element.Element;

/**
 * @author neillin
 *
 */
public interface ICodeGenerator {
	void process(Set<? extends Element> elements,ICodeGenerationContext context);
	void finishProcessing(ICodeGenerationContext context);
}
