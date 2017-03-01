package org.swift.serenebao.orm.anotation;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Target({FIELD, METHOD, TYPE})
@Retention(RUNTIME)
public @interface ForeignKey {
	/**
	 * 外键名称
	 */
	String name() default "";
	/**
	 * 外键字段名
	 */
	String[] columnNames();
	/**
	 * 外键表
	 */
	String refTable();
	/**
	 * 外键表的字段
	 */
	String[] refColumnNames() default {};
	/**
	 * 外键表删除时如何操作本表数据,FKOnUDAction中的操作
	 */
	String onDelete() default "";
	/**
	 * 外键表更新时如何操作本表数据
	 */
	String onUpdate() default "";
	
}