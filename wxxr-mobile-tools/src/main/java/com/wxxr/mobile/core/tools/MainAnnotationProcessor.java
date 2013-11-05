/**
 * 
 */
package com.wxxr.mobile.core.tools;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.ServiceLoader;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedOptions;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import com.sun.source.util.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wxxr.mobile.core.annotation.Generator;
import com.wxxr.mobile.core.util.StringUtils;

/**
 * @author neillin
 *
 */
@SuppressWarnings("restriction")
@SupportedSourceVersion(SourceVersion.RELEASE_6)
public class MainAnnotationProcessor extends AbstractProcessor {
	private static final Logger log = LoggerFactory.getLogger(MainAnnotationProcessor.class);
	
	private Map<Class<? extends Annotation>, ICodeGenerator> supportingAnnotations = new HashMap<Class<? extends Annotation>,ICodeGenerator>();
	private Set<String> supportAnnotationTypes;
	private Set<String> options = new HashSet<String>();
	private Properties props = new Properties();
	private VelocityTemplateRenderer renderer;
	private Trees trees;
	private LinkedList<ICodeGenerator> processors = new LinkedList<ICodeGenerator>();
	/* (non-Javadoc)
	 * @see javax.annotation.processing.AbstractProcessor#process(java.util.Set, javax.annotation.processing.RoundEnvironment)
	 */
	@Override
	public boolean process(Set<? extends TypeElement> annotations,
			RoundEnvironment roundEnv) {
		log.info("processing annations :"+annotations+", processor :"+this+", is last round :"+roundEnv.processingOver());
		GenContext ctx = null;
		if(!this.supportingAnnotations.isEmpty()){
			for (Entry<Class<? extends Annotation>, ICodeGenerator> entry : this.supportingAnnotations.entrySet()) {
				Class<? extends Annotation> annClazz = entry.getKey();
				ICodeGenerator gen = entry.getValue();
				Set<? extends Element> elems = roundEnv.getElementsAnnotatedWith(annClazz);
				log.info("Found annotated elements : {}",elems);
				if((elems != null)&&(elems.size() > 0)){
					if(ctx == null){
						ctx = new GenContext(roundEnv);
					}
					gen.process(elems, ctx);
					if(!processors.contains(gen)){
						processors.add(gen);
					}
				}
			}
		}
		if(roundEnv.processingOver()){
			for (ICodeGenerator gen : processors) {
				if(ctx == null){
					ctx = new GenContext(roundEnv);
				}
				gen.finishProcessing(ctx);
			}
		}else if(annotations.isEmpty()){
			for (ICodeGenerator gen : processors) {
				if(ctx == null){
					ctx = new GenContext(roundEnv);
				}
				gen.process(Collections.EMPTY_SET,ctx);
			}
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see javax.annotation.processing.AbstractProcessor#init(javax.annotation.processing.ProcessingEnvironment)
	 */
	@Override
	public synchronized void init(ProcessingEnvironment processingEnv) {
		log.info("Initializing Main Annotation processor ...");
		this.trees = Trees.instance(processingEnv);
		ServiceLoader<IProcessorConfigure> loader = ServiceLoader.load(IProcessorConfigure.class,getClass().getClassLoader());
		for (IProcessorConfigure config : loader) {	
			log.info("Found processor configure : {}",config);
			SupportedOptions so = config.getClass().getAnnotation(SupportedOptions.class);
			if  (so != null)  {
			    for (String s : so.value()) {
			    	this.options.add(s);
					int idx = s.indexOf('=');
					if(idx >0){
						String key = StringUtils.trimToNull(s.substring(0,idx));
						String val =  StringUtils.trimToNull(s.substring(idx+1));
						if((key != null)&&(val != null)){
							this.props.setProperty(key, val);
						}
					}
				}
		    }
			Method[] methods = config.getClass().getMethods();
			if(methods != null){
				for (Method method : methods) {
					if(method.getDeclaringClass() != Object.class){
						log.info("Found processor method : {}, annotaions : {}",method, StringUtils.join(method.getDeclaredAnnotations()));
					}else{
						continue;
					}
					Generator ann = method.getAnnotation(Generator.class);
					if(ann != null) {
						log.info("Found Generator annotation : {}",ann);
						Class<? extends Annotation> annClazz = ann.forAnnotation();
						Class<?>[] paramTypes = method.getParameterTypes();
						Class<?> returnType = method.getReturnType();
						if((annClazz != null)&&(paramTypes != null)&&(paramTypes.length == 1) && 
								(paramTypes[0] == Generator.class)&&(ICodeGenerator.class.isAssignableFrom(returnType))){
							try {
								ICodeGenerator gen = (ICodeGenerator)method.invoke(config, ann);
								this.supportingAnnotations.put(annClazz, gen);
							} catch (Exception e) {
								log.error("Failed to create code generator for annotation : {}",annClazz.getCanonicalName());
							}
						}						
					}
				}
			}
			
		}
		if(this.supportingAnnotations.isEmpty()){
			this.supportAnnotationTypes = Collections.EMPTY_SET;
		}else{
			this.supportAnnotationTypes = new HashSet<String>();
			for (Class<? extends Annotation> clazz  : this.supportingAnnotations.keySet()) {
				this.supportAnnotationTypes.add(clazz.getCanonicalName());
			}
		}
		this.renderer = new VelocityTemplateRenderer();
		this.renderer.configure(this.props);
		super.init(processingEnv);
		log.info("Done Main Annotation processor intialization !");
		log.info("support annotations {}",getSupportedAnnotationTypes());
	}

	
	
	/* (non-Javadoc)
	 * @see javax.annotation.processing.AbstractProcessor#getSupportedAnnotationTypes()
	 */
	@Override
	public Set<String> getSupportedAnnotationTypes() {
		return this.supportAnnotationTypes;
	}

	/* (non-Javadoc)
	 * @see javax.annotation.processing.AbstractProcessor#getSupportedOptions()
	 */
	@Override
	public Set<String> getSupportedOptions() {
		return this.options;
	}
	
	protected ITemplateRenderer getVelocityRenderer(){
		return this.renderer;
	}
	
	private class GenContext implements ICodeGenerationContext {
		private final RoundEnvironment env;
		
		public GenContext(RoundEnvironment env){
			this.env = env;
		}
		@Override
		public ProcessingEnvironment getProcessingEnvironment() {
			return processingEnv;
		}
		@Override
		public RoundEnvironment getRoundEnvironment() {
			return this.env;
		}
		@Override
		public ITemplateRenderer getTemplateRenderer() {
			return getVelocityRenderer();
		}
		@Override
		public Trees getTrees() {
			return trees;
		}
		
		
	}

}
