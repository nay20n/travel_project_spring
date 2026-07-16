package com.nh.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class APIController {
	String GoogleApiKey = "";
	
	@GetMapping("/getBoardImg")
	public String getBoardImg(String center, String path) {
		String url = "https://maps.googleapis.com/maps/api/staticmap?" +
				"center=" + center +
				"&zoom=12&size=290x220&path=" + path +
				"&key=" + GoogleApiKey;
		return url;
	}
}
