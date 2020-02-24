package com.techelevator;

import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;

public class CampsiteJBDCTests {

	private static SingleConnectionDataSource dataSource;
	private static CampsiteJBCD dao;
	

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

		dao = new CampsiteJBCD(dataSource);
	}
	@After
	public void rollback() throws SQLException {
		dataSource.getConnection().rollback();
	}

	

	protected DataSource getDataSource() {
		return dataSource;
	}
	
	
	@Test
	public void test_find_campsite_by_campground() {
		
		int thisSize = dao.findSitesByCampgroundId(2).size();
		Campsite campysitey = campsiteHelper();
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		String fakeCampsite = "INSERT INTO site VALUES (Default, 2, 420, 6, true, 69, true)";
		jdbcTemplate.update(fakeCampsite);
		int campgroundID = campysitey.getCampground_id();
		Assert.assertNotNull(campgroundID);
		Assert.assertEquals(2, campysitey.getCampground_id());
		int thatSize = dao.findSitesByCampgroundId(campgroundID).size();
		Assert.assertEquals(thisSize +1, thatSize);
		
	}
	

	public Campsite campsiteHelper() {
		Campsite c = new Campsite();
		c.setSite_id(dao.getNextSiteId());
		c.setCampground_id(2);
		c.setSite_number(dao.getSite_number());
		c.setMaxOccupancy(dao.getMaxOccupancy());
		c.setHcAccessible(dao.isHcAccessible());
		c.setUtilities(dao.isUtilities());

		return c;
	}
}
