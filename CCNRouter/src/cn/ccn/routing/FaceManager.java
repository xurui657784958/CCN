package cn.ccn.routing;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import cn.ccn.model.protocol.Face;

public class FaceManager {
	private static Map<Integer,Face> facelist = new HashMap<Integer,Face>();
	private static int faceNum = 0;
	
	public static Face isFaceExist(String ip,int port){
		Iterator<Integer> it = facelist.keySet().iterator();
		int key;
		Face f = null,temp = null;
		while(it.hasNext()){
			key = it.next();
			temp = facelist.get(key);
			if(temp.getIp().equals(ip)&&temp.getPort()==port){
				f = temp;
				break;
			};
		}
		return f;
	}
	
	public static Face addFace(String ip,int port){
		Face f = isFaceExist(ip, port);
		if(f==null){
			f = new Face(ip,port,faceNum);
			System.out.println("FaceManager.class: addFace "+f.getFaceID());
			faceNum++;
			facelist.put(f.getFaceID(), f);
		}
		return f;
	}
	
	public static void delFace(int faceid){
		Face f = getFace(faceid);
		Iterator<String>it = f.getPitList().iterator();
		while(it.hasNext()){
			PitManager.getPit(it.next()).delWaitingFaces(faceid);
		}
		facelist.remove(faceid);
	}
	
	public static Face getFace(int faceid){
		return facelist.get(faceid);
	}
	
	public static Map<Integer,Face> getFaceMap(){
		return facelist;
	}
}
