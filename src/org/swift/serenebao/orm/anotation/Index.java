package org.swift.serenebao.orm.anotation;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
@Target({FIELD, METHOD})
@Retention(RUNTIME)
public @interface Index {
	/**
	 * 索引名称
	 */
	String name() default "";
	/**
	 * 索引字段
	 */
	String[] columnNames();
	/**
	 * 索引类型,IndexType类型
	 */
	String type() default "";
}
