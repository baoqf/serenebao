package org.swift.serenebao.dao;

/*
 * 单个java类型和数据库字段类型关系
 * 鲍庆丰
 * 2007-3-29
 * baoqingfeng@gmail.com
 */
public class FieldUnit
{
	/**
	 * 字段类型
	 */
	private int field_type;
	/**
	 * 字段对应的java类型
	 */
	private Class field_java_class;
	/**
	 * 字段对应的java.sql.Types类型
	 */
	private int field_java_sql_types;
	/**
	 * 字段对应的数据库类型
	 */
	private String field_db_type_name;
	/**
	 * 通过jdbc从表->对象字段值获取方法
	 */
	private String rs_getname;
	/**
	 * 保存对象->表字段设置方法
	 */
	private String rs_setname;
	public FieldUnit()
	{
		
	}
	public FieldUnit(int field_type,Class field_java_class,String field_db_type_name,int field_java_sql_types,String rs_getname,String rs_setname)
	{
		this.field_type = field_type;
		this.field_java_class = field_java_class;
		this.field_db_type_name = field_db_type_name;
		this.field_java_sql_types = field_java_sql_types;
		this.rs_getname = rs_getname;
		this.rs_setname = rs_setname;
	}
	public int getField_type() {
		return field_type;
	}
	public void setField_type(int field_type) {
		this.field_type = field_type;
	}	
	
	public String getRs_getname() {
		return rs_getname;
	}
	public void setRs_getname(String rs_getname) {
		this.rs_getname = rs_getname;
	}
	public String getRs_setname() {
		return rs_setname;
	}
	public void setRs_setname(String rs_setname) {
		this.rs_setname = rs_setname;
	}
	public String getField_db_type_name() {
		return field_db_type_name;
	}
	public void setField_db_type_name(String field_db_type_name) {
		this.field_db_type_name = field_db_type_name;
	}
	public Class getField_java_class() {
		return field_java_class;
	}
	public void setField_java_class(Class field_java_class) {
		this.field_java_class = field_java_class;
	}
	public int getField_java_sql_types() {
		return field_java_sql_types;
	}
	public void setField_java_sql_types(int fieldJavaSqlTypes) {
		field_java_sql_types = fieldJavaSqlTypes;
	}

}