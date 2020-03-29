package config;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class Config {
	public static final Config config = new Config("config/config");
	
	public Integer getPort() {
		return this.port;
	}

	public String getService() {
		return this.service;
	}

	public Config(String resourceBundleName) {
		ResourceBundle rb = null;
		try {
			rb = ResourceBundle.getBundle(resourceBundleName);			
		} catch (MissingResourceException e) {
			this.port = DEFAULT_PORT;
			this.service = DEFAULT_SERVICE;
		}
		if (rb != null) {
			this.readRb(rb);
		}
	}
	
	protected void readRb(ResourceBundle rb) {
		try {
			this.port = Integer.valueOf(rb.getString("port"));			
		} catch (MissingResourceException e) {
			this.port = DEFAULT_PORT;
		}
		try {
			this.service = rb.getString("service");
		} catch (MissingResourceException e) {
			this.service = DEFAULT_SERVICE;
		}
	}
	
	protected static final int DEFAULT_PORT = 10000;
	protected static final String DEFAULT_SERVICE = "sor-project";
	protected Integer port;
	protected String service;
}
