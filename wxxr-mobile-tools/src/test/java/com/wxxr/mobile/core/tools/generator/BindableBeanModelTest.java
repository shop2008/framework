package com.wxxr.mobile.core.tools.generator;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.wxxr.mobile.core.tools.ICodeGenerator;
import com.wxxr.mobile.core.tools.TestProcessorConfigure;
import com.wxxr.mobile.core.tools.VelocityTemplateRenderer;
import com.wxxr.mobile.core.tools.annotation.Generator;
import com.wxxr.mobile.core.util.StringUtils;
import com.wxxr.mobile.tools.model.BindableBeanModel;

public class BindableBeanModelTest extends BindableBeanModel {

	private static final String TEMPATE_NAME = "/META-INF/template/BindableBean.vm";
	private VelocityTemplateRenderer renderer;

	@Before
	public void setUp() throws Exception {
		renderer = new VelocityTemplateRenderer();
		renderer.configure(null);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testRender() throws IOException {
		BindableBeanModel model = new BindableBeanModel();
		model.setName("MyBindableBean");
		model.setPkgName("com.wxxr.mobile.test");
		model.addField("firstName", String.class);
		model.addField("lastName", String.class);
		model.addField("birthDate", Date.class);
		Map<String, Object> attributes = new HashMap<String, Object>();
		attributes.put("model", model);

		String text = renderer.renderFromFile(TEMPATE_NAME, attributes);
		
		System.out.println(text);

	}

	@Test
	public void testProcessorConfigure() throws Exception {
		TestProcessorConfigure config = new TestProcessorConfigure();
		Method[] methods = config.getClass().getMethods();
		Map<Class<? extends Annotation>, ICodeGenerator> gens = new HashMap<Class<? extends Annotation>, ICodeGenerator>();
		if(methods != null){
			for (Method method : methods) {
				System.out.println("Found processor method : "+method+", annotaions : "+ StringUtils.join(method.getAnnotations()));
				Generator ann = method.getAnnotation(Generator.class);
				if(ann != null) {
					Class<? extends Annotation> annClazz = ann.forAnnotation();
					Class<?>[] paramTypes = method.getParameterTypes();
					Class<?> returnType = method.getReturnType();
					if((annClazz != null)&&(paramTypes != null)&&(paramTypes.length == 1) && 
							(paramTypes[0] == Generator.class)&&(ICodeGenerator.class.isAssignableFrom(returnType))){
							ICodeGenerator gen = (ICodeGenerator)method.invoke(config, ann);
							gens.put(annClazz, gen);
					}
					
				}
			}
		}
		assertEquals(3, gens.size());
	}
}
