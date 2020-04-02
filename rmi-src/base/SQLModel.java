package base;

import java.lang.reflect.Field;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import annotation.AutoIncrement;
import annotation.Column;
import annotation.NotNull;
import annotation.PrimaryKey;
import annotation.Table;
import helpers.ListHelpers;

public abstract class SQLModel<T> {
	
	@SuppressWarnings("rawtypes")
	public static String getTableName(Class<? extends SQLModel> c) {
		Table t = c.getAnnotation(Table.class);
		try {
			return t.name();
		} catch (NullPointerException e) {
			throw new Error(String.format("%s must be annoted by annotation.Table", c.getName()));
		}
	}
	
	@SuppressWarnings("rawtypes")
	public static String generateTable(Class<? extends SQLModel> c) {
		String tableName = getTableName(c);
		PrimaryKeys primaries = new PrimaryKeys(tableName);
		Field[] fields = c.getDeclaredFields();
		List<String> l = new ArrayList<>();
		for (Field field : fields) {
			List<String> fl = new ArrayList<>();
			String colName = field.getName();
			Column col = field.getAnnotation(Column.class);
			if (col == null) {
				continue;
			}
			fl.add(String.format("`%s`", colName));
			fl.add(col.type().toString());
			if (field.isAnnotationPresent(PrimaryKey.class)) {
				primaries.addKey(colName);
			}
			if (field.isAnnotationPresent(NotNull.class)) {
				fl.add("NOT NULL");
			}
			if (field.isAnnotationPresent(AutoIncrement.class)) {
				fl.add("AUTO_INCREMENT");
			}
			l.add(ListHelpers.join(fl, " "));
		}
		return String.format(
			"CREATE TABLE `%s`(%s,%s) CHARACTER SET utf8",
			tableName,
			ListHelpers.join(l),
			primaries.toString()
		);
	}
	
	@SuppressWarnings("rawtypes")
	public static String generateDrop(Class<? extends SQLModel> c, boolean exists) {
		return exists 
			? String.format("DROP TABLE IF EXISTS %s", getTableName(c))
			: String.format("DROP TABLE %s", getTableName(c));
	}
	
	@SuppressWarnings("unchecked")
	T insert(Base b) throws IllegalAccessException, SQLException {
		String tableName = getTableName(this.getClass());
		Field[] fields = this.getClass().getDeclaredFields();
		InsertStatement insert = new InsertStatement(tableName);
		Field primaryKey = null;
		for (Field field : fields) {
			Column column = field.getAnnotation(Column.class);
			if (column == null) {
				continue;
			}
			if (field.isAnnotationPresent(PrimaryKey.class)) {
				primaryKey = primaryKey == null ? field : null;
			}
			insert.put(field.getName(), field.get(this), column.type().sqlType());
		}
		b.open();
		try (PreparedStatement statement = b.conn.prepareStatement(insert.toString(), Statement.RETURN_GENERATED_KEYS)) {
			SQLValue[] values = insert.values();
			for (int i = 0; i < values.length; i++) {
				SQLValue value = values[i];
				statement.setObject(i + 1, value.getValue(), value.getSqlType());
			}
			int result = statement.executeUpdate();
			ResultSet rs = statement.getGeneratedKeys();
			if (rs.next()) {
				result = rs.getInt(1);
			}
			if (primaryKey != null) {
				primaryKey.set(this, result);
			}
		} finally {
			b.close();
		}
		return (T) this;
	}
}
