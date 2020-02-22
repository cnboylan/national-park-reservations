package com.techelevator;

public class Campsite {
	int site_id;
	int campground_id;
	int site_number;
	int maxOccupancy;
	boolean hcAccessible;
	int max_rv_length;
	boolean utilities;
	double daily_fee;
	
	public double getDaily_fee() {
		return daily_fee;
	}
	public void setDaily_fee(double daily_fee) {
		this.daily_fee = daily_fee;
	}
	public int getSite_id() {
		return site_id;
	}
	public void setSite_id(int site_id) {
		this.site_id = site_id;
	}
	public int getCampground_id() {
		return campground_id;
	}
	public void setCampground_id(int campground_id) {
		this.campground_id = campground_id;
	}
	public int getSite_number() {
		return site_number;
	}
	public void setSite_number(int site_number) {
		this.site_number = site_number;
	}
	public int getMaxOccupancy() {
		return maxOccupancy;
	}
	public void setMaxOccupancy(int maxOccupancy) {
		this.maxOccupancy = maxOccupancy;
	}
	public boolean isHcAccessible() {
		return hcAccessible;
	}
	public void setHcAccessible(boolean hcAccessible) {
		this.hcAccessible = hcAccessible;
	}
	public int getMax_rv_length() {
		return max_rv_length;
	}
	public void setMax_rv_length(int max_rv_length) {
		this.max_rv_length = max_rv_length;
	}
	public boolean isUtilities() {
		return utilities;
	}
	public void setUtilities(boolean utilities) {
		this.utilities = utilities;
	}

}
