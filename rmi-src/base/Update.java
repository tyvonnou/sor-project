package base;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import annotation.Column;

import java.lang.reflect.Field;

import helpers.ListHelpers;

public class Update implements Function<String, String> {

	protected String table;
	protected Map<String, SQLValue> set;
	protected Map<String, SQLValue> where;

	Update(String table) {
		this.table = table;
		this.set = new HashMap<>();
		this.where = new HashMap<>();
	}

	public void putSet(Field f, Object model) throws IllegalArgumentException, IllegalAccessException {
		String key = f.getName();
		Object value = f.get(model);
		int type = f.getAnnotation(Column.class).type().sqlType();
		this.set.put(key, new SQLValue(value, type));
	}

	public void putWhere(Field f, Object model) throws IllegalArgumentException, IllegalAccessException {
		String key = f.getName();
		Object value = f.get(model);
		int type = f.getAnnotation(Column.class).type().sqlType();
		this.where.put(key, new SQLValue(value, type));
	}

	public SQLValue[] values() {
		return this.set.values().toArray(new SQLValue[this.set.size()]);
	}
	
	public SQLValue[] where() {
		return this.where.values().toArray(new SQLValue[this.where.size()]);
	}

	@Override
	public String toString() {
		if (this.set.isEmpty()) {
			return "";
		}
		Object[] set = this.set.keySet().stream().map(this).toArray();
		Object[] where = this.where.keySet().stream().map(this).toArray();
		return String.format("UPDATE %s SET %s WHERE %s", this.table, ListHelpers.join(set), ListHelpers.join(where));
	}

	@Override
	public String apply(String t) {
		return t + "=?";
	}

}
