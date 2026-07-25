package com.nh.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
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
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nh.service.BlockService;
import com.nh.service.BoardService;
import com.nh.service.PlaceService;

@PropertySource("classpath:secret.properties")
@Configuration
@RestController
public class APIController {
	@Value("${google.api.key}")
    private String GoogleApiKey;
	@Value("${google.r.key}")
	private String GoogleRKey;
	@Value("${openAI.api.key}")
	private String OpenAIApiKey;
	
	@Autowired
	PlaceService pSvc;
	@Autowired
	BlockService blSvc;
	@Autowired
	BoardService bSvc;
	
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
	
	// 장소 삽입 
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
	
	@PostMapping("/searchCost")
	public Map<String,Integer> searchCost(int bno){
		// 게시글 번호로 일정 블록 정보 조회 
		List<Map<String, Object>> list = blSvc.getBlocksForAiCount(bno);
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
		
		Map<String, Integer> ret = new HashMap<>();
		ret.put("transportCost", budgetJson.path("transportCost").asInt(0));
		ret.put("foodCost", budgetJson.path("foodCost").asInt(0));
		ret.put("roomCost", budgetJson.path("roomCost").asInt(0));
		ret.put("etcCost", budgetJson.path("etcCost").asInt(0));
		ret.put("maxCost", budgetJson.path("maxCost").asInt(0));

		System.out.println("결과 Map: " + ret);
		
		bSvc.modifyCost(bno, ret.get("maxCost"), ret.get("transportCost"), ret.get("foodCost"), ret.get("roomCost"), ret.get("etcCost"));
		
		return ret;
	}
	
	
}
