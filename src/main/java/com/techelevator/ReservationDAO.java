package com.techelevator;

import java.util.List;

public interface ReservationDAO {

	public List<Reservation> getAllReservations();
	public Reservation findRezById(int rezId);
	public List<Reservation> findRezBySiteId(int siteId);
	public void createRez(Reservation newRez);
	public void updateReservation(Reservation updatedRez);
	public void deleteReservation(Reservation deletedRez);
}
