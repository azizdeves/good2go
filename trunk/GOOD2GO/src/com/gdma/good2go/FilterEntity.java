package com.gdma.good2go;

public class FilterEntity {
	
	private String Animal="";
	private String Children="";
	private String Disabled="";
	private String Env="";
	private String Elderly="";
	private String Special="";
	private int radius=20;
	private int duration=2;

	public void filter(String Animal,String Children,String Disabled,String Env,String Elderly,String Special){
		this.Animal=Animal;
		this.Children=Children;
		this.Disabled=Disabled;
		this.Env=Env;
		this.Elderly=Elderly;
		this.Special=Special;
	}


}