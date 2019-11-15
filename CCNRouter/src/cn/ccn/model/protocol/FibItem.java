package cn.ccn.model.protocol;

import java.util.ArrayList;
import java.util.List;


public class FibItem {
	private String name;
	private List<Integer> waitingFaces;

	
	public FibItem(String name,int faceid) {
		// TODO Auto-generated constructor stub
		this.name = name;
		this.waitingFaces = new ArrayList<Integer>();
		addWaitingFaces(faceid);

	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public List<Integer> getWaitingFaces() {
		return waitingFaces;
	}
	public void addWaitingFaces(int faceid) {
		this.waitingFaces.add(faceid);
	}
	public void delWaitingFaces(int faceid) {
		for(int i=0;i<waitingFaces.size();i++){
			if(waitingFaces.get(i) == faceid){
				waitingFaces.remove(i);
				break;
			}
		}
	}
//------------------------------test with no problem
}
