package cn.ccn.model.protocol;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Timer;



public class PitItem {
	private String name;
	private Set<Integer> waitingFaces;
	private long lifetime;
	private Timer timer;
	
	public PitItem(String name,int faceid) {
		// TODO Auto-generated constructor stub
		this.name = name;
		this.waitingFaces = new HashSet<Integer>();
		addWaitingFaces(faceid);
		lifetime = 15000;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public Set<Integer> getWaitingFaces() {
		return waitingFaces;
	}
	public void addWaitingFaces(int faceid) {
		this.waitingFaces.add(faceid);
	}
	public void delWaitingFaces(int faceid) {
		waitingFaces.remove(faceid);
	}
	
	public void startTimer(){
		timer = new Timer();
		timer.schedule(new PitTask(name),lifetime);
	}
	
	public void updateTimer(){
		cancleTimer();
		startTimer();
	}
	
	public void cancleTimer(){
		timer.cancel();
	}
	
	
}
