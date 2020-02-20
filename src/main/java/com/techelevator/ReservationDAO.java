package com.techelevator;

import java.util.List;

public interface ReservationDAO {

	public List<Reservation> getAllReservations();
	public Reservation findRezById(int rezId);
	
}
