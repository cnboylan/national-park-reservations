package com.techelevator;

import java.util.Date;
import java.util.List;

public interface CampsiteDAO {
	
	public List<Campsite> getAllSites();
	public Campsite findSiteById(int siteId);
	public List<Campsite> findSitesByCampgroundId(int campgroundId);
	public void createCampsite(Campsite newCampsite);
	public void updateCampsite(Campsite updatedCampsite);
	public void deleteCampsite(Campsite deletedCampsite);
	public List<Campsite> getAvailableSites(int campgroundID, Date fromDate, Date toDate);
	public void printCampsiteInfo(Campground cg, Date fromDate, Date toDate, String startDate, String endDate);
	public void printCampsiteInfoByPark(int parkID, Date fromDate, Date toDate, String startDate, String endDate);
}
