package com.techelevator;

import java.util.Date;
import java.util.List;

public interface ReservationDAO {

	public List<Reservation> getAllReservations();
	public Reservation findRezById(int rezId);
	public void createRez(int siteId, String rezName, Date fromDate, Date toDate);
	
}
