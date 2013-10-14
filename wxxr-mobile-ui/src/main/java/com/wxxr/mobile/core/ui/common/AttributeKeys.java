package com.wxxr.mobile.core.ui.common;

import com.wxxr.mobile.core.ui.api.AttributeKey;


public interface AttributeKeys {
			
	AttributeKey<Boolean> enabled = new AttributeKey<Boolean>() {

		public Class<Boolean> getValueType() {
			return Boolean.class;
		}

		public String getName() {
			return "enabled";
		}

	};
	
	AttributeKey<Integer> backgroundColor = new AttributeKey<Integer>() {

		public Class<Integer> getValueType() {
			return Integer.class;
		}

		public String getName() {
			return "backgroundColor";
		}
	};
	
	AttributeKey<Integer> foregroundColor = new AttributeKey<Integer>() {

		public Class<Integer> getValueType() {
			return Integer.class;
		}

		public String getName() {
			return "foregroundColor";
		}
	};


	AttributeKey<Integer> backgroundImageRid = new AttributeKey<Integer>() {

		public Class<Integer> getValueType() {
			return Integer.class;
		}

		public String getName() {
			return "backgroundImageRid";
		}
	};
	
	AttributeKey<String> backgroundImageURL = new AttributeKey<String>() {

		public Class<String> getValueType() {
			return String.class;
		}

		public String getName() {
			return "backgroundImageURL";
		}
	};


	
	AttributeKey<Boolean> visible = new AttributeKey<Boolean>() {

		public Class<Boolean> getValueType() {
			return Boolean.class;
		}

		public String getName() {
			return "visible";
		}

	};
	
	AttributeKey<String> name = new AttributeKey<String>() {

		public Class<String> getValueType() {
			return String.class;
		}

		public String getName() {
			return "name";
		}

	};
	
	AttributeKey<Boolean> takeSpaceWhenInvisible = new AttributeKey<Boolean>() {

		public Class<Boolean> getValueType() {
			return Boolean.class;
		}

		public String getName() {
			return "takeSpaceWhenInvisible";
		}

	};

	AttributeKey<?>[] keys = new AttributeKey<?>[] {
			enabled,
			visible,
			takeSpaceWhenInvisible,
			backgroundColor,
			foregroundColor,
			backgroundImageRid,
			backgroundImageURL,
			name
	};


}
