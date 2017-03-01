package org.swift.serenebao.orm.cache;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.swift.serenebao.dao.mysql.DAOFactory;


public class CacheClusterListen implements Runnable {
	Logger log = Logger.getLogger(DAOFactory.class);
    private String vector_unsend = "unsend";
    private SqlCache mtList = SqlCache.getInstance();
    private ThreadPoolExecutor pool;
	public CacheClusterListen() {
		pool = new ThreadPoolExecutor(10,10,50000L,TimeUnit.MILLISECONDS,new LinkedBlockingQueue<Runnable>());
	}

	public void run() {
		try {
			ServerSocket s = new ServerSocket(DAOFactory.cache_cluster_localport);
			log.info("Cache Server Started,listening on ip:"+DAOFactory.cache_cluster_localip+":"+DAOFactory.cache_cluster_localport);
			try {
				while (true) {
					//服务器监听
					Socket socket=null;
					try {
						socket = s.accept();
						DealInfo di  = new DealInfo(socket,new DataInputStream(new BufferedInputStream(socket.getInputStream())));
						pool.execute(di);
					} catch (Exception e) {
						if(socket!=null)
							socket.close();
					}
				}
			} finally {
				s.close();
			}
		}
		catch (Exception e) {
			
		}
	}
	
    public class DealInfo implements Runnable {
        private Socket socket;
        public DealInfo(Socket socket,DataInputStream	inBound) {
            this.socket = socket;
        }
        public void run() {
        	try {
				ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
				Object obj = in.readObject();
				in.close();
				mtList.add(obj,obj,vector_unsend);

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
      }

}
