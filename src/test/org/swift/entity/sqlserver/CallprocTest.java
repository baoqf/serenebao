package test.org.swift.entity.sqlserver;

import java.util.List;

import org.swift.database.pool.DBConnect;
import org.swift.serenebao.dao.sqlserver.CommonDAO;
import org.swift.serenebao.dao.sqlserver.DAOFactory;

public class CallprocTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		List list = PLAT_UIDListGetUID(58,0,21,"",0);
		System.out.println(list.size());
	}
	
	// 用户账户充值
	public static int feeInduct(String account, int feekind, float feeMoney,
			String feeCurrency, String user, int expireDate, String remark) {
		DBConnect connect = new DBConnect("FeeCenter");
		CommonDAO dao = DAOFactory.getDAO(TestChild.class);
		try {
			int ret = Integer.valueOf(dao.callProcMiddleOutParam(
					"FEE_Induct",
					new Object[] { account, feekind, feeMoney, feeCurrency,
							user, expireDate, remark }, 7,Integer.class, connect)
					.toString());
			return ret;
		} finally {
			if (connect != null)
				connect.close();
		}
	}
	
	//获得用户ID
	public static List PLAT_UIDListGetUID(int AreaID	, int Kind, int Type, String Phone, int Flag) {
		DBConnect connect = new DBConnect("UserCenter");
		CommonDAO dao = DAOFactory.getDAO(TestChild.class);
		try {
			List ret = dao.callProcMultiOutParam(
					"PLAT_UIDListGetUID",
					new Object[] { AreaID, Kind, Type, Phone,
							Flag}, new int[] {4,7},new Class[] {String.class,Integer.class}, connect);
			return ret;
		} finally {
			if (connect != null)
				connect.close();
		}
	}
	
	// 用户账户充值
	public static List feeInduct2(String account, int feekind, float feeMoney,
			String feeCurrency, String user, int expireDate, String remark) {
		DBConnect connect = new DBConnect("FeeCenter");
		CommonDAO dao = DAOFactory.getDAO(TestChild.class);
		try {
			List ret = dao.callProcMultiOutParam(
					"FEE_Induct",
					new Object[] { account, feekind, feeMoney, feeCurrency,
							user, expireDate, remark }, new int[] {7},new Class[] {Integer.class}, connect) ;
			return ret;
		} finally {
			if (connect != null)
				connect.close();
		}
	}

}
