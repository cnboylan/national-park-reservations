package com.techelevator;

import java.util.List;

public interface CampsiteDAO {
	
	public List<Campsite> getAllSites();
	public Campsite findSiteById(int siteId);
	public List<Campsite> findSitesByCampgroundId(int campgroundId);
	
}
