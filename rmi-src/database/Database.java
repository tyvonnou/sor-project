package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Logger;

import config.Config;

public class Database {
	private static final Logger logger = Logger.getLogger(Database.class.getName());
	protected String url;
	protected String user;
	protected String password;
	protected Connection conn;
	
	static {
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
			logger.severe(e.getMessage());
		}
	}
	
	public Database() {
		this(Config.config);
	}
	
	public Database(Config config) {
		this.user = config.getDatabaseUser();
		this.password = config.getDatabasePassword();
		this.url = String.format(
			"jdbc:mysql://%s:%d/%s?useUnicode=yes&characterEncoding=UTF-8",
			config.getDatabaseHost(),
			config.getDatabasePort(),
			config.getDatabaseName()
		);
	}
	
	public boolean isClosed() {
		if (this.conn == null) {
			return true;
		}
		try {
			return this.conn.isClosed();
		} catch (SQLException e) {
			logger.fine(e.getMessage());
		}
		return true;
	}
	
	public boolean open() {
		try {
			this.conn = DriverManager.getConnection(this.url, this.user, this.password);
		} catch (SQLException e) {
			logger.severe(e.getMessage());
			return false;
		}
		return true;
	}
	
	public boolean close() {
		try {
			conn.close();
		} catch (SQLException e) {
			logger.fine(e.getMessage());
			return false;
		}
		return true;
	}	
	
}
