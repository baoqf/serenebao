package test.org.swift.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.swift.database.pool.DBConnect;
import org.swift.serenebao.dao.*;
import org.swift.serenebao.dao.oracle.*;
import org.swift.util.datetime.CTime;

import test.org.swift.entity.oracle.TestChild;

public class OracleDAOTest {
	public static List list = new ArrayList();
	 public static void main(String[] args) throws Exception
	 {
		 initTable();
		 testDAOFunction();
	 }
	 static void testDAOFunction() throws Exception
	 {
		 TestChild  object = new TestChild();
		 CommonDAO dao = DAOFactory.getDAO(TestChild.class);
		 dao.setSql_print(true);
		 dao.getOneRowColBySql(new Query("select * from testchild where iden=?",1),Long.class, null);
		 dao.getRowCount(new Query("iden=?",1), null);
		 dao.getRowCountBySql(new Query("select * from testchild where iden=?",1), null);
		 long seq = dao.getSeqNextValue("SEQ");
		 object.setId(seq);
		 object.setP_name("ddd");
		 object.setForei_id((long)11);
		 String id = dao.insert(object, null);
		 dao.listAllAll(new Query("iden=?",1), null);
		 dao.get(1, null);
		 dao.listAllBySql(new Query("select * from testchild where iden=?",1), null);
		 dao.listPage(1, 15, new Query("iden=?",1), null);
		 dao.listPageBySql(1, 15, new Query("select * from testchild where iden=?",1), null);
		 dao.listTopN(new Query("iden=?",1), 1, null);
		 dao.listTopNBySql(new Query("select * from testchild where iden=?",1), 1, null);
		 object.setP_name("1111");
		 dao.save(object, null);
		 object.setP_name("asdsdf");
		 dao.update(object, 1, null);
		 dao.update(new UpdateFields("parent_name=?","asdfasd","iden=?",1), null);
		 dao.delete(1, null);
		 dao.delete(new Query("iden=?",1), null);
		 dao.executeQuery(new Query("select * from testchild where id=?",1), null);
		 long cur = dao.getSeqCurValue("userId");
		 long next = dao.getSeqNextValue("userId");
		 Date date = dao.getServerTime();
		 List<UpdateFields> list = new ArrayList<UpdateFields>();
		 list.add(new UpdateFields("parent_name=?","asdfasd","id=?",11));
		 list.add(new UpdateFields("parent_name=?","asdfasd","id=?",11));
		 list.add(new UpdateFields("parent_name=?","asdfasd","id=?",11));
		 dao.updateBatch(list, null);
	 }
	 static void initTable()
	 {
		 CreateTable.init(TestChild.class);

	 }

	 static void testPresure() throws Exception
	 {
		 System.out.println(CTime.getFormatTodayTimeStr("yyyy-MM-dd HH:mm:ss:SSS"));
		 CommonDAO dao = DAOFactory.getDAO(TestChild.class);
		 dao.setSql_print(false);
		 DBConnect conn = new DBConnect(false);
		 for(int i=0;i<10000;i++)
		 {
			 TestChild  object = new TestChild();
			 dao.insert(object, conn);
			 //if(i%1000==0)
		 }
		 conn.commit();
		 conn.close();
		 System.out.println(CTime.getFormatTodayTimeStr("yyyy-MM-dd HH:mm:ss:SSS"));

	 }
}
