/**
 * 
 */
package com.wxxr.mobile.core.tools.generator;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.lang.model.element.*;
import javax.lang.model.type.*;
import javax.lang.model.util.*;

import com.wxxr.mobile.core.tools.ICodeGenerationContext;
import com.wxxr.mobile.core.util.StringUtils;
import com.wxxr.mobile.el.generator.ELException;
import com.wxxr.mobile.el.generator.JavaDomainResolver;

/**
 * @author neillin
 *
 */
@SuppressWarnings("restriction")
public class ViewModelDomainResolver implements JavaDomainResolver {

	private final ICodeGenerationContext context;
	private final Elements elemUtil;
	private final Types typeUtil;
	
	private Map<String, TypeMirror> registeredBeans = new HashMap<String, TypeMirror>();
	
	public ViewModelDomainResolver(ICodeGenerationContext ctx){
		this.context = ctx;
		this.elemUtil = ctx.getProcessingEnvironment().getElementUtils();
		this.typeUtil = ctx.getProcessingEnvironment().getTypeUtils();
	}
	
	public void registerBean(String name, TypeMirror type){
		this.registeredBeans.put(name, type);
	}
	/* (non-Javadoc)
	 * @see com.wxxr.mobile.el.generator.JavaDomainResolver#resoveIdentifierType(java.lang.String)
	 */
	@Override
	public TypeMirror resoveIdentifierType(String ident) {
		return this.registeredBeans.get(ident);
	}

   public boolean isBooleanType(TypeMirror type){
	   if(type.getKind() == TypeKind.BOOLEAN){
		   return true;
	   }
	   TypeMirror boolType = elemUtil.getTypeElement(Boolean.class.getCanonicalName()).asType();
	   return typeUtil.isSameType(type, boolType);
   }
   
   public boolean isTypeOfComparable(TypeMirror type){
	   TypeMirror numberType = elemUtil.getTypeElement(Comparable.class.getCanonicalName()).asType();
	   return typeUtil.isSubtype(type, numberType);
   }
   
   public boolean isNumberType(final TypeMirror type) {
	   if((type.getKind() == TypeKind.BYTE)||(type.getKind() == TypeKind.SHORT)||(type.getKind() == TypeKind.INT)||(type.getKind() == TypeKind.LONG)||(type.getKind() == TypeKind.FLOAT)||(type.getKind() == TypeKind.DOUBLE)){
		   return true;
	   }
	   TypeMirror numberType = elemUtil.getTypeElement(Number.class.getCanonicalName()).asType();
	   return typeUtil.isSubtype(type, numberType);
   }


