package org.swift.serenebao.dao.mysql;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.List;

import org.apache.log4j.Logger;
import org.swift.database.pool.DBConnect;
import org.swift.serenebao.dao.FieldType;
import org.swift.serenebao.dao.FieldUnit;
import org.swift.serenebao.orm.anotation.Column;
import org.swift.serenebao.orm.anotation.ForeignKey;
import org.swift.serenebao.orm.anotation.Index;
import org.swift.serenebao.orm.anotation.Table;
import org.swift.util.file.Path;

/**
 * java类生成数据库表结构
 * <br>用法：
 * <br>CreateTable.init(TestParent.class);
 * <br>TestParent类为你的实体包中的任意一个类。
 * <br>run本java类，系统将在默认数据库下建立TestParent类所在包下的所有类对应的表的结构，主键，索引，外键等信息。
 * <br>注意：对于新增的类可以直接使用第三步导入为表。对于字段的修改，需要手工修改数据库表结构。
 * @author 鲍庆丰 baoqingfeng@gmail.com 
 * @since 2007-3-27
 */
public class CreateTable {
	static public Logger log = Logger.getLogger(CreateTable.class);
	public static boolean isEmpty(String str)
	{
		 return str == null || str.length() == 0;
	}
	 public static boolean isNotEmpty(String str) {
	        return !isEmpty(str);
	    }
	 public static void init(Class entity_class)
	 {
			List<String> list=null;
			try {
				String path = Path.getFullPathRelateClass("/",
						entity_class);
				log.info(path);
				list = Path.getClasses(path);
			} catch (IOException e2) {
				log.error(e2.getMessage());
			}
			String entityname=entity_class.getName();
			String pre = entityname.substring(0,entityname.lastIndexOf(".")+1);
			DBConnect 	connect = new DBConnect(false);
			try{	
			for (String entity : list) {
				String classname=pre + entity;
				Class clazz = Class.forName(classname);
				String create_table_sql = "";
				String class_name = clazz.getName();
				String table_name = class_name.substring((class_name
						.lastIndexOf('.') == -1 ? 0
						: class_name.lastIndexOf('.') + 1), class_name.length()).toLowerCase();
				String id = "";
				String id_column = "";
				boolean auto_crement = false;
				//有表属性说明
				if (clazz.isAnnotationPresent(Table.class))
				{
					Table tableLabel = (Table) clazz.getAnnotation(Table.class);
					table_name = isEmpty(tableLabel.name())?table_name:tableLabel.name();
					id = isEmpty(tableLabel.id())?id:tableLabel.id();
					id_column = new String(id);
					auto_crement = tableLabel.auto_increment();
				}
				try {
					// 获得字段属性
					for (Field fieldClass : clazz.getDeclaredFields()) {
						Class fieldType = fieldClass.getType();
						FieldUnit unit = FieldType.getFieldUnit(fieldType);
						boolean if_known_field = true;
						// 有annotation则使用annotation的声明，没有annotation 使用默认字段类型的默认类型和长度
						if (fieldClass.isAnnotationPresent(Column.class)) {
							Column fieldLabel = fieldClass
									.getAnnotation(Column.class);
							try {
								String label = isEmpty(fieldLabel
										.col_name()) ? fieldClass.getName()
										: fieldLabel.col_name();
								if(fieldClass.getName().equalsIgnoreCase(id) && auto_crement && (!String.class.equals(fieldClass.getType())))
								{
									label = id;
								}
								if(fieldClass.getName().equalsIgnoreCase(id))
								{
									label = fieldLabel.col_name();
									id_column = fieldLabel.col_name();
								}
								String width = fieldLabel.length();
								String type = fieldLabel.type();
								if(unit==null)
									if_known_field = false;
								else
								{
									if(isEmpty(type))
										create_table_sql += label +" "+unit.getField_db_type_name()+" ("+width+ ") ";
									else
									{
										create_table_sql += label +" "+ type + " ";
									}
									
									//注释
									create_table_sql += isEmpty(fieldLabel.comment())?"":" COMMENT '"+fieldLabel.comment()+"'";
									//是否允许为空
									create_table_sql += fieldLabel.nullable()?"":" not null ";
									//默认值
									create_table_sql += isEmpty(fieldLabel.default_value())?"":" default '"+fieldLabel.default_value()+"'";
								}
								
							} catch (IllegalArgumentException ex) {
								ex.printStackTrace();
							}
						} else {
							String label = fieldClass.getName();
							if(unit==null)
								if_known_field = false;
							else
							{
								create_table_sql += label +" "+unit.getField_db_type_name()+" ";
								if(FieldType.getFieldType(fieldType)==FieldType.JSTRING)
									create_table_sql += "(255)";
							}
						}
						
						if(fieldClass.getName().equalsIgnoreCase(id) && auto_crement && (!"java.lang.String".equalsIgnoreCase(fieldClass.getType().getName())))
						{
							create_table_sql += " auto_increment ";
						}
						if(if_known_field)
							create_table_sql += ",";
						if_known_field = true;

					}

					//有表属性说明
					if (clazz.isAnnotationPresent(Table.class))
					{
						Table tableLabel = (Table) clazz.getAnnotation(Table.class);
						//加主键
						create_table_sql += " PRIMARY KEY  (`"+id_column+"`),";
						create_table_sql += " UNIQUE KEY (`"+id_column+"`),";
						//加索引
						Index[] indexs = tableLabel.indexes();
						if(indexs!=null && indexs.length>0)
						{
							for(Index index : indexs)
							{
								create_table_sql += index.type() +" INDEX (`"+getStringSplitDot(index.columnNames())+"`),";								
							}
						}
						//加外键
						ForeignKey[] forks = tableLabel.foreignKey();
						if(forks!=null && forks.length>0)
						{
							for(ForeignKey fork : forks)
							{
								for(String str : fork.columnNames())
									create_table_sql += " KEY (`"+str+"`),";
								create_table_sql += " CONSTRAINT  FOREIGN KEY (`"+getStringSplitDot(fork.columnNames())+"`) REFERENCES `"+fork.refTable()+"` (`"+getStringSplitDot(fork.refColumnNames())+"`) "
									+(isEmpty(fork.onDelete())?"":" ON DELETE "+fork.onDelete())+" "
									+(isEmpty(fork.onUpdate())?"":" ON UPDATE "+fork.onUpdate())+",";
							}
						}
					}
					
					create_table_sql = create_table_sql.substring(0,
							create_table_sql.length() - 1);
					create_table_sql = "create table  if   not   exists " + table_name + "   ("
							+ create_table_sql + ")";
					
					//有表属性说明
					if (clazz.isAnnotationPresent(Table.class))
					{
						Table tableLabel = (Table) clazz.getAnnotation(Table.class);
						create_table_sql += isEmpty(tableLabel.tableType())?"":" ENGINE="+tableLabel.tableType();
						create_table_sql += isEmpty(tableLabel.charset())?"":" DEFAULT CHARSET="+tableLabel.charset();
						create_table_sql += isEmpty(tableLabel.comment())?"":" COMMENT='"+tableLabel.comment()+"'";
						table_name = isEmpty(tableLabel.name())?table_name:tableLabel.name();
						id = isEmpty(tableLabel.id())?id:tableLabel.id();
						auto_crement = tableLabel.auto_increment();
					}
					
					log.info(create_table_sql);
					connect.prepareStatement(create_table_sql);
					connect.executeUpdate();
				} catch (IllegalArgumentException ex) {
					ex.printStackTrace();
				}
			}
			} catch (Exception e) {
				log.error(e.getMessage());
				try {
					connect.rollback();
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			finally {
				try {
					if (connect != null)
						connect.close();
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			} 
	 }
	/**
	 * 
	 * 批注是用于Java语言的本机元数据标记。它们的输入严格与Java语言的其他部分类似，可以通过反映被发现，更容易地让IDE和编译器的编写者管理。
	 * 1.找到类的相关annotation和所有的属性 2.找到字段的类型，并检查字段是否有annotation
	 * 3.没有annotation使用默认长度和字段名(使用属性名)。有annotation找到对应annotation的所有属性值组成一个字段
	 * 4.找到主键并建立
	 * 
	 */
	
	private static String getStringSplitDot(String[] str )
	{
		if(str==null || str.length==0)
			return "";
		String ret = "";
		for(String string:str)
		{
			ret +=  string+",";
		}
		return ret.substring(0,ret.length() - 1);
	}
}
