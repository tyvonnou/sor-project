package config;

public class Config {
	public static final Config config = new Config("config/config");

	public Integer getPort() {
		return port;
	}

	public String getService() {
		return service;
	}

	public String getDatabaseHost() {
		return databaseHost;
	}

	public Integer getDatabasePort() {
		return databasePort;
	}

	public String getDatabaseName() {
		return databaseName;
	}
	
	public String getDatabaseUser() {
		return databaseUser;
	}
	
	public String getDatabasePassword() {
		return databasePassword;
	}
	
	public Integer getBufferSize() {
		return bufferSize;
	}

	public Config(String resourceBundleName) {
		ResourceBundle rb = new ResourceBundle(resourceBundleName);
		this.port = rb.getInteger("port", 10000);
		this.service = rb.getString("service", "sor-project");
		this.databaseHost = rb.getString("database.host", "localhost");
		this.databasePort = rb.getInteger("database.port", 3306);
		this.databaseName = rb.getString("database.name", "SorProject");
		this.databaseUser = rb.getString("database.user", "ubo");
		this.databasePassword = rb.getString("database.password", "ubo");
		this.bufferSize = rb.getInteger("bufferSize", 1000);
	}
	
	protected Integer port;
	protected String service;
	protected String databaseHost;
	protected Integer databasePort;
	protected String databaseName;
	protected String databaseUser;
	protected String databasePassword;
	protected Integer bufferSize;
}
