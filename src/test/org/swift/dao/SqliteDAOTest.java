package test.org.swift.dao;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.swift.database.pool.DBConnect;
import org.swift.serenebao.dao.*;
import org.swift.serenebao.dao.sqlite.*;
import org.swift.util.datetime.CTime;

import test.org.swift.entity.sqlite.Test;


public class SqliteDAOTest {
	public static List list = new ArrayList();
	 public static void main(String[] args) throws Exception
	 {
		CommonDAO dao = DAOFactory.getDAO(Test.class);
		dao.listAllAll(new Query(" gameId=?",1));
//		dao.setSql_print(true);
//		testDAOFunction();
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
		 Test  object = new Test();
//		 object.setDays(22);
//		 object.setP_name("ceshi");
		 CommonDAO dao = DAOFactory.getDAO(Test.class);
		 dao.setSql_print(true);
		 dao.insert(object, null);
	 }

	 static void testDAOFunction() throws Exception
	 {
		 CommonDAO dao = DAOFactory.getDAO(Test.class);
		 dao.setSql_print(true);

//		 List list = dao.listPage(1, 2, new Query(" where 1=1"), null);
//
//		 dao.getRowCount(new Query(" where id=1"), null);
//
//		 dao.getOneRowColBySql(new Query("select * from test where id=?",1),Long.class, null);
//
//		 dao.getRowCount(new Query("id=?",1), null);
//
//		 dao.getRowCountBySql(new Query("select * from test where id=?",1), null);
//
		 Test object = new Test();
//		 object.setName("haha����");
//		 dao.insert(object, null);
//		 dao.listAllAll(new Query("id=?",1), null);
//		 dao.get(1, null);
//		 dao.listAllBySql(new Query("select * from test where id=?",1), null);
//		 dao.listPage(1, 15, new Query("id=?",1), null);
//		 dao.listPageBySql(1, 15, new Query("select * from test where id=?",1), null);
//		 dao.listTopN(new Query("id=?",1), 1, null);
//		 dao.listTopNBySql(new Query("select * from test where id=?",1), 1, null);
//		 object.setName("1111");
//		 dao.save(object, null);
//		 object.setName("11111111111111");
//		 dao.update(object, 1, null);
//		 object.setName("22222222222");
//		 dao.updateObjByCondition(object, new Query("id=?",1), null);
//		 dao.update(new UpdateFields("name=?","555555555","id=?",1), null);
//		 dao.delete(11, null);
//		 dao.delete(new Query("id=?",2), null);
//		 dao.executeQuery(new Query("select * from test where id=?",1), null);
//		 Date date = dao.getServerTime();
//		 List<UpdateFields> list2 = new ArrayList<UpdateFields>();
//		 list2.add(new UpdateFields("parent_name=?","asdfasd","id=?",11));
//		 list2.add(new UpdateFields("parent_name=?","asdfasd","id=?",11));
//		 list2.add(new UpdateFields("parent_name=?","asdfasd","id=?",11));
//		 dao.updateBatch(list2, null);

	 }
	 static void initTable()
	 {
		 CreateTable.init(Test.class);
	 }

	 static void testPresure() throws Exception
	 {
		 System.out.println(CTime.getFormatTodayTimeStr("yyyy-MM-dd HH:mm:ss:SSS"));
		 CommonDAO dao = DAOFactory.getDAO(Test.class);
		 dao.setSql_print(false);
		 DBConnect conn = new DBConnect(false);
		 for(int i=0;i<10000;i++)
		 {
			 Test  object = new Test();
			 dao.insert(object, conn);
			 //if(i%1000==0)
		 }
		 conn.commit();
		 conn.close();
		 System.out.println(CTime.getFormatTodayTimeStr("yyyy-MM-dd HH:mm:ss:SSS"));

	 }
}
