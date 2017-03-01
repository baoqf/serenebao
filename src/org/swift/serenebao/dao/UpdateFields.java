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

import java.util.Date;

import org.swift.database.pool.DBConnect;
/**
 * 更新指定字段
 * 鲍庆丰
 * baoqingfeng@gmail.com
 * 2007-3-27
 */
public class UpdateFields {
	private ConditionValue fieldCV;
	private ConditionValue conditionCV;
	
	/**
	 * 构造update赋值字段和条件,0个附值0个条件
	 * <br>如：new UpdateFields("state=1","id=1")
	 * @param setfield_sql 赋值字段
	 * @param condition_sql 条件
	 */
	public UpdateFields(String setfield_sql, String condition_sql)
	{
		this.fieldCV = new ConditionValue(setfield_sql);
		this.conditionCV = new ConditionValue(condition_sql);
	}
	/**
	 * 构造update赋值字段和条件,0个附值1个条件
	 * <br>如：new UpdateFields("state=1","id=?",1)
	 * @param setfield_sql 赋值字段
	 * @param condition_sql 条件
	 * @param condition_values1 条件值
	 */
	public UpdateFields(String setfield_sql, String condition_sql,Object condition_values1)
	{
		this.fieldCV = new ConditionValue(setfield_sql);
		this.conditionCV = new ConditionValue(condition_sql,condition_values1);
	}

	/**
	 * 构造update赋值字段和条件,1个附值0个条件
	 * <br>如：new UpdateFields("state=?",2,"id=122")
	 * @param setfield_sql 赋值字段
	 * @param setfield_value1 赋值的值
	 * @param condition_sql 条件
	 */
	public UpdateFields(String setfield_sql,Object setfield_value1, String condition_sql)
	{
		this.fieldCV = new ConditionValue(setfield_sql,setfield_value1);
		this.conditionCV = new ConditionValue(condition_sql);
	}

	/**
	 * 构造update赋值字段和条件,1个附值1个条件
	 * <br>如：new UpdateFields("state=?",2,"id=?",122)
	 * @param setfield_sql 赋值字段
	 * @param setfield_value1 赋值的值
	 * @param condition_sql 条件
	 * @param condition_values1 条件值
	 */
	public UpdateFields(String setfield_sql,Object setfield_value1, String condition_sql,Object condition_values1)
	{
		this.fieldCV = new ConditionValue(setfield_sql,setfield_value1);
		this.conditionCV = new ConditionValue(condition_sql,condition_values1);
	}

	/**
	 * 构造update赋值字段和条件,1个附值2个条件
	 * <br>如：new UpdateFields("state=?",2,"begintime between ? and ?",date1,date2)
	 * @param setfield_sql 赋值字段
	 * @param setfield_value1 赋值1的值
	 * @param condition_sql 条件
	 * @param condition_values1 条件1的值
	 * @param condition_values2 条件2的值
	 */
	public UpdateFields(String setfield_sql,Object setfield_value1, String condition_sql,Object condition_values1,Object condition_values2)
	{
		this.fieldCV = new ConditionValue(setfield_sql,setfield_value1);
		this.conditionCV = new ConditionValue(condition_sql,condition_values1,condition_values2);
	}

	/**
	 * 构造update赋值字段和条件,2个附值1个条件
	 * <br>如：new UpdateFields("state=?,fee=?",2,fee,"id=?",id)
	 * @param setfield_sql 赋值字段
	 * @param setfield_value1 赋值1的值
	 * @param setfield_value2 赋值2的值
	 * @param condition_sql 条件
	 * @param condition_values1 条件1的值
	 */
	public UpdateFields(String setfield_sql,Object setfield_value1,Object setfield_value2, String condition_sql,Object condition_values1)
	{
		this.fieldCV = new ConditionValue(setfield_sql,setfield_value1,setfield_value2);
		this.conditionCV = new ConditionValue(condition_sql,condition_values1);
	}

