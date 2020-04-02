package base;

import java.util.ArrayList;
import java.util.List;

import helpers.ListHelpers;

class PrimaryKeys {
	protected String table;
	protected List<String> keys;
	
	PrimaryKeys(String table) {
		this.table = table;
		this.keys = new ArrayList<>();
	}
	
	public void addKey(String key) {
		keys.add(key);
	}
	
	@Override
	public String toString() {
		if (this.keys.isEmpty()) {
			return "";
		}
		return String.format(
			"CONSTRAINT PK_%s PRIMARY KEY(%s)",
			table,
			ListHelpers.join(this.keys)
		);
	}
}
