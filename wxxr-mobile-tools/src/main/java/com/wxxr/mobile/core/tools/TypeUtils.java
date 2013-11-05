package com.wxxr.mobile.core.tools;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.lang.model.element.Name;
import javax.lang.model.element.TypeElement;


@SuppressWarnings("restriction")
public class TypeUtils {

	private Logger logger = Logger.getLogger("TYPES");

	private final ImportOrganizer importOrganizer;
	private List<String> typeImports = new ArrayList<String>();

	public TypeUtils(ImportOrganizer importOrganizer) {
		this.importOrganizer = importOrganizer;
	}

	public String useType(Object type) {
		if (type instanceof TypeElement) {
			return getTypeUsage(((TypeElement) type).getQualifiedName().toString());
		} else if (type instanceof Name) {
			return getTypeUsage(((Name) type).toString());
		} else {
			return getTypeUsage(String.valueOf(type));
		}
	}

	private String getTypeUsage(String type) {
		String[] imports = importOrganizer.getTypeImports(type);
		String typeUsage = importOrganizer.getTypeUsage(type);

		if(logger.isLoggable(Level.FINE))
			logger.log(Level.FINE,String.format("Using type: %s => %s", type, typeUsage));

		List<String> newImports = Arrays.asList(imports);
		if (!newImports.isEmpty()) {
			if(logger.isLoggable(Level.FINE))
			logger.log(Level.FINE,String.format("Adding %s new imports: %s", newImports.size(),
					newImports));
		}
		typeImports.addAll(newImports);

		return typeUsage;
	}

	public List<String> getTypeImports() {
		Collections.sort(typeImports);
		return typeImports;
	}

	@Override
	public String toString() {
		return "methods=[useType($type)]";
	}
}