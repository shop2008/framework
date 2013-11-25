/**
 * 
 */
package com.wxxr.mobile.core.tools.generator;

import static com.wxxr.mobile.tools.model.ViewModelUtils.createViewModelClass;
import static com.wxxr.mobile.tools.model.ViewModelUtils.createWorkbenchModel;

import java.io.Writer;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.Filer;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.tools.JavaFileObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wxxr.mobile.android.ui.BindableFragment;
import com.wxxr.mobile.android.ui.BindableFragmentActivity;
import com.wxxr.mobile.android.ui.annotation.AndroidBinding;
import com.wxxr.mobile.core.tools.AbstractCodeGenerator;
import com.wxxr.mobile.core.tools.ICodeGenerationContext;
import com.wxxr.mobile.core.ui.annotation.View;
import com.wxxr.mobile.core.ui.annotation.Workbench;
import com.wxxr.mobile.core.util.StringUtils;
import com.wxxr.mobile.tools.model.PModeProviderClass;
import com.wxxr.mobile.tools.model.TargetUIClass;
import com.wxxr.mobile.tools.model.ViewDescriptorClass;
import com.wxxr.mobile.tools.model.ViewModelClass;
import com.wxxr.mobile.tools.model.WorkbenchModel;

/**
 * @author neillin
 *
 */
@SuppressWarnings("restriction")
public class UIViewModelGenerator extends AbstractCodeGenerator {
	
	private static final String TEMPATE_NAME = "/META-INF/template/ViewModel.vm";

	private static final Logger log = LoggerFactory.getLogger(UIViewModelGenerator.class);
	
