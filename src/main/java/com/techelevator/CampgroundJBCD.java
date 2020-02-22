package com.techelevator;
import java.text.DateFormatSymbols;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

public class CampgroundJBCD extends Campground implements CampgroundDAO {

	private JdbcTemplate jdbcTemplate;

	public CampgroundJBCD (DataSource dataSource){
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	public CampgroundJBCD() {

	}


	@Override
	public List<Campground> getAllCampgrounds() { 
		List<Campground> campgrounds = new ArrayList<Campground>();
		String query = "SELECT name FROM campground;";
		SqlRowSet results = jdbcTemplate.queryForRowSet(query);
		while(results.next()){
			Campground c = mapRowToCampground(results);
			campgrounds.add(c);
		}
		return campgrounds;
	}
	
	public void printCampgroundInfo(int parkChoice) {
		List<Campground> cList = findCampgroundsByParkId(parkChoice);
		
		for (Campground c : cList) {
			String monthOpen = new DateFormatSymbols().getMonths()[Integer.parseInt(c.getMonthOpen()) -1];
			String monthClose = new DateFormatSymbols().getMonths()[Integer.parseInt(c.getMonthClose()) -1];
			
			
			System.out.println(c.getCampground_id() + " " + c.getName() + " " + monthOpen + " " + monthClose + " " + c.getDaily_fee());
		}
		
	}

	@Override
	public Campground findCampgroundById(int campgroundId) {
		Campground c = null;
		String query = "Select * FROM campground WHERE campground_id = ?";
		SqlRowSet results = jdbcTemplate.queryForRowSet(query, campgroundId);
		if (results.next()) {
			c = mapRowToCampground(results);
		}
		return c;
	}

	@Override
	public List<Campground> findCampgroundsByParkId(int parkId) {
		List<Campground> campgrounds = new ArrayList<Campground>();
		String query = "SELECT * FROM campground where park_id = ?;";
		SqlRowSet results = jdbcTemplate.queryForRowSet(query, parkId);
		//System.out.println("Please select a campground from the following list: "); Not sure if we'll use this til we get to
		// the main menu.
		while (results.next()){
			Campground c = mapRowToCampground(results);
			campgrounds.add(c);
		}
		return campgrounds;

	}
	private Campground mapRowToCampground(SqlRowSet results)
	{
		Campground campground = new Campground();
		campground.setCampground_id(results.getInt("campground_id"));
		campground.setName(results.getString("name"));
		campground.setPark_id(results.getInt("park_id"));
		campground.setMonthOpen(results.getString("open_from_mm")); // we need to implement these methods
		campground.setMonthClose(results.getString("open_to_mm")); // at some point
		campground.setDaily_fee(results.getDouble("daily_fee"));

		return campground;
	}

	@Override
	public void createCampground(Campground newCampground) {
		// TODO Auto-generated method stub
	
		
	}

	@Override
	public void updateCampground(Campground updatedCampground) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteCampground(Campground deletedCampground) {
		// TODO Auto-generated method stub
		
	}
	protected int getNextId() {
	SqlRowSet nextIdResult = jdbcTemplate.queryForRowSet("SELECT nextval('campground_campground_id_seq')");
	if(nextIdResult.next()) {
		return nextIdResult.getInt(1);
	} else {
		throw new RuntimeException("Something went wrong while getting an id for the new campsite");
	}
}

}