	/* (non-Javadoc)
	 * @see com.wxxr.mobile.el.generator.JavaDomainResolver#generateGetIdentifierStatement(java.lang.String)
	 */
	@Override
	public String generateGetIdentifierStatement(String ident) {
		return "this".equals(ident) ? "this" : "this."+ident;
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.el.generator.JavaDomainResolver#resolvePropertyType(javax.lang.model.type.TypeMirror, java.lang.String)
	 */
	@Override
	public TypeMirror resolvePropertyType(String className, String propertyName) {
		TypeMirror source = elemUtil.getTypeElement(className).asType();
		if((source.getKind() == TypeKind.ARRAY)&&"length".equals(propertyName)){
			return typeUtil.getPrimitiveType(TypeKind.INT);
		}
		TypeElement typeElem = (TypeElement)typeUtil.asElement(source);
		List<ExecutableElement> methods = ElementFilter.methodsIn(typeElem.getEnclosedElements());
		String getterMethodName = "get"+StringUtils.capitalize(propertyName);
		for (ExecutableElement m : methods) {
			if(m.getSimpleName().equals(getterMethodName)&&m.getParameters().isEmpty()){
				return m.getReturnType();
			}
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.el.generator.JavaDomainResolver#generateGetPropertyStatement(javax.lang.model.type.TypeMirror, java.lang.String)
	 */
	@Override
	public String generateGetPropertyStatement(String className,
			String propertyName) {
		TypeMirror source = elemUtil.getTypeElement(className).asType();
		if((source.getKind() == TypeKind.ARRAY)&&"length".equals(propertyName)){
			return "length";
		}
		TypeElement typeElem = (TypeElement)typeUtil.asElement(source);
		List<ExecutableElement> methods = ElementFilter.methodsIn(typeElem.getEnclosedElements());
		String getterMethodName = "get"+StringUtils.capitalize(propertyName);
		String isMethodName = "is"+StringUtils.capitalize(propertyName);
		for (ExecutableElement m : methods) {
			if(m.getSimpleName().equals(getterMethodName)&&m.getParameters().isEmpty()){
				return getterMethodName+"()";
			}else if(m.getSimpleName().equals(isMethodName)&&m.getParameters().isEmpty()){
				return isMethodName+"()";
			}
		}
		throw new ELException("Cannot find getter method of property["+propertyName+"]@"+source.toString());
	}

	public boolean isSignatureSame(TypeMirror[] requiringParamTypes,TypeMirror[] providingParamTypes){
		int pNum1 = requiringParamTypes == null ? 0 : requiringParamTypes.length;
		int pNum2 = providingParamTypes == null ? 0 : providingParamTypes.length;
		if(pNum1 != pNum2){
			return false;
		}
		int size = providingParamTypes.length;
		for (int i=0 ; i < size ; i++) {
			TypeMirror type1 = requiringParamTypes[i];
			TypeMirror type2 = providingParamTypes[i];
			if(!typeUtil.isSameType(type1, type2)){
				return false;
			}
		}
		return true;
	}
	
	public boolean isSignatureAssignable(TypeMirror[] requiringParamTypes,TypeMirror[] providingParamTypes){
		int pNum1 = requiringParamTypes == null ? 0 : requiringParamTypes.length;
		int pNum2 = providingParamTypes == null ? 0 : providingParamTypes.length;
		if(pNum1 != pNum2){
			return false;
		}
		int size = providingParamTypes.length;
		for (int i=0 ; i < size ; i++) {
			TypeMirror type1 = requiringParamTypes[i];
			TypeMirror type2 = providingParamTypes[i];
			if(!typeUtil.isAssignable(type1, type2)){
				return false;
			}
		}
		return true;
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.el.generator.JavaDomainResolver#resolveMethodReturnType(javax.lang.model.type.TypeMirror, java.lang.String, javax.lang.model.type.TypeMirror[])
	 */
	@Override
	public TypeMirror resolveMethodReturnType(String className,
			String methodName, TypeMirror[] paramTypes) {
		TypeMirror source = elemUtil.getTypeElement(className).asType();
		TypeElement typeElem = (TypeElement)typeUtil.asElement(source);
		List<ExecutableElement> methods = ElementFilter.methodsIn(typeElem.getEnclosedElements());
		LinkedList<ExecutableElement> possibleMatchMethods = new LinkedList<ExecutableElement>();
		for (ExecutableElement m : methods) {
			ExecutableType exeType = (ExecutableType)m.asType();
			TypeMirror[]  providingSig = null;
			List<? extends TypeMirror> types = exeType.getParameterTypes();
			if((types != null)&&(types.size() > 0)){
				providingSig = types.toArray(new TypeMirror[0]);
			}
			if(m.getSimpleName().equals(methodName)){
				if(isSignatureSame(paramTypes,providingSig)){
					return m.getReturnType();
				}else if(isSignatureAssignable(paramTypes, providingSig)){
					possibleMatchMethods.add(m);
				}
			}
		}
		ExecutableElement m = possibleMatchMethods.size() > 0 ? possibleMatchMethods.iterator().next() : null;
		if(m != null){
			return m.getReturnType();
		}
		throw new ELException("Cannot find method :["+methodName+"("+((paramTypes != null)&&(paramTypes.length > 0) ?StringUtils.join(paramTypes,',') :"")+")]");
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.el.generator.JavaDomainResolver#resolveMultiplyResultType(javax.lang.model.type.TypeMirror, javax.lang.model.type.TypeMirror)
	 */
	@Override
	public TypeMirror resolveMultiplyResultType(TypeMirror op1, TypeMirror op2) {
	      if((op1.getKind() == TypeKind.DOUBLE)||(op2.getKind() == TypeKind.DOUBLE)){
	          return typeUtil.getPrimitiveType(TypeKind.DOUBLE);
	       }
	      if((op1.getKind() == TypeKind.LONG)||(op2.getKind() == TypeKind.LONG)){
	          return typeUtil.getPrimitiveType(TypeKind.LONG);
	       }
	      if((op1.getKind() == TypeKind.FLOAT)||(op2.getKind() == TypeKind.FLOAT)){
	          return typeUtil.getPrimitiveType(TypeKind.FLOAT);
	       }
	      if((op1.getKind() == TypeKind.INT)||(op2.getKind() == TypeKind.INT)){
	          return typeUtil.getPrimitiveType(TypeKind.INT);
	       }
	      if((op1.getKind() == TypeKind.SHORT)||(op2.getKind() == TypeKind.SHORT)){
	          return typeUtil.getPrimitiveType(TypeKind.SHORT);
	       }
	       return typeUtil.getPrimitiveType(TypeKind.BYTE);
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.el.generator.JavaDomainResolver#generateMethodCallStatement(javax.lang.model.type.TypeMirror, java.lang.String, java.lang.String[])
	 */
	@Override
	public String generateMethodCallStatement(String className,
			String methodName, String[] paramNames) {
	      return new StringBuffer(methodName).append('(').append((paramNames != null)&&(paramNames.length > 0) ? StringUtils.join(paramNames,',') : "").append(')').toString();
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.el.generator.JavaDomainResolver#getDefaultValueOfType(javax.lang.model.type.TypeMirror)
	 */
	@Override
	public String getDefaultValueOfType(TypeMirror type) {
	      if(!(type instanceof PrimitiveType)){
	          return "null";
	       }else if(isNumberType(type)){
	          return "0";
	       }else if(isBooleanType(type)){
	          return "false";
	       }
	       return "null";
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.el.generator.JavaDomainResolver#resolveCollectionType(javax.lang.model.type.TypeMirror)
	 */
	@Override
	public TypeMirror resolveCollectionType(TypeMirror type) {
		TypeMirror collectionType = elemUtil.getTypeElement(Collection.class.getCanonicalName()).asType();
		if(typeUtil.isSubtype(type, collectionType)){
			return ((DeclaredType)type).getTypeArguments().iterator().next();
		}
		throw new ELException("type :"+type+" is not type of collection !");
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.el.generator.JavaDomainResolver#getNextVarName()
	 */
	@Override
	public String getNextVarName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ICodeGenerationContext getContext() {
		return this.context;
	}

}