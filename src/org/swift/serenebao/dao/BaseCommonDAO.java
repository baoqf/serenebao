/*
 * Created on 2004-2-10
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
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

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.swift.database.pool.DBConnect;
import org.swift.serenebao.orm.anotation.Column;
import org.swift.serenebao.orm.anotation.Table;
import org.swift.serenebao.orm.cache.EntityCache;
import org.swift.util.lang.StringUtil;

/**
 * O/R MAPING类 鲍庆丰 baoqingfeng@gmail.com 2007-3-27 <br>
 * CommonDAO申请方式: <br>
 * CommonDAO dao = DAOFactory.getDAO(TestParent.class);(建议使用) <br>
 * CommonDAO dao = new CommonDAO(TestParent.class);(不建议使用) <br>
 * 如果一个连接只涉及一个表操作,建议传入的数据库连接为null,由内部申请， <br>
 * 涉及到多表操作，方法参数的数据库连接申请方法如下: <li>方法1,使用默认数据库并手动提交,用在对同一数据库多表更新并有事务的情况下 <br>
 * DBConnect conn = new DBConnect(false); <br>
 * CommonDAO dao = DAOFactory.getDAO(TestParent.class); <br>
 * try{ <br>
 * dao.update(obj); <br>
 * dao.insert(obj2); <br>
 * conn.commit(); <br>
 * catch (Exception e) { <br>
 * conn.rollback(); <br>
 * finally { <br>
 * conn.close(); <br> <li>方法2,使用指定数据库并手动提交，用在多数据库有事务的情况下 <br>
 * CommonDAO dao1 = DAOFactory.getDAO(TestParent.class); <br>
 * CommonDAO dao2 = DAOFactory.getDAO(TestParent2.class); <br>
 * DBConnect conn1 = new DBConnect(false); <br>
 * DBConnect conn2 = new DBConnect(false); <br>
 * try{ <br>
 * dao1.update(obj,conn1); <br>
 * dao2.insert(obj2,conn2); <br>
 * conn1.commit(); <br>
 * conn2.commit(); <br>
 * catch (Exception e) { <br>
 * conn1.rollback(); <br>
 * conn2.rollback(); <br>
 * finally { <br>
 * conn1.close(); <br>
 * conn2.close(); <br>
 */
public abstract class BaseCommonDAO {
	/**
	 * 更新缓存类型
	 */
	protected int INSERT_BATCH = 0;
	protected int UPDATE_BATCH = 1;
	protected int DELETE_BATCH = 2;
	protected int SELECT_BATCH = 3;
	/**
	 * 使用Log4j打印sql
	 */
	protected boolean sql_print = false;
	/**
	 * 本DAO对象对应的实体类
	 */
	protected Class table_clazz;
	/**
	 * 本DAO对象对应的表名
	 */
	protected String table_name;
	/**
	 * 本DAO对象对应的主键信息
	 */
	protected ColumnMeta identity;
	/**
	 * 主键是否自增
	 */
	protected Boolean if_auto_increment = true;
	/**
	 * 是否使用缓存
	 */
	// protected boolean use_cache = false;
	/**
	 * 取得日志
	 */
	protected Logger log = Logger.getLogger(BaseCommonDAO.class);
	/**
	 * entity中所有的字段信息，构造时确定
	 */
	protected HashMap<String, ColumnMeta> columnList = new HashMap<String, ColumnMeta>();
	/**
	 * 表中所有的字段信息，构造时确定,其中String为对应数据库中的字段的小写名称
	 */
	protected HashMap<String, ColumnMeta> fieldList = null;

	/**
	 * 构造函数，参数为实体类，如：Article.class
	 */
	public BaseCommonDAO() {
	}

	public BaseCommonDAO(Class<?> clazz) {
		init(clazz);
		// initField();
	}

	private void init(Class<?> clazz) {
		table_clazz = clazz;
		if (clazz != null) {
			// 默认使用类名作为表名
			String class_name = clazz.getName();
			table_name = class_name.substring((class_name.lastIndexOf('.') == -1 ? 0 : class_name.lastIndexOf('.') + 1), class_name.length()).toLowerCase();
			identity = new ColumnMeta();
			identity.setColumn_clazz(Long.class);
			identity.setColumn_name("id");
			identity.setField_name("id");
			int cache_size = 0;
			// 获得表的属性
			if (clazz.isAnnotationPresent(Table.class)) {
				Table formLabel = (Table) clazz.getAnnotation(Table.class);
				table_name = StringUtil.isEmpty(formLabel.name()) ? table_name : formLabel.name();
				if_auto_increment = formLabel.auto_increment();
				identity.setField_name(formLabel.id());
				cache_size = formLabel.cache_size();
				// use_cache = formLabel.use_cache();
			}

			// 获得字段名和类型，默认使用类成员属性作为表字段名，如果在@Column中自定义过按照自定义取
			for (Field fieldClass : clazz.getDeclaredFields()) {
				if (FieldType.getFieldType(fieldClass.getType()) == 0)
					continue;
				ColumnMeta meta = new ColumnMeta();
				meta.setColumn_clazz(fieldClass.getType());
				meta.setColumn_name(fieldClass.getName());
				meta.setField_name(fieldClass.getName());
				if (fieldClass.isAnnotationPresent(Column.class)) {
					Column fieldLabel = fieldClass.getAnnotation(Column.class);
					String label = StringUtil.isEmpty(fieldLabel.col_name()) ? fieldClass.getName() : fieldLabel.col_name();
					meta.setColumn_name(label);
				}
				if (meta.getField_name().equalsIgnoreCase(identity.getField_name())) {

					identity = meta;
				}
				columnList.put(meta.getColumn_name().toLowerCase(), meta);
			}
			// if(use_cache)
			// EntityCache.regedit(table_clazz,cache_size);
		}
	}

	private void initField(DBConnect connect) throws Exception {
		DBConnect connect2 = getConnect(connect);
		ResultSet rs = null;
		try {
			String update_sql = getLimitString("select * from " + getTableName(), 0, 0);
			connect2.prepareStatement(update_sql);
			if (sql_print)
				log.info(update_sql);
			rs = connect2.executeQuery();
			ResultSetMetaData metadate = rs.getMetaData();
			int colcount = metadate.getColumnCount();
			fieldList = new HashMap<String, ColumnMeta>();
			for (int i = 1; i <= colcount; i++) {
				ColumnMeta meta = new ColumnMeta();
				meta.setColumn_name(metadate.getColumnName(i));
				fieldList.put(meta.getColumn_name(), meta);
			}
		} catch (Exception e) {
			e.printStackTrace();
			if (connect == null)
				connect2.rollback();
			throw new Exception(e);
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (connect == null) {
				connect2.close();
			}
		}
	}

