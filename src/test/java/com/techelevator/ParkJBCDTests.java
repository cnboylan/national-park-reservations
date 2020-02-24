package com.techelevator;

import java.sql.SQLException;

import javax.sql.DataSource;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;

public class ParkJBCDTests {
	private static SingleConnectionDataSource dataSource;
	private static ParkJBCD dao;
	

	@BeforeClass
	public static void setupDataSource() {
		dataSource = new SingleConnectionDataSource();
		dataSource.setUrl("jdbc:postgresql://localhost:5432/campground");
		dataSource.setUsername("postgres");
		dataSource.setPassword("postgres1");
		
		dataSource.setAutoCommit(false);
	}

	
	@AfterClass
	public static void closeDataSource() throws SQLException {
		dataSource.destroy();
	}

	@Before
	public void setUp() {

		dao = new ParkJBCD(dataSource);
	}
	@After
	public void rollback() throws SQLException {
		dataSource.getConnection().rollback();
	}

	

	protected DataSource getDataSource() {
		return dataSource;
	}
	
	
	@Test
	public void test_find_park_by_id_and_get_all_parks() {
		
		int thisSize = dao.getAllParks().size();
		Park thisPark = parkHelper();
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		String fakePark = "INSERT INTO park VALUES (Default, 'Jurrassic Park', 'Columbia', '1996-09-17', 27503, 14,"
				+ " 'The only place on earth where you can get killed by a velociraptor.')";
		jdbcTemplate.update(fakePark);
		int thisParkId = thisPark.getPark_id();
		//Assert.assertNotNull(thisParkId);
		//Assert.assertEquals(dao.getNextId()-1, thisPark.getPark_id());
		int thatSize = dao.getAllParks().size();
		Assert.assertEquals(thisSize +1, thatSize);
		//Assert.assertEquals("Jurrassic Park", thisPark.getName());
	}
	

	public Park parkHelper() {
		
		Park p = new Park();
		p.setPark_id(dao.getNextId());
		p.setName(dao.getName());
		p.setLocation(dao.getLocation());
		p.setDate_est(dao.getDate_est());
		p.setArea(dao.getArea());
		p.setVisitors(dao.getVisitors());
		p.setDescription(dao.getDescription());
		return p;
	}
}
