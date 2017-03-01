package org.swift.serenebao.dao;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
/*
 * java绫诲瀷鍜屾暟鎹簱瀛楁绫诲瀷鍏崇郴
 * 椴嶅簡涓�
 * 2007-3-29
 * baoqingfeng@gmail.com
 */
public class FieldType{
	private static Map<Class,FieldUnit> TLIST = new HashMap<Class,FieldUnit>(); 
	
	public final static int BOOLEAN = 1;
	public final static int JBOOLEAN = 2;
	
	public final static int INTEGER = 3;
	public final static int JINTEGER = 4;
	
	public final static int LONG = 5;
	public final static int JLONG = 6;
	
	public final static int SHORT = 7;
	public final static int JSHORT = 8;
	
	public final static int FLOAT = 9;
	public final static int JFLOAT = 10;
	
	public final static int DOUBLE = 11;
	public final static int JDOUBLE = 12;
	
	public final static int JSTRING = 13;
	public final static int JDATE = 14;
	public final static int CLOB = 15;
	public final static int BLOB = 15;
	static{
		TLIST.put(boolean.class,new FieldUnit(BOOLEAN,boolean.class,"tinyint",java.sql.Types.BOOLEAN,"getBoolean","setBoolean"));
		TLIST.put(Boolean.class,new FieldUnit(JBOOLEAN,Boolean.class,"tinyint",java.sql.Types.BOOLEAN,"getBoolean","setBoolean"));
		
		TLIST.put(int.class,new FieldUnit(INTEGER,int.class,"int",java.sql.Types.INTEGER,"getInt","setInt"));
		TLIST.put(Integer.class,new FieldUnit(JINTEGER,Integer.class,"int",java.sql.Types.INTEGER,"getInt","setInt"));
		
		TLIST.put(long.class,new FieldUnit(LONG,long.class,"bigint",java.sql.Types.BIGINT,"getLong","setLong"));
		TLIST.put(Long.class,new FieldUnit(JLONG,Long.class,"bigint",java.sql.Types.BIGINT,"getLong","setLong"));
		
		TLIST.put(short.class,new FieldUnit(SHORT,short.class,"smallint",java.sql.Types.SMALLINT,"getShort","setShort"));
		TLIST.put(Short.class,new FieldUnit(JSHORT,Short.class,"smallint",java.sql.Types.SMALLINT,"getShort","setShort"));
		
		TLIST.put(float.class,new FieldUnit(FLOAT,float.class,"decimal",java.sql.Types.FLOAT,"getFloat","setFloat"));
		TLIST.put(Float.class,new FieldUnit(JFLOAT,Float.class,"decimal",java.sql.Types.FLOAT,"getFloat","setFloat"));
		
		TLIST.put(double.class,new FieldUnit(DOUBLE,double.class,"decimal",java.sql.Types.DOUBLE,"getDouble","setDouble"));
		TLIST.put(Double.class,new FieldUnit(JDOUBLE,Double.class,"decimal",java.sql.Types.DOUBLE,"getDouble","setDouble"));
		
		TLIST.put(String.class,new FieldUnit(JSTRING,String.class,"varchar",java.sql.Types.VARCHAR,"getString","setString"));
		
		TLIST.put(Date.class,new FieldUnit(JDATE,Date.class,"datetime",java.sql.Types.TIMESTAMP,"getTimestamp","setTimestamp"));
		
		TLIST.put(Clob.class,new FieldUnit(CLOB,Clob.class,"text",java.sql.Types.CLOB,"getValue","setValue"));
		TLIST.put(Blob.class,new FieldUnit(BLOB,Blob.class,"blob",java.sql.Types.BLOB,"getValue","setValue"));
	}

	public static boolean isObjectField(Class type)
	{
		if(type.getName().indexOf(".")>0)
			return true;
		return false;
	}
	public static int getFieldType(Class type){
		FieldUnit unit = TLIST.get(type);
		if(unit==null)
			return 0;
		Integer pos = unit.getField_type();
		if(pos == null)
			return 0;
		else
			return pos.intValue();
	}
	
	public static FieldUnit getFieldUnit(Class type){
		return TLIST.get(type);
	}
	
	public static String getFieldResultSetGetMethod(Class type){
		return TLIST.get(type).getRs_getname();
	}
	
	public static String getFieldResultSetSetMethod(Class type){
		return TLIST.get(type).getRs_setname();
	}
	
	public static void main(String[] args) {
	}
}