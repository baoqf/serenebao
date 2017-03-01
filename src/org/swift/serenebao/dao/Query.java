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
 * 查询条件类
 * 鲍庆丰
 * baoqingfeng@gmail.com
 * 2007-3-27
 */
public class Query {
	private ConditionValue conditionCV;

	/**
	 * 构造任意查询条件
	 * <br>如：new Query(" id>? and name=? and passwd=? and state=?",12,"bao","135246",0)
	 * @param conditionvalue 任意长度条件
	 */
	public Query(Object ... conditionvalue)
	{
		if(conditionvalue==null || conditionvalue.length==0)
			return;
		if(conditionvalue.length==1)
			this.conditionCV = new ConditionValue((String)conditionvalue[0]);

		Object dest[] = new Object[conditionvalue.length-1];
		System.arraycopy(conditionvalue, 1, dest, 0, conditionvalue.length-1);
		this.conditionCV = new ConditionValue((String)conditionvalue[0],dest);
	}
	/**
	 * 构造查询条件
	 * <br>如：new Query(" where id>1")
	 * @param pre_condition 需要带where
	 */
	public Query(String pre_condition)
	{
		this.conditionCV = new ConditionValue(pre_condition);
	}
	/**
	 * 构造查询条件
	 * <br>如：new Query(" where id>?",id)
	 * @param pre_condition 条件
	 * @param value 值
	 */
	public Query(String pre_condition,Object value)
	{
		this.conditionCV = new ConditionValue(pre_condition,value);
	}
	/**
	 * 构造查询条件
	 * <br>如：new Query(" where id>? and name like ?",id,"%"+name+"%")
	 * @param pre_condition 条件
	 * @param value1 条件中的第一个值
	 * @param value2 条件中的第二个值
	 */
	public Query(String pre_condition,Object value1,Object value2)
	{
		this.conditionCV = new ConditionValue(pre_condition,value1,value2);
	}
	/**
	 * 构造查询条件
	 * <br>如：new Query(" where id>? and name like ? and type=?",id,"%"+name+"%",2)
	 * @param pre_condition 条件
	 * @param value1 条件中的第一个值
	 * @param value2 条件中的第二个值
	 * @param value3 条件中的第三个值
	 */
	public Query(String pre_condition,Object value1,Object value2,Object value3)
	{
		this.conditionCV = new ConditionValue(pre_condition,value1,value2,value3);
	}
	/**
	 * 构造查询条件
	 * <br>如：new Query(" where id>? and name like ? and type=? and sex=?",new Object[]{id,name,2,0})
	 * @param pre_condition 条件
	 * @param values 值数组
	 */
	public Query(String pre_condition,Object values[])
	{
		this.conditionCV = new ConditionValue(pre_condition,values);
	}
	/**
	 * 构造查询条件
	 * <br>如：new Query(new ConditionValue(" where id>? and name like ? and type=? and sex=? and addr like ?",id,name,2,0,addr))
	 * @param condition 条件和值对象，可以任意多个值
	 */
	public Query(ConditionValue condition)
	{
		this.conditionCV = condition;
	}

	public void setValueToField(DBConnect ps,int beginPs) throws Exception
	{
		if(conditionCV==null)
			return;
		Object[] values = conditionCV.getValues();
		if(values==null || values.length==0)
			return;
		int end = (beginPs+values.length);
		for(int i=beginPs,j=0;i<end || j<values.length;i++,j++)
		{
			if(values[j]==null)
				values[j] = "";
			Class fieldClass = values[j]==null?null:values[j].getClass();
			// int类型 long类型
			int fType = FieldType.getFieldType(fieldClass);
			switch (fType) {
			case INTEGER:
			case JINTEGER:
			case LONG:
			case JLONG:
			case SHORT:
			case JSHORT:
				ps.setLong(i+1, Long.parseLong(values[j].toString()));
				break;
			case FLOAT:
			case JFLOAT:
			case DOUBLE:
			case JDOUBLE:
				ps.setDouble(i+1, Double.parseDouble(values[j].toString()));
				break;
			case BOOLEAN:
			case JBOOLEAN:
				ps.setBoolean(i+1, new Boolean(values[j].toString()));
				break;
			case JSTRING:
				ps.setString(i+1,values[j].toString());
				break;
			case JDATE:
				ps.setTimestamp(i+1, new java.sql.Timestamp(((Date)values[j]).getTime()));
				break;
			default:
				ps.setString(i+1,values[j].toString());
			break;
			}
		}
	}
	public void setValueToFiled(DBConnect ps) throws Exception
	{
		setValueToField(ps,0);
	}

	public String getPre_condition() {
		if(conditionCV==null)
			return " ";
		String pre_condition=conditionCV.getCondition();
		Object[] values = conditionCV.getValues();
		//有where条件
		if(pre_condition!=null && pre_condition.length()>0 && values!=null && values.length>0)
			return " where " +pre_condition;
		//只是有order或其它语句
		if(pre_condition!=null && pre_condition.length()>0 && (values==null || values.length==0))
			return pre_condition;
		//统一都自己加where
		if(pre_condition!=null && pre_condition.length()>0)
			return " "+pre_condition;
		return " ";
	}

	public String getSqlDirect() {
		if(conditionCV==null)
			return "";
		return conditionCV.getCondition();
	}
}
