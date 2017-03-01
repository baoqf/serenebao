package org.swift.serenebao.dao.sqlite;
import java.util.HashMap;

import org.apache.log4j.Logger;

/**
 * DAO工厂类：
 * <br>用法：
 * <br>CommonDAO dao = DAOFactory.getDAO(TestParent.class);
 * <br>本方法一种实体类在工厂中只保存一个CommonDAO对象
 * @author 鲍庆丰
 *
 */
public class DAOFactory {
	static Logger log = Logger.getLogger(DAOFactory.class);
	private static HashMap<String, CommonDAO> map = new HashMap<String, CommonDAO>();
	public static String cache_cluster_ips = null;
	public static String cache_cluster_localip = null;
	public static int cache_cluster_localport;
	public static HashMap<String, String> ipmap = null;
	static
	{
//		String thisFilePath;
//		try {
//			thisFilePath = Path.getFullPathRelateClass("", DBConnectionPools.class);
//			int webInfo = thisFilePath.indexOf("WEB-INF");
//			if(webInfo == -1)
//				throw new IOException("no WEB-INF found:"+thisFilePath);
//			String dbfile = thisFilePath.substring(0, webInfo)+"/WEB-INF/db.prop";
//			if (INIFile.checkFile(dbfile)) {
//			INIFile objINI = new INIFile(dbfile);
//			String logFile = objINI.getStringProperty("dbconfig", "logfile", "DBConnectionManager.log");
//			DBLog Log = DBLog.getInstance();
//			Log.init(logFile);
//			cache_cluster_ips = objINI.getStringProperty("dbconfig", "cache_cluster_ips", null);
//			cache_cluster_localip = objINI.getStringProperty("dbconfig", "cache_cluster_localip", "127.0.0.1");
//			cache_cluster_localport = objINI.getIntegerProperty("dbconfig", "cache_cluster_localport", 8899);
//			//只有配置了集群才启动
//			if(cache_cluster_ips!=null && cache_cluster_ips.length()!=0)
//			{
//				String[] ips = cache_cluster_ips.split(",");
//				ipmap = new HashMap<String, String>();
//				for(String ip:ips)
//				{
//					String ipport[] = ip.split(":");
//					ipmap.put(ipport[0], ipport[1]);
//				}
//				Thread m_SocketAdp = new Thread(new CacheClusterListen(), "CacheClusterListen");
//				m_SocketAdp.start();
//				Thread cacheClusterSync = new Thread(new CacheClusterSync(), "CacheClusterSync");
//				cacheClusterSync.start();
//			}
//		}
//		else
//		{
//			log.error("can't read db.prop~! " + dbfile);
//		}
//
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}

	}
	public static CommonDAO getDAO(Class clazz)
	{
		if(clazz==null)
			return null;
		String entity_name = clazz.getName();
		if(map.get(entity_name)==null)
		{
			CommonDAO dao = new CommonDAO(clazz);
			map.put(entity_name, dao);
		}
		return map.get(entity_name);
	}
}
