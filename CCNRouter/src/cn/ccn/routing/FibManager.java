package cn.ccn.routing;

import java.util.Hashtable;

import cn.ccn.model.protocol.FibItem;

public class FibManager {
	private static Hashtable<String ,FibItem> fibTree = new Hashtable<String ,FibItem>();

	public static Hashtable<String, FibItem> getFibTree() {
		return fibTree;
	}

	public static void setFibTree(Hashtable<String, FibItem> fibTree) {
		FibManager.fibTree = fibTree;
	}

	public synchronized static void  insertFib(FibItem fibitem){
		fibTree.put(fibitem.getName(),fibitem);
	}
	
	public synchronized static void removeFib(String name){
		fibTree.remove(name);
	}
	
	public synchronized static void updateFib(FibItem fibitem){
		fibTree.put(fibitem.getName(), fibitem);
	}
	
	public synchronized static FibItem getFib(String name){
		return fibTree.get(name);
	}
}
