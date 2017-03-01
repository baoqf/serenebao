package test.org.swift.dao;

import java.sql.CallableStatement;
import java.sql.SQLException;
import java.util.List;

import org.swift.database.pool.DBConnect;
import org.swift.serenebao.dao.sqlserver.CommonDAO;
import org.swift.serenebao.dao.sqlserver.DAOFactory;

import test.org.swift.entity.FEE_TransInfo;
import test.org.swift.entity.UserInfo;

public class TestProc {

	public static void main(String[] args) throws SQLException {
		System.out.println(FeeReg("122111",92));

	}
	
	public static List<UserInfo> login()
	{
		DBConnect connect = new DBConnect("UserCenter");
		CommonDAO dao = DAOFactory.getDAO(UserInfo.class);
		List<UserInfo> list = dao.callProcRetList("PLAT_LoginByUID"
				, new Object[] {"72000042","123456"},connect);
		return list;
	}
	
	public static boolean FeeReg(String account, int pakageid) throws SQLException {
		DBConnect connect = new DBConnect("FeeCenter");
		CommonDAO dao = DAOFactory.getDAO(UserInfo.class);
		try {
			Integer count = Integer.valueOf(dao.callProcLastOutParam("FEE_Reg"
					, new Object[] {account, pakageid},Integer.class, connect).toString());
			connect.close();
		} finally {
			if (connect != null)
				connect.close();
		}
		return true;
	}
	
	public static int call()
	{
		DBConnect connect = new DBConnect("FeeCenter");
		CommonDAO dao = DAOFactory.getDAO(FEE_TransInfo.class);
		Integer count = Integer.valueOf(dao.callProcRetOneRowCol("UTIL_GetOnePage"
				, new Object[] {"FEE_TransInfo", "*"
						,"AccountID = '72000042' AND not CallerPhone is NULL"
						,"uniid"
						,"desc"},Integer.class, connect).toString());
		connect.close();
		return count;
	}
	
	
	static int listFeeInfo()
	{
		DBConnect connect = new DBConnect("FeeCenter");
		CommonDAO dao = DAOFactory.getDAO(FEE_TransInfo.class);
		try {
			List<FEE_TransInfo> list = dao.callProcRetList("UTIL_GetOnePage"
					,new Object[] {
					"FEE_TransInfo"
					, "*"
					,"AccountID = '72000042' AND not CallerPhone is NULL"
					,"uniid"
					,"desc"
					,1
					,10
					,1}, connect);
			
			return list.size();
		}
		finally {
			if(connect!=null)
				connect.close();
		}
	}

}
