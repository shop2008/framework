/**
 * 
 */
package com.wxxr.mobile.core.tools.generator;

import static com.wxxr.mobile.core.model.ViewModelUtils.addField;
import static com.wxxr.mobile.core.model.ViewModelUtils.addMethod;

import java.io.Writer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.Filer;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Types;
import javax.tools.JavaFileObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.source.tree.ImportTree;
import com.sun.source.util.TreePath;
import com.wxxr.mobile.android.ui.BindableActivity;
import com.wxxr.mobile.android.ui.BindableFragment;
import com.wxxr.mobile.android.ui.BindableFragmentActivity;
import com.wxxr.mobile.android.ui.annotation.AndroidBinding;
import com.wxxr.mobile.core.model.PModeProviderClass;
import com.wxxr.mobile.core.model.TargetUIClass;
import com.wxxr.mobile.core.model.ViewDescriptorClass;
import com.wxxr.mobile.core.model.ViewModelClass;
import com.wxxr.mobile.core.tools.AbstractCodeGenerator;
import com.wxxr.mobile.core.tools.ICodeGenerationContext;
import com.wxxr.mobile.core.ui.annotation.View;
import com.wxxr.mobile.core.ui.api.IPage;
import com.wxxr.mobile.core.ui.common.PageBase;
import com.wxxr.mobile.core.util.StringUtils;

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
			if(ann != null){
				cnt++;
				String pkg = null; //
				TypeElement typeElem = (TypeElement)element;
//				UIViewModelSourceScanner scanner = new UIViewModelSourceScanner();
				String elementFQN = typeElem.getQualifiedName().toString();
//				log.warn("Class source :"+context.getTrees().getTree(element));
//				scanner.scan(treePath, context);
				int idx = elementFQN.lastIndexOf('.');
				String defaultPkg ="";
				String defaultName = elementFQN;
				if(idx > 0){
					defaultPkg = elementFQN.substring(0,idx);
					defaultName = elementFQN.substring(idx+1);
				}
				if(StringUtils.isBlank(pkg)){
					pkg = defaultPkg;
				}
				if(pkg.endsWith(".model")){
					pkg = pkg.substring(0,pkg.length()-6);
				}
				if(provider == null){
					provider = new PModeProviderClass();
					provider.setPkgName(pkg+".view");
					provider.setName("DeclarativePModelProvider");
				}
				ViewModelClass model = new ViewModelClass();
				model.setApplicationId(pkg);
				model.setPkgName(pkg+".view");
				model.setName(defaultName+"Model");
				if(StringUtils.isNotBlank(ann.name())){
					model.setId(StringUtils.trimToNull(ann.name()));
				}else{
					model.setId(defaultName);
				}
				TreePath treePath = context.getTrees().getPath(context.getProcessingEnvironment().getElementUtils().getTypeElement(elementFQN));
				List<? extends ImportTree> imports = treePath.getCompilationUnit().getImports();
				if(imports != null){
					for (ImportTree importTree : imports) {
						String importClass = importTree.getQualifiedIdentifier().toString();
						TypeElement importElem = context.getProcessingEnvironment().getElementUtils().getTypeElement(importClass);
						if((importElem != null)&&(importElem.getKind() == ElementKind.ANNOTATION_TYPE)){
							continue;
						}
						model.addImport(importClass);
					}
				}
				TypeMirror superClass = typeElem.getSuperclass();
				Types util = context.getProcessingEnvironment().getTypeUtils();
				TypeMirror type = typeElem.asType();
				TypeMirror pageType = context.getProcessingEnvironment().getElementUtils().getTypeElement(IPage.class.getCanonicalName()).asType();
				model.setPage(util.isAssignable(type, pageType));
				if(!util.isSameType(util.getNoType(TypeKind.NONE),superClass)){
					model.setSuperClass(((TypeElement)util.asElement(superClass)).getQualifiedName().toString());
				}else{
					model.setSuperClass(PageBase.class.getCanonicalName());
				}
				List<? extends TypeMirror> interfaces = typeElem.getInterfaces();
				if(interfaces != null){
					for (TypeMirror typeMirror : interfaces) {
						model.addInterface(((TypeElement)util.asElement(typeMirror)).getQualifiedName().toString());
					}
				}
				List<? extends Element> children = typeElem.getEnclosedElements();
				if(children != null){
					for (Element child : children) {
						switch(child.getKind()){
						case FIELD:
							addField(context,model,child);
							break;
						case METHOD:
							addMethod(context,model, child);
							break;
						}
					}
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
						targetUI.setName(defaultName);
						targetUI.setPkgName(pkg+".ui");
						String className = StringUtils.trimToNull(binding.superClassName());
						if(className != null){
							targetUI.setSuperClass(className);
						}else{
							switch(binding.type()){
							case ACTIVITY:
								targetUI.setSuperClass(BindableActivity.class.getCanonicalName());
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
				
				ViewDescriptorClass descriptor = new ViewDescriptorClass();
				descriptor.setTargetUI(targetUI);
				descriptor.setViewModel(model);
				descriptor.setName(defaultName+"Descriptor");
				descriptor.setPkgName(pkg+".view");
				descriptor.setLayoutId(binding.layoutId());
				descriptor.setBindingType(binding.type().name());
				this.provider.addDescriptor(descriptor.getClassName());
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
	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.tools.AbstractCodeGenerator#finishProcessing(com.wxxr.mobile.core.tools.ICodeGenerationContext)
	 */
	@Override
	public void finishProcessing(ICodeGenerationContext context) {
		log.info("Finish view model generation !");
	}

}
