package org.swift.serenebao.orm.cache;
import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.Map;


public class EntityCache {
	public static String cache_cluster_ips = null;
	/**
	 * 缓存大小
	 * 存放方式：
	 * cache
	 * 	---Hashtable(table,LinkedHashMap)
	 * 		--LinkedHashMap (id,row)
	 * 	---Hashtable(table,LinkedHashMap)
	 * 		--LinkedHashMap (id,row)
	 */
	private static Hashtable<Class, LinkedHashMap<?, ?>> cache = new Hashtable<Class, LinkedHashMap<?, ?>>(300);

	/**
	 * 注册一张表的一个Cache
	 */
    public static boolean regedit(Class tablename, final int MAX_ENTRIES){
        if(tablename==null) 
        	return false;
        if(cache.containsKey(tablename)){
            return true;
        }else{
        	cache.put(tablename,new LinkedHashMap(MAX_ENTRIES+1, .75F, true) {
				private static final long serialVersionUID = 1L;
				// This method is called just after a new entry has been added
                public boolean removeEldestEntry(Map.Entry eldest) {
                    return size() > MAX_ENTRIES;
                }
            });
            return true;
        }
    }
    
    /**
	 * 向cache添加一条记录
	 */
    public static synchronized boolean put(Object key,Object value,Class tablename){
        if(value==null||tablename==null)
        	return false;
        if(cache.containsKey(tablename)){
            ((LinkedHashMap)cache.get(tablename)).put(key,value);
        }
        else
            return false;
        return true;
    }
    
    /**
	 * 从cache中获得记录
	 */
    public static synchronized Object get(Object key,Class tablename){
        if(tablename==null) 
        	return null;
        if(cache.containsKey(tablename)){
        	LinkedHashMap hs = ((LinkedHashMap)cache.get(tablename));
        	if(!hs.isEmpty())
        	{
        		return hs.get(key);
        	}
        	else return null;
        }
        else
            return null;
    }

    /**
	 * 从cache中删除记录
	 */
    public static synchronized Object remove(Object key,Class tablename){
        if(tablename==null) 
        	return null;
        if(cache.containsKey(tablename)){
        	LinkedHashMap hs = (LinkedHashMap)cache.get(tablename);
            if(!hs.containsKey(key)) return null;
            	return hs.remove(key);
        }
        else
            return null;
    }

}
