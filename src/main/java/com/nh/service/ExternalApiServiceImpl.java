package com.nh.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nh.dao.AiBlockDao;
import com.nh.dao.BlockDao;
import com.nh.dao.BoardDao;
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
	@Autowired
	BoardDao bDao;
	@Autowired
	BlockDao blDao;
	@Autowired
	AiBlockDao aDao;
	
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
	                "places.id,places.displayName,places.formattedAddress,places.regularOpeningHours,"
	                + "places.primaryTypeDisplayName,places.location,places.photos,places.websiteUri"
	            )
	            .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
	            .build();

	    HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

	    //System.out.println("Google Places 응답 : " + response.body());

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
	    
	    // 영업시간
	    JsonNode weekdayDescriptions = places.get(0).path("regularOpeningHours").path("weekdayDescriptions");
	    List<String> businessHoursList = new ArrayList<>();
	    if (weekdayDescriptions.isArray()) {
	        for (JsonNode day : weekdayDescriptions) {
	            businessHoursList.add(day.asText());
	        }
	    }
	    String businessHours = String.join("<br/>", businessHoursList);

	    // 사진
	    JsonNode photosNode = places.get(0).path("photos");
	    List<String> photoList = new ArrayList<>();
	    if (photosNode.isArray()) {
	        for (int i=0; i<Math.min(photosNode.size(),5);i++) {
	            String photoName = photosNode.get(i).path("name").asText();
	            String photoUrl = "https://places.googleapis.com/v1/" + photoName 
	            		+ "/media?maxWidthPx=400"
	                    + "&key=" + GoogleApiKey;
	            photoList.add(photoUrl);
	        }
	    }
	    
	    boolean flag = pDao.isExistPlace(placeId);
	    
	    // 장소 삽입
	    try{
	    	if(!flag)
	    		pDao.addPlace(placeId, name, category, address, lat, lng, websiteUrl, businessHours);
		} catch(Exception e) {return null;}
	    
	    // 사진 삽입
	    try {
	    	if(!flag) {
	    		for(int i=0;i<photoList.size();i++) {
	    			pDao.addPlaceImg(placeId, photoList.get(i), i+1);
	    		}
	    	}
	    } catch(Exception e) { e.printStackTrace(); }
	    
	    return places.get(0).path("id").asText();
	}

	@Override
	public String getRoute(String travelMode, List<String> placeIds) {
		String url = "https://routes.googleapis.com/directions/v2:computeRoutes";
		try {
			URL apiUrl = new URL(url);
			HttpURLConnection con = (HttpURLConnection) apiUrl.openConnection();
			
			// 헤더
			con.setRequestMethod("POST");
			con.setRequestProperty("Content-Type", "application/json");
			con.setRequestProperty("X-Goog-Api-Key", GoogleRKey);
			con.setRequestProperty("X-Goog-FieldMask", "routes.polyline.encodedPolyline");
			//con.setRequestProperty("X-Goog-FieldMask", "*");
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
	        System.out.println(json);
	        
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
	        	//System.out.println("!"+line);
	        	response.append(line);
	        }
	        
	        System.out.println(response.toString());
	        return response.toString();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "fail";
	}

	@Override
	public Map<String, Object> searchCost(int bno) {
		List<Map<String, Object>> list = blDao.getBlocksForAiCount(bno);
		System.out.println(list);
		
		String startAddress = "";
		String startName = "";
		Map<String, Object> targetMap = null;
		for(Map<String,Object> map : list) {
			if (map.get("idx") != null && "0".equals(String.valueOf(map.get("idx")))) {
		        startAddress = (String) map.get("address"); 
		        startName = (String) map.get("name");
		        targetMap = map;                      
		        break;                              
		    }
		}
		if (targetMap != null) {
		    list.remove(targetMap);
		}
		System.out.println("출발장소 이름 : " + startName);
		System.out.println("출발장소 주소 : " + startAddress);
		
		//OpenAI API 호출 준비
	    String url = "https://api.openai.com/v1/chat/completions";
	    
	    String systemPrompt = "너는 한국 여행 예산 산출 전문가다.\n"
	    		+ "[역할]\n"
	    		+ "- 사용자의 출발지와 여행 일정을 분석하여 현실적인 국내 여행 예산을 추정한다.\n"
	    		+ "- 대한민국 평균 물가와 일반적인 여행 패턴을 기준으로 계산한다.\n"
	    		+ "[예산 산정 기준]\n"
	    		+ "1. transportCost : 출발지와 여행지 간 이동거리, 이동횟수, 예상 교통수단을 고려하여 추정한다.\n"
	    		+ "2. foodCost : 일정 동안 필요한 식사 횟수를 고려하여 추정한다.\n"
	    		+ "3. roomCost : 숙박이 필요한 일정이면 적절한 숙박비를 추정하고, 당일치기라면 0으로 한다.\n"
	    		+ "4. etcCost : 카페, 입장료, 주차비, 간식 등 기타 비용을 평균을 뽑아서 추정한다.\n"
	    		+ "2. maxCost는 위 4개 항목의 합계(기본 총액)가 절대 아니야!\n"
	            + "5. maxCost는 현지 돌발 상황, 택시/쇼핑/비상금 등 예기치 못한 지출 오차범위를 고려해서 '4개 항목 합계 금액의 약 110%~125% 수준(더 여유 있는 최대 견적 금액)'으로 반드시 계산해서 제시해줘.\n\n"
	    		+ "[규칙]\n"
	    		+ "- 모든 금액은 KRW 기준의 정수(integer)이다.\n"
	    		+ "- 확실하지 않은 경우 일반적인 국내 여행 평균 비용을 사용한다.\n"
	    		+ "- 설명, 마크다운, 코드블록을 출력하지 않는다.\n"
	    		+ "- 반드시 아래 JSON 객체만 반환한다.\n"
	    		+ "{\n"
	    		+ "  \"transportCost\": 0,\n"
	    		+ "  \"foodCost\": 0,\n"
	    		+ "  \"roomCost\": 0,\n"
	    		+ "  \"etcCost\": 0,\n"
	    		+ "  \"maxCost\": 0\n"
	    		+ "}";
	    String userPrompt = "출발 장소 이름:" + startName + "\n"
	    		+ "출발 장소 주소:" + startAddress + "\n"
	    		+ "여행 장소 목록 : " + list + "\n"
	    		+ "위 일정을 바탕으로 예상 여행 예산을 계산해줘";
	    
	    Map<String, Object> requestMap = new HashMap<>();
	    requestMap.put("model", "gpt-4o-mini");
	   
	    Map<String, String> responseFormat = new HashMap<>();
	    responseFormat.put("type", "json_object");
	    requestMap.put("response_format", responseFormat);

	    List<Map<String,Object>> messages = new ArrayList<>();
	    Map<String,Object> system = new HashMap<>();
	    system.put("role", "system");
	    system.put("content", systemPrompt);
	    messages.add(system);
	    Map<String,Object> user = new HashMap<>();
	    user.put("role", "user");
	    user.put("content", userPrompt);
	    messages.add(user);
	    requestMap.put("messages", messages);
		
	    //수정: Map을 JSON String 문자열로 변환하는 과정 추가
	    ObjectMapper objectMapper = new ObjectMapper();
	    String jsonBody = "";
		try {
			jsonBody = objectMapper.writeValueAsString(requestMap);
		} catch (JsonProcessingException e) {e.printStackTrace();}

	    HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + OpenAIApiKey)
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                .build();

        HttpResponse<String> response = null;
        JsonNode rootNode = null;
        String content = null;
        JsonNode budgetJson = null;
		try {
			response = client.send(request, HttpResponse.BodyHandlers.ofString());
			// OpenAI API 응답 전체(JSON) 파싱
			rootNode = objectMapper.readTree(response.body());
			// choices[0].message.content 안의 JSON 문자열만 추출
			content = rootNode.path("choices").get(0).path("message").path("content").asText();
			// content 내용(AI가 계산한 예산 JSON)을 파싱
			budgetJson = objectMapper.readTree(content);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		Map<String, Object> ret = new HashMap<>();
		ret.put("transportCost", budgetJson.path("transportCost").asInt(0));
		ret.put("foodCost", budgetJson.path("foodCost").asInt(0));
		ret.put("roomCost", budgetJson.path("roomCost").asInt(0));
		ret.put("etcCost", budgetJson.path("etcCost").asInt(0));
		ret.put("maxCost", budgetJson.path("maxCost").asInt(0));
		ret.put("bno", bno);
		System.out.println("결과 Map: " + ret);
		
		bDao.modifyCost(ret);
		ret.remove("bno");
		return ret;
	}

	@Override
	@Transactional
	public List<Map<String, Object>> searchAiRecommend(List<Map<String, Object>> userBlocks, int bno,
			String arrPlaceCity) {
		List<Map<String, Object>> aiChecked = aDao.getAiChecked(bno);
		
		// 기존 ai 추천 일정 지우기
		aDao.deleteAiBlock(bno);
		
		//OpenAI API 호출 준비
		String url = "https://api.openai.com/v1/chat/completions";
		
		String systemPrompt = "너는 여행 하루 일정을 계획하는 여행 일정 추천 전문가다.\n"
				+ "[역할]\n"
		        + "- 사용자가 작성한 기존 일정과 여행 지역 및 방문 예정 장소를 분석하여 효율적인 하루 여행 일정을 구성한다.\n"
		        + "- 기존 일정은 사용자가 직접 정한 일정이므로 가능한 한 유지한다.\n"
		        + "- 기존 일정 사이의 빈 시간에는 사용자가 제공한 장소를 우선적으로 배치한다.\n"
		        + "- 일정에 빈 시간이 있고 여행 효율을 높일 수 있다면 해당 지역에서 방문하기 좋은 장소를 추가로 추천할 수 있다.\n"
		        + "- 추가로 추천하는 장소는 실제 국내 여행지 또는 실제 운영 중인 장소로 추정되는 장소를 우선한다.\n"
		        + "[일정 산정 기준]\n"
		        + "1. 기존 일정에 포함된 장소와 시작 시간, 종료 시간은 변경하지 않는다.\n"
		        + "2. 사용자가 방문하고자 하는 장소는 가능한 한 일정에 포함한다.\n"
		        + "3. 기존 일정과 시간이 겹치는 새로운 일정을 생성하지 않는다.\n"
		        + "4. 장소 간 이동 동선을 고려하여 가까운 장소를 연속적으로 방문하도록 구성한다.\n"
		        + "5. 장소 간 이동에 필요한 시간을 고려하여 충분한 이동 시간을 확보한다.\n"
		        + "6. 일반적인 관광지 방문 시간을 고려하여 적절한 체류 시간을 설정한다.\n"
		        + "7. 식사와 휴식에 필요한 시간을 고려하여 하루 일정이 지나치게 빡빡하지 않도록 한다.\n"
		        + "8. 기존 일정에 이미 포함된 장소는 중복하여 추가하지 않는다.\n"
		        + "9. 사용자가 제공한 장소를 우선적으로 일정에 포함하고, 필요한 경우에만 새로운 장소를 추천한다.\n"
		        + "10. 추가로 추천하는 장소는 기존 일정 및 사용자가 제공한 장소와 이동 동선이 자연스럽도록 선택한다.\n"
		        + "11. 일정은 시작 시간이 빠른 순서대로 정렬한다.\n"
		        + "[규칙]\n"
		        + "- 기존 일정의 장소명, 시작 시간, 종료 시간은 절대 변경하지 않는다.\n"
		        + "- 기존 일정과 사용자가 제공한 장소를 우선적으로 고려한다.\n"
		        + "- 새로운 장소를 추천하는 경우 실제 존재할 가능성이 높은 장소명을 사용한다.\n"
		        + "- 설명, 마크다운, 코드블록을 출력하지 않는다.\n"
		        + "- 반드시 아래 JSON 배열만 반환한다.\n"
		        + "{\n"
		        + "  \"places\": ["
		        + "		{\n"
		        + "    		\"placeName\": \"방문할 장소 이름\",\n"
		        + "    		\"startTime\": \"YYYY-MM-DD HH:mm:ss\",\n"
		        + "    		\"endTime\": \"YYYY-MM-DD HH:mm:ss\"\n"
		        + "  	}\n"
		        + " ]"
		        + "}";
		String userPrompt = "기존 일정 : " + userBlocks + "\n"
		        + "방문 예정 장소 : " + aiChecked + "\n"
		        + "방문 지역 : " + arrPlaceCity + "\n"
		        + "위 정보를 바탕으로 하루 여행 일정을 추천해줘.";
		
		Map<String, Object> requestMap = new HashMap<>();
		requestMap.put("model", "gpt-4o-mini");
		
		Map<String, String> responseFormat = new HashMap<>();
		responseFormat.put("type", "json_object");
		requestMap.put("response_format", responseFormat);
		
		List<Map<String,Object>> messages = new ArrayList<>();
		Map<String,Object> system = new HashMap<>();
		system.put("role", "system");
		system.put("content", systemPrompt);
		messages.add(system);
		Map<String,Object> user = new HashMap<>();
		user.put("role", "user");
		user.put("content", userPrompt);
		messages.add(user);
		requestMap.put("messages", messages);
		
		//수정: Map을 JSON String 문자열로 변환하는 과정 추가
		ObjectMapper objectMapper = new ObjectMapper();
		String jsonBody = "";
		try {
			jsonBody = objectMapper.writeValueAsString(requestMap);
		} catch (JsonProcessingException e) {e.printStackTrace();}
		
		HttpClient client = HttpClient.newHttpClient();
		HttpRequest request = HttpRequest.newBuilder()
				.uri(URI.create(url))
				.header("Content-Type", "application/json")
				.header("Authorization", "Bearer " + OpenAIApiKey)
				.POST(HttpRequest.BodyPublishers.ofString(jsonBody))
				.build();
		
		HttpResponse<String> response = null;
		JsonNode rootNode = null;
		String content = null;
		JsonNode placeJson = null;
		try {
			response = client.send(request, HttpResponse.BodyHandlers.ofString());
			//System.out.println("HTTP 상태 코드 : " + response.statusCode());
			//System.out.println("OpenAI 응답 : " + response.body());
			// OpenAI API 응답 전체(JSON) 파싱
			rootNode = objectMapper.readTree(response.body());
			// choices[0].message.content 안의 JSON 문자열만 추출
			content = rootNode.path("choices").get(0).path("message").path("content").asText();
			// content 내용을 파싱
			placeJson = objectMapper.readTree(content);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		//구글 placeId 가져오기
		JsonNode places = placeJson.path("places");
		for (JsonNode place : places) {
			//System.out.println("장소명 : " + place.path("placeName").asText());
			//System.out.println("시작시간 : " + place.path("startTime").asText());
			//System.out.println("종료시간 : " + place.path("endTime").asText());
			
			String placeName = place.path("placeName").asText();
			String startTime = place.path("startTime").asText();
			String endTime = place.path("endTime").asText();
			String placeId = null;
			try {
				placeId = searchGooglePlace(placeName);
			} catch (Exception e) {
				e.printStackTrace();
			}
			if(placeId!=null) {
				System.out.println("ai 장소 삽입: " + placeName);
				aDao.insertAiBlock(bno, placeId, startTime, endTime, 0);
			}
		}
		
		System.out.println("결과: " + aDao.getAiBlock(bno));
		
		return aDao.getAiBlock(bno);
	}
}
