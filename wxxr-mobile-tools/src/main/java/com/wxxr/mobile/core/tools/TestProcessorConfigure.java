/**
 * 
 */
package com.wxxr.mobile.core.tools;

import com.wxxr.mobile.core.annotation.BindableBean;
import com.wxxr.mobile.core.annotation.Entity;
import com.wxxr.mobile.core.annotation.Generator;
import com.wxxr.mobile.core.tools.generator.BindableBeanGenerator;
import com.wxxr.mobile.core.tools.generator.DaoGenerator;

/**
 * @author neillin
 *
 */
//@SupportedOptions({"version=2.1.0"})
public class TestProcessorConfigure implements IProcessorConfigure {

	@Generator(forAnnotation=BindableBean.class)
	public BindableBeanGenerator getBindableBeanGenerator(Generator annotation){
		return new BindableBeanGenerator();
	}
	
	@Generator(forAnnotation=Entity.class)
	public DaoGenerator getDaoGenerator(Generator annotation){
		return new DaoGenerator();
	}
}