	/**
	 * 根据Sql和条件获得对像列表
	 * 
	 * @param sql
	 * @param query
	 * @param connect
	 * @return
	 * @throws Exception
	 */
	protected List getCurrentCollection(String sql, Query query, DBConnect connect) throws Exception {
		List col = new ArrayList();

		ResultSet rs = null;
		if (sql_print)
			log.info(sql.toLowerCase());
		// 不处理事务时可以传null进来
		DBConnect connect2 = getConnect(connect);
		try {
			connect2.prepareStatement(sql);
			if (query != null)
				query.setValueToFiled(connect2);
			rs = connect2.executeQuery();
			col = resultSetToCollection(rs);
		} catch (Exception e) {
			if (connect == null)
				connect2.rollback();
			throw new Exception(e);
		} finally {
			if (rs != null)
				rs.close();
			if (connect == null) {
				connect2.close();
			}
		}
		return col;
	}

	/**
	 * <br>
	 * 把数据库里的ResultSet转换为List对象 <br>
	 * 如果数据库中表字段在对象中没有get方法或者对象中属性没有对应表中的字段。都忽略
	 */
	protected List resultSetToCollection(ResultSet rs) throws Exception {
		List<Object> col = new ArrayList<Object>();
		ResultSetMetaData metadate = rs.getMetaData();
		int colcount = metadate.getColumnCount();
		while (rs.next()) {
			Object obj = table_clazz.newInstance();
			// 只取表和类中共有的字段
			for (int j = 1; j <= colcount; j++) {
				ColumnMeta meta = columnList.get(metadate.getColumnLabel(j).toLowerCase());
				if (meta == null)
					continue;
				String column = meta.getColumn_name();
				String field = meta.getField_name();
				// 方法名
				String filedName = "set" + field.substring(0, 1).toUpperCase() + field.substring(1, field.length());
				// 属性类型
				Class fieldClazz = meta.getColumn_clazz();

				// set到类中
				Class[] paraClass = { fieldClazz };
				Method method = table_clazz.getDeclaredMethod(filedName, paraClass);
				// 获得Rs值
				Class[] paraRsClass = { String.class };
				Method method_rs = ResultSet.class.getDeclaredMethod(FieldType.getFieldResultSetGetMethod(fieldClazz), paraRsClass);
				Object[] paraRsObj = { new String(column) };
				Object rsValue = method_rs.invoke(rs, paraRsObj);
				if(rs.wasNull() && FieldType.isObjectField(fieldClazz))
				{
					rsValue = null;
				}

				// set到类中
				method.invoke(obj, new Object[] { rsValue });

			}
			col.add(obj);
		}
		return col;
	}

	/**
	 * 根据条件获得一个实体对象
	 * 
	 * @param query
	 *            条件
	 * @return 一条记录
	 * @throws Exception
	 */
	public Object getOne(Object... objects) throws Exception {
		return getOne(new Query(objects));
	}

	public Object getOne(Query query) throws Exception {
		List list = this.listAllAll(query);
		if (list == null || list.size() == 0)
			return null;
		return list.iterator().next();
	}

	/**
	 * 根据条件获得一个实体对象
	 * 
	 * @param query
	 *            条件
	 * @return 一条记录
	 * @throws Exception
	 */

	public Object getOneBySql(Query query) throws Exception {
		List list = this.listAllBySql(query);
		if (list == null || list.size() == 0)
			return null;
		return list.iterator().next();
	}
	
	/**
	 * 获得实体对象主键的值
	 * 
	 * @param obj
	 *            已获得的实体对象
	 * @return 主键值
	 * @throws Exception
	 */
	public Object getIdValue(Object obj) throws Exception {
		if (identity == null || obj == null)
			return null;
		String field = identity.getField_name();

		String filedName = "get" + field.substring(0, 1).toUpperCase() + field.substring(1, field.length());

		Method method = obj.getClass().getDeclaredMethod(filedName, null);
		Object retObj = method.invoke(obj, null);
		return retObj;
	}

	/**
	 * 根据主键查询一条记录 <br>
	 * 示例： <br>
	 * CommonDAO dao = DAOFactory.getDAO(TestChild.class); <br>
	 * dao.get(1, null);
	 * 
	 * @param id
	 *            主键值
	 * @param connect
	 *            数据库连接，如果为null由函数内部申请
	 * @return 对象，需要在外部强制转换
	 * @throws Exception
	 */
	public Object get(Object id) throws Exception {
		return get(id, null);
	}

	public Object get(Object id, DBConnect connect) throws Exception {
		Object obj = null;
		// if(use_cache)
		// {
		// obj = EntityCache.get(id,table_clazz);
		// if(obj!=null)
		// return obj;
		// }

		List col = listAllAll(new Query(identity.getColumn_name() + "=?", id), connect);

		if (col == null || col.size() == 0)
			return null;
		obj = col.get(0);
		// if(use_cache)
		// {
		// Object idobj = getIdValue(obj);
		// if(idobj!=null)
		// EntityCache.put(idobj,obj,table_clazz);
		// }
		return obj;
	}

	/**
	 * 根据主键delete <br>
	 * 示例： <br>
	 * CommonDAO dao = DAOFactory.getDAO(TestChild.class); <br>
	 * dao.delete(1, null);
	 * 
	 * @param id
	 *            主键值
	 * @param connect
	 *            数据库连接，如果为null由函数内部申请
	 * @return 返回删除的行数，确保要等于1才成功
	 * @throws Exception
	 */
	public int delete(Object id) throws Exception {
		return delete(id, null);
	}

	public int delete(Object id, DBConnect connect) throws Exception {
		return delete(new Query(identity.getColumn_name() + "=?", id), connect);
	}

	// protected void change_cache(List list,int batch_type) throws Exception
	// {
	// if(list==null || list.size()==0)
	// return;
	// if(use_cache)
	// {
	// for(Object obj : list)
	// {
	// Object idobj = getIdValue(obj);
	// if(idobj==null)
	// continue;
	// switch(batch_type)
	// {
	// case 0:
	// case 1:
	// case 3:
	// EntityCache.put(idobj,obj, table_clazz);
	// break;
	// case 2:
	// EntityCache.remove(idobj, table_clazz);
	// break;
	// }
	// }
	// }
	// }

