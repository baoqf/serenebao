package org.swift.serenebao.dao;

import static org.swift.serenebao.dao.FieldType.BOOLEAN;
import static org.swift.serenebao.dao.FieldType.DOUBLE;
import static org.swift.serenebao.dao.FieldType.FLOAT;
import static org.swift.serenebao.dao.FieldType.INTEGER;
import static org.swift.serenebao.dao.FieldType.JBOOLEAN;
import static org.swift.serenebao.dao.FieldType.JDATE;
import static org.swift.serenebao.dao.FieldType.JDOUBLE;
import static org.swift.serenebao.dao.FieldType.JFLOAT;
import static org.swift.serenebao.dao.FieldType.JINTEGER;
import static org.swift.serenebao.dao.FieldType.JLONG;
import static org.swift.serenebao.dao.FieldType.JSHORT;
import static org.swift.serenebao.dao.FieldType.JSTRING;
import static org.swift.serenebao.dao.FieldType.LONG;
import static org.swift.serenebao.dao.FieldType.SHORT;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.util.Date;

import org.swift.database.pool.DBConnect;

/*
 * 条件和赋值类
 * 鲍庆丰
 * 2007-3-29
 * baoqingfeng@gmail.com
 */
public class ConditionValue {
	private String condition;
	private Object[] values;
	
	
	/**
	 * 条件和值对象构造函数
	 * @param conditon 条件
	 * @param values 任意多个值,注意值个数要和条件中的?号个数相同
	 */
	public ConditionValue(String conditon,Object ... values)
	{
		this.condition = conditon;
		if(values!=null && values.length>0)
			this.values = values;
	}
	
	
	public int size()
	{
		if(values==null || values.length==0)
			return 0;
		return values.length;
	}
	
	public void setValueToCondition(int beginPos, DBConnect ps) throws Exception
	{
		setValueToCondition(beginPos,ps.getPreparedStatement());
	}
	public void setValueToCondition(DBConnect ps) throws Exception
	{
		setValueToCondition(0,ps);
	}
	public void setValueToCondition(CallableStatement cs) throws Exception
	{
		setValueToCondition(0,cs,null);
	}
	public void setValueToCondition(CallableStatement cs,int[] excludePos) throws Exception
	{
		setValueToCondition(0,cs,excludePos);
	}
	
	boolean isInExclue(int pos , int[] excludePos)
	{
		if(excludePos==null || excludePos.length==0)
			return false;
		
		for(int iPos : excludePos)
		{
			if(iPos==pos)
				return true;
		}
		return false;
	}
	
	public void setValueToCondition(int beginPos,CallableStatement ps,int[] excludePos) throws Exception
	{
		if(condition==null || values==null || values.length==0)
			return;
		for(int i=0;i<values.length;i++)
		{
			if(isInExclue(i+1,excludePos))
				continue;
			
			Class fieldClass = values[i].getClass();
			// int类型 long类型
			int fType = FieldType.getFieldType(fieldClass);
			switch (fType) {
			case INTEGER:
			case JINTEGER:
			case LONG:
			case JLONG:
			case SHORT:
			case JSHORT:
				ps.setLong(i+1+beginPos, Long.parseLong(values[i].toString()));
				break;
			case FLOAT:
			case JFLOAT:
			case DOUBLE:
			case JDOUBLE:
				ps.setDouble(i+1+beginPos, Double.parseDouble(values[i].toString()));
				break;
			case BOOLEAN:
			case JBOOLEAN:
				ps.setBoolean(i+1+beginPos, new Boolean(values[i].toString()));
				break;
			case JSTRING:
				ps.setString(i+1+beginPos,values[i].toString());
				break;
			case JDATE:
				ps.setTimestamp(i+1+beginPos, new java.sql.Timestamp(((Date)values[i]).getTime()));
				break;
			default:
				ps.setString(i+1+beginPos,values[i].toString());
			break;
			}
		}
	}	
	
	public void setValueToCondition(int beginPos,PreparedStatement ps) throws Exception
	{
		if(condition==null || values==null || values.length==0)
			return;
		for(int i=beginPos;i<values.length;i++)
		{
			Class fieldClass = values[i].getClass();
			// int类型 long类型
			int fType = FieldType.getFieldType(fieldClass);
			switch (fType) {
			case INTEGER:
			case JINTEGER:
			case LONG:
			case JLONG:
			case SHORT:
			case JSHORT:
				ps.setLong(i+1, Long.parseLong(values[i].toString()));
				break;
			case FLOAT:
			case JFLOAT:
			case DOUBLE:
			case JDOUBLE:
				ps.setDouble(i+1, Double.parseDouble(values[i].toString()));
				break;
			case BOOLEAN:
			case JBOOLEAN:
				ps.setBoolean(i+1, new Boolean(values[i].toString()));
				break;
			case JSTRING:
				ps.setString(i+1,values[i].toString());
				break;
			case JDATE:
				ps.setTimestamp(i+1, new java.sql.Timestamp(((Date)values[i]).getTime()));
				break;
			default:
				ps.setString(i+1,values[i].toString());
			break;
			}
		}
	}
	
	
	
	public String getCondition() {
		return condition;
	}
	public void setCondition(String condition) {
		this.condition = condition;
	}
	public Object[] getValues() {
		return values;
	}
	public void setValues(Object[] values) {
		this.values = values;
	}
	
	public String toString()
	{
		if(condition==null || condition.length()==0 || values==null || values.length==0)
			return condition;
		return String.format(condition.replaceAll("?", "%s"), values);
	}
}
