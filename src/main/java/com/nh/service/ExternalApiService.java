package com.nh.service;

import java.util.List;

public interface ExternalApiService {
	/**
	 * 
	 * @param travelMode
	 * @param placeIds
	 * @return
	 */
	String getRoute(String travelMode, List<String> placeIds);
}
