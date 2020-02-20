package com.techelevator;

public class Campground {
int campground_id;
int park_id;
String name;
String monthOpen; // Two digit string parse to int 
String monthClose; // Two digit string parse to int 
Double daily_fee; // comes in as money data type parse to double?
public int getCampground_id() {
	return campground_id;
}
public void setCampground_id(int campground_id) {
	this.campground_id = campground_id;
}
public int getPark_id() {
	return park_id;
}
public void setPark_id(int park_id) {
	this.park_id = park_id;
}
public String getName() {
	return name;
}
public void setName(String name) {
	this.name = name;
}
public String getMonthOpen() {
	return monthOpen;
}
public void setMonthOpen(String monthOpen) {
	this.monthOpen = monthOpen;
}
public String getMonthClose() {
	return monthClose;
}
public void setMonthClose(String monthClose) {
	this.monthClose = monthClose;
}
public Double getDaily_fee() {
	return daily_fee;
}
public void setDaily_fee(Double daily_fee) {
	this.daily_fee = daily_fee;
}


}
