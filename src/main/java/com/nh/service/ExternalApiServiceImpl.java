package com.nh.service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Repository;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nh.dao.PlaceDao;

@PropertySource("classpath:secret.properties")
@Configuration
@Repository
public class ExternalApiServiceImpl implements ExternalApiService {
	@Value("${google.api.key}")
    private String GoogleApiKey;
	@Value("${google.r.key}")
	private String GoogleRKey;
	@Value("${openAI.api.key}")
	private String OpenAIApiKey;
	
	@Autowired
	PlaceDao pDao;
	
	/**
	 * 장소 이름으로 장소 정보 DB 추가 및 placeId 반환 함수
	 * @param placeName
	 * @return placeId
	 * @throws Exception 발생 시 placeId null
	 */
	private String searchGooglePlace(String placeName) throws Exception {
	    String url = "https://places.googleapis.com/v1/places:searchText";
	    ObjectMapper objectMapper = new ObjectMapper();
	    Map<String, Object> requestMap = new HashMap<>();
	    requestMap.put("textQuery", placeName);
	    requestMap.put("languageCode", "ko");

	    String jsonBody = objectMapper.writeValueAsString(requestMap);
	    HttpClient client = HttpClient.newHttpClient();
	    HttpRequest request = HttpRequest.newBuilder()
	            .uri(URI.create(url))
	            .header("Content-Type", "application/json")
	            .header("X-Goog-Api-Key", GoogleRKey)
	            .header(
	                "X-Goog-FieldMask",
	                "places.id,places.displayName,places.formattedAddress,"
	                + "places.primaryTypeDisplayName,places.location,places.websiteUri"
	            )
	            .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
	            .build();

	    HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

	    System.out.println("Google Places 응답 : " + response.body());

	    JsonNode rootNode = objectMapper.readTree(response.body());
	    JsonNode places = rootNode.path("places");
	    if (!places.isArray() || places.isEmpty()) {
	        return null;
	    }
	    String placeId = places.get(0).path("id").asText();
	    String address = places.get(0).path("formattedAddress").asText();
	    String name = places.get(0).path("displayName").path("text").asText();
	    String category = places.get(0).path("primaryTypeDisplayName").path("text").asText();
	    double lat = places.get(0).path("location").path("latitude").asDouble();
	    double lng = places.get(0).path("location").path("longitude").asDouble();
	    String websiteUrl = places.get(0).path("websiteUri").asText(null);
	    try{
			//pDao.addPlace(placeId, name, category, address, lat, lng, websiteUrl, null, null);
		} catch(Exception e) {return null;}
	    
	    return places.get(0).path("id").asText();
	}
}
