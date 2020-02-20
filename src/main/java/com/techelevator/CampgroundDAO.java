package com.techelevator;

import java.util.List;

public interface CampgroundDAO {

	public List<Campground> getAllCampgrounds();
	public Campground findCampgroundById(int campgroundId);
	public List<Campground> findCampgroundsByParkId(int parkId);
	public void createCampground(Campground newCampground);
	public void updateCampground(Campground updatedCampground);
	public void deleteCampground(Campground deletedCampground);
	
}
