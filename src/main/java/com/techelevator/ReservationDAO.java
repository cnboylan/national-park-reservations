package com.techelevator;

import java.util.Date;
import java.util.List;

public interface ReservationDAO {

	public List<Reservation> getAllReservations();
	public Reservation findRezById(int rezId);
	public List<Reservation> findRezBySiteId(int siteId);
	public void createRez(int siteID, String rezName, Date fromDate, Date toDate);
	public void updateReservation(Reservation updatedRez);
	public void deleteReservation(Reservation deletedRez);
	public Reservation findRezByName(String rezName);
	public void printReservationInfo(int parkChoice);
}