	/**
	 * 构造update赋值字段和条件,2个附值2个条件
	 * <br>如：new UpdateFields("state=?,fee=?",2,fee,"begintime between ? and ?",date1,date2)
	 * @param setfield_sql 赋值字段
	 * @param setfield_value1 赋值1的值
	 * @param setfield_value2 赋值2的值
	 * @param condition_sql 条件
	 * @param condition_values1 条件1的值
	 * @param condition_values2 条件2的值
	 */
	public UpdateFields(String setfield_sql,Object setfield_value1,Object setfield_value2, String condition_sql,Object condition_values1,Object condition_values2)
	{
		this.fieldCV = new ConditionValue(setfield_sql,setfield_value1,setfield_value2);
		this.conditionCV = new ConditionValue(condition_sql,condition_values1,condition_values2);
	}

	/**
	 * 构造update赋值字段和条件,1个附值n个条件
	 * <br>如：new UpdateFields("state=?",2,new ConditionValue("begintime between ? and ? and id=?",date1,date2,id))
	 * @param setfield_sql 赋值字段
	 * @param setfield_value1 赋值1的值
	 * @param condition 条件和赋值
	 */
	public UpdateFields(String setfield_sql,Object setfield_value1, ConditionValue condition)
	{
		this.fieldCV = new ConditionValue(setfield_sql,setfield_value1);
		this.conditionCV = condition;
	}

	/**
	 * 构造update赋值字段和条件,2个附值n个条件
	 * <br>如：new UpdateFields("state=?,fee=?",2,fee,new ConditionValue("begintime between ? and ? and id=?",date1,date2,id))
	 * @param setfield_sql 赋值字段
	 * @param setfield_value1 赋值1的值
	 * @param setfield_value2 赋值2的值
	 * @param condition 条件和赋值
	 */
	public UpdateFields(String setfield_sql,Object setfield_value1,Object setfield_value2 , ConditionValue condition)
	{
		this.fieldCV = new ConditionValue(setfield_sql,setfield_value1,setfield_value2);
		this.conditionCV = condition;
	}

	/**
	 * 构造update赋值字段和条件,n个附值1个条件
	 * <br>如：new UpdateFields(new ConditionValue("state=?,fee=?,money=?",2,fee,money),"id=?",id))
	 * @param field 赋值字段和值
	 * @param condition_sql 条件
	 * @param condition_value1 条件1的值
	 */
	public UpdateFields(ConditionValue field, String condition_sql,Object condition_value1)
	{
		this.fieldCV = field;
		this.conditionCV = new ConditionValue(condition_sql,condition_value1);
	}

	/**
	 * 构造update赋值字段和条件,n个附值2个条件
	 * <br>如：new UpdateFields(new ConditionValue("state=?,fee=?,money=?",2,fee,money),"begintime between ? and ?",date1,date2)
	 * @param field 赋值字段和值
	 * @param condition_sql 条件
	 * @param condition_value1 条件1的值
	 * @param condition_value2 条件2的值
	 */
	public UpdateFields(ConditionValue field, String condition_sql,Object condition_value1,Object condition_value2)
	{
		this.fieldCV = field;
		this.conditionCV = new ConditionValue(condition_sql,condition_value1,condition_value2);

	}

	/**
	 * 构造update赋值字段和条件,n个附值n个条件
	 * <br>如：new UpdateFields(new ConditionValue("state=?,fee=?,money=?",2,fee,money),new ConditionValue("begintime between ? and ? and id=?",date1,date2,id))
	 * @param field 赋值字段和值
	 * @param condition 条件和赋值
	 */
	public UpdateFields(ConditionValue field,ConditionValue condition)
	{
		this.fieldCV = field;
		this.conditionCV = condition;
	}
	public String getSetString()
	{
		if(fieldCV==null)
			return null;
		return fieldCV.getCondition();
	}
	
