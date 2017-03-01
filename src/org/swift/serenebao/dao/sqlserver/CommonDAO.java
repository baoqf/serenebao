/*
 * Created on 2004-2-10 To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package org.swift.serenebao.dao.sqlserver;

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

import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.Date;
import java.util.List;

import org.swift.database.pool.DBConnect;
import org.swift.serenebao.dao.BaseCommonDAO;
import org.swift.serenebao.dao.ColumnMeta;
import org.swift.serenebao.dao.FieldType;
import org.swift.serenebao.dao.Query;
import org.swift.serenebao.dao.UpdateFields;
import org.swift.serenebao.orm.cache.EntityCache;


/**
 * O/R MAPING类 鲍庆丰 baoqingfeng@gmail.com 2007-3-27 <br>
 * CommonDAO申请方式: <br>
 * CommonDAO dao = DAOFactory.getDAO(TestParent.class);(建议使用) <br>
 * CommonDAO dao = new CommonDAO(TestParent.class);(不建议使用) <br>
 * 如果一个连接只涉及一个表操作,建议传入的数据库连接为null,由内部申请， <br>
 * 涉及到多表操作，方法参数的数据库连接申请方法如下:
 * <li>方法1,使用默认数据库并手动提交,用在对同一数据库多表更新并有事务的情况下 <br>
 * DBConnect conn = new DBConnect(false); <br>
 * CommonDAO dao = DAOFactory.getDAO(TestParent.class); <br>
 * try{ <br>
 * dao.update(obj); <br>
 * dao.insert(obj2); <br>
 * conn.commit(); <br>} catch (Exception e) { <br>
 * conn.rollback(); <br>} finally { <br>
 * conn.close(); <br>}
 * <li>方法2,使用指定数据库并手动提交，用在多数据库有事务的情况下 <br>
 * CommonDAO dao1 = DAOFactory.getDAO(TestParent.class); <br>
 * CommonDAO dao2 = DAOFactory.getDAO(TestParent2.class); <br>
 * DBConnect conn1 = new DBConnect(false); <br>
 * DBConnect conn2 = new DBConnect(false); <br>
 * try{ <br>
 * dao1.update(obj,conn1); <br>
 * dao2.insert(obj2,conn2); <br>
 * conn1.commit(); <br>
 * conn2.commit(); <br>} catch (Exception e) { <br>
 * conn1.rollback(); <br>
 * conn2.rollback(); <br>} finally { <br>
 * conn1.close(); <br>
 * conn2.close(); <br>}
 */
public class CommonDAO extends BaseCommonDAO {
	public CommonDAO(Class<?> clazz) {
		super(clazz);
	}

	protected String getLimitString(String sql, int row_min, int page_size) {
		StringBuffer pagingSelect = new StringBuffer(1000);
		if (row_min == 0) {
			pagingSelect.append(sql);
			pagingSelect.insert(sql.indexOf("select") + 6, " top " + page_size);
		}
		else {
			pagingSelect.append("select top " + page_size
								+ " * from ("
								+ sql
								+ ") a where "
								+ super.identity.getColumn_name()
								+ " not in ( select top "
								+ row_min
								+ " "
								+ super.identity.getColumn_name()
								+ " from "
								+ super.getTableName()
								+")");
		}
		return pagingSelect.toString();
	}

