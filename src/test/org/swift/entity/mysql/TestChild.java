package test.org.swift.entity.mysql;

import org.swift.serenebao.orm.anotation.Table;

@Table(id = "id", auto_increment = false)
public class TestChild {
	private Long id;
	private String p_name;
	private String test;
	// private Long forei_id;
	private TestParent parent;
	private Boolean if_parent;
	private Integer days;

	// public Long getForei_id() {
	// return forei_id;
	// }
	// public void setForei_id(Long forei_id) {
	// this.forei_id = forei_id;
	// }
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getP_name() {
		return p_name;
	}

	public void setP_name(String p_name) {
		this.p_name = p_name;
	}

	public TestParent getParent() {
		return parent;
	}

	public void setParent(TestParent parent) {
		this.parent = parent;
	}

	public Boolean getIf_parent() {
		return if_parent;
	}

	public void setIf_parent(Boolean if_parent) {
		this.if_parent = if_parent;
	}

	public String getTest() {
		return test;
	}

	public void setTest(String test) {
		this.test = test;
	}

	public Integer getDays() {
		return days;
	}

	public void setDays(Integer days) {
		this.days = days;
	}

}
