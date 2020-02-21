package com.techelevator;

import java.sql.Date;
import java.time.LocalDate;

public class Park {
int park_id;
String name;
String location;
LocalDate date_est;
int area;
int visitors;
String description;
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
public String getLocation() {
	return location;
}
public void setLocation(String location) {
	this.location = location;
}
public LocalDate getDate_est() {
	return date_est;
}
public void setDate_est(LocalDate date_est) {
	this.date_est = date_est;
}
public int getArea() {
	return area;
}
public void setArea(int area) {
	this.area = area;
}
public int getVisitors() {
	return visitors;
}
public void setVisitors(int visitors) {
	this.visitors = visitors;
}
public String getDescription() {
	return description;
}
public void setDescription(String description) {
	this.description = description;
}



}
