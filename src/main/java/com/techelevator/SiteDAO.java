package com.techelevator;

public interface SiteDAO {
	
	public List<Site> getAllSites();
	public Site findSiteById(int siteId);
}