	private PModeProviderClass provider;
	
	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.tools.AbstractCodeGenerator#doCodeGeneration(java.util.Set, com.wxxr.mobile.core.tools.ICodeGenerationContext)
	 */
	@Override
	protected void doCodeGeneration(Set<? extends Element> elements,
			ICodeGenerationContext context) {
		log.info("Generate code for elements : {}",elements);
		log.info("Processor in end round : {}",context.getRoundEnvironment().processingOver());
		Filer filer = context.getProcessingEnvironment().getFiler();
		int cnt = 0;
		for (Element element : elements) {
			View ann = element.getAnnotation(View.class);
			Workbench wbAnn = element.getAnnotation(Workbench.class);
			if(wbAnn != null){
				WorkbenchModel model = createWorkbenchModel(context,(TypeElement)element,wbAnn);
				if(provider == null){
					provider = new PModeProviderClass();
					provider.setPkgName(model.getPkgName());
					provider.setName("DeclarativePModelProvider");
				}
				provider.setWorkbenchDescriptor(model.getClassName());
				generateWorkbenchDescriptor(context,model,filer);
			}else if(ann != null){
				cnt++;
				TypeElement typeElem = (TypeElement)element;
				ViewModelClass model = createViewModelClass(context,typeElem,ann);
				if(provider == null){
					provider = new PModeProviderClass();
					provider.setPkgName(model.getApplicationId()+".view");
					provider.setName("DeclarativePModelProvider");
				}
				model.prepare(context);
				Map<String, Object> attributes = new HashMap<String, Object>();
				attributes.put("model", model);

				try {
					String text = context.getTemplateRenderer().renderFromFile(TEMPATE_NAME, attributes);
					JavaFileObject file = filer.createSourceFile(model.getPkgName()+"."+model.getName());
					log.info("Generate java class file : {}",file.toUri());
					Writer w = file.openWriter();
					w.write(text);
					w.close();
				} catch (Throwable e) {
					log.error("Failed to generate presentation model class for :"+model.getPkgName()+"."+model.getName(),e);
				}
				
				AndroidBinding binding = element.getAnnotation(AndroidBinding.class);
				TargetUIClass targetUI = null;
				if(binding != null){
					String vmFile = null;
					switch(binding.type()){
					case ACTIVITY:
						vmFile = "ActivityUI.vm";
						break;
					case FRAGMENT:
						vmFile = "FragmentUI.vm";
						break;
					case FRAGMENT_ACTIVITY:
						vmFile = "ActivityUI.vm";
						break;
					case VIEW:
						break;
					}
					if(vmFile != null){
						targetUI = new TargetUIClass();
						targetUI.setViewModel(model);
						targetUI.setName(model.getDefaultName());
						targetUI.setPkgName(model.getApplicationId()+".ui");
						String className = StringUtils.trimToNull(binding.superClassName());
						if(className != null){
							targetUI.setSuperClass(className);
						}else{
							switch(binding.type()){
							case ACTIVITY:
								targetUI.setSuperClass(BindableFragmentActivity.class.getCanonicalName());
								break;
							case FRAGMENT:
								targetUI.setSuperClass(BindableFragment.class.getCanonicalName());
								break;
							case FRAGMENT_ACTIVITY:
								targetUI.setSuperClass(BindableFragmentActivity.class.getCanonicalName());
								break;
							case VIEW:
								break;
							}
						}
						attributes.clear();
						attributes.put("model", targetUI);
						try {
							String text = context.getTemplateRenderer().renderFromFile("/META-INF/template/"+vmFile, attributes);
							JavaFileObject file = filer.createSourceFile(targetUI.getPkgName()+"."+targetUI.getName());
							log.info("Generate java class file : {}",file.toUri());
							Writer w = file.openWriter();
							w.write(text);
							w.close();
						} catch (Throwable e) {
							log.error("Failed to generate UI class for :"+targetUI.getPkgName()+"."+targetUI.getName(),e);
						}

					}
				}
				
				generateViewDescriptor(model.getName(),context, model,
						attributes, binding, targetUI,filer);
//				if((!model.isPage())&&(model.getAlias() != null)){
//					for (String aliasName : model.getAlias()) {
//						generateViewDescriptor(aliasName,context, model,
//								attributes, binding, targetUI,filer);
//					}
//				}
			}
		}
		if((cnt == 0)&&(this.provider != null)){
			HashMap<String, Object> attributes = new HashMap<String, Object>();
			attributes.put("model", this.provider);
			try {
				String text = context.getTemplateRenderer().renderFromFile("/META-INF/template/PModelProvider.vm", attributes);
				JavaFileObject file = filer.createSourceFile(this.provider.getPkgName()+"."+this.provider.getName());
				log.info("Generate java class file : {}",file.toUri());
				Writer w = file.openWriter();
				w.write(text);
				w.close();
				this.provider = null;
			} catch (Throwable e) {
				log.error("Failed to generate UI class for :"+this.provider.getPkgName()+"."+this.provider.getName(),e);
			}
			
		}

	}
	/**
	 * @param context
	 * @param filer
	 * @param pkg
	 * @param defaultName
	 * @param model
	 * @param attributes
	 * @param binding
	 * @param targetUI
	 */
	protected void generateViewDescriptor(String name,ICodeGenerationContext context,ViewModelClass model,
			Map<String, Object> attributes, AndroidBinding binding,
			TargetUIClass targetUI,Filer filer) {
		ViewDescriptorClass descriptor = new ViewDescriptorClass();
		descriptor.setTargetUI(targetUI);
		descriptor.setViewModel(model);
		descriptor.setName(name+"Descriptor");
		descriptor.setPkgName(model.getApplicationId()+".view");
		descriptor.setLayoutId(binding.layoutId());
		descriptor.setBindingType(binding.type().name());
		this.provider.addDescriptor(descriptor.getClassName());
		if(model.getAlias() != null){
			for (String a : model.getAlias()) {
				this.provider.addAlias(a, descriptor.getClassName());
			}
		}
		attributes.clear();
		attributes.put("model", descriptor);
		String vmFile = "ViewDescriptor.vm";
		if(model.isPage()){
			vmFile = "PageDescriptor.vm";
		}
		try {
			String text = context.getTemplateRenderer().renderFromFile("/META-INF/template/"+vmFile, attributes);
			JavaFileObject file = filer.createSourceFile(descriptor.getPkgName()+"."+descriptor.getName());
			log.info("Generate java class file : {}",file.toUri());
			Writer w = file.openWriter();
			w.write(text);
			w.close();
		} catch (Throwable e) {
			log.error("Failed to generate UI class for :"+descriptor.getPkgName()+"."+descriptor.getName(),e);
		}
	}
	
	protected void generateWorkbenchDescriptor(ICodeGenerationContext context,WorkbenchModel model,Filer filer) {
		Map<String, Object> attributes = new HashMap<String, Object>();
		attributes.put("model", model);
		String vmFile = "WorkbenchDescriptor.vm";
		try {
			String text = context.getTemplateRenderer().renderFromFile("/META-INF/template/"+vmFile, attributes);
			JavaFileObject file = filer.createSourceFile(model.getPkgName()+"."+model.getName());
			log.info("Generate java class file : {}",file.toUri());
			Writer w = file.openWriter();
			w.write(text);
			w.close();
		} catch (Throwable e) {
			log.error("Failed to generate Workbench descriptor for :"+model.getPkgName()+"."+model.getName(),e);
		}
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.tools.AbstractCodeGenerator#finishProcessing(com.wxxr.mobile.core.tools.ICodeGenerationContext)
	 */
	@Override
	public void finishProcessing(ICodeGenerationContext context) {
		log.info("Finish view model generation !");
	}

}
