package com.techelevator;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

public class ParkJBCD extends Park implements ParkDAO {
	private JdbcTemplate jdbcTemplate;

	public ParkJBCD(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	@Override
	public List<Park> getAllParks() {
		List<Park> parks = new ArrayList<Park>();
		String query = "Select * FROM park;";
		SqlRowSet results = jdbcTemplate.queryForRowSet(query);
		while (results.next()) {
			Park p = mapRowToParks(results);
			parks.add(p);
		}
		return parks;
	}

	public void printParkNames() {
		List<Park> parks = getAllParks();
		for (Park park : parks) {
			System.out.print(park.getPark_id() + " - ");
			System.out.println(park.getName());

		}

	}

	}

	public void printParkInfo(int userChoice) {
		Park p = new Park();
		p = findParkById(userChoice);
		System.out.println(p.getName() + " National Park");
		System.out.println("Location: " + p.getLocation());

		System.out.println("Date Established: " + p.getDate_est());
		System.out.println("Area: " + p.getArea());
		System.out.println("Annual Visitors: " + p.getVisitors());

		System.out.println("Date Established: " + p.getDate_est());
		System.out.println("Area: " + p.getArea());
		System.out.println("Annual Visitors: " + p.getVisitors() + "\n");

		System.out.println(p.getDescription());
	}

	@Override
	public Park findParkById(int parkId) {
		Park p = null;
		String query = "SELECT * FROM park where park_id = ?";
		SqlRowSet results = jdbcTemplate.queryForRowSet(query, parkId);
		if (results.next())
			p = mapRowToParks(results);
		return p;
	}

	private Park mapRowToParks(SqlRowSet results) {
		Park park = new Park();
		park.setPark_id(results.getInt("park_id"));
		park.setName(results.getString("name"));
		park.setLocation(results.getString("location"));
		park.setDate_est(results.getDate("establish_date").toLocalDate());
		park.setArea(results.getInt("area"));
		park.setVisitors(results.getInt("visitors"));
		park.setDescription(results.getString("description"));

		return park;
	}

	@Override
	public Park findParkByCampgroundId(int campgroundId) {
		String query = "SELECT * FROM park JOIN campground ON campground.park_id = park.park_id where campground_id = "
				+ campgroundId;
		SqlRowSet results = jdbcTemplate.queryForRowSet(query);
		Park p = mapRowToParks(results);
		return p;
	}

	@Override
	public void createPark(Park newPark) {
		//

	}

	@Override
	public void updatePark(Park updatedPark) {
		// TODO Auto-generated method stub

	}

	@Override
	public void deletePark(Park deletedPark) {
		// TODO Auto-generated method stub

	}

	protected int getNextId() {
		SqlRowSet nextIdResult = jdbcTemplate.queryForRowSet("SELECT nextval('park_park_id_seq')");
		if (nextIdResult.next()) {
			return nextIdResult.getInt(1);
		} else {
			throw new RuntimeException("Something went wrong while getting an id for the new campsite");
		}
	}
}
