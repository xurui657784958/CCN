package cn.ccn.protocol;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cn.ccn.routing.ContentLocation;
import cn.ccn.topology.PathNode;
import cn.ccn.topology.TopyInfo;



public class ProtocolFunctions {
	TopyInfo topy;
	DatagramSocket socket;
	DatagramPacket packet;
	public ProtocolFunctions(TopyInfo topy, DatagramSocket socket) {
		this.topy = topy;
		this.socket = socket;
	}
	
	/* 1|rn=/pku/sz|contents=/sdfsdf */
	public void registerContent(JSONObject json){ //路由器名称+请求内容名称
		String rn = null;
		JSONArray cn = null;
		try {
			rn = json.getString("rn");
			cn = json.getJSONArray("contents");
			for(int i=0;i<cn.length();i++){
				ContentLocation.locations.put(cn.getString(i), rn);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		System.out.println(ContentLocation.locations.toString());
	}
	
	/* 2|rn=/pku/sz|cn=/sdfsdf|net=5 */
	public void executePathCompute(JSONObject json){ //路由器名称+请求内容名称+服务承载网号
		String rn = null;
		String cn = null;
		String net = null;
		int pid = 0;
		try {
			rn = json.getString("rn");
			cn = json.getString("cn");
			net = json.getString("net");
			pid = json.getInt("pid");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		String en = ContentLocation.locations.get(cn);
		if(en==null)return;
		List<PathNode>path = topy.computeRoutingPath(rn, en, Integer.parseInt(net));
		if(path==null)return;
		for(int i=path.size()-1;i>0;i--){
			if(i==(path.size()-1))sendFibMessage(path.get(i), path.get(i-1), cn, Integer.parseInt(net),pid,true);
			else
				sendFibMessage(path.get(i), path.get(i-1), cn, Integer.parseInt(net),pid,false);
		}
	}
	
	public void sendFibMessage(PathNode cur, PathNode to, String content,int flag, int pid, boolean si){
		JSONObject json = new JSONObject();
		try {
			json.put("from", cur.name);
			json.put("to", to.name);
			json.put("content", content+"/SN="+pid+"/F="+flag);
			json.put("id", 2);
			json.put("flag", si);
		} catch (JSONException e1) {
			e1.printStackTrace();
		}
		System.out.println(json.toString()+"-->"+cur.nodeIP+":"+cur.protocolPort);
		byte[] buf = json.toString().getBytes();
		try {
			packet = new DatagramPacket(buf, buf.length,InetAddress.getByName(cur.nodeIP),cur.protocolPort);
			socket.send(packet);
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}
	

	
}