	/**
	 * 使用insert插入数据 <br>
	 * 示例： <br>
	 * CommonDAO dao = DAOFactory.getDAO(TestChild.class); <br>
	 * TestChild object = new TestChild(); <br>
	 * object.setName("鲍庆丰"); <br>
	 * dao.insert(object, null);
	 *
	 * @param object ：待新增的对象，插入哪张表由对象的实体决定
	 * @param connect 数据库连接，如果为null由函数内部申请
	 * @return String的主键值，如果实体主键为Long型，需要外面自己转换
	 * @throws Exception
	 */
	public String insert(Object object) throws Exception {
		return insert(object,null);
	}
	public String insert(Object object, DBConnect connect) throws Exception {
		StringBuffer sql = new StringBuffer();
		Class clazz = object.getClass();
		ResultSet rs = null;
		// 获得主键的序列自增值
		Object iden = null;
		// 不处理事务时可以传null进来
		DBConnect connect2 = null;
		try {
			if (connect == null)
				connect2 = new DBConnect(false);
			else
				connect2 = connect;
			String rsstr = "select top 1 * from " + getTableName();
			rs = connect2.executeQuery(rsstr.toLowerCase());
			ResultSetMetaData metadate = rs.getMetaData();
			int colcount = metadate.getColumnCount();
			StringBuffer colBuf = new StringBuffer();
			StringBuffer parasBuf = new StringBuffer();
			// 按照数据库表中的字段来决定插入的对象
			for (int i = 1; i <= colcount; i++) {
				String colName = metadate.getColumnName(i);
				if (colName.equalsIgnoreCase(identity.getColumn_name()) && if_auto_increment)
					continue;
				ColumnMeta meta = this.columnList.get(colName.toLowerCase());
				if (meta == null)
					continue;
				colBuf.append(colName).append(",");
				parasBuf.append("?,");
			}
			colBuf.deleteCharAt(colBuf.length() - 1);
			parasBuf.deleteCharAt(parasBuf.length() - 1);
			// 组装sql语句
			sql.append("insert into ").append(getTableName()).append(" (").append(colBuf.toString());
			sql.append(") values (").append(parasBuf.toString()).append(")");

			if (sql_print)
				log.info(sql);
			connect2.prepareStatement(sql.toString());

			// 字段赋值
			int pos = 1;
			for (int i = 1; i <= colcount; i++) {
				String colName = metadate.getColumnName(i);
				if (colName.equalsIgnoreCase(identity.getColumn_name()) && if_auto_increment)
					continue;
				ColumnMeta meta = this.columnList.get(colName.toLowerCase());
				if (meta == null)
					continue;
				// 字段名
				String column = meta.getColumn_name();
				// 属性名
				String field = meta.getField_name();
				if (column.equals(identity.getColumn_name()) && if_auto_increment)
					continue;

				int fType = FieldType.getFieldType(meta.getColumn_clazz());

				String filedName = ((fType == FieldType.BOOLEAN) ? "is" : "get") + field.substring(0, 1).toUpperCase()
									+ field.substring(1, field.length());

				Method method = clazz.getDeclaredMethod(filedName, null);
				Object retObj = method.invoke(object, null);

				switch (fType) {
					case INTEGER:
					case JINTEGER:
						connect2.setInt(pos++, Integer.parseInt(retObj == null ? "0" : retObj.toString()));
						break;
					case LONG:
					case JLONG:
						connect2.setLong(pos++, Long.parseLong(retObj == null ? "0" : retObj.toString()));
						break;
					case SHORT:
					case JSHORT:
						connect2.setInt(pos++, Short.parseShort(retObj == null ? "0" : retObj.toString()));
						break;
					case FLOAT:
					case JFLOAT:
						connect2.setFloat(pos++, retObj == null ? 0 : new Float(retObj.toString()));
						break;
					case DOUBLE:
					case JDOUBLE:
						connect2.setDouble(pos++, retObj == null ? 0 : new Double(retObj.toString()));
						break;
					case JSTRING:
						connect2.setString(pos++, retObj == null ? "" : retObj.toString());
						break;
					case BOOLEAN:
					case JBOOLEAN:
						connect2.setBoolean(pos++, retObj == null ? false : new Boolean(retObj.toString()));
						break;
					case JDATE:
						Date date = (Date) retObj;
						connect2.setTimestamp(pos++, retObj == null ? null : new java.sql.Timestamp(date.getTime()));
						break;
				}
			}
			connect2.executeUpdate();
			// 不同连接保存各自的last_insert_id
			sql.delete(0, sql.length());
			sql.append("SELECT IDENT_CURRENT('"+this.table_name+"')");
			connect2.prepareStatement(sql.toString());
			rs = connect2.executeQuery();
			if (rs.next()) {
				iden = rs.getString(1);
			}
		}
		catch (Exception e) {
			if (connect == null)
				connect2.rollback();
			throw new Exception(e);
		}

		finally {
			if (connect == null) {
				connect2.close();
			}
		}
		putObj2Cache(iden, object);
		String ret = (iden == null) ? null : (String.valueOf(iden));
		return ret;
	}
	public int update(UpdateFields uf, DBConnect connect) throws Exception {

		// 不处理事务时可以传null进来
		DBConnect connect2 = null;
		int commit_rows = 0;
		try {
			if (connect == null)
				connect2 = new DBConnect(false);
			else
				connect2 = connect;
			String sql_where = uf.getCondition_sql();
			// 组装sql语句
			StringBuffer sql = new StringBuffer(200);
			sql.append("update ").append(getTableName()).append(" set ");
			sql.append(uf.getSetString());
			sql.append(" ").append(sql_where);
			if (sql_print)
				log.info(sql);
			connect2.prepareStatement(sql.toString());
			// 赋值
			uf.setValueToFiled(connect2);
			commit_rows = connect2.executeUpdate();

//			List list = getCurrentCollection("select * from " + getTableName()
//					+ sql_where,new Query(uf.getConditionCV()),connect2);
//
//			change_cache(list,UPDATE_BATCH);

		} catch (Exception e) {
			if (connect == null)
				connect2.rollback();
			throw new Exception(e);
		} finally {
			if (connect == null) {
				connect2.close();
			}
		}
		return commit_rows;
	}

