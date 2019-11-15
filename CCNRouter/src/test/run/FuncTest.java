package test.run;

import cn.ccn.model.ContentObject;
import cn.ccn.routing.CheckForFile;
import cn.ccn.routing.ContentManager;

public class FuncTest {
	public static void main(String[] args) {
		ContentObject co = new ContentObject();
		for(int i=0;i<1500;i++){
			ContentManager.addContent("/t"+i, co);
		}
	}
}
