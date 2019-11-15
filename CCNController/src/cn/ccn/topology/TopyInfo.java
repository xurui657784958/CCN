package cn.ccn.topology;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class TopyInfo {
	final int NODELENTH;
	final int MAXDELAY = 9999;
	public Map<String,NodeInfo>nodes = null;
	public String[] nodesNameMapper;
	
	private int [][] linksMatrix;
//	private int [][] mnl ={{0,1,16,MAXDELAY,MAXDELAY,MAXDELAY,MAXDELAY},
//			{1,0,MAXDELAY,5,MAXDELAY,10,MAXDELAY},
//			{16,MAXDELAY,0,5,MAXDELAY,MAXDELAY,8},
//			{MAXDELAY,5,5,0,3,MAXDELAY,MAXDELAY},
//			{MAXDELAY,MAXDELAY,MAXDELAY,3,0,20,2},
//			{MAXDELAY,10,MAXDELAY,MAXDELAY,20,0,MAXDELAY},
//			{MAXDELAY,MAXDELAY,8,MAXDELAY,2,MAXDELAY,0}};
	
	private int [][] mnl;
			
	
	
	public TopyInfo(){
		
		NODELENTH = NetInfo.topology.length();
		linksMatrix = new int[NODELENTH][NODELENTH];
		mnl = new int[NODELENTH][NODELENTH];
		nodes= new HashMap<String,NodeInfo>();
		nodesNameMapper = new String[NODELENTH];
		
		for(int i=0;i<NODELENTH;i++)
			for(int j=0;j<NODELENTH;j++){
				mnl[i][j] = 9999;
				linksMatrix[i][j] = 9999;
			}
		
		try {		
			Iterator<String>it = NetInfo.topology.keys();
			while(it.hasNext()){
				String nodeName = it.next();
				JSONObject node = NetInfo.topology.getJSONObject(nodeName);
				int id = node.getInt("id");
				nodesNameMapper[id] = nodeName;
				nodes.put(nodeName, new NodeInfo(id, nodeName, node.getString("ip"), 55000+id));
			}
		
		
			for(int i=0;i<nodesNameMapper.length;i++){
				JSONObject from = NetInfo.topology.getJSONObject(nodesNameMapper[i]);
				JSONObject to = null;
				JSONArray nbs = from.getJSONArray("nb");
				for(int j=0;j<nbs.length();j++){
					to = NetInfo.topology.getJSONObject(nbs.getString(j));
					if(to.getString("ip").length()>0){
						mnl[from.getInt("id")][to.getInt("id")]=1;
						linksMatrix[from.getInt("id")][to.getInt("id")]=1;
					}
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		System.out.println(nodesNameMapper);
		// configure node information
//		nodes.put("/router/sz/n0", new NodeInfo(0, "/router/sz/n0", "219.223.194.80", 55000, 3));
//		nodes.put("/router/sz/n1", new NodeInfo(1, "/router/sz/n1", "219.223.193.143", 55001, 3));
//		nodes.put("/router/sz/n2", new NodeInfo(2, "/router/sz/n2", "219.223.199.166", 55002, 1));
//		nodes.put("/router/sz/n3", new NodeInfo(3, "/router/sz/n3", "219.223.199.161", 55003, 1));
//		nodes.put("/router/sz/n4", new NodeInfo(4, "/router/sz/n4", "219.223.199.163", 55004, 3));
//		nodes.put("/router/sz/n5", new NodeInfo(5, "/router/sz/n5", "219.223.199.167", 55005, 3));
//		nodes.put("/router/sz/n6", new NodeInfo(6, "/router/sz/n6", "219.223.199.168", 55006, 3));
	}
	
	
	private int[][] getLatestLinksMatrix(){
		int[][] result = new int[NODELENTH][NODELENTH];
		for(int i=0;i<NODELENTH;i++)
			for(int j=0;j<NODELENTH;j++)result[i][j] = linksMatrix[i][j];
		return result;
	}
	
	/*routing compute*/
	public List<PathNode> computeRoutingPath(String startNodeName, String endNodeName, int net){
		int[][] tempMatrix = getLatestLinksMatrix();
		int s = nodes.get(startNodeName).id;
		int e = nodes.get(endNodeName).id;
		
		int[] path = DijsktraPathDetail(tempMatrix, s, e); //(0->4)  -1 0 3 1 3 1 4
		List<PathNode>result = new ArrayList<>();
		
		while(e!=-1){
			PathNode pathNode = new PathNode(nodesNameMapper[e], null, 0, 0);
			result.add(pathNode);
			e = path[e];
		}
		return result;
	}
	
	/*tools*/
	private int[] DijsktraPathDetail(int[][] weight,int start, int end){
        int n = weight.length;        
        int[] shortPath = new int[n];    
        int[] visited = new int[n];        
        int[] preNode = new int[n];
        for(int i=0;i<n;i++){
        	shortPath[i] = 99999;
        	preNode[i] = -1;
        	
        }
        shortPath[start] = 0;     
        int pos = -1, posMin = 99999;
        int k = start;
        
        for(int count = 0;count <= n - 1;count++){
        	visited[k] = 1;
        	for(int i = 0;i < n;i++){
        		if(visited[i] == 0 && shortPath[k]+weight[k][i]<shortPath[i]){
        			shortPath[i] = shortPath[k]+weight[k][i];
        			preNode[i] = k;
        		}
        	}
        	pos = -1;
        	posMin = 99999;
        	for(int i = 0;i < n;i++){
        		if(visited[i] == 0){
        			if(shortPath[i]<posMin){
        				pos = i;
        				posMin = shortPath[i];
        			}
        		}
        	}
        	k=pos;
        }
        return preNode;
    }
}



