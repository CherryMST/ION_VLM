package com.qx.framelib.utlis;

public class CommonUtil {
	
	private static int mUniqueId = 1;
	
	public synchronized static int getUniqueId() {
		return mUniqueId++;
	}

}
