package test.org.swift.entity.mysql;

import org.swift.serenebao.orm.anotation.Table;

@Table(id = "id", auto_increment = true)
public class Test {
	Long id;
	String name;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
}
