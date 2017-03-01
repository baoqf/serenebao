package test.org.swift.dao;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.List;

import org.swift.database.pool.DBConnect;
import org.swift.serenebao.dao.mysql.CommonDAO;
import org.swift.serenebao.dao.mysql.CreateTable;
import org.swift.serenebao.dao.mysql.DAOFactory;
import org.swift.util.datetime.CTime;

import test.org.swift.entity.mysql.Test;
import test.org.swift.entity.mysql.TestChild;
import test.org.swift.entity.mysql.TestParent;

public class MysqlDAOTest {
	public static List list = new ArrayList();
	 public static void main(String[] args) throws Exception
	 {
		CommonDAO dao = DAOFactory.getDAO(TestChild.class);
		dao.setSql_print(true);
		testDAOFunction();
	 }

	 static void testrsMeta() throws Exception
	 {
		 DBConnect conn = new DBConnect(false);
		 ResultSet rs = conn.executeQuery("select * from tbill limit 0,0");
		 ResultSetMetaData metadate = rs.getMetaData();
		 conn.close();
		int colcount = metadate.getColumnCount();
		for(int i=1;i<colcount;i++)
		{
			System.out.println(metadate.getColumnName(i));
		}

	 }
	 static void testInsert() throws Exception
	 {
		 TestChild  object = new TestChild();
//		 object.setDays(22);
//		 object.setP_name("ceshi");
		 CommonDAO dao = DAOFactory.getDAO(TestChild.class);
		 dao.setSql_print(true);
		 dao.insert(object, null);
	 }

	 static void testDAOFunction() throws Exception
	 {
		 Test object = new Test();
		 object.setId(1L);
		 object.setName("3333");
		 CommonDAO dao = DAOFactory.getDAO(Test.class);
		 dao.insert(object, true,null);
//		 SessionDetail  object = new SessionDetail();
//		 dao.setSql_print(true);
//		 List list = dao.listPage(1, 2, new Query(" where 1=1"), null);
//		 TestChild  object = new TestChild();
//		 CommonDAO dao = DAOFactory.getDAO(TestChild.class);
//		 dao.setSql_print(true);
//		 dao.getRowCount(new Query(" where id=1"), null);
//		 dao.getOneRowColBySql(new Query("select * from testchild where id=?",1),Long.class, null);
//		 dao.getRowCount(new Query("id=?",1), null);
//		 dao.getRowCountBySql(new Query("select * from testchild where id=?",1), null);
//		 object.setId((long)11);
//		 dao.insert(object, null);
//		 dao.listAllAll(new Query("id=?",1), null);
//		 dao.get(1, null);
//		 dao.listAllBySql(new Query("select * from testchild where id=?",1), null);
//		 dao.listPage(1, 15, new Query("id=?",1), null);
//		 dao.listPageBySql(1, 15, new Query("select * from testchild where id=?",1), null);
//		 dao.listTopN(new Query("id=?",1), 1, null);
//		 dao.listTopNBySql(new Query("select * from testchild where id=?",1), 1, null);
//		 object.setP_name("1111");
//		 dao.save(object, null);
//		 object.setP_name("asdsdf");
//		 dao.update(object, 11, null);
//		 object.setP_name("1111");
//		 object.setDays(111);
//		 object.setIf_parent(true);
//		 dao.updateObjByCondition(object, new Query("id=? and if_parent=?",1,false), null);
//		 dao.update(new UpdateFields("parent_name=?","asdfasd","id=?",11), null);
//		 dao.delete(11, null);
//		 dao.delete(new Query("id=?",11), null);
//		 dao.executeQuery(new Query("select * from testchild where id=?",1), null);
//		 long cur = dao.getSeqCurValue("userId");
//		 long next = dao.getSeqNextValue("userId");
//		 Date date = dao.getServerTime();
//		 List<UpdateFields> list = new ArrayList<UpdateFields>();
//		 list.add(new UpdateFields("parent_name=?","asdfasd","id=?",11));
//		 list.add(new UpdateFields("parent_name=?","asdfasd","id=?",11));
//		 list.add(new UpdateFields("parent_name=?","asdfasd","id=?",11));
//		 dao.updateBatch(list, null);

	 }
	 static void initTable()
	 {
		 CreateTable.init(TestParent.class);
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
