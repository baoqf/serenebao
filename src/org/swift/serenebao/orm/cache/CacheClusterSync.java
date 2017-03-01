package org.swift.serenebao.orm.cache;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Map.Entry;

import org.swift.serenebao.dao.mysql.DAOFactory;


public class CacheClusterSync implements Runnable {
	private SqlCache mtList = SqlCache.getInstance();
	private String vector_unsend = "unsend";

	public void run() {
		Socket comm = null;
		OutputStream out=null;
		while (true) {
					String key=(String)mtList.getFirstKey("vector_unsend");
						Object obj = mtList.get(key, vector_unsend);
						mtList.remove(key, vector_unsend);
						/////deal begin
						try {
							for(Entry<String, String> entry : DAOFactory.ipmap.entrySet()){
								comm = new Socket(entry.getKey(),new Integer(entry.getValue()));
								comm.setSoTimeout(20000);
								out = comm.getOutputStream();
								ObjectOutputStream out1 = new ObjectOutputStream(out);
								out1.writeObject(obj);
								out1.close();
							}
						} catch (Exception e) {
							try {
								if(out!=null) out.close();
								if(comm!=null) comm.close();
								Thread.sleep(5000);
							} catch (InterruptedException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							} catch (IOException e2) {
								// TODO Auto-generated catch block
								e2.printStackTrace();
							}

						}
						///deal end
					}
	}

}
