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

	@Override
	public List<Reservation> getAllReservations() {
		List<Reservation> rezList = new ArrayList<Reservation>();
		String sqlGetAllRez = "SELECT * FROM reservation";
		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlGetAllRez);
		while(results.next()) {
			rezList.add(mapRowToRez(results));
		}
		return rezList;
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
	public List<Reservation> findRezBySiteId(int siteId) {
		List<Reservation> rezList = new ArrayList<Reservation>();
		String sqlGetRezBySiteID = "SELECT * "+
							   "FROM reservation "+
							   "WHERE site_id = ?";
		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlGetRezBySiteID, siteId);
		while(results.next()) {
			rezList.add(mapRowToRez(results));
		}
		return rezList;
	}
	
	@Override //Return value needed?
	public void createRez(Reservation newRez) {
		String sqlCreateRez = "INSERT INTO reservation(site_id, name, from_date, to_date)"
								+ "VALUES(?, ?, ?, ?)";
		jdbcTemplate.update(sqlCreateRez, newRez.getSite_id(), newRez.getName(), newRez.getFrom_date(), newRez.getTo_date());
	}

	@Override //Return value needed?
	public void updateReservation(Reservation updatedRez) {
		String sqlUpdateRez = "UPDATE reservation "
							+ "SET site_id = ?, name = ?, from_date = ?, to_date = ? "
							+ "WHERE reservation_id = ?";
		jdbcTemplate.update(sqlUpdateRez, updatedRez.getSite_id(), updatedRez.getName(), updatedRez.getFrom_date(), updatedRez.getTo_date(), updatedRez.getReservation_id());
	}

	@Override //Return value needed?
	public void deleteReservation(Reservation deletedRez) {
		String sqlDeleteRez = "DELETE FROM reservation "
								+ "WHERE reservation_id = ?";
		jdbcTemplate.update(sqlDeleteRez, deletedRez.getReservation_id());
	}
	
	public List<Reservation> getRezNext30Days(int parkId) {
		List<Reservation> rezList = new ArrayList<Reservation>();
		String sqlGetAllRez = "SELECT * FROM reservation "
							+ "JOIN site ON site.site_id = reservation.site_id "
							+ "JOIN campground ON campground.campground_id = site.campground_id "
							+ "WHERE park_id = ? AND from_date "
							+ "BETWEEN current_date AND current_date + 29";
		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlGetAllRez);
		while(results.next()) {
			rezList.add(mapRowToRez(results));
		}
		return rezList;
	}
	
//	private int getNextRezId() {
//		SqlRowSet nextIdResult = jdbcTemplate.queryForRowSet("SELECT nextval('seq_reservation_id')");
//		if(nextIdResult.next()) {
//			return nextIdResult.getInt(1);
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
