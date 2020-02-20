package com.techelevator;

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
	public List<Campground> getAllCampgrounds() { // I don't think we need a get ALL campgrounds method but here it is anyway
		List<Campground> campgrounds = new ArrayList<Campground>();
		String query = "SELECT name FROM campground;";
		SqlRowSet results = jdbcTemplate.queryForRowSet(query);
		while(results.next()){
			Campground c = mapRowToCampground(results);
			campgrounds.add(c);
		}
		return campgrounds;
	}

	@Override
	public Campground findCampgroundById(int campgroundId) {

		int userChoice = campgroundId;

		String query = "Select * FROM campground WHERE campground_id =" + userChoice;
		SqlRowSet results = jdbcTemplate.queryForRowSet(query);
		Campground c = mapRowToCampground(results);
		return c;
	}

	@Override
	public List<Campground> findCampgroundsByParkId(int parkId) {

		String userChoice = null;

		List<Campground> campgrounds = new ArrayList<Campground>();
		String query = "SELECT * FROM campground where park_id =" + userChoice +";";

		SqlRowSet results = jdbcTemplate.queryForRowSet(query);
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
		//campground.setOpen_from_mm(results.getString("open_from_mm")); // we need to implement these methods
		//campground.setOpen_to_mm(results.getString("open_to_mm")); // at some point
		campground.setDaily_fee(results.getDouble("daily_fee"));

		return campground;
	}


}
