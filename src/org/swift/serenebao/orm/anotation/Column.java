package org.swift.serenebao.orm.anotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
@Retention (RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
/**
 * 字段属性定义
 * 鲍庆丰
 * baoqingfeng@gmail.com
 * 2007-3-27
 */
public @interface Column {
	/**
	 * 如果col_name为""，使用entity属性作为字段
	 */
	String col_name() default "";
	/**
	 * 默认值
	 */
	String default_value() default "";
	/**
	 * 备注
	 */
	String comment() default "";
	/**
	 * 默认允许null,不允许null填not null
	 */
	boolean nullable() default true;
	/**
	 * 字段类型，默认允许null,不为null时，表示特殊类型：text,blob等
	 */
	String type() default "";//CLOB NOT NULL
	/**
	 * 如果使用Field,长度必须填,如果使用字段类型的默认长度，不要使用Field
	 * <br>float使用8,2填精度
	 */
	String length();

}
