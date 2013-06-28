/**
 * 
 */
package com.wxxr.mobile.core.tools;

import javax.annotation.processing.SupportedOptions;

import com.wxxr.mobile.core.annotation.Generator;
import com.wxxr.mobile.core.annotation.BindableBean;
import com.wxxr.mobile.core.tools.generator.BindableBeanGenerator;

/**
 * @author neillin
 *
 */
@SupportedOptions({"version=2.1.0"})
public class TestProcessorConfigure implements IProcessorConfigure {

	@Generator(forAnnotation=BindableBean.class)
	public BindableBeanGenerator getBindableBeanGenerator(Generator annotation){
		return new BindableBeanGenerator();
	}
}
