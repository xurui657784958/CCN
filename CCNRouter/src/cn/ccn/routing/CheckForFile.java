package cn.ccn.routing;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.security.auth.login.Configuration;
import javax.swing.text.AbstractDocument.Content;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cn.ccn.ctrl.CtrlProtocol;
import cn.ccn.model.ContentObject;
import cn.pku.util.GlobalVar;
class FileInfo{
	String fileName[]=null;
	long fileLen[] = null;
}


public class CheckForFile {
	public static final String dirPath = "CCNRouter\\content";
	
	public static void autoCheckFiles(){
		FileInfo filesInfo = getStoredFile();
		register(FilesToString(filesInfo.fileName,filesInfo.fileLen));
	}
	
	private static FileInfo getStoredFile(){  //识别文件的名称和大小
		String[] result = null;
		long[] fileLen = null;
		File file = new File(dirPath);
		if(!file.exists())return null;
		File[] tempList = file.listFiles();
		result = new String[tempList.length];
		fileLen=new long[tempList.length];
		for(int i=0;i<tempList.length;i++){
			result[i] = tempList[i].getName();
			fileLen[i] = tempList[i].length();
		}
		FileInfo fileInfos = new FileInfo();
		fileInfos.fileName = result;
		fileInfos.fileLen = fileLen;
		return fileInfos;
	}
	
	private static void register(JSONObject json){
		new CtrlProtocol().transferNameLSA(json);
	}
	
	private static JSONObject FilesToString(String[] files,long[] filesLen){
		if(files == null||files.length==0) return null;
		StringBuffer sbf = new StringBuffer();
		JSONObject json = new JSONObject();
		JSONArray jarray = new JSONArray();
		try {
			for(int i = 0; i<files.length;i++){
				jarray.put(files[i]);
			}
			json.put("contents", jarray);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return json;
	}
	
	public static ContentObject getContentFromFile(String fileName, int pid ,int size){
		File file = new File(dirPath+File.separator+fileName);
		if(!file.exists())return null;
		byte[] buf = null;
		long fileLength = file.length();
		long skip = (pid-1)*size;
		if((fileLength-skip)>=size)buf = new byte[size];
		else
			buf = new byte[(int)(fileLength-skip)];
		int total = (int) (file.length()/size)+1;
		try {
			InputStream in = new FileInputStream(file);
			in.skip(skip);
			in.read(buf);
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		ContentObject co = new ContentObject(fileName, 0, total, pid, buf);
		//System.out.println(total+" "+new String(buf));
		return co;
	}
}
