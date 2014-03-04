/**
 * 
 */
package com.wxxr.trading.core.api;


/**
 * @author neillin
 *
 */
public class AttributeKey<T> {
	private final Class<T> clazz;
	private final String name;
	
	private AttributeKey(String name, Class<T> clazz){
		this.name = name;
		this.clazz = clazz;		
	}

	public static <V> AttributeKey<V> createKey(String name, Class<V> clazz) {
		return new AttributeKey<V>(name, clazz);
	}
	
	public Class<T> getValueType(){
		return this.clazz;
	}
	
	public String getName() {
		return this.name;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		@SuppressWarnings("rawtypes")
		AttributeKey other = (AttributeKey) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return name+"/"+clazz.getSimpleName();
	}

	public T valueof(String value){
		Object val = getValueByType(clazz, value);
		return clazz.cast(val);
	}
	

	/**
	 * @param valType
	 * @param value
	 */
	protected Object getValueByType(Class<?> valType, String value) {
		if(Byte.class == valType){
			return Byte.valueOf(value);
		}else if(Short.class == valType){
			return Short.valueOf(value);
		}else if(Integer.class == valType){
			return Integer.valueOf(value);
		}else if(Long.class == valType){
			return Long.valueOf(value);
		}else if(Float.class == valType){
			return Float.valueOf(value);
		}else if(Double.class == valType){
			return Double.valueOf(value);
		}else if(Boolean.class == valType){
			return Boolean.valueOf(value);
		}else if(String.class == valType){
			return value;
		}
		throw new IllegalArgumentException("Cannot convert string value :["+value+"] to type of :"+valType);
	}

	public T cast(Object val) {
		return this.clazz.cast(val);
	}
	
}
