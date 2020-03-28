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
		ResourceBundle rb = ResourceBundle.getBundle(resourceBundleName);
		try {
			this.port = Integer.valueOf(rb.getString("port"));			
		} catch (MissingResourceException e) {
			this.port = 10000;
		}
		try {
			this.service = rb.getString("service");
		} catch (MissingResourceException e) {
			this.service = "sor-project";
		}
	}
	
	protected Integer port;
	protected String service;
}
