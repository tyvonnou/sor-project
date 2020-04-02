package base;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

class InsertStatement {
	
	protected String table;
	protected Map<String, SQLValue> keys;

	public InsertStatement(String table) {
		this.table = table;
		this.keys = new HashMap<>();
	}

	public void put(String key, Object value, int type) {
		this.keys.put(key, new SQLValue(value, type));
	}

	public SQLValue[] values() {
		return this.keys.values().toArray(new SQLValue[this.keys.size()]);
	}

	@Override
	public String toString() {
		if (this.keys.isEmpty()) {
			return "";
		}
		StringBuilder keys = new StringBuilder("INSERT INTO ");
		StringBuilder values = new StringBuilder("VALUES (?");
		Iterator<String> it = this.keys.keySet().iterator();
		keys.append(table);
		keys.append(" (");
		keys.append(it.next());
		while (it.hasNext()) {
			keys.append(",");
			keys.append(it.next());
			values.append(",?");
		}
		keys.append(") ");
		values.append(")");
		keys.append(values);
		return keys.toString();
	}
}
