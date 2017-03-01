package org.swift.serenebao.orm.cache;
import java.util.Hashtable;
public class SqlCache {
        /** 消息模块实例 */
        static private SqlCache _instance;
        /** 消息队列，Hash表中存储多份Vector，每份Vector表示关键字标识的队列 
         * 	使用前先用regedit注册关键字，多个队列共用
         * */
        private Hashtable<String, Hashtable<?, ?>> list = null;
        /** 线程同步控制确保模块仅有一个实例 */
        static synchronized public SqlCache getInstance() {
                if (_instance == null) {
                        _instance = new SqlCache();
                }
                return _instance;
        }
        
        /*
         * 注册Hash关键字队列
         */
        public boolean regedit(String listName){
            if(listName==null) 
            	return false;
            if(list.containsKey(listName)){
                return true;
            }else{
                list.put(listName,new Hashtable());
                return true;
            }
        }
        /** 构造器*/
        private SqlCache() {
                list=new Hashtable<String, Hashtable<?, ?>>();
        }
        /** 向消息队列添加消息 */
        public synchronized boolean add(Object key,Object value,String listName){
            if(value==null||listName==null) return false;
            if(list.containsKey(listName)){
                ((Hashtable)list.get(listName)).put(key,value);
            }
            else
                return false;
            return true;
        }
        
        /** 返回并删除消息队列起始处消息，若消息队列为空，返回空 */
        public synchronized Object getFirstAndRemove(String listName){
            if(listName==null) return null;
            if(list.containsKey(listName)){
            	Hashtable hs = ((Hashtable)list.get(listName));
            	if(!hs.keySet().isEmpty())
            	{
            		Object key = hs.keySet().iterator().next();
            		Object value = hs.get(key);
            		hs.remove(key);
            		return value;
            	}
            	else return null;
            }
            else
                return null;
        }
        
        /** 返回消息队列起始处key，若消息队列为空，返回空 */
        public synchronized Object getFirstKey(String listName){
            if(listName==null) return null;
            if(list.containsKey(listName)){
            	Hashtable hs = ((Hashtable)list.get(listName));
            	if(!hs.keySet().isEmpty())
            	{
            		Object key = hs.keySet().iterator().next();
            		return key;
            	}
            	else return null;
            }
            else
                return null;
        }
        
        /** 返回消息，若消息队列为空，返回空 */
        public synchronized Object get(String key,String listName){
            if(listName==null) return null;
            if(list.containsKey(listName)){
            	Hashtable hs = ((Hashtable)list.get(listName));
            	if(!hs.isEmpty())
            	{
            		return hs.get(key);
            	}
            	else return null;
            }
            else
                return null;
        }
        /** 返回并删除消息队列起始处消息，若消息队列为空，返回空 */
        public synchronized Object remove(Object key,String listName){
            if(listName==null) return null;
            if(list.containsKey(listName)){
            	Hashtable hs = (Hashtable)list.get(listName);
                if(!hs.containsKey(key)) return null;
                return hs.remove(key);
            }
            else
                return null;
        }
        /** 返回消息队列长度 */
        public int getSize(String listName){
            if(listName==null) return -1;
            if(list.containsKey(listName)){
                return ((Hashtable)list.get(listName)).size();
            }
            else
                return -1;
        }
}