	/**
	 * 根据条件delete <br>
	 * 示例： <br>
	 * CommonDAO dao = DAOFactory.getDAO(TestChild.class); <br>
	 * dao.delete(new Query("id=?",1), null); <br>
	 * 或者 <br>
	 * CommonDAO dao = DAOFactory.getDAO(TestChild.class); <br>
	 * dao.delete(new Query(" where id=1"), null);
	 * 
	 * @param query
	 *            删除时的查询和值
	 * @param connect
	 *            数据库连接，如果为null由函数内部申请
	 * @return 返回删除的行数
	 * @throws Exception
	 * @see Query
	 */
	public int delete(Object... condition) throws Exception {
		return delete(new Query(condition), null);
	}

	public int delete(Query query) throws Exception {
		return delete(query, null);
	}

	public int delete(Query query, DBConnect connect) throws Exception {
		int ret = -1;

		// 不处理事务时可以传null进来
		DBConnect connect2 = getConnect(connect);
		try {
			// List list = getCurrentCollection("select * from " + getTableName()
			// + query.getPre_condition(),query,connect2);
			// change_cache(list,DELETE_BATCH);

			String sqlStr = "delete from " + getTableName() + query.getPre_condition();
			if (sql_print)
				log.info(sqlStr);
			connect2.prepareStatement(sqlStr);
			if (query != null)
				query.setValueToFiled(connect2);

			int n = connect2.executeUpdate();
			ret = n;
		} catch (Exception e) {
			if (connect == null)
				connect2.rollback();
			throw new Exception(e);
		} finally {
			if (connect == null) {
				connect2.close();
			}
		}
		return ret;
	}

	/**
	 * 使用updateBatch批量更新，本方法主要用于对效率比较看中的场合
	 * 
	 * @param fields
	 *            ：更新的字段和条件列表
	 * @param connect
	 *            数据库连接，如果为null由函数内部申请
	 * @return 各个更新返回影响的行数
	 * @throws Exception
	 */
	public int[] updateBatch(List<UpdateFields> fields) throws Exception {
		return updateBatch(fields, null);
	}

