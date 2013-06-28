/**
 * 
 */
package com.wxxr.mobile.core.tools;

import java.util.Collection;
import java.util.Set;

import com.sun.mirror.apt.AnnotationProcessor;
import com.sun.mirror.apt.AnnotationProcessorEnvironment;
import com.sun.mirror.apt.AnnotationProcessorFactory;
import com.sun.mirror.declaration.AnnotationTypeDeclaration;

/**
 * @author neillin
 *
 */
public class MyAnnotationProcessorFactory implements AnnotationProcessorFactory {

	/* (non-Javadoc)
	 * @see com.sun.mirror.apt.AnnotationProcessorFactory#supportedOptions()
	 */
	@Override
	public Collection<String> supportedOptions() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.sun.mirror.apt.AnnotationProcessorFactory#supportedAnnotationTypes()
	 */
	@Override
	public Collection<String> supportedAnnotationTypes() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.sun.mirror.apt.AnnotationProcessorFactory#getProcessorFor(java.util.Set, com.sun.mirror.apt.AnnotationProcessorEnvironment)
	 */
	@Override
	public AnnotationProcessor getProcessorFor(
			Set<AnnotationTypeDeclaration> atds,
			AnnotationProcessorEnvironment env) {
		// TODO Auto-generated method stub
		return null;
	}

}
