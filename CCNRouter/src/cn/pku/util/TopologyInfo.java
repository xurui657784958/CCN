package cn.pku.util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class TopologyInfo {
	public static JSONObject topology;
	static {
		topology = new JSONObject();
		
		try {
			JSONObject n = new JSONObject();
			String routerName = "/router/sz/n0";
			n.put("id", 0); n.put("rn", routerName); n.put("ip", "219.223.192.45");
			JSONArray nbs = new JSONArray();
			nbs.put("/router/pku/n2"); nbs.put("/router/main/n2"); nbs.put("/router/sz/n1"); nbs.put("/router/sz/n2");
			n.put("nb", nbs);
			topology.put(routerName,n);
			/*------------------------------*/
			n = new JSONObject();
			routerName = "/router/sz/n1";
			n.put("id", 1); n.put("rn", routerName); n.put("ip", "219.223.199.165");
			nbs = new JSONArray();
			nbs.put("/router/sz/n0"); nbs.put("/router/sz/n3");nbs.put("/router/sz/n6");nbs.put("/router/sz/n5");
			n.put("nb", nbs);
			topology.put(routerName,n);
			/*------------------------------*/
			n = new JSONObject();
			routerName = "/router/sz/n2";
			n.put("id", 2); n.put("rn", routerName); n.put("ip", "219.223.199.166");
			nbs = new JSONArray();
			nbs.put("/router/sz/n3"); nbs.put("/router/sz/n0"); nbs.put("/router/hit/n2");
			n.put("nb", nbs);
			topology.put(routerName,n);
			/*------------------------------*/
			n = new JSONObject();
			routerName = "/router/sz/n3";
			n.put("id", 3); n.put("rn", routerName); n.put("ip", "219.223.199.161");
			nbs = new JSONArray();
			nbs.put("/router/sz/n1"); nbs.put("/router/sz/n4");nbs.put("/router/sz/n2");
			n.put("nb", nbs);
			topology.put(routerName,n);
			/*------------------------------*/
			n = new JSONObject();
			routerName = "/router/sz/n4";
			n.put("id", 4); n.put("rn", routerName); n.put("ip", "219.223.199.163");
			nbs = new JSONArray();
			nbs.put("/router/sz/n3"); nbs.put("/router/sz/n5");nbs.put("/router/sz/n6");
			n.put("nb", nbs);
			topology.put(routerName,n);
			/*------------------------------*/
			n = new JSONObject();
			routerName = "/router/sz/n5";
			n.put("id", 5); n.put("rn", routerName); n.put("ip", "219.223.199.167");
			nbs = new JSONArray();
			nbs.put("/router/main/n6"); nbs.put("/router/sz/n1");nbs.put("/router/sz/n4");
			n.put("nb", nbs);
			topology.put(routerName,n);
			/*------------------------------*/
			n = new JSONObject();
			routerName = "/router/sz/n6";
			n.put("id", 6); n.put("rn", routerName); n.put("ip", "219.223.199.168");
			nbs = new JSONArray();
			nbs.put("/router/sz/n2"); nbs.put("/router/sz/n4");
			n.put("nb", nbs);
			topology.put(routerName,n);
			/*------------------------------*/
			n = new JSONObject();
			routerName = "/router/pku/n1";
			n.put("id", 7); n.put("rn", routerName); n.put("ip", "219.223.196.226");
			nbs = new JSONArray();
			nbs.put("/router/pku/n2");nbs.put("/router/pku/n3"); nbs.put("/router/main/n2");
			n.put("nb", nbs);
			topology.put(routerName,n);
			/*------------------------------*/
			n = new JSONObject();
			routerName = "/router/pku/n2";
			n.put("id", 8); n.put("rn", routerName); n.put("ip", "219.223.196.227");
			nbs = new JSONArray();
			nbs.put("/router/pku/n1");nbs.put("/router/pku/n3"); nbs.put("/router/hit/n1");nbs.put("/router/sz/n0");
			n.put("nb", nbs);
			topology.put(routerName,n);
			/*------------------------------*/
			n = new JSONObject();
			routerName = "/router/pku/n3";
			n.put("id", 9); n.put("rn", routerName); n.put("ip", "219.223.196.229");
			nbs = new JSONArray();
			nbs.put("/router/pku/n1");nbs.put("/router/pku/n2");
			n.put("nb", nbs);
			topology.put(routerName,n);
			/*------------------------------*/
			n = new JSONObject();
			routerName = "/router/hit/n1";
			n.put("id", 10); n.put("rn", routerName); n.put("ip", "");
			nbs = new JSONArray();
			nbs.put("/router/pku/n2"); nbs.put("/router/hit/n2"); nbs.put("/router/hit/n3");
			n.put("nb", nbs);
			topology.put(routerName,n);
			/*------------------------------*/
			n = new JSONObject();
			routerName = "/router/hit/n2";
			n.put("id", 11); n.put("rn", routerName); n.put("ip", "");
			nbs = new JSONArray();
			nbs.put("/router/hit/n1"); nbs.put("/router/hit/n3"); nbs.put("/router/sz/n2");
			n.put("nb", nbs);
			topology.put(routerName,n);
			/*------------------------------*/
			n = new JSONObject();
			routerName = "/router/hit/n3";
			n.put("id", 12); n.put("rn", routerName); n.put("ip", "");
			nbs = new JSONArray();
			nbs.put("/router/hit/n1"); nbs.put("/router/hit/n2");
			n.put("nb", nbs);
			topology.put(routerName,n);
			/*------------------------------*/
			n = new JSONObject();
			routerName = "/router/main/n1";
			n.put("id", 13); n.put("rn", routerName); n.put("ip", "");
			nbs = new JSONArray();
			nbs.put("/router/main/n2"); nbs.put("/router/main/n5");
			n.put("nb", nbs);
			topology.put(routerName,n);
			/*------------------------------*/
			n = new JSONObject();
			routerName = "/router/main/n2";
			n.put("id", 14); n.put("rn", routerName); n.put("ip", "");
			nbs = new JSONArray();
			nbs.put("/router/pku/n1"); nbs.put("/router/sz/n0"); nbs.put("/router/main/n4"); nbs.put("/router/main/n6");
			n.put("nb", nbs);
			topology.put(routerName,n);
			/*------------------------------*/
			n = new JSONObject();
			routerName = "/router/main/n3";
			n.put("id", 15); n.put("rn", routerName); n.put("ip", "");
			nbs = new JSONArray();
			nbs.put("/router/main/n4");
			n.put("nb", nbs);
			topology.put(routerName,n);
			/*------------------------------*/
			n = new JSONObject();
			routerName = "/router/main/n4";
			n.put("id", 16); n.put("rn", routerName); n.put("ip", "");
			nbs = new JSONArray();
			nbs.put("/router/main/n3"); nbs.put("/router/main/n5"); nbs.put("/router/main/n6"); nbs.put("/router/main/n2");
			n.put("nb", nbs);
			topology.put(routerName,n);
			/*------------------------------*/
			n = new JSONObject();
			routerName = "/router/main/n5";
			n.put("id", 17); n.put("rn", routerName); n.put("ip", "");
			nbs = new JSONArray();
			nbs.put("/router/main/n1"); nbs.put("/router/main/n4"); nbs.put("/router/main/n6");
			n.put("nb", nbs);
			topology.put(routerName,n);
			/*------------------------------*/
			n = new JSONObject();
			routerName = "/router/main/n6";
			n.put("id", 18); n.put("rn", routerName); n.put("ip", "");
			nbs = new JSONArray();
			nbs.put("/router/main/n2"); nbs.put("/router/main/n4"); nbs.put("/router/main/n5"); nbs.put("/router/sz/n5");
			n.put("nb", nbs);
			topology.put(routerName,n);
				
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		System.out.println(TopologyInfo.topology.toString());
	}
}
