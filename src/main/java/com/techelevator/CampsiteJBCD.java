package com.techelevator;

import java.text.DateFormatSymbols;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

public class CampsiteJBCD implements CampsiteDAO {
	
	private JdbcTemplate jdbcTemplate;
	DecimalFormat df = new DecimalFormat("#.00");

	public CampsiteJBCD (DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	@Override
	public List<Campsite> getAllSites() {
		List<Campsite> siteList = new ArrayList<Campsite>();
		String sqlGetAllSites = "SELECT * FROM site";
		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlGetAllSites);
		while(results.next()) {
			siteList.add(mapRowToSite(results));
		}
		return siteList;
	}
	
	public void printCampsiteInfo(Campground cg, Date fromDate, Date toDate, String startDate, String endDate) {
		List<Campsite> siteList = getAvailableSites(cg, fromDate, toDate);
		LocalDate startDateDate = LocalDate.parse(startDate);
		LocalDate endDateDate = LocalDate.parse(endDate);
		
		int days = (int)ChronoUnit.DAYS.between(startDateDate, endDateDate);
		
		System.out.println("Site No. | Max Occupancy | Accessible | Max RV Length | Utilities? | Total Cost");
		
		for (Campsite c : siteList) {
			String yes = "YES";
			String accessible = "NO";
			String utilities = "NO";
			
			if (c.isHcAccessible()) {
				accessible = yes;
			}	
			if (c.isUtilities()) {
				utilities = yes;
			}
			
			System.out.println(c.getSite_number() + " | " + c.getMaxOccupancy() + " | " + accessible + " | " 
								+ c.getMax_rv_length() + " | " + utilities + " | " + df.format(cg.getDaily_fee() * days));
		}	
	}
	
	public void printCampsiteInfoByPark(int parkID, Date fromDate, Date toDate, String startDate, String endDate) {
		List<Campsite> siteList = getAvailSitesByPark(parkID, fromDate, toDate);
		LocalDate startDateDate = LocalDate.parse(startDate);
		LocalDate endDateDate = LocalDate.parse(endDate);
		
		int days = (int)ChronoUnit.DAYS.between(startDateDate, endDateDate);
		
		System.out.println("Site No. | Max Occupancy | Accessible | Max RV Length | Utilities? | Total Cost");
		
		for (Campsite c : siteList) {
			String yes = "YES";
			String accessible = "NO";
			String utilities = "NO";
			
			if (c.isHcAccessible()) {
				accessible = yes;
			}	
			if (c.isUtilities()) {
				utilities = yes;
			}
			
			System.out.println(c.getSite_number() + " | " + c.getMaxOccupancy() + " | " + accessible + " | " 
								+ c.getMax_rv_length() + " | " + utilities + " | " + df.format(c.getDaily_fee() * days));
		}	
	}

