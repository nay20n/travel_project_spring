package com.nh.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
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
	@Value("${google.r.key}")
	private String GoogleRKey;
	@Autowired
	PlaceService pSvc;
	
	// 메인화면 게시글 지도 이미지
	@GetMapping(value="/getBoardImg", produces = MediaType.IMAGE_JPEG_VALUE)
	public byte[] getBoardImg(@RequestParam String center, @RequestParam String path) throws MalformedURLException, IOException {
		String url = "https://maps.googleapis.com/maps/api/staticmap?" +
				"center=" + URLEncoder.encode(center, StandardCharsets.UTF_8) +
				"&size=290x220&path=" + URLEncoder.encode(path, StandardCharsets.UTF_8) +
				"&key=" + GoogleRKey;
		System.out.println(url);
		try (InputStream is = new URL(url).openStream()) {
	        return is.readAllBytes();
	    }
	}
	
	// 내 일정 블럭 팝업 시 지도 이미지
	@GetMapping(value="/getBlockImg", produces = MediaType.IMAGE_JPEG_VALUE)
	public byte[] getBlockImg(@RequestParam String center, @RequestParam String marker) throws MalformedURLException, IOException {
		String url = "https://maps.googleapis.com/maps/api/staticmap?" +
				"center=" + URLEncoder.encode(center, StandardCharsets.UTF_8) + "&markers=" + URLEncoder.encode(marker, StandardCharsets.UTF_8) +
				"&size=145x145&zoom=18&key=" + GoogleRKey;
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
	
	// 게시글 일정 경로 생성
	@PostMapping("/getRoute")
	public String getRoute(@RequestBody Map<String,Object> mapReq) {
		String travelMode = (String)mapReq.get("travelMode");
		List<String> placeIds = (List)mapReq.get("placeIds");
		//System.out.println(placeIds);
		String url = "https://routes.googleapis.com/directions/v2:computeRoutes";
		try {
			URL apiUrl = new URL(url);
			HttpURLConnection con = (HttpURLConnection) apiUrl.openConnection();
			
			// 헤더
			con.setRequestMethod("POST");
			con.setRequestProperty("Content-Type", "application/json");
			con.setRequestProperty("X-Goog-Api-Key", GoogleRKey);
			con.setRequestProperty("X-Goog-FieldMask", "routes.polyline.encodedPolyline");
			con.setDoOutput(true);
			
			// 바디
			StringBuffer json = new StringBuffer();

	        json.append("{");
	        json.append("\"origin\":{");
	        json.append("\"placeId\":\"");
	        json.append(placeIds.get(0));
	        json.append("\"},");
	        
	        json.append("\"destination\":{");
	        json.append("\"placeId\":\"");
	        json.append(placeIds.get(placeIds.size() - 1));
	        json.append("\"},");
	        
	        json.append("\"travelMode\":\"");
	        json.append(travelMode);
	        json.append("\"");
	        
	        if (placeIds.size() > 2) {
	            json.append(",\"intermediates\":[");
	            for (int i=1;i<placeIds.size()-1;i++) {
	                if (i>1) { json.append(","); }
	                json.append("{");
	                json.append("\"placeId\":\"");
	                json.append(placeIds.get(i));
	                json.append("\"");
	                json.append("}");
	            }
	            json.append("]");
	        }
	        json.append("}");
	        
	        try ( OutputStream os = con.getOutputStream()) {
                byte[] input = json.toString().getBytes(StandardCharsets.UTF_8);
                os.write(input);
            }
	        
	        // 응답
	        int responseCode = con.getResponseCode();
	        System.out.println("responseCode : "+ responseCode);
	        
	        InputStream inputStream;
	        if (responseCode>=200 && responseCode<300) {
	                inputStream = con.getInputStream();
	        } else { inputStream =con.getErrorStream(); }
	        
	        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
	        
	        StringBuffer response = new StringBuffer();
	        
	        String line;
	        while ((line = br.readLine()) != null) {
	        	response.append(line);
	        }
	        
	        System.out.println(response.toString());
	        return response.toString();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "fail";
	}
}
