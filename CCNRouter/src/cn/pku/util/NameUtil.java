package cn.pku.util;

public class NameUtil {
	public static String contentStoreName(String name,int pid){
		return name+"/SN="+pid;
	}
	
	public static String distinctName(String name,int pid, int flag){
		return name+"/SN="+pid+"/F="+flag;
	}
}
