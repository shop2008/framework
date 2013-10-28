/**
 * 
 */
package com.wxxr.mobile.core.tools;

import com.wxxr.mobile.core.annotation.BindableBean;
import com.wxxr.mobile.core.annotation.Entity;
import com.wxxr.mobile.core.annotation.Generator;
import com.wxxr.mobile.core.tools.generator.BindableBeanGenerator;
import com.wxxr.mobile.core.tools.generator.UIViewModelGenerator;
import com.wxxr.mobile.core.ui.annotation.View;
import com.wxxr.mobile.core.tools.generator.DaoGenerator;

/**
 * @author neillin
 *
 */
@SuppressWarnings("restriction")
//@SupportedOptions({"version=2.1.0"})
public class TestProcessorConfigure implements IProcessorConfigure {

	@Generator(forAnnotation=BindableBean.class)
	public BindableBeanGenerator getBindableBeanGenerator(Generator annotation){
		return new BindableBeanGenerator();
	}
	@Generator(forAnnotation=View.class)
	public UIViewModelGenerator getUIPageModelGenerator(Generator annotation){
		return new UIViewModelGenerator();
	}
	
	@Generator(forAnnotation=Entity.class)
	public DaoGenerator getDaoGenerator(Generator annotation){
		return new DaoGenerator();
	}
}
