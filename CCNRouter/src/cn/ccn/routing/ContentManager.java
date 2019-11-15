package cn.ccn.routing;

import java.util.Hashtable;
import java.util.Random;
import java.util.Set;

import cn.ccn.model.ContentObject;
import cn.pku.util.GlobalVar;

public class ContentManager {
	private static int dataSize = GlobalVar.packetDataSize;
	private static int capacity = 1000;
	private static int count = 0;
	private static Hashtable<String, ContentObject> ctree = new Hashtable<String, ContentObject>();
	
	public static void addContent(String name,ContentObject cobj){
		if(ctree.size()>=capacity){
			System.out.println("start clean!");
			Object key[] = ctree.keySet().toArray();
			Random r = new Random();
			int n = capacity/20;
			for(int i=0;i<n;i++){
				r.nextInt(key.length);
				ctree.remove(key[i]);
			}
		}
		ctree.put(name,cobj);
		System.out.println("ctree size:"+ctree.size());
	}
	
	public static void delContent(String name){
		ctree.remove(name);
	}
	
	public static ContentObject getContent(String csname, String name, int pid){
		ContentObject con = ctree.get(csname);
		if(con==null){
			con = CheckForFile.getContentFromFile(name, pid, dataSize);
			if(con!=null)addContent(csname, con);
		}
		return con;
	}
}
