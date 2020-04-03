package config;

import java.util.MissingResourceException;

class ResourceBundle {
	
	public ResourceBundle(String baseName) {
		try {
			this.rb = java.util.ResourceBundle.getBundle(baseName);
		} catch (MissingResourceException e) {
			this.rb = null;
		}
	}
	
	public String getString(String key, String defaultValue) {
		try {
			return rb.getString(key);
		} catch (MissingResourceException | NullPointerException e) {
			return defaultValue;
		}
	}
	
	public Integer getInteger(String key, Integer defaultValue) {
		try {
			return Integer.valueOf(rb.getString(key));
		} catch (MissingResourceException | NullPointerException e) {
			return defaultValue;
		}
	}
	
	protected java.util.ResourceBundle rb;
}