	@Override
	public Campsite findSiteById(int siteId) {
		Campsite theSite = null;
		String sqlGetSiteByID = "SELECT * "+
							   "FROM site "+
							   "WHERE site_id = ?";
		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlGetSiteByID, siteId);
		if(results.next()) {
			theSite = mapRowToSite(results);
		}
		return theSite;
	}

	@Override
	public List<Campsite> findSitesByCampgroundId(int campgroundId) {
		List<Campsite> siteList = new ArrayList<Campsite>();
		String sqlGetSitesByCampgroundID = "SELECT * "+
							   			"FROM site "+
							   			"WHERE campground_id = ?";
		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlGetSitesByCampgroundID, campgroundId);
		while(results.next()) {
			siteList.add(mapRowToSite(results));
		}
		return siteList;
	}

	@Override //return value needed?
	public void createCampsite(Campsite newCampsite) {
		String sqlCreateSite = "INSERT INTO site(campground_id, site_number, max_occupancy, max_rv_length, accessible, utilities) "
								+ "VALUES(?, ?, ?, ?, ?, ?)";
		jdbcTemplate.update(sqlCreateSite, newCampsite.getCampground_id(), newCampsite.getSite_number(), newCampsite.getMaxOccupancy(), 
								newCampsite.getMax_rv_length(), newCampsite.isHcAccessible(), newCampsite.isUtilities());
	}

	@Override //return value?
	public void updateCampsite(Campsite updatedCampsite) {
		String sqlUpdateSite = "UPDATE site "
				+ "SET campground_id = ?, site_number = ?, max_occupancy = ?, max_rv_length = ?, accessible = ?, utilities = ?)";
		jdbcTemplate.update(sqlUpdateSite, updatedCampsite.getCampground_id(), updatedCampsite.getSite_number(), updatedCampsite.getMaxOccupancy(), 
				updatedCampsite.getMax_rv_length(), updatedCampsite.isHcAccessible(), updatedCampsite.isUtilities());
	}

	@Override //return value?
	public void deleteCampsite(Campsite deletedCampsite) {
		String sqlDeleteSite = "DELETE FROM site WHERE site_id = ?";
		jdbcTemplate.update(sqlDeleteSite, deletedCampsite.getSite_id());
	}
	
	@SuppressWarnings("deprecation") //ok to use getMonth method??
	public List<Campsite> getAvailableSites(Campground cg, Date fromDate, Date toDate) {
		List<Campsite> siteList = new ArrayList<Campsite>();
		String sqlGetOpenMonths = "SELECT open_from_mm, open_to_mm FROM campground WHERE campground_id = ?";
		SqlRowSet months = jdbcTemplate.queryForRowSet(sqlGetOpenMonths, cg.getCampground_id());
		if(months.next()) {
			int open = Integer.parseInt(months.getString("open_from_mm"));
			int close = Integer.parseInt(months.getString("open_to_mm"));
			if (fromDate.getMonth() <= open || toDate.getMonth() <= open || fromDate.getMonth() >= close || toDate.getMonth() >= close) {
				return siteList;
			}
		}
		String sqlGetAvailSites = "SELECT DISTINCT site_number, site.site_id, site.campground_id, max_occupancy, accessible, max_rv_length, utilities, daily_fee FROM site "
								+ "LEFT JOIN reservation ON reservation.site_id = site.site_id "
								+ "WHERE site.campground_id = ? "
								+ "AND site.site_id NOT IN(SELECT site_id FROM reservation) "
								+ "OR (from_date NOT BETWEEN ? AND ?) "
								+ "OR (to_date NOT BETWEEN ? AND ?) "
								+ "OR ((from_date NOT BETWEEN ? AND ?) "
								+ "AND (to_date NOT BETWEEN ? AND ?)) "
								+ "ORDER BY site_number LIMIT 5";
		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlGetAvailSites, cg.getCampground_id(), fromDate, toDate, fromDate, toDate, fromDate, toDate, fromDate, toDate);
		while(results.next()) {
			siteList.add(mapRowToSite(results));
		}
		return siteList;
	}

	public List<Campsite> getAvailSitesByPark(int parkId, Date fromDate, Date toDate) {
		List<Campsite> siteList = new ArrayList<Campsite>();
		//need to validate the campground is open
		String sqlGetAvailSitesByPark = "SELECT DISTINCT site_number, site.site_id, site.campground_id, max_occupancy, accessible, max_rv_length, utilities, daily_fee FROM site "
								+ "LEFT JOIN reservation ON reservation.site_id = site.site_id "
								+ "JOIN campground ON campground.campground_id = site.campground_id "
								+ "WHERE park_id = ? "
								+ "AND site.site_id NOT IN(SELECT site_id FROM reservation) "
								+ "OR (from_date NOT BETWEEN ? AND ?) "
								+ "OR (to_date NOT BETWEEN ? AND ?) "
								+ "OR ((from_date NOT BETWEEN ? AND ?) "
								+ "AND (to_date NOT BETWEEN ? AND ?)) "
								+ "ORDER BY site_number LIMIT 5";
		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlGetAvailSitesByPark, parkId, fromDate, toDate, fromDate, toDate, fromDate, toDate, fromDate, toDate);
		while(results.next()) {
			siteList.add(mapRowToSite(results));
		}		
		return siteList;
	}
	
//	private long getNextSiteId() {
//		SqlRowSet nextIdResult = jdbcTemplate.queryForRowSet("SELECT nextval('seq_site_id')");
//		if(nextIdResult.next()) {
//			return nextIdResult.getLong(1);
//		} else {
//			throw new RuntimeException("Something went wrong while getting an id for the new campsite");
//		}
//	}
	
	private Campsite mapRowToSite(SqlRowSet results) {
		Campsite newSite;
		newSite = new Campsite();
		newSite.setSite_id(results.getInt("site_id"));
		newSite.setCampground_id(results.getInt("campground_id"));
		newSite.setSite_number(results.getInt("site_number"));
		newSite.setMaxOccupancy(results.getInt("max_occupancy"));
		newSite.setHcAccessible(results.getBoolean("accessible"));
		newSite.setMax_rv_length(results.getInt("max_rv_length"));
		newSite.setUtilities(results.getBoolean("utilities"));
		newSite.setDaily_fee(results.getDouble("daily_fee"));
		return newSite;
	}

}
