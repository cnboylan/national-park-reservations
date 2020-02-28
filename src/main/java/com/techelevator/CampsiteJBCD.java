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

public class CampsiteJBCD extends Campsite implements CampsiteDAO  {
	
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
		List<Campsite> siteList = getAvailableSites(cg.getCampground_id(), fromDate, toDate);
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
		
		System.out.println("Campground | Site No. | Max Occupancy | Accessible | Max RV Length | Utilities? | Total Cost");
		
		String cgName = null;
		
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
			
			String sqlGetCgName = "SELECT name FROM campground WHERE campground_id = ?";
			SqlRowSet results = jdbcTemplate.queryForRowSet(sqlGetCgName, c.getCampground_id());
			if(results.next()) {
				cgName = results.getString("name");
			}
			
			System.out.println(cgName + " | " + c.getSite_number() + " | " + c.getMaxOccupancy() + " | " + accessible + " | " 
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

	@Override
	public void createCampsite(Campsite newCampsite) {
		String sqlCreateSite = "INSERT INTO site(campground_id, site_number, max_occupancy, max_rv_length, accessible, utilities) "
								+ "VALUES(?, ?, ?, ?, ?, ?)";
		jdbcTemplate.update(sqlCreateSite, newCampsite.getCampground_id(), newCampsite.getSite_number(), newCampsite.getMaxOccupancy(), 
								newCampsite.getMax_rv_length(), newCampsite.isHcAccessible(), newCampsite.isUtilities());
	}

	@Override
	public void updateCampsite(Campsite updatedCampsite) {
		String sqlUpdateSite = "UPDATE site "
				+ "SET campground_id = ?, site_number = ?, max_occupancy = ?, max_rv_length = ?, accessible = ?, utilities = ?)";
		jdbcTemplate.update(sqlUpdateSite, updatedCampsite.getCampground_id(), updatedCampsite.getSite_number(), updatedCampsite.getMaxOccupancy(), 
				updatedCampsite.getMax_rv_length(), updatedCampsite.isHcAccessible(), updatedCampsite.isUtilities());
	}

	@Override
	public void deleteCampsite(Campsite deletedCampsite) {
		String sqlDeleteSite = "DELETE FROM site WHERE site_id = ?";
		jdbcTemplate.update(sqlDeleteSite, deletedCampsite.getSite_id());
	}
	
	@SuppressWarnings("deprecation")
	public List<Campsite> getAvailableSites(int campground_ID, Date fromDate, Date toDate) {
		List<Campsite> siteList = new ArrayList<Campsite>();
		String sqlGetOpenMonths = "SELECT CAST(open_from_mm AS INT), CAST(open_to_mm AS INT) FROM campground WHERE campground_id = ?";
		SqlRowSet months = jdbcTemplate.queryForRowSet(sqlGetOpenMonths, campground_ID);
		if(months.next()) {
			int open = months.getInt("open_from_mm");
			int close = months.getInt("open_to_mm");
			if (fromDate.getMonth() + 1 < open || toDate.getMonth() + 1 < open || fromDate.getMonth() + 1 > close || toDate.getMonth() + 1 > close) {
				return siteList;
			}
		}
		String sqlGetAvailSites = "SELECT DISTINCT site_number, site.site_id, site.campground_id, max_occupancy, accessible, max_rv_length, utilities, daily_fee FROM site "
								+ "LEFT JOIN reservation ON reservation.site_id = site.site_id "
								+ "JOIN campground ON campground.campground_id = site.campground_id "
								+ "WHERE site.campground_id = ? "
								+ "AND (site.site_id NOT IN (SELECT site_id FROM reservation) "
								+ "OR site.site_id NOT IN (SELECT site.site_id FROM site "
								+ "JOIN reservation ON reservation.site_id = site.site_id "
								+ "WHERE to_date > ? AND from_date < ?)) "
								+ "ORDER BY site_number LIMIT 5";
		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlGetAvailSites, campground_ID, fromDate, toDate);
		while(results.next()) {
			siteList.add(mapRowToSite(results));
		}
		return siteList;
	}

	@SuppressWarnings("deprecation")
	public List<Campsite> getAvailSitesByPark(int parkId, Date fromDate, Date toDate) {
		List<Integer> cgList = new ArrayList<Integer>();
		List<Campsite> siteList = new ArrayList<Campsite>();
		
		String sqlGetCampgroundsByPark = "SELECT campground_id FROM campground WHERE park_id = ?";
		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlGetCampgroundsByPark, parkId);
		cgList = mapCampgroundsToPark(results);
		
		for (Integer cg : cgList) {
			siteList.addAll(getAvailableSites(cg, fromDate, toDate));
		}
		
		return siteList;
	}
	
	protected int getNextSiteId() {
		SqlRowSet nextIdResult = jdbcTemplate.queryForRowSet("SELECT nextval('site_site_id_seq')");
		if(nextIdResult.next()) {
			return nextIdResult.getInt(1);
		} else {
			throw new RuntimeException("Something went wrong while getting an id for the new campsite");
		}
	}
	
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
	
	private List<Integer> mapCampgroundsToPark(SqlRowSet results) {
		List<Integer> campgroundList = new ArrayList<Integer>();
		while(results.next()) {
			campgroundList.add(results.getInt("campground_id"));
		}
		return campgroundList;
	}

}
