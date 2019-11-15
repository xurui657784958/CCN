package cn.ccn.routing;

import java.util.Hashtable;
import java.util.Iterator;

import cn.ccn.model.protocol.Face;
import cn.ccn.model.protocol.PitItem;

public class PitManager {
	private static Hashtable<String ,PitItem> pitTable = new Hashtable<String ,PitItem>();
	
	
	public static synchronized void addPit(PitItem pit){
		System.out.println("add pit : "+pit.getName() );
		Iterator<Integer> it = pit.getWaitingFaces().iterator();
		while(it.hasNext()){
			FaceManager.getFace(it.next()).addPitList(pit.getName());
		}
		PitItem t = pitTable.get(pit.getName());
		if(t!=null){
			t.updateTimer();
		}else{
			pitTable.put(pit.getName(), pit);
			pit.startTimer();
		}
	}
	
	public static void removePitAFace(String name,int faceid){
		pitTable.get(name).delWaitingFaces(faceid);
	}
	public static synchronized void delPit(String name){
		PitItem pit = pitTable.get(name);
		if(pit==null)return;
		System.out.println("DEL PIT :"+name);
		pit.cancleTimer();  //have some bug
		Iterator<Integer> it = pit.getWaitingFaces().iterator();
		while(it.hasNext()){
			Face face = FaceManager.getFace(it.next());
			face.delPitList(name);
		}
		pitTable.remove(name);
	}
	public static void updatePit(String name){
		pitTable.get(name).updateTimer();
	}
	public static PitItem getPit(String name){
		return pitTable.get(name);
	}

	public static Hashtable<String, PitItem> getPitTable() {
		return pitTable;
	}
	
	
}
