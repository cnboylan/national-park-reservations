package com.techelevator;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.sql.DataSource;

import org.apache.commons.dbcp2.BasicDataSource;

public class CampgroundCLI {
	private ParkDAO parkDAO;
	private CampgroundDAO campgroundDAO;
	private CampsiteDAO campsiteDAO;
	private ReservationDAO reservationDAO;

	public static void main(String[] args) {
		BasicDataSource dataSource = new BasicDataSource();
		dataSource.setUrl("jdbc:postgresql://localhost:5432/campground");
		dataSource.setUsername("postgres");
		dataSource.setPassword("postgres1");

		CampgroundCLI application = new CampgroundCLI(dataSource);
		application.run();
	}

	public CampgroundCLI(DataSource datasource) {
		// create your DAOs here
		parkDAO = new ParkJBCD(datasource);
		campgroundDAO = new CampgroundJBCD(datasource);
		campsiteDAO = new CampsiteJBCD(datasource);
		reservationDAO = new ReservationJBCD(datasource);
	}

	public void run() {
		mainMenu();
	}

	public void mainMenu() {
		// ********** INPUT VALIDATION **************
		// *****Don't let Tom break your CLI again***
		while (true) {
			//is it ok to open and close scanners in each method?
			Scanner scanner = new Scanner(System.in);

			System.out.println("VIEW PARKS INTERFACE\n");
			System.out.println("Select a park for further details:");
			parkDAO.printParkNames();
			System.out.println("0 - Quit");
			int parkChoice = Integer.parseInt(scanner.nextLine());

			// add input validation sometime in the future maybe never.

			if (parkChoice == 0) {
				System.exit(1);
			}
			//this shouldn't be hard coded, maybe make a list using park_id?
			if (parkChoice == 1 || parkChoice == 2 || parkChoice == 3) {
				parkInformationScreen(parkChoice);
			}
			
			System.out.println("Invalid entry! Try again.\n");
		}
		
	}

	public void parkInformationScreen(int parkChoice) {
		Scanner scanner = new Scanner(System.in);
		System.out.println("PARK INFORMATION SCREEN\n");
		parkDAO.printParkInfo(parkChoice);

		System.out.println("\nSelect one of the following options: ");
		System.out.println("1 - View Campgrounds");
		System.out.println("2 - Search for Parkwide Reservation");
		System.out.println("3 - Show Upcoming Reservations");
		System.out.println("4 - Return to Previous Screen");
		int userChoice = Integer.parseInt(scanner.nextLine());

		if (userChoice == 1) {
			parkCampgroundMenya(parkChoice);

		} else if (userChoice == 2) {
			searchForCampsitesByParkMenya(parkChoice);

		} else if (userChoice == 3) {
			System.out.println("Rez No. | Site No. | Name | Start Date | End Date | Create Date");
			reservationDAO.printReservationInfo(parkChoice);
			System.out.println("************************************************************");
			mainMenu();
			
		} else {
			mainMenu();
		}
	}

	public void parkCampgroundMenya(int userChoice) {
		Scanner scanner = new Scanner(System.in);

		System.out.println("PARK CAMPGROUNDS\n");
		System.out.println("No. | Name | Open | Close | Daily Fee");
		campgroundDAO.printCampgroundInfo(userChoice);
		System.out.println("\nSelect an option: ");
		System.out.println("1 - Search for Available Reservation");
		System.out.println("2 - Return to Previous Screen");
		System.out.println("3 - Cancel");
		int resMenuChoice = Integer.parseInt(scanner.nextLine());

		if (resMenuChoice == 1) {
			while (true) {
				System.out.println("Select a campground from the menu:");
				campgroundDAO.printCampgroundInfo(userChoice);
				int campgroundID = Integer.parseInt(scanner.nextLine());
				searchForCampsitesMenya(campgroundID);
			}
		}

		if (resMenuChoice == 2) {
			parkInformationScreen(userChoice);
		}

		if (resMenuChoice == 3) {
			mainMenu();
		}

	}

	public void searchForCampsitesMenya(int campgroundID) {
		Scanner scanner = new Scanner(System.in);
		String startDate;
		String endDate;
		Date start_date = null;
		Date end_date = null;
		
		System.out.println("Enter the start date for the reservation. (Format: yyyy-mm-dd)");
		startDate = scanner.nextLine();
		String pattern = "yyyy-MM-dd";
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		
		try {
			start_date = sdf.parse(startDate);
		} catch (ParseException e) {
			errorHandler(e);
			mainMenu();
		}
		
		System.out.println("Enter the end date for the reservation. (Format: yyyy-mm-dd)");
		endDate = scanner.nextLine();
		
		try {
			end_date = sdf.parse(endDate);
		} catch (ParseException e) {
			errorHandler(e);
			mainMenu();
		}

		Campground cg = campgroundDAO.findCampgroundById(campgroundID);
		campsiteDAO.printCampsiteInfo(cg, start_date, end_date, startDate, endDate);
		makeRez(start_date, end_date);

	}
	
	public void searchForCampsitesByParkMenya(int parkID) {
		Scanner scanner = new Scanner(System.in);
		String startDate;
		String endDate;
		Date start_date = null;
		Date end_date = null;
		
		System.out.println("Enter the start date for the reservation. (Format: yyyy-mm-dd)");
		startDate = scanner.nextLine();
		String pattern = "yyyy-MM-dd";
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		
		try {
			start_date = sdf.parse(startDate);
		} catch (ParseException e) {
			errorHandler(e);
			mainMenu();
		}
		
		System.out.println("Enter the end date for the reservation. (Format: yyyy-mm-dd)");
		endDate = scanner.nextLine();
		
		try {
			end_date = sdf.parse(endDate);
		} catch (ParseException e) {
			errorHandler(e);
			mainMenu();
		}

		campsiteDAO.printCampsiteInfoByPark(parkID, start_date, end_date, startDate, endDate);
		makeRez(start_date, end_date);

	}
	
	public void makeRez(Date start_date, Date end_date) {
		Scanner scanner = new Scanner(System.in);
		System.out.println("Which site should be reserved (0 to cancel)? ");
		int siteChoice = Integer.parseInt(scanner.nextLine());
		
		if (siteChoice == 0) {
			mainMenu();
		}
		
		System.out.println("Enter name for the reservation: ");
		String rezName = scanner.nextLine();
		reservationDAO.createRez(siteChoice, rezName, start_date, end_date);
		int rezID = reservationDAO.findRezByName(rezName).getReservation_id();
		System.out.println("Reservation Confirmation #: " + rezID);
		mainMenu();
	}

	public void errorHandler(Exception e){
		System.out.println("*****************************************");
		System.out.println("Your date input is incorrect.");
		System.out.println("*****************************************");
		System.out.println("You are returning to the main menu.");
		System.out.println("*****************************************");

	}

}
