package org.swift.serenebao.orm.anotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
@Retention (RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
/**
 * 表属性定义
 * 鲍庆丰
 * baoqingfeng@gmail.com
 * 2007-3-27
 */
public @interface Table {
	/**
	 * 表名
	 */
	String name() default "";
	/**
	 * 主键,默认为id
	 */
	String id() default "id";
	/**
	 * 主键是否自增
	 */
	boolean auto_increment() default true;
	/**
	 * 如果不自增，主键使用的序列
	 */
	String seq() default "SEQ_DEFAULT";
	/**
	 * 使用的表空间
	 */
	String tablespace() default "SPACE_DEFAULT";
	/**
	 * 索引
	 */
	Index[] indexes() default {};
	/**
	 * 外键
	 */
	ForeignKey[] foreignKey() default {};
	/**
	 * 备注
	 */
	String comment() default "";
	/**
	 * 表类别
	 */
	String tableType() default "";
	/**
	 * 编码
	 */
	String charset() default "";
	/**
	 * 是否使用二级缓存大小,由于系统有多台服务器需要修改表记录
	 * ，所以使用缓存会导致数据不同步，需要缓存的时候再设置为需要缓存，
	 * 这样会尽量保留原来系统的延续性
	 */
	boolean use_cache() default false;
	/**
	 * 二级缓存大小,默认为每个表10000条
	 */
	int cache_size() default 10000;
}
