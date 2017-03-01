package test.org.swift.entity.sqlite;

import java.util.Date;

import org.swift.serenebao.orm.anotation.*;

@Table (name="local_game_type",id="id",auto_increment=true)
public class Test {
	private Long id ;	
	private String type ;
	private Date logtime ;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}


	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Date getLogtime() {
		return logtime;
	}
	public void setLogtime(Date logtime) {
		this.logtime = logtime;
	}


}
