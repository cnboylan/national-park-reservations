package com.techelevator;

import java.sql.SQLException;
import java.util.ArrayList;
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
import org.springframework.jdbc.support.rowset.SqlRowSet;




public class CampgroundJBCDTests {
	private static SingleConnectionDataSource dataSource;
	private static CampgroundJBCD dao;

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

		dao = new CampgroundJBCD(dataSource);
	}
	

	@After
	public void rollback() throws SQLException {
		dataSource.getConnection().rollback();
	}
	@Test
	public void test_find_campground_by_id() {
		Campground campy = campgroundHelper();
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		String fakeCampground = "INSERT INTO campground VALUES (Default, 3, 'Meme Park Theme Park', '04', '10', 1000000)";
		jdbcTemplate.update(fakeCampground);
		int searchID = campy.getCampground_id();
		Assert.assertNotNull(searchID);
			
		
	}
	
	@Test
	public void test_find_campground_by_parks() {
		List<Campground> thisList = dao.findCampgroundsByParkId(3);
		int thisListLength = thisList.size();
		Campground campy = campgroundHelper();
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		String fakeCampground = "INSERT INTO campground VALUES (DEFAULT, 3, 'DADTOWN USA', '03', '11', 5)";
		jdbcTemplate.update(fakeCampground);
		List<Campground> newList = dao.findCampgroundsByParkId(3);
		int newListLength = newList.size();
		Assert.assertEquals(newListLength, thisListLength + 1);
	}

	
	protected DataSource getDataSource() {
		return dataSource;
	}
	
	public Campground campgroundHelper() {
		Campground c = new Campground();
		c.setCampground_id(dao.getNextId());
		c.setPark_id(dao.getPark_id());
		c.setName(dao.getName());
		c.setMonthOpen(dao.getMonthOpen());
		c.setMonthClose(dao.getMonthClose());
		c.setDaily_fee(dao.getDaily_fee());
		return c;
	}
}
