package com.techelevator;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

public class ParkJBCD implements ParkDAO {
	private JdbcTemplate jdbcTemplate;

	public ParkJBCD(DataSource dataSource)
	{
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	@Override
	public List<Park> getAllParks() {
	List<Park> parks = new ArrayList<Park>();
	String query = "Select name FROM park;";
	SqlRowSet results = jdbcTemplate.queryForRowSet(query);
	while (results.next()){
		Park p = mapRowToParks(results);
		parks.add(p);
	}
	return parks;
	}

	@Override
	public Park findParkById(int parkId) {
		int userchoice = parkId;

		String query = "SELECT name FROM park where park_id = " + userchoice;
		SqlRowSet results = jdbcTemplate.queryForRowSet(query);
		Park p = mapRowToParks(results);
		return p;
	}

	private Park mapRowToParks(SqlRowSet results)
	{
		Park park = new Park();
		park.setPark_id(results.getInt("park_id"));
		park.setName(results.getString("name"));
		park.setLocation(results.getString("location"));
		//park.setEstablish_date(results.getDate("establish_date")); need to implement this as well
		park.setArea(results.getInt("area"));
		park.setVisitors(results.getInt("visitors"));
		park.setDescription(results.getString("description"));


		return park;
	}
}
