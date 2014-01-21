package com.horizonx.simplesaving;

public class NameValuePair{
	private String varName;
	private int varValue;
	
	public NameValuePair(String varNames,int varValues){
		super();
		this.varName=varNames;
		this.varValue=varValues;
	}
	public String getName(){
		return varName;
	}
	public int getValue(){
		return varValue;
	}
}
