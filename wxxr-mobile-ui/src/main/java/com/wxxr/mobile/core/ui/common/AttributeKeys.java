package com.wxxr.mobile.core.ui.common;

import java.util.List;

import com.wxxr.mobile.core.ui.api.AttributeKey;
import com.wxxr.mobile.core.ui.api.ValidationError;


public interface AttributeKeys {
			
	AttributeKey<Boolean> enabled = new AttributeKey<Boolean>(Boolean.class,"enabled");
	
	AttributeKey<Integer> backgroundColor = new AttributeKey<Integer>(Integer.class,"backgroundColor");
	
	AttributeKey<Integer> foregroundColor = new AttributeKey<Integer>(Integer.class,"foregroundColor");


	AttributeKey<String> backgroundImageURI = new AttributeKey<String>(String.class,"backgroundImageURI");
	
	AttributeKey<String> menuCategory = new AttributeKey<String>(String.class,"menuCategory");
	
	AttributeKey<String> title = new AttributeKey<String>(String.class,"title");

	AttributeKey<String> imageURI = new AttributeKey<String>(String.class,"imageURI");
	
	
	AttributeKey<String> icon = new AttributeKey<String>(String.class,"icon");


	AttributeKey<String> label = new AttributeKey<String>(String.class,"label");
	
	AttributeKey<String> text = new AttributeKey<String>(String.class,"text");

	AttributeKey<ValidationError[]> validationErrors = new AttributeKey<ValidationError[]>(ValidationError[].class,"validationErrors");

	
	AttributeKey<Boolean> visible = new AttributeKey<Boolean>(Boolean.class,"visible");
	
	AttributeKey<Boolean> selected = new AttributeKey<Boolean>(Boolean.class,"selected");

	AttributeKey<Integer> textColor = new AttributeKey<Integer>(Integer.class,"textColor");
	
	AttributeKey<Boolean> takeSpaceWhenInvisible = new AttributeKey<Boolean>(Boolean.class,"takeSpaceWhenInvisible");

	AttributeKey<String> name = new AttributeKey<String>(String.class,"name");
	
	@SuppressWarnings("rawtypes")
	AttributeKey<List> options = new AttributeKey<List>(List.class,"options");

	AttributeKey<?>[] keys = new AttributeKey<?>[] {
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


}
