package com.gdma.good2go.utils;

public class KeyManager {
	
	static private KeyManager me=null;
	private long key;
	
	public static KeyManager init(){
		if (me==null)
			me= new KeyManager();
		return me;
		
	}
	
	private KeyManager(){
		key=0;
	}
	
	public long getKey(){
		return key++;
	}

}