	public int[] updateBatch(List<UpdateFields> fields, DBConnect connect) throws Exception {
		// 不处理事务时可以传null进来
		if (fields == null || fields.size() == 0)
			return null;
		DBConnect connect2 = getConnect(connect);
		int[] commit_rows = null;
		try {
			for (UpdateFields uf : fields) {
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
				connect2.addBatch();

				// List list = getCurrentCollection("select * from " + getTableName()
				// + sql_where,new Query(uf.getConditionCV()),connect2);
				//
				// change_cache(list,UPDATE_BATCH);
			}
			commit_rows = connect2.executeBatch();
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
	 * 根据传入字段和条件，修改表中的数据 <br>
	 * 示例： <br>
	 * CommonDAO dao = DAOFactory.getDAO(TestChild.class); <br>
	 * dao.update(new UpdateFields("name=?,state=?","鲍庆丰",2,"id=?",1), null); <br>
	 * 其中UpdateFields的前半部分为待更新的字段，后半部分为条件。 <br>
	 * 字段在0-2个，条件在0-2个时可以直接通过逗号分割书写。 <br>
	 * 如果多于2个条件或者字段，可以使用ConditionValue指定任意多个值。 <br>
	 * 如：dao.update(new UpdateFields(new ConditionValue("name=?,state=?,cardid=?,sex=?","鲍庆丰",2, "330725197777777777",0),"id=?",1), null); <br>
	 * 注意：在2个字段1个条件或者1个字段2个条件时会有编译错误，需要把2个值中的一个强制转换为Object类型
	 * 
	 * @param uf
	 *            ：更新的字段和条件
	 * @param connect
	 *            数据库连接，如果为null由函数内部申请
	 * @return 返回影响的行数
	 * @throws Exception
	 */

	public int update(UpdateFields uf) throws Exception {
		return update(uf, null);

	}

	public int update(UpdateFields uf, DBConnect connect) throws Exception {

		// 不处理事务时可以传null进来
		DBConnect connect2 = getConnect(connect);
		int commit_rows = 0;
		try {
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

			// List list = getCurrentCollection("select * from " + getTableName()
			// + sql_where,new Query(uf.getConditionCV()),connect2);
			//
			// change_cache(list,UPDATE_BATCH);

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
	 * 根据主键，修改表中一条的数据，更新的字段根据对象的值是否为null确定 <br>
	 * 示例： <br>
	 * CommonDAO dao = DAOFactory.getDAO(TestChild.class); <br>
	 * TestChild object = new TestChild(); <br>
	 * object.setId ((long)1); <br>
	 * object.setName("鲍庆丰"); <br>
	 * dao.update(object,1, null);
	 * 
	 * @param object
	 *            ：待更新的对象，插入哪张表由对象的实体决定
	 * @param indentityObj
	 *            ：主键值
	 * @param connect
	 *            数据库连接，如果为null由函数内部申请
	 * @return 影响的行数
	 * @throws Exception
	 * @throws Exception
	 */
	public String update(Object object, Object indentityObj) throws Exception {
		return update(object, indentityObj, null);
	}

	public String update(Object object, Object indentityObj, DBConnect connect) throws Exception {
		long updateRows = updateObjByCondition(object, new Query(identity.getColumn_name() + "=?", indentityObj), connect);
		if (updateRows > 0)
			return indentityObj.toString();
		return null;

	}

	/**
	 * 根据条件更新记录，修改表中的数据，更新的字段根据对象的值是否为null确定 <br>
	 * 示例： <br>
	 * CommonDAO dao = DAOFactory.getDAO(TestChild.class); <br>
	 * TestChild object = new TestChild(); <br>
	 * object.setId ((long)1); <br>
	 * object.setName("鲍庆丰"); <br>
	 * dao.update(object,new ConditionValue("id=? and name=?",1,"baoqf"), null);
	 * 
	 * @param object
	 *            ：待更新的对象，插入哪张表由对象的实体决定
	 * @param condition
	 *            ：条件
	 * @param connect
	 *            数据库连接，如果为null由函数内部申请
	 * @return long 更新条数
	 * @throws Exception
	 */
	public long updateObjByCondition(Object object, Query condition) throws Exception {
		return updateObjByCondition(object, condition, null);
	}

	public long updateObjByCondition(Object object, Object... condition) throws Exception {
		return updateObjByCondition(object, new Query(condition), null);
	}

	public long updateObjByCondition(Object object, Query condition, DBConnect connect) throws Exception {
		StringBuffer sql = new StringBuffer();
		Class clazz = object.getClass();

		// 不处理事务时可以传null进来
		DBConnect connect2 = getConnect(connect);
		try {
			if (fieldList == null)
				initField(connect2);
			Set<String> colList = fieldList.keySet();
			// 按照数据库表中的字段来决定更新的字段
			StringBuffer colBuf = new StringBuffer();
			for (String colName : colList) {
				if (colName.equalsIgnoreCase(identity.getColumn_name()))
					continue;
				ColumnMeta meta = this.columnList.get(colName.toLowerCase());
				if (meta == null)
					continue;

				Method method = clazz.getDeclaredMethod(meta.getFieldMethod(), null);
				Object retObj = method.invoke(object, null);
				// 不更新未更改字段
				if (retObj == null)
					continue;
				// 不更新未定义的类型
				if (FieldType.getFieldType(meta.getColumn_clazz()) == 0)
					continue;
				colBuf.append(colName).append("=?,");
			}
			colBuf.deleteCharAt(colBuf.length() - 1);
			sql.append("update ").append(getTableName()).append(" set ").append(colBuf.toString());
			sql.append(condition.getPre_condition());

			if (sql_print)
				log.info(sql);
			connect2.prepareStatement(sql.toString());

			// 赋值
			int pos = 1;
			for (String colName : colList) {
				if (colName.equalsIgnoreCase(identity.getColumn_name()))
					continue;
				ColumnMeta meta = this.columnList.get(colName.toLowerCase());
				if (meta == null)
					continue;

				Method method = clazz.getDeclaredMethod(meta.getFieldMethod(), null);
				Object retObj = null;
				// if(method.isAccessible())
				retObj = method.invoke(object, null);
				// 不更新未更改字段及主键
				if (retObj == null)
					continue;
				// 不更新未定义的类型
				int fType = FieldType.getFieldType(meta.getColumn_clazz());
				if (fType == 0)
					continue;

				// 生成值串
				switch (fType) {
				case INTEGER:
				case JINTEGER:
				case LONG:
				case JLONG:
				case SHORT:
				case JSHORT:
					connect2.setLong(pos++, Long.parseLong((retObj == null) ? "0" : retObj.toString()));
					break;
				case FLOAT:
				case JFLOAT:
				case DOUBLE:
				case JDOUBLE:
					connect2.setDouble(pos++, Double.parseDouble((retObj == null) ? "0" : retObj.toString()));
					break;
				case BOOLEAN:
				case JBOOLEAN:
					connect2.setBoolean(pos++, retObj == null ? false : new Boolean(retObj.toString()));
					break;
				case JSTRING:
					connect2.setString(pos++, retObj == null ? "" : retObj.toString());
					break;
				case JDATE:
					connect2.setTimestamp(pos++, retObj == null ? null : new java.sql.Timestamp(((Date) retObj).getTime()));
					break;
				default:
					connect2.setString(pos++, retObj.toString());
				}

			}
			condition.setValueToField(connect2, pos - 1);
			return connect2.executeUpdate();
		} catch (Exception e) {
			if (connect == null)
				connect2.rollback();
			throw new Exception(e);
		} finally {
			if (connect == null) {
				connect2.close();
			}
		}
	}

	/**
	 * 使用insert插入数据 <br>
	 * 示例： <br>
	 * CommonDAO dao = DAOFactory.getDAO(TestChild.class); <br>
	 * TestChild object = new TestChild(); <br>
	 * object.setName("鲍庆丰"); <br>
	 * dao.insert(object, null);
	 * 
	 * @param object
	 *            ：待新增的对象，插入哪张表由对象的实体决定
	 * @param connect
	 *            数据库连接，如果为null由函数内部申请
	 * @return String的主键值，如果实体主键为Long型，需要外面自己转换
	 * @throws Exception
	 */
	public abstract String insert(Object object, DBConnect connect) throws Exception;

	/**
	 * 把对象保存到数据库 根据对象的主键是否有值来insert或者update <br>
	 * 示例： <br>
	 * CommonDAO dao = DAOFactory.getDAO(TestChild.class); <br>
	 * TestChild object = new TestChild(); <br>
	 * object.setName("鲍庆丰"); <br>
	 * dao.insert(object, null); <br>
	 * 注意：当更新时对象的属性为null时不更新这个字段
	 * 
	 * @param object
	 *            ：待新增或修改的对象，插入或更新哪张表由对象的实体决定, 在新增操作使用save方法一定要保证主键为null/空/0，如果有值将会认为是update操作，在更新操作使用save方法一定要保证主键有值，否则将进行insert操作
	 * @param connect
	 *            数据库连接，如果为null由函数内部申请
	 * @return String的主键值，如果实体主键为Long型，需要外面自己转换
	 * @throws Exception
	 */
	public List<String> save(List<Object> list) throws Exception {
		DBConnect connect = new DBConnect(false);
		;
		List<String> listKey = new ArrayList<String>();
		try {
			for (Object obj : list)
				listKey.add(save(obj, connect));
			connect.commit();
		} catch (Exception e) {
			if (connect != null)
				connect.rollback();
			throw new Exception(e);
		} finally {
			if (connect != null) {
				connect.close();
			}
		}
		return listKey;
	}

	public String save(Object object) throws Exception {
		return save(object, null);
	}

	public String save(Object object, DBConnect connect) throws Exception {
		if (object == null)
			return null;
		Class clazz = object.getClass();
		String iden = identity.getField_name();
		String indentityFiledName = "get" + iden.substring(0, 1).toUpperCase() + iden.substring(1, iden.length());
		Method indentityMethod = clazz.getDeclaredMethod(indentityFiledName, null);
		Object indentityObj = indentityMethod.invoke(object, null);
		if (indentityObj == null || indentityObj.toString().length() == 0 || (identity != null && (!identity.getColumn_clazz().equals(String.class)) && new Long(indentityObj.toString()).longValue() == 0))// 新增
		{
			return insert(object, connect);
		} else// 修改
		{
			update(object, indentityObj, connect);
			return indentityObj.toString();
		}
	}

	/**
	 * 执行批量插入、更新或者删除操作 <br>
	 * 示例： <br>
	 * CommonDAO dao = DAOFactory.getDAO(TestChild.class); <br>
	 * TestChild object = new TestChild(); <br>
	 * dao.execute(new Query("delete from user where username=?","bao"), null); <br>
	 * 注意：当更新时对象的属性为null时不更新这个字段
	 * 
	 * @param query
	 *            ：待执行的SQL语句
	 * @param connect
	 *            数据库连接，如果为null由函数内部申请
	 * @return 影响的条数
	 * @throws Exception
	 */
	public int executeUpdate(Query query) throws Exception {
		return executeUpdate(query, null);
	}

	public int executeUpdate(Object... objects) throws Exception {
		return executeUpdate(new Query(objects), null);
	}

	public int executeUpdate(Query query, DBConnect connect) throws Exception {

		int n = 0;
		DBConnect connect2 = getConnect(connect);
		try {
			String sql = query.getSqlDirect();
			if (sql_print)
				log.info(sql);

			connect2.prepareStatement(query.getSqlDirect());
			if (query != null)
				query.setValueToFiled(connect2);
			n = connect2.executeUpdate();
		} catch (Exception e) {
			if (connect == null)
				connect2.rollback();
			throw new Exception(e);
		} finally {
			if (connect == null) {
				connect2.close();
			}
		}
		return n;
	}

	/**
	 * 通过sql获得行数 <br>
	 * 示例： <br>
	 * CommonDAO dao = DAOFactory.getDAO(TestChild.class); <br>
	 * long rows = dao.getRowCountBySql(new Query("select * from testchild where like ?", "%鲍庆丰%"), null);
	 * 
	 * @param query
	 *            ：查询sql和值
	 * @param connect
	 *            数据库连接，如果为null由函数内部申请
	 * @return 行数
	 * @throws Exception
	 */
	public long getRowCountBySql(Query query) throws Exception {
		return getRowCountBySql(query, null);
	}

	public long getRowCountBySql(Object... objects) throws Exception {
		return getRowCountBySql(new Query(objects), null);
	}

	public long getRowCountBySql(Query query, DBConnect connect) throws Exception {

		ResultSet rs = null;
		long rows = 0;
		DBConnect connect2 = getConnect(connect);
		try {
			String sqlStr = "select count(1) ncount from (" + query.getSqlDirect() + ") b";
			if (sql_print)
				log.info(sqlStr);
			connect2.prepareStatement(sqlStr);
			if (query != null)
				query.setValueToFiled(connect2);

			rs = connect2.executeQuery();
			while (rs.next()) {
				rows = (rs.getLong(1));
			}
		} catch (Exception e) {
			if (connect == null)
				connect2.rollback();
			throw new Exception(e);
		} finally {
			if (connect == null) {
				connect2.close();
			}
		}
		return rows;
	}

	/**
	 * 获得一行一列数据 <br>
	 * 示例： <br>
	 * CommonDAO dao = DAOFactory.getDAO(TestChild.class); <br>
	 * Object col = dao.getOneRowColBySql(new Query("select sum(money) from testchild",1),Float.class, null);
	 * 
	 * @param query
	 *            查询sql和值
	 * @param valueType
	 *            获得的数据类型
	 * @param connect
	 *            数据库连接，如果为null由函数内部申请
	 * @return 返回本第一行第一列数据
	 * @throws Exception
	 */
	public Object getOneRowColBySql(Class valueType, Query query) throws Exception {
		return getOneRowColBySql(query, valueType, null);
	}

	public Object getOneRowColBySql(Class valueType, Object... objects) throws Exception {
		return getOneRowColBySql(new Query(objects), valueType, null);
	}

	public Object getOneRowColBySql(Query query, Class valueType, DBConnect connect) throws Exception {

		ResultSet rs = null;
		// 不处理事务时可以传null进来
		DBConnect connect2 = getConnect(connect);
		Object rsValue = null;
		try {
			if (sql_print)
				log.info(query.getSqlDirect());
			connect2.prepareStatement(query.getSqlDirect());
			if (query != null)
				query.setValueToFiled(connect2);
			rs = connect2.executeQuery();
			while (rs.next()) {
				// 获得Rs值
				Class[] paraRsClass = { int.class };
				Method method_rs = ResultSet.class.getDeclaredMethod(FieldType.getFieldResultSetGetMethod(valueType), paraRsClass);
				Object[] paraRsObj = { 1 };
				rsValue = method_rs.invoke(rs, paraRsObj);
				if(rs.wasNull())
					rsValue = null;
				return rsValue;
			}
		} catch (Exception e) {
			if (connect == null)
				connect2.rollback();
			throw new Exception(e);
		} finally {
			if (connect == null) {
				connect2.close();
			}
		}
		return rsValue;
	}

	/**
	 * 通过sql语句获得查询到的所有数据，直接通过sql语句的select语句不缓存，因为这种语句一般是报表统计用的 <br>
	 * 示例： <br>
	 * CommonDAO dao = DAOFactory.getDAO(TestChild.class); <br>
	 * List list = dao.listAllBySql(new Query("select * from testchild where name like ?", "%鲍庆丰%"), null);
	 * 
	 * @param query
	 *            ：查询sql和值
	 * @param connect
	 *            数据库连接，如果为null由函数内部申请
	 * @return 对象列表,本方法可用于非实体类
	 * @throws Exception
	 */
	public List listAllBySql(Query query) throws Exception {
		return listAllBySql(query, null);
	}

	public List listAllBySql(Object... objs) throws Exception {
		return listAllBySql(new Query(objs), null);
	}

	public List listAllBySql(Query query, DBConnect connect) throws Exception {
		List list = new ArrayList();
		list = getCurrentCollection(query.getSqlDirect(), query, connect);
		return list;
	}

	/**
	 * 获得行数 <br>
	 * 示例： <br>
	 * CommonDAO dao = DAOFactory.getDAO(TestChild.class); <br>
	 * long rows = dao.getRowCount(new Query(" name like ?", "%鲍庆丰%"), null);
	 * 
	 * @param query
	 *            ：查询sql和值
	 * @param connect
	 *            数据库连接，如果为null由函数内部申请
	 * @return 行数
	 * @throws Exception
	 */
	public long getRowCount(Query query) throws Exception {
		return getRowCount(query, null);
	}

	public long getRowCount(Object... obj) throws Exception {
		return getRowCount(new Query(obj), null);
	}

	public long getRowCount(Query query, DBConnect connect) throws Exception {

		ResultSet rs = null;
		long rows = 0;
		// 不处理事务时可以传null进来
		DBConnect connect2 = getConnect(connect);
		try {
			String sqlStr = "select count(*) ncount from " + getTableName() + " ";
			if (query != null)
				sqlStr += query.getPre_condition();
			if (sql_print)
				log.info(sqlStr);
			connect2.prepareStatement(sqlStr);
			if (query != null)
				query.setValueToFiled(connect2);
			rs = connect2.executeQuery();
			while (rs.next()) {
				rows = (rs.getLong(1));
			}
		} catch (Exception e) {
			if (connect == null)
				connect2.rollback();
			throw new Exception(e);
		} finally {
			if (connect == null) {
				connect2.close();
			}
		}
		return rows;
	}

	/**
	 * 通过sql语句获得分页数据 <br>
	 * 示例： <br>
	 * CommonDAO dao = DAOFactory.getDAO(TestChild.class); <br>
	 * List list = dao.listPageBySql(1, 15, new Query("select * from testchild where name like ?", "%鲍庆丰%"), null);
	 * 
	 * @param currpage
	 *            需要获得哪页数据
	 * @param pagesize
	 *            本页多少条
	 * @param query
	 *            查询sql和值
	 * @param connect
	 *            数据库连接，如果为null由函数内部申请
	 * @return 对象列表,本方法可用于非实体类
	 * @throws Exception
	 */
	public List listPageBySql(final int currpage, final int pagesize, Query query) throws Exception {
		return listPageBySql(currpage, pagesize, query, null);
	}

	public List listPageBySql(final int currpage, final int pagesize, Object... objs) throws Exception {
		return listPageBySql(currpage, pagesize, new Query(objs), null);
	}

	public List listPageBySql(final int currpage, final int pagesize, Query query, DBConnect connect) throws Exception {
		List list = new ArrayList();
		String sqlStr = getLimitString(query.getSqlDirect(), (currpage - 1) * pagesize, pagesize);
		list = getCurrentCollection(sqlStr, query, connect);
		return list;
	}

	/**
	 * 获得分页数据 <br>
	 * 示例： <br>
	 * CommonDAO dao = DAOFactory.getDAO(TestChild.class); <br>
	 * List list = dao.listPage(1, 15, new Query("name like?", "%鲍庆丰%"), null);
	 * 
	 * @param currpage
	 *            需要获得哪页数据
	 * @param pagesize
	 *            本页多少条
	 * @param query
	 *            查询sql和值
	 * @param connect
	 *            数据库连接，如果为null由函数内部申请
	 * @return 本页数据对象列表
	 * @throws Exception
	 */
	public List listPage(final int currpage, final int pagesize, Query query) throws Exception {
		return listPage(currpage, pagesize, query, null);
	}

	public List listPage(final int currpage, final int pagesize, Object... objs) throws Exception {
		return listPage(currpage, pagesize, new Query(objs), null);
	}

	public List listPage(final int currpage, final int pagesize, Query query, DBConnect connect) throws Exception {
		List list = new ArrayList();
		String sql = "select * from " + getTableName() + " ";
		if (query != null)
			sql += query.getPre_condition();
		String sqlStr = getLimitString(sql, (currpage - 1) * pagesize, pagesize);
		list = getCurrentCollection(sqlStr, query, connect);
		// change_cache(list,SELECT_BATCH);
		return list;
	}

	/**
	 * 获得前多少条数据，也可以使用listPage方法 0,N替代 <br>
	 * 示例： <br>
	 * CommonDAO dao = DAOFactory.getDAO(TestChild.class); <br>
	 * List list = dao. listTopN (new Query("name like?",10, "%鲍庆丰%"), null);
	 * 
	 * @param query
	 *            ：查询sql和值
	 * @param connect
	 *            数据库连接，如果为null由函数内部申请
	 * @return 对象列表
	 * @throws Exception
	 */
	public List listTopN(Query query, int top) throws Exception {
		return listTopN(query, top, null);
	}

	public List listTopN(int top, Object... objects) throws Exception {
		return listTopN(new Query(objects), top, null);
	}

	public List listTopN(Query query, int top, DBConnect connect) throws Exception {
		List list = new ArrayList();
		String sql = "select * from " + getTableName() + " ";
		if (query != null)
			sql += query.getPre_condition();
		String sqlStr = getLimitString(sql, 0, top);
		list = getCurrentCollection(sqlStr, query, connect);
		// change_cache(list,SELECT_BATCH);

		return list;
	}

	/**
	 * 通过sql语句获得前多少条数据 <br>
	 * 示例： <br>
	 * CommonDAO dao = DAOFactory.getDAO(TestChild.class); <br>
	 * List list = dao.listTopNBySql(new Query("select * from testchild where iden=?",10), 1, null);
	 * 
	 * @param query
	 *            ：查询sql和值
	 * @param connect
	 *            数据库连接，如果为null由函数内部申请
	 * @return 对象列表
	 * @throws Exception
	 */
	public List listTopNBySql(Query query, int top) throws Exception {
		return listTopNBySql(query, top, null);
	}

	public List listTopNBySql(int top, Object... objects) throws Exception {
		return listTopNBySql(new Query(objects), top, null);
	}

	public List listTopNBySql(Query query, int top, DBConnect connect) throws Exception {
		List list = new ArrayList();
		String sqlStr = getLimitString(query.getSqlDirect(), 0, top);
		list = getCurrentCollection(sqlStr, query, connect);
		return list;
	}

	/**
	 * 获得查询到的所有数据 <br>
	 * 示例： <br>
	 * CommonDAO dao = DAOFactory.getDAO(TestChild.class); <br>
	 * List list = dao. executeQuery (new Query("select * from TestChild where name like ?", "%鲍庆丰%"), null);
	 * 
	 * @param query
	 *            ：查询sql和值
	 * @param connect
	 *            数据库连接，如果为null由函数内部申请
	 * @return 对象列表
	 * @throws Exception
	 */
	public List executeQuery(Object... objects) throws Exception {
		return executeQuery(new Query(objects), null);
	}

	public List executeQuery(Query query) throws Exception {
		return executeQuery(query, null);
	}

	public List executeQuery(Query query, DBConnect connect) throws Exception {
		List list = new ArrayList();
		list = getCurrentCollection(query.getSqlDirect(), query, connect);
		// change_cache(list,SELECT_BATCH);
		return list;
	}

	/**
	 * 获得查询到的所有数据 <br>
	 * 示例： <br>
	 * CommonDAO dao = DAOFactory.getDAO(TestChild.class); <br>
	 * List list = dao. listAllAll (new Query("name like?", "%鲍庆丰%"), null);
	 * 
	 * @param query
	 *            ：查询sql和值
	 * @param connect
	 *            数据库连接，如果为null由函数内部申请
	 * @return 对象列表
	 * @throws Exception
	 */
	public List listAllAll(Query query) throws Exception {
		return listAllAll(query, null);
	}

	public List listAllAll(Object... objects) throws Exception {
		return listAllAll(new Query(objects), null);
	}

	public List listAllAll(Query query, DBConnect connect) throws Exception {
		List list = new ArrayList();
		String sql = "select * from " + getTableName() + " ";
		if (query != null)
			sql += query.getPre_condition();
		list = getCurrentCollection(sql, query, connect);
		// change_cache(list,SELECT_BATCH);

		return list;
	}

	protected abstract String getLimitString(String sql, int row_min, int page_size);

	/**
	 * 获得序列当前值 <br>
	 * 示例： <br>
	 * CommonDAO dao = DAOFactory.getDAO(TestChild.class); <br>
	 * long rows = dao.getSeqCurValue ("user_code"); <br>
	 * 注意：使用本方法必须定义id_sequence表，包含idnumber和idname字段，同时要添加一条记录idname=序列名,idnumber=初始值，使用其它序列可以再增加一行。
	 * 
	 * @param seq
	 *            序列名称
	 * @return 序列值
	 * @throws Exception
	 */
	public abstract Long getSeqCurValue(String seq) throws Exception;

	/**
	 * 获得序列下一个值 <br>
	 * 示例： <br>
	 * CommonDAO dao = DAOFactory.getDAO(TestChild.class); <br>
	 * long rows = dao. getSeqNextValue ("user_code"); <br>
	 * 注意：使用本方法必须定义id_sequence表，包含idnumber和idname字段，同时要添加一条记录idname=序列名,idnumber=初始值，使用其它序列可以再增加一行。
	 * 
	 * @param seq
	 *            序列名称
	 * @return 序列值
	 * @throws Exception
	 */
	public abstract Long getSeqNextValue(String seq) throws Exception;

	/**
	 * 获得数据库服务器时间 <br>
	 * 示例： <br>
	 * CommonDAO dao = DAOFactory.getDAO(TestChild.class); <br>
	 * long rows = dao.getServerTime (");
	 * 
	 * @param
	 * @return 时间
	 * @throws Exception
	 */
	public abstract Date getServerTime() throws Exception;

	protected void putObj2Cache(Object id, Object obj) {
		EntityCache.put(id, obj, obj.getClass());
	}

	protected String getTableName() {
		return table_name;
	}

	public boolean isSql_print() {
		return sql_print;
	}

	public void setSql_print(boolean sql_print) {
		this.sql_print = sql_print;
	}

	private DBConnect getConnect(DBConnect connect) {
		if (connect == null)
			return new DBConnect(false);
		else
			return connect;
	}

	private String obj2UpdateSet(Object object) throws Exception {
		StringBuffer sql = new StringBuffer();
		Class clazz = object.getClass();

		// 不处理事务时可以传null进来
		DBConnect connect2 = getConnect(null);
		try {
			String update_sql = "select * from " + getTableName() + " limit 0 ";
			connect2.prepareStatement(update_sql);
			if (sql_print)
				log.info(update_sql);
			ResultSet rs = connect2.executeQuery();
			ResultSetMetaData metadate = rs.getMetaData();
			int colcount = metadate.getColumnCount();
			// 组装sql语句

			// 按照数据库表中的字段来决定更新的字段
			StringBuffer colBuf = new StringBuffer();
			for (int i = 1; i <= colcount; i++) {
				String colName = metadate.getColumnName(i);
				if (colName.equalsIgnoreCase(identity.getColumn_name()))
					continue;
				ColumnMeta meta = this.columnList.get(colName.toLowerCase());
				if (meta == null)
					continue;
				// 字段名
				String column = meta.getColumn_name();
				// 类字段
				String field = meta.getField_name();
				// 属性类型
				Class fieldClazz = meta.getColumn_clazz();
				// 方法名
				String filedName = ((fieldClazz.equals(boolean.class)) ? "is" : "get") + field.substring(0, 1).toUpperCase() + field.substring(1, field.length());
				Method method = clazz.getDeclaredMethod(filedName, null);
				Object retObj = method.invoke(object, null);
				// 不更新未更改字段
				if (retObj == null)
					continue;
				// 不更新未定义的类型
				if (FieldType.getFieldType(fieldClazz) == 0)
					continue;
				colBuf.append(colName).append("=?,");
			}
			colBuf.deleteCharAt(colBuf.length() - 1);
			return colBuf.toString();
		} finally {
			if (connect2 != null)
				connect2.close();
		}
	}

	/**
	 * 
	 * @param size
	 *            长度
	 * @return 返回类似(?,?,?)字符串
	 */
	private String getQuotString(int size) {
		String ret = "";
		if (size > 0) {
			ret += "(";
			for (int i = 0; i < size; i++) {
				if (i == (size - 1))
					ret += "?";
				else
					ret += "?,";
			}
			ret += ")";
		}
		return ret;
	}

	/**
	 * @param procName
	 *            存储过程名称
	 * @param inParams
	 *            输入参数
	 * @param outClazz
	 *            输出参数类型
	 * @param connect
	 *            数据库连接
	 * @return 存储过程 通过?=call(?,?,?)方式返回值
	 */
	public Object callProcRetOutParam(String procName, Object[] inParams, Class outClazz, DBConnect connect) {

		DBConnect connect2 = getConnect(connect);
		CallableStatement cstmt = null;
		try {
			String condition = "{?=call " + procName + getQuotString(inParams == null ? 0 : inParams.length) + "}";
			ConditionValue cv = new ConditionValue(condition, inParams == null ? null : inParams);
			cstmt = connect2.prepareCall(cv.getCondition());

			cstmt.registerOutParameter(1, FieldType.getFieldUnit(outClazz).getField_java_sql_types());
			cv.setValueToCondition(1, cstmt);

			if (sql_print)
				log.info(cv.toString());
			cstmt.execute();

			Class[] paraRsClass = { int.class };
			Method method_rs = CallableStatement.class.getDeclaredMethod(FieldType.getFieldResultSetGetMethod(outClazz), paraRsClass);
			Object[] paraRsObj = { 1 };
			return method_rs.invoke(cstmt, paraRsObj);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (cstmt != null)
				try {
					cstmt.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			if (connect == null) {
				connect2.close();
			}
		}
		return null;
	}

	/**
	 * @param procName
	 *            存储过程名称
	 * @param inParams
	 *            输入参数
	 * @param outClazz
	 *            输出参数类型
	 * @param connect
	 *            数据库连接
	 * @return 存储过程 通过call(?,?,?)的最后一个参数返回值
	 */
	public Object callProcLastOutParam(String procName, Object[] inParams, Class outClazz, DBConnect connect) {

		DBConnect connect2 = getConnect(connect);
		CallableStatement cstmt = null;
		try {
			String condition = "{call " + procName + getQuotString(inParams == null ? 0 : (inParams.length + 1)) + "}";
			ConditionValue cv = new ConditionValue(condition, inParams == null ? null : inParams);
			cstmt = connect2.prepareCall(cv.getCondition());
			cv.setValueToCondition(cstmt);
			int outPos = cv.size() + 1;
			cstmt.registerOutParameter(outPos, FieldType.getFieldUnit(outClazz).getField_java_sql_types());
			if (sql_print)
				log.info(cv.toString());
			cstmt.execute();

			Class[] paraRsClass = { int.class };
			Method method_rs = CallableStatement.class.getDeclaredMethod(FieldType.getFieldResultSetGetMethod(outClazz), paraRsClass);
			Object[] paraRsObj = { outPos };
			return method_rs.invoke(cstmt, paraRsObj);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (cstmt != null)
				try {
					cstmt.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			if (connect == null) {
				connect2.close();
			}
		}
		return null;
	}

	/**
	 * @param procName
	 *            存储过程名称
	 * @param inParams
	 *            输入参数
	 * @param outClazz
	 *            输出参数类型
	 * @param connect
	 *            数据库连接
	 * @return 存储过程 通过call(?,?,?,?,?)的中间一个参数做为返回值
	 */
	public Object callProcMiddleOutParam(String procName, Object[] inParams, int iOutPos, Class outClazz, DBConnect connect) {

		DBConnect connect2 = getConnect(connect);
		CallableStatement cstmt = null;
		try {
			List listParams = new ArrayList(Arrays.asList(inParams));
			listParams.add(iOutPos - 1, 0);
			String condition = "{call " + procName + getQuotString(listParams == null ? 0 : (listParams.size())) + "}";
			ConditionValue cv = new ConditionValue(condition, listParams == null ? null : listParams.toArray());
			cstmt = connect2.prepareCall(cv.getCondition());
			cv.setValueToCondition(cstmt, new int[] { iOutPos });
			cstmt.registerOutParameter(iOutPos, FieldType.getFieldUnit(outClazz).getField_java_sql_types());
			if (sql_print)
				log.info(cv.toString());
			cstmt.execute();

			Class[] paraRsClass = { int.class };
			Method method_rs = CallableStatement.class.getDeclaredMethod(FieldType.getFieldResultSetGetMethod(outClazz), paraRsClass);
			Object[] paraRsObj = { iOutPos };
			return method_rs.invoke(cstmt, paraRsObj);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (cstmt != null)
				try {
					cstmt.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			if (connect == null) {
				connect2.close();
			}
		}
		return null;
	}

	/**
	 * @param procName
	 *            存储过程名称
	 * @param inParams
	 *            输入参数
	 * @param outClazz
	 *            输出参数类型
	 * @param connect
	 *            数据库连接
	 * @return 存储过程 通过call(?,?,?,?,?)的中间多个参数做为返回值
	 */
	public List callProcMultiOutParam(String procName, Object[] inParams, int[] iOutPoses, Class[] outClazzs, DBConnect connect) {

		DBConnect connect2 = getConnect(connect);
		CallableStatement cstmt = null;
		try {
			List listParams = new ArrayList(Arrays.asList(inParams));
			for (int i = 0; i < iOutPoses.length; i++)
				listParams.add(iOutPoses[i] - 1, 0);

			String condition = "{call " + procName + getQuotString(listParams == null ? 0 : (listParams.size())) + "}";
			ConditionValue cv = new ConditionValue(condition, listParams == null ? null : listParams.toArray());
			cstmt = connect2.prepareCall(cv.getCondition());
			cv.setValueToCondition(cstmt, iOutPoses);

			for (int i = 0; i < iOutPoses.length; i++)
				cstmt.registerOutParameter(iOutPoses[i], FieldType.getFieldUnit(outClazzs[i]).getField_java_sql_types());

			if (sql_print)
				log.info(cv.toString());
			cstmt.execute();

			List outList = new ArrayList();

			Class[] paraRsClass = { int.class };
			for (int i = 0; i < iOutPoses.length; i++) {
				Method method_rs = CallableStatement.class.getDeclaredMethod(FieldType.getFieldResultSetGetMethod(outClazzs[i]), paraRsClass);
				Object[] paraRsObj = { iOutPoses[i] };
				outList.add(method_rs.invoke(cstmt, paraRsObj));
			}
			return outList;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (cstmt != null)
				try {
					cstmt.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			if (connect == null) {
				connect2.close();
			}
		}
		return null;
	}

	/**
	 * @param procName
	 *            存储过程名称
	 * @param inParams
	 *            输入参数
	 * @param connect
	 *            数据库连接
	 * @return 存储过程返回对象列表，类似select * from table
	 */
	public List callProcRetList(String procName, Object[] inParams, DBConnect connect) {

		DBConnect connect2 = getConnect(connect);
		ResultSet rs = null;
		try {
			String condition = "{ call " + procName + getQuotString(inParams == null ? 0 : inParams.length) + "}";
			ConditionValue cv = new ConditionValue(condition, inParams == null ? null : inParams);

			connect2.prepareStatement(cv.getCondition());
			cv.setValueToCondition(connect2);
			if (sql_print)
				log.info(cv.toString());
			rs = connect2.executeQuery();
			List list = this.resultSetToCollection(rs);
			return list;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (rs != null)
				try {
					rs.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			if (connect == null) {
				connect2.close();
			}
		}
		return null;
	}

	/**
	 * 
	 * @param procName
	 *            存储过程名称
	 * @param inParams
	 *            输入参数
	 * @param connect
	 *            数据库连接
	 * @return 存储过程返回一行一列数据，类似 select count(*) from table结果
	 */
	public Object callProcRetOneRowCol(String procName, Object[] inParams, Class outClazz, DBConnect connect) {

		DBConnect connect2 = getConnect(connect);
		ResultSet rs = null;
		try {
			String condition = "{ call " + procName + getQuotString(inParams == null ? 0 : inParams.length) + "}";
			ConditionValue cv = new ConditionValue(condition, inParams == null ? null : inParams);

			connect2.prepareStatement(cv.getCondition());
			cv.setValueToCondition(connect2);
			if (sql_print)
				log.info(cv.toString());
			rs = connect2.executeQuery();
			Object result ;
			if (rs.next()) {
				Class[] paraRsClass = { int.class };
				Method method_rs = rs.getClass().getDeclaredMethod(FieldType.getFieldResultSetGetMethod(outClazz), paraRsClass);
				Object[] paraRsObj = { 1 };
				result =  method_rs.invoke(rs, paraRsObj);
				if(rs.wasNull())
					result=null;
				return result;
			}
			return null;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (rs != null)
				try {
					rs.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			if (connect == null) {
				connect2.close();
			}
		}
		return null;
	}

}
