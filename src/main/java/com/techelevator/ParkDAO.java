package com.techelevator;

import java.util.List;

public interface ParkDAO {
	
	public List<Park> getAllParks();
	public Park findParkById(int parkId);
	public Park findParkByCampgroundId(int campgroundId);
	public void createPark(Park newPark);
	public void updatePark(Park updatedPark);
	public void deletePark(Park deletedPark);
}
