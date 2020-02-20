package com.techelevator;

public interface ReservationDAO {

	public List<Reservation> getAllReservations();
	public Reservation findRezById(int rezId);
	
}
