package com.techelevator;

import java.util.Scanner;

import javax.sql.DataSource;

import org.apache.commons.dbcp2.BasicDataSource;

public class CampgroundCLI {
	private ParkDAO parkDAO;
	private CampgroundDAO campgroundDAO;
	private CampsiteDAO campsiteDAO;
	private ReservationDAO reservationDAO;
// TO-DO Build tests.
// TO-DO Build CLI


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

	public void mainMenu(){
		// ********** INPUT VALIDATION **************
		// *****Don't let Tom break your CLI again***

		// Select from:
		//         ---1. View All Parks
		//         ---2. Select park from All Parks
		//         ---3. Select Campground from above park
		//         ---4. Select campsite from above campground
		//         ---5. Top 5 Campsites in no particular order ?
		//         ---6. Bonus - off season date returns 0 campsites
		//		   ---7. Double Bonus - Advanced search to filter campsite amenities
		//		   ---8. Book reservation at available campsite
		// 					- Requires name for reservation, start date, end date
		// 					- Returns confirmation ID once the reservation has been submitted
		//         ---9. Double Super Bonus - Select park and search campsite availability within
		// 					park to make a reservation.
		// 					- Return up to 5 campsites for each campground along with daily cost or duration cost?
		//         ---10. ULTRA BONUS - View list of all upcoming reservations within 30 days.
			while (true) {
				Scanner scanner = new Scanner(System.in);
				
				System.out.println("View parks interface.");
				System.out.println("Select a park for further details: ");
				parkDAO.printParkNames();
				System.out.println("0 - Quit");
				int parkChoice = Integer.parseInt(scanner.nextLine());
				
				// add input validation sometime in the future maybe never.
				
				if (parkChoice == 0) {
					break;
				}
				System.out.println("Park information screen.");
				parkDAO.printParkInfo(parkChoice);
				
				System.out.println("Select one of the following options: ");
				System.out.println("1 - View Campgrounds");
				System.out.println("2 - Search for Reservation");
				System.out.println("3 - Return to Previous Screen");
				int userChoice = Integer.parseInt(scanner.nextLine());
				
				if (userChoice == 1) {
					System.out.println("Name\tOpen\tClose\tDaily Fee");
					campgroundDAO.printCampgroundInfo(userChoice);
					
				} else if (userChoice == 2) {
					
				}
				
				
			}
	}
}
