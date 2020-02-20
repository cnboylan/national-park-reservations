package com.techelevator;

public interface CampgroundDAO {

	public List<Campground> getAllCampgrounds();
	public Campground findCampgroundById(int campgroundId);
	
}
