package com.restaurant;

import java.sql.SQLException;
import java.util.List;



public interface RestaurantDAO {
	public int insertRestaurant(RestaurantDTO dto) throws SQLException;
	public int updateRestaurant(RestaurantDTO dto) throws SQLException;
	public int deleteRestaurant(int num, String userId) throws SQLException;
	
	public int dataCount();
	public int dataCount(String condition, String keyword);
	
	public List<RestaurantDTO> listCount();
	public List<RestaurantDTO> listRestaurant(int offset, int rows);
	public List<RestaurantDTO> listRestaurant(int offset, int rows, String condition, String keyword);

	public int updateHitcount(int num);
	public RestaurantDTO readRestaurant(int num);
	public RestaurantDTO preRestaurant(int num, String condition, String keyword);
	public RestaurantDTO nextRestaurant(int num, String condition, String keyword);
}
