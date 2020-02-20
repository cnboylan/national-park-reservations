package com.techelevator;

public interface ParkDAO {
	
	public List<Park> getAllParks();
	public Park findParkById(int parkId);

}
