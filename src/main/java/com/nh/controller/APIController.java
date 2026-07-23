package com.nh.controller;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.nh.service.PlaceService;

@PropertySource("classpath:secret.properties")
@Configuration
@RestController
public class APIController {
	@Value("${google.api.key}")
    private String GoogleApiKey;
	@Autowired
	PlaceService pSvc;
	
	@GetMapping(value="/getBoardImg", produces = MediaType.IMAGE_JPEG_VALUE)
	public byte[] getBoardImg(@RequestParam String center, @RequestParam String path) throws MalformedURLException, IOException {
		String url = "https://maps.googleapis.com/maps/api/staticmap?" +
				"center=" + URLEncoder.encode(center, StandardCharsets.UTF_8) +
				"&size=290x220&path=" + URLEncoder.encode(path, StandardCharsets.UTF_8) +
				"&key=" + GoogleApiKey;
		System.out.println(url);
		try (InputStream is = new URL(url).openStream()) {
	        return is.readAllBytes();
	    }
	}
	
	@GetMapping(value="/getBlockImg", produces = MediaType.IMAGE_JPEG_VALUE)
	public byte[] getBlockImg(@RequestParam String center, @RequestParam String marker) throws MalformedURLException, IOException {
		String url = "https://maps.googleapis.com/maps/api/staticmap?" +
				"center=" + URLEncoder.encode(center, StandardCharsets.UTF_8) + "&markers=" + URLEncoder.encode(marker, StandardCharsets.UTF_8) +
				"&size=145x145&zoom=18&key=" + GoogleApiKey;
		System.out.println(url);
		try (InputStream is = new URL(url).openStream()) {
			return is.readAllBytes();
		}
	}
	@PostMapping("/addPlace")
	public String addPlace(@RequestBody Map<String,Object> mapReq) throws MalformedURLException, IOException {
		String placeId = (String)mapReq.get("placeId");
		String name = (String)mapReq.get("name");
		String category = (String)mapReq.get("category");
		String address = (String)mapReq.get("address");
		double lat = (double)mapReq.get("lat");
		double lng = (double)mapReq.get("lng");
		String websiteUrl = (String)mapReq.get("websiteUrl");
		String businessHours = (String)mapReq.get("businessHours");
		String photos = (String)mapReq.get("photos");
		try{
			pSvc.addPlace(placeId, name, category, address, lat, lng, websiteUrl, businessHours, photos);
			return "insert";
		} catch(Exception e) {return "fail";}
		
		
		
	}
	
}
