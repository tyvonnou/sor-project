package base;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Logger;

import config.Config;
import models.Image;

public class Base {	
	static {
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public Base() {
		this(Config.config);
	}
	
	public Base(Config config) {
		this.user = config.getDatabaseUser();
		this.password = config.getDatabasePassword();
		this.url = String.format(
			"jdbc:mysql://%s:%d/%s?useUnicode=yes&characterEncoding=UTF-8&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC",
			config.getDatabaseHost(),
			config.getDatabasePort(),
			config.getDatabaseName()
		);
	}
	
	public boolean isClosed() {
		try {
			return this.conn.isClosed();
		} catch (NullPointerException e) {
			return true;
		} catch (SQLException e) {
			logger.severe(e.getMessage());
		}
		return true;
	}
	
	public boolean open() {
		if (!this.isClosed()) {
			return true;
		}
		try {
			this.conn = DriverManager.getConnection(this.url, this.user, this.password);
		} catch (SQLException e) {
			logger.severe(e.getMessage());
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public boolean close() {
		if (this.isClosed()) {
			return true;
		}
		try {
			conn.close();
		} catch (SQLException e) {
			logger.severe(e.getMessage());
			return false;
		}
		return true;
	}
	
	@SuppressWarnings("rawtypes")
	public void createTable(Class<? extends SQLModel> c) throws SQLException {
		String drop = SQLModel.generateDrop(c, true);
		String create = SQLModel.generateTable(c);
		this.open();
		try (Statement stm = this.conn.createStatement()) {
			logger.info(drop);
			stm.executeUpdate(drop);
			logger.info(create);
			stm.executeUpdate(create);
		}
		this.close();
	}
	
	public byte[] selectJpeg(String title) throws SQLException {
		this.open();
		PreparedStatement statement = this.conn.prepareStatement("SELECT jpeg FROM t_image WHERE titre=?");
		statement.setString(1, title);
		ResultSet rs = statement.executeQuery();
		byte[] result = new byte[0];
		if (rs.next()) {
			result = rs.getBytes("jpeg");
		}
		statement.close();
		rs.close();
		this.close();
		return result;
	}
	
	public static void main(String[] args) {
		Base b = new Base();
		try {
			b.createTable(Image.class);
		} catch (SQLException e) {
			logger.severe(e.getMessage());
		}
	}
	
	protected String url;
	protected String user;
	protected String password;
	protected Connection conn;
	private static final Logger logger = Logger.getLogger(Base.class.getName()); 
}
