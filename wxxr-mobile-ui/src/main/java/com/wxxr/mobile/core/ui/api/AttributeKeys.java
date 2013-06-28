package com.wxxr.mobile.core.ui.api;

import com.wxxr.mobile.core.ui.utils.BooleanValueConvertor;
import com.wxxr.mobile.core.ui.utils.StringValueConvertor;

public interface AttributeKeys {
	
	 BooleanValueConvertor boolConvertor = new BooleanValueConvertor();
	 StringValueConvertor stringConvertor = new StringValueConvertor();
	
	AttributeKey<Boolean> enabled = new AttributeKey<Boolean>() {

		public Class<Boolean> getValueType() {
			return Boolean.class;
		}

		public String getName() {
			return "enabled";
		}

		public IValueConvertor<Boolean> getValueConvertor() {
			return boolConvertor;
		}
	};
	
	AttributeKey<Boolean> visible = new AttributeKey<Boolean>() {

		public Class<Boolean> getValueType() {
			return Boolean.class;
		}

		public String getName() {
			return "visible";
		}

		public IValueConvertor<Boolean> getValueConvertor() {
			return boolConvertor;
		}
	};
	
	AttributeKey<String> name = new AttributeKey<String>() {

		public Class<String> getValueType() {
			return String.class;
		}

		public String getName() {
			return "name";
		}

		public IValueConvertor<String> getValueConvertor() {
			return stringConvertor;
		}
	};

	AttributeKey<Boolean> required = new AttributeKey<Boolean>() {

		public Class<Boolean> getValueType() {
			return Boolean.class;
		}

		public String getName() {
			return "required";
		}

		public IValueConvertor<Boolean> getValueConvertor() {
			return boolConvertor;
		}
	};


}
