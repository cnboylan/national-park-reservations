package com.techelevator;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

public class ReservationJBCD implements ReservationDAO {
	
	private JdbcTemplate jdbcTemplate;

	public ReservationJBCD (DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	@Override //gets reservations in the next 30 days
	public List<Reservation> getAllReservations() {
		List<Reservation> siteList = new ArrayList<Reservation>();
		String sqlGetAllRez = "SELECT * FROM reservation "
							+ "WHERE from_date "
							+ "BETWEEN current_date AND current_date + 29";
		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlGetAllRez);
		while(results.next()) {
			siteList.add(mapRowToRez(results));
		}
		return siteList;
	}

	@Override
	public Reservation findRezById(int rezId) {
		Reservation theRez = null;
		String sqlGetRezByID = "SELECT * "+
							   "FROM reservation "+
							   "WHERE reservation_id = ?";
		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlGetRezByID, rezId);
		if(results.next()) {
			theRez = mapRowToRez(results);
		}
		return theRez;
	}

	@Override
	public void createRez(int siteId, String rezName, Date fromDate, Date toDate) {
		String sqlCreateRez = "INSERT INTO reservation(site_id, name, from_date, to_date)"
								+ "VALUES(?, ?, ?, ?)";
		jdbcTemplate.update(sqlCreateRez, siteId, rezName, fromDate, toDate);
	}
	
//	private long getNextRezId() {
//		SqlRowSet nextIdResult = jdbcTemplate.queryForRowSet("SELECT nextval('seq_reservation_id')");
//		if(nextIdResult.next()) {
//			return nextIdResult.getLong(1);
//		} else {
//			throw new RuntimeException("Something went wrong while getting an id for the new reservation");
//		}
//	}
	
	private Reservation mapRowToRez(SqlRowSet results) {
		Reservation newRez;
		newRez = new Reservation();
		newRez.setSite_id(results.getInt("site_id"));
		newRez.setReservation_id(results.getInt("reservation_id"));
		newRez.setName(results.getString("name"));
		newRez.setFrom_date(results.getDate("from_date").toLocalDate());
		newRez.setTo_date(results.getDate("to_date").toLocalDate());
		newRez.setCreate_date(results.getDate("create_date").toLocalDate());
		return newRez;
	}

}
