package base;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.logging.Logger;

import annotation.Column;
import annotation.PrimaryKey;
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
	
	public <T extends SQLModel<T>> T insert(T model) throws SQLException, IllegalAccessException {
		return model.insert(this);
	}

	public <R> List<R> select(String sql, Function<ResultSet, R> function) throws SQLException {
		this.open();
		PreparedStatement statement = this.conn.prepareStatement(sql);
		ResultSet rs = statement.executeQuery();
		List<R> objects = new ArrayList<>();
		while (rs.next()) {
			objects.add(function.apply(rs));
		}
		statement.close();
		rs.close();
		this.close();
		return objects;
	}
	
	public <R> List<R> select(String sql, Consumer<PreparedStatement> setStatement, Function<ResultSet, R> function) throws SQLException {
		this.open();
		PreparedStatement statement = this.conn.prepareStatement(sql);
		setStatement.accept(statement);
		ResultSet rs = statement.executeQuery();
		List<R> objects = new ArrayList<>();
		while (rs.next()) {
			objects.add(function.apply(rs));
		}
		rs.close();
		statement.close();
		this.close();
		return objects;
	}

	public Integer deleteOne(SQLModel<?> model) throws SQLException, IllegalArgumentException, IllegalAccessException {
		Delete delete = new Delete(SQLModel.getTableName(model.getClass()));
		for (Field f : model.getClass().getFields()) {
			if (f.isAnnotationPresent(Column.class) && f.isAnnotationPresent(PrimaryKey.class)) {
				delete.putWhere(f, model);
			}
		}
		this.open();
		PreparedStatement statement = this.conn.prepareStatement(delete.toString());
		SQLValue[] where = delete.where();
		for (int i = 0; i < where.length; i++) {
			statement.setObject(i + 1, where[i].getValue(), where[i].getSqlType());
		}
		Integer res = statement.executeUpdate();
		try {
			statement.close();
			this.close();
		} catch (Exception e) {}
		return res;
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
