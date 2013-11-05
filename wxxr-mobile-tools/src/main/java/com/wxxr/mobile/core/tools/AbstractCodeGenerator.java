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
public abstract class AbstractCodeGenerator implements ICodeGenerator {

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.tools.ICodeGenerator#process(java.util.Set, com.wxxr.mobile.core.tools.ICodeGenerationContext)
	 */
	@Override
	public final void process(Set<? extends Element> elements,
			ICodeGenerationContext context) {
		doCodeGeneration(elements, context);
	}

	
	protected abstract void doCodeGeneration(Set<? extends Element> elements,
			ICodeGenerationContext context);


	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.tools.ICodeGenerator#finishProcessing(com.wxxr.mobile.core.tools.ICodeGenerationContext)
	 */
	@Override
	public void finishProcessing(ICodeGenerationContext context) {
		
	}
}