	public void setValueToCondition(DBConnect ps) throws Exception
	{
		if(conditionCV==null || conditionCV.getValues()==null || conditionCV.getValues().length==0)
			return;
		Object[] condition_values = conditionCV.getValues();
		if(condition_values==null || condition_values.length==0)
			return;
		for(int i=0;i<condition_values.length;i++)
		{
			Class fieldClass = condition_values[i].getClass();
			// int类型 long类型
			int fType = FieldType.getFieldType(fieldClass);
			switch (fType) {
			case INTEGER:
			case JINTEGER:
			case LONG:
			case JLONG:
			case SHORT:
			case JSHORT:
				ps.setLong(i+1, Long.parseLong(condition_values[i].toString()));
				break;
			case FLOAT:
			case JFLOAT:
			case DOUBLE:
			case JDOUBLE:
				ps.setDouble(i+1, Double.parseDouble(condition_values[i].toString()));
				break;
			case BOOLEAN:
			case JBOOLEAN:
				ps.setBoolean(i+1, new Boolean(condition_values[i].toString()));
				break;
			case JSTRING:
				ps.setString(i+1,condition_values[i].toString());
				break;
			case JDATE:
				ps.setTimestamp(i+1, new java.sql.Timestamp(((Date)condition_values[i]).getTime()));
				break;
			default:
				ps.setString(i+1,condition_values[i].toString());
			break;
			}
		}
	}
	
	
	public void setValueToFiled(DBConnect ps) throws Exception
	{
		int pos_update=1;
		if(fieldCV!=null  && fieldCV.getValues()!=null && fieldCV.getValues().length>0)
		{
			Object[] setfield_values = fieldCV.getValues();
			//条件赋值
			for(int j=0;j<setfield_values.length;j++,pos_update++)
			{
				Class fieldClass = setfield_values[j].getClass();
				int fType = FieldType.getFieldType(fieldClass);
				switch (fType) {
				case INTEGER:
				case JINTEGER:
				case LONG:
				case JLONG:
				case SHORT:
				case JSHORT:
					ps.setLong(pos_update, Long.parseLong(setfield_values[j].toString()));
					break;
				case FLOAT:
				case JFLOAT:
				case DOUBLE:
				case JDOUBLE:
					ps.setDouble(pos_update, Double.parseDouble(setfield_values[j].toString()));
					break;
				case BOOLEAN:
				case JBOOLEAN:
					ps.setBoolean(pos_update, new Boolean(setfield_values[j].toString()));
					break;
				case JSTRING:
					ps.setString(pos_update, setfield_values[j].toString());
					break;
				case JDATE:
					ps.setTimestamp(pos_update, new java.sql.Timestamp(((Date)setfield_values[j]).getTime()));
					break;
				default:
					ps.setString(pos_update,setfield_values[j].toString());
					break;
				}
			}
		}
			if(conditionCV==null || conditionCV.getValues()==null || conditionCV.getValues().length==0)
				return;
			Object[] condition_values = conditionCV.getValues();
			for(int j=0;j<condition_values.length;j++,pos_update++)
			{
				Class fieldClass = condition_values[j].getClass();
				int fType = FieldType.getFieldType(fieldClass);
				switch (fType) {
				case INTEGER:
				case JINTEGER:
				case LONG:
				case JLONG:
				case SHORT:
				case JSHORT:
					ps.setLong(pos_update, Long.parseLong(condition_values[j].toString()));
					break;
				case FLOAT:
				case JFLOAT:
				case DOUBLE:
				case JDOUBLE:
					ps.setDouble(pos_update, Double.parseDouble(condition_values[j].toString()));
					break;
				case BOOLEAN:
				case JBOOLEAN:
					ps.setBoolean(pos_update, new Boolean(condition_values[j].toString()));
					break;
				case JSTRING:
					ps.setString(pos_update,condition_values[j].toString());
					break;
				case JDATE:
					ps.setTimestamp(pos_update, new java.sql.Timestamp(((Date)condition_values[j]).getTime()));
					break;
				default:
					ps.setString(pos_update,condition_values[j].toString());
					break;
				}
			}
	}
	
	public String getCondition_sql() {
		if(conditionCV==null)
			return "";
		String condition_sql = conditionCV.getCondition();
		Object []condition_values = conditionCV.getValues();
		//有where条件
		if(condition_sql!=null && condition_sql.length()>0 && condition_values!=null && condition_values.length>0)
			return " where " +condition_sql;
		//只是有order或其它语句
		if(condition_sql!=null && condition_sql.length()>0 && (condition_values==null || condition_values.length==0))
			return condition_sql;
		return "";
	}
	public ConditionValue getConditionCV() {
		return conditionCV;
	}
	public void setConditionCV(ConditionValue conditionCV) {
		this.conditionCV = conditionCV;
	}
	public ConditionValue getFieldCV() {
		return fieldCV;
	}
	public void setFieldCV(ConditionValue fieldCV) {
		this.fieldCV = fieldCV;
	}
}
