package com.timsnky.mymoney;

public class NavDrawerItem {
	private String title;
	private int icon;
	private boolean isSection = false;
	
	public NavDrawerItem(){}
	
	public NavDrawerItem(String title, int icon){
		this.title = title;
		this.icon = icon;
	}
	
	public NavDrawerItem(String title,boolean isSection){
		this.title = title;
		this.isSection = isSection;
	}
	
	public String getTitle(){
		return this.title;
	}
	
	public int getIcon(){
		return this.icon;
	}
		
	public boolean getSection(){
		return this.isSection;
	}
	
	public void setTitle(String title){
		this.title = title;
	}
	
	public void setIcon(int icon){
		this.icon = icon;
	}
	
	public void setSection(boolean isSection){
		this.isSection = isSection;
	}
}

