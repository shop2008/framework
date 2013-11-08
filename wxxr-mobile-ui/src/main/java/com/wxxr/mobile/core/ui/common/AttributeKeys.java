package com.wxxr.mobile.core.ui.common;

import java.util.List;

import com.wxxr.mobile.core.ui.api.AttributeKey;
import com.wxxr.mobile.core.ui.api.ValidationError;


public abstract class AttributeKeys {
			
	public static final AttributeKey<Boolean> enabled = new AttributeKey<Boolean>(Boolean.class,"enabled");
	
	public static final AttributeKey<Integer> backgroundColor = new AttributeKey<Integer>(Integer.class,"backgroundColor");
	
	public static final AttributeKey<Integer> foregroundColor = new AttributeKey<Integer>(Integer.class,"foregroundColor");


	public static final AttributeKey<String> backgroundImageURI = new AttributeKey<String>(String.class,"backgroundImageURI");
	
	public static final AttributeKey<String> menuCategory = new AttributeKey<String>(String.class,"menuCategory");
	
	public static final AttributeKey<String> title = new AttributeKey<String>(String.class,"title");

	public static final AttributeKey<String> imageURI = new AttributeKey<String>(String.class,"imageURI");
	
	
	public static final AttributeKey<String> icon = new AttributeKey<String>(String.class,"icon");


	public static final AttributeKey<String> label = new AttributeKey<String>(String.class,"label");
	
	public static final AttributeKey<String> text = new AttributeKey<String>(String.class,"text");

	public static final AttributeKey<ValidationError[]> validationErrors = new AttributeKey<ValidationError[]>(ValidationError[].class,"validationErrors");

	
	public static final AttributeKey<Boolean> visible = new AttributeKey<Boolean>(Boolean.class,"visible");
	
	public static final AttributeKey<Boolean> selected = new AttributeKey<Boolean>(Boolean.class,"selected");

	public static final AttributeKey<Integer> textColor = new AttributeKey<Integer>(Integer.class,"textColor");
	
	public static final AttributeKey<Boolean> takeSpaceWhenInvisible = new AttributeKey<Boolean>(Boolean.class,"takeSpaceWhenInvisible");

	public static final AttributeKey<String> name = new AttributeKey<String>(String.class,"name");
	
	@SuppressWarnings("rawtypes")
	public static final AttributeKey<List> options = new AttributeKey<List>(List.class,"options");

	public static final AttributeKey<?>[] keys = new AttributeKey<?>[] {
			enabled,
			visible,
			takeSpaceWhenInvisible,
			backgroundColor,
			foregroundColor,
			backgroundImageURI,
			imageURI,
			name,
			text,
			imageURI,
			title,
			label,
			selected,
			menuCategory,
			options,
			textColor
	};

	public static AttributeKey<?> getByName(String name){
		for (AttributeKey<?> key : keys) {
			if(key.getName().equals(name)){
				return key;
			}
		}
		return null;
	}
}
