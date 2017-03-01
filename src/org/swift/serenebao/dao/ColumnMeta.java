package org.swift.serenebao.dao;

public class ColumnMeta {
	/**
	 * 类属性名
	 */
	private String field_name;
	/**
	 * 字段名
	 */
	private String column_name;
	/**
	 * 字段类型
	 */
	private Class column_clazz;


	public String getFieldMethod()
	{
		return ((column_clazz.equals(boolean.class)) ? "is" : "get")
		+ field_name.substring(0, 1).toUpperCase()
		+ field_name.substring(1, field_name.length());
	}
	public Class getColumn_clazz() {
		return column_clazz;
	}
	public void setColumn_clazz(Class column_clazz) {
		this.column_clazz = column_clazz;
	}
	public String getColumn_name() {
		return column_name;
	}
	public void setColumn_name(String column_name) {
		this.column_name = column_name;
	}
	public String getField_name() {
		return field_name;
	}
	public void setField_name(String field_name) {
		this.field_name = field_name;
	}
}
