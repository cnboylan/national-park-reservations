package com.techelevator;

import java.util.List;

public interface CampsiteDAO {
	
	public List<Campsite> getAllSites();
	public Campsite findSiteById(int siteId);
	public List<Campsite> findSitesByCampgroundId(int campgroundId);
	public void createCampsite(Campsite newCampsite);
	public void updateCampsite(Campsite updatedCampsite);
	public void deleteCampsite(Campsite deletedCampsite);
}
