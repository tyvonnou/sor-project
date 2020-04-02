package base;

class SQLValue {
	private Object value;
	private int sqlType;

	SQLValue(Object value, int sqlType) {
		this.value = value;
		this.sqlType = sqlType;
	}

	public Object getValue() {
		return value;
	}

	public int getSqlType() {
		return sqlType;
	}
	
	@Override
	public String toString() {
		return String.format("Value: %s, Type: %d", this.getValue().toString(), this.getSqlType());
	}
}