	/**
	 * 获得序列当前值 <br>
	 * 示例： <br>
	 * CommonDAO dao = DAOFactory.getDAO(TestChild.class); <br>
	 * long rows = dao.getSeqCurValue ("user_code"); <br>
	 * 注意：使用本方法必须定义id_sequence表，包含idnumber和idname字段，同时要添加一条记录idname=序列名,idnumber=初始值，使用其它序列可以再增加一行。
	 *
	 * @param seq 序列名称
	 * @return 序列值
	 * @throws Exception
	 */
	public Long getSeqCurValue(String seq) throws Exception {
		DBConnect connect2 = null;
		Long id = null;
		try {
			connect2 = new DBConnect(false);
			String sql = "select top 1 idnumber from id_sequence where idname=?";
			id = (Long) this.getOneRowColBySql(new Query(sql, seq), Long.class, connect2);
		}
		finally {
			connect2.close();
		}
		return id;
	}

	/**
	 * 获得序列下一个值 <br>
	 * 示例： <br>
	 * CommonDAO dao = DAOFactory.getDAO(TestChild.class); <br>
	 * long rows = dao. getSeqNextValue ("user_code"); <br>
	 * 注意：使用本方法必须定义id_sequence表，包含idnumber和idname字段，同时要添加一条记录idname=序列名,idnumber=初始值，使用其它序列可以再增加一行。
	 *
	 * @param seq 序列名称
	 * @return 序列值
	 * @throws Exception
	 */
	public Long getSeqNextValue(String seq) throws Exception {
		DBConnect connect2 = null;
		Long id = null;
		try {
			connect2 = new DBConnect(false);
			String sql = "select idnumber from id_sequence where idname=? limit 0,1";
			String sql2 = "update id_sequence set idnumber=idnumber+1 where idname=?";
			connect2.prepareStatement(sql2);
			connect2.setString(1, seq);
			connect2.executeUpdate();
			id = (Long) this.getOneRowColBySql(new Query(sql, seq), Long.class, connect2);
			connect2.commit();
		}
		catch (Exception e) {
			connect2.rollback();
			throw new Exception(e);
		}
		finally {
			connect2.close();
		}
		return id;
	}
	/**
	 *  获得数据库服务器时间
	 * 	<br>示例：
	 * 	<br>CommonDAO dao = DAOFactory.getDAO(TestChild.class);
	 * 	<br>Date date = dao.getServerTime();
	 * @param
	 * @return 时间
	 * @throws Exception
	 */
	public Date getServerTime() throws Exception {
		DBConnect connect2 = null;
		Date date = null;
		try {
			connect2 = new DBConnect(false);
			String sql = "select getdate()";
			connect2.prepareStatement(sql);
			ResultSet rs = connect2.executeQuery();
			if(rs.next())
				date = rs.getTimestamp(1);
			connect2.commit();
		} catch (Exception e) {
			connect2.rollback();
			throw new Exception(e);
		} finally {
			connect2.close();
		}
		return date;
	}
}
