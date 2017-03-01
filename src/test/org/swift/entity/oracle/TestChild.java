package test.org.swift.entity.oracle;

import java.util.Date;

import org.swift.serenebao.orm.*;
import org.swift.serenebao.orm.anotation.*;
@Table (id="id",auto_increment=false,tablespace="TEST")
public class TestChild {
	@Column(col_name="iden",type="number(20)",length="")
	private Long id ;
	@Column(col_name="parent_name",length="12")
	private String p_name ;
	@Column(col_name="forei_id",type="number(20)",length="")
	private Long forei_id;
	@Column(type="date",length="")
	private Date begintime ;
	private Integer test1 ;
	private Short sht ;
	@Column(type="number(1)",length="")
	private Boolean bl ;
	@Column(length="10,2")
	private Float ff ;
	@Column(length="10,2")
	private Double dd ;
	
	public Long getForei_id() {
		return forei_id;
	}
	public void setForei_id(Long forei_id) {
		this.forei_id = forei_id;
	}
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
	public Boolean getBl() {
		return bl;
	}
	public void setBl(Boolean bl) {
		this.bl = bl;
	}

	public Date getBegintime() {
		return begintime;
	}
	public void setBegintime(Date begintime) {
		this.begintime = begintime;
	}
	public Short getSht() {
		return sht;
	}
	public void setSht(Short sht) {
		this.sht = sht;
	}
	public Integer getTest1() {
		return test1;
	}
	public void setTest1(Integer test1) {
		this.test1 = test1;
	}
	public Double getDd() {
		return dd;
	}
	public void setDd(Double dd) {
		this.dd = dd;
	}
	public Float getFf() {
		return ff;
	}
	public void setFf(Float ff) {
		this.ff = ff;
	}

}
