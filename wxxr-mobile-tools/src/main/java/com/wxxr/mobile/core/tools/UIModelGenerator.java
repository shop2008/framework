/**
 * 
 */
package com.wxxr.mobile.core.tools;

import java.util.Set;

import javax.annotation.processing.Messager;
import javax.lang.model.element.Element;
import javax.tools.Diagnostic.Kind;

import com.wxxr.mobile.core.annotation.BindableBean;

/**
 * @author neillin
 *
 */
public class UIModelGenerator extends AbstractCodeGenerator {

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.tools.AbstractCodeGenerator#doCodeGeneration(java.util.Set, com.wxxr.mobile.core.tools.ICodeGenerationContext)
	 */
	@Override
	protected void doCodeGeneration(Set<? extends Element> elements,
			ICodeGenerationContext context) {
		for (Element element : elements) {
			BindableBean ann = element.getAnnotation(BindableBean.class);
			if(ann != null){
				String pkg = ann.pkg();
				Messager msgr = context.getProcessingEnvironment().getMessager();
				msgr.printMessage(Kind.WARNING, "Element :"+element.getSimpleName());
				msgr.printMessage(Kind.WARNING, "Packgae :"+pkg);
			}
		}
	}

}
