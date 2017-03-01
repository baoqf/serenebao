package test.org.swift.entity.sqlserver;
import org.swift.serenebao.orm.anotation.*;
@Table (name="asdkasdjf"
		,id="idnumber"
		,auto_increment=true
		,comment="����"
		,tableType="innoDB")
public class TestParent {
	//@Column(col_name="id",comment="����",length="12")
	private Long idnumber ;
	//@Column(col_name="parent_name",default_value="����",comment="���",nullable=true,length="100")
	private String name ;
	private Short test_short ;
	public Long getIdnumber() {
		return idnumber;
	}
	public void setIdnumber(Long idnumber) {
		this.idnumber = idnumber;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Short getTest_short() {
		return test_short;
	}
	public void setTest_short(Short test_short) {
		this.test_short = test_short;
	}
}
