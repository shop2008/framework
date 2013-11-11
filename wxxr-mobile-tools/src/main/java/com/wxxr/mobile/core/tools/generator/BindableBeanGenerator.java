/**
 * 
 */
package com.wxxr.mobile.core.tools.generator;

import java.io.Writer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.Filer;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.tools.JavaFileObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wxxr.mobile.core.annotation.BindableBean;
import com.wxxr.mobile.core.tools.AbstractCodeGenerator;
import com.wxxr.mobile.core.tools.ICodeGenerationContext;
import com.wxxr.mobile.core.util.StringUtils;
import com.wxxr.mobile.tools.model.BindableBeanModel;

/**
 * @author neillin
 *
 */
@SuppressWarnings("restriction")
public class BindableBeanGenerator extends AbstractCodeGenerator {
	
	private static final String TEMPATE_NAME = "/META-INF/template/BindableBean.vm";

	private static final Logger log = LoggerFactory.getLogger(BindableBeanGenerator.class);
	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.tools.AbstractCodeGenerator#doCodeGeneration(java.util.Set, com.wxxr.mobile.core.tools.ICodeGenerationContext)
	 */
	@Override
	protected void doCodeGeneration(Set<? extends Element> elements,
			ICodeGenerationContext context) {
		log.info("Generate code for elements : {}",elements);
		Filer filer = context.getProcessingEnvironment().getFiler();
		for (Element element : elements) {
			BindableBean ann = element.getAnnotation(BindableBean.class);
			if(ann != null){
				String pkg = StringUtils.trimToNull(ann.pkg());
				String className = StringUtils.trimToNull(ann.className());
				TypeElement typeElem = (TypeElement)element;
				String elementFQN = typeElem.getQualifiedName().toString();
				int idx = elementFQN.lastIndexOf('.');
				String defaultPkg ="";
				String defaultName = elementFQN;
				if(idx > 0){
					defaultPkg = elementFQN.substring(0,idx);
					if(defaultPkg.endsWith(".model")){
						defaultPkg = defaultPkg.substring(0,defaultPkg.length()-5)+"bean";
					}
					defaultName = elementFQN.substring(idx+1);
				}

				if(pkg == null){
					pkg = defaultPkg;
				}
				if(className == null){
					className = defaultName;
				}
				BindableBeanModel model = new BindableBeanModel();
				model.setPkgName(pkg);
				model.setName(className);
				List<? extends Element> children = typeElem.getEnclosedElements();
				if(children != null){
					for (Element child : children) {
						if(child.getKind() == ElementKind.FIELD){
							log.info("Found field {}, type :{}" , child,child.asType());
							//TypeElement type = (TypeElement)context.getProcessingEnvironment().getTypeUtils().asElement(child.asType());
							model.addField(child.getSimpleName().toString(), child.asType().toString());
						}
					}
				}
				if(model.getFields().size() > 0){
					Map<String, Object> attributes = new HashMap<String, Object>();
					attributes.put("model", model);

					try {
						String text = context.getTemplateRenderer().renderFromFile(TEMPATE_NAME, attributes);
						JavaFileObject file = filer.createSourceFile(model.getPkgName()+"."+model.getName());
						log.info("Generate java class file : {}",file.toUri());
						Writer w = file.openWriter();
						w.write(text);
						w.close();
					} catch (Exception e) {
						log.error("Failed to generate bindable bean class for :"+model.getPkgName()+"."+model.getName());
					}
				}
			}
		}

	}

}
