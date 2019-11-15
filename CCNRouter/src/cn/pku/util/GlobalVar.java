package cn.pku.util;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;

import cn.ccn.model.protocol.Face;
import cn.ccn.model.protocol.Neighbour;
import cn.ccn.routing.FaceManager;

public class GlobalVar {
	public static String routerName;
	public static String routerIp;
	public static int ctrlRecvPort;
	
	public static int routerPort = 9685;
	public static int protocolPort = 3562;
	public static String protocolIP = "219.223.192.45";
	
	public static int packetDataSize = 40960;
	
		
	public static Map<Integer,Boolean> icnCarry = new HashMap<Integer, Boolean>();
	public static Map<String,Neighbour>neighbours = new HashMap<>();
	
	public static void init(){
		DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();     
        DocumentBuilder builder;
        Document document = null;
		try {
			builder = builderFactory.newDocumentBuilder();
			document = builder.parse(new File("E:\\CCNNC\\2016.5.10\\CCNRouter\\conf\\conf.xml"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		protocolIP = document.getElementsByTagName("controllerIp").item(0).getTextContent();
		protocolPort = Integer.parseInt(document.getElementsByTagName("controllerPort").item(0).getTextContent());
		routerName = document.getElementsByTagName("routerName").item(0).getTextContent();
		

		icnCarry.put(0, true);
		icnCarry.put(1, true);
		icnCarry.put(2, true);
		icnCarry.put(3, true);
		icnCarry.put(4, true);
		icnCarry.put(5, true);
		
		try {
			JSONObject info = TopologyInfo.topology.getJSONObject(routerName);
			routerIp = info.getString("ip");
			ctrlRecvPort = info.getInt("id")+55000;
			JSONArray nbs = info.getJSONArray("nb");
			for(int i=0;i<nbs.length();i++){
				String nb = nbs.getString(i);
				String ip = TopologyInfo.topology.getJSONObject(nb).getString("ip");
				if(ip.trim().length()>1){
					Face f = FaceManager.addFace(ip, routerPort);
					Neighbour n = new Neighbour(nb, ip, f);
					neighbours.put(nb, n);
					System.out.println("add neighbour:"+nb+"-->"+ip);
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
}
