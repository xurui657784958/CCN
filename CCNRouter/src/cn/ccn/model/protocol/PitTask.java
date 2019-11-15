package cn.ccn.model.protocol;

import java.util.TimerTask;

import cn.ccn.routing.PitManager;

public class PitTask extends TimerTask{
	String name;
	public PitTask(String name){
		this.name = name;
	}
	public void run() {
		PitManager.delPit(name);
	}
}
