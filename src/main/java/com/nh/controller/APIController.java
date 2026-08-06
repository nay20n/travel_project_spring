package com.nh.controller;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

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
import org.springframework.web.multipart.MultipartFile;

import com.nh.service.AiBlockService;
import com.nh.service.BlockService;
import com.nh.service.BoardService;
import com.nh.service.ExternalApiService;
import com.nh.service.MemberService;
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
	@Autowired
	AiBlockService aSvc;
	@Autowired
	ExternalApiService eSvc;
	@Autowired
	MemberService mSvc;
	
//	// 게시글 일정 경로 생성
//	@GetMapping("/test")
//	public String getRoute() {
//		String travelMode = "DRIVE";
//		System.out.println(travelMode);
//		String url = "https://routes.googleapis.com/directions/v2:computeRoutes";
//		try {
//			URL apiUrl = new URL(url);
//			HttpURLConnection con = (HttpURLConnection) apiUrl.openConnection();
//			
//			// 헤더
//			con.setRequestMethod("POST");
//			con.setRequestProperty("Content-Type", "application/json");
//			con.setRequestProperty("X-Goog-Api-Key", GoogleRKey);
//			con.setRequestProperty("X-Goog-FieldMask", "routes.polyline.encodedPolyline");
//			con.setDoOutput(true);
//			
//			// 바디
//			StringBuffer json = new StringBuffer();
//
//	        json.append("{");
//	        json.append("\"origin\":{");
//	        json.append("\"location\":{");
//	        json.append("\"latLng\":{");
//	        json.append("\"latitude\": 37.419734,");
//	        json.append("\"longitude\": -122.0827784");
//	        json.append("}");
//	        json.append("}");
//	        json.append("},");
//	        
//	        json.append("\"destination\":{");
//	        json.append("\"location\":{");
//	        json.append("\"latLng\":{");
//	        json.append("\"latitude\": 37.417670,");
//	        json.append("\"longitude\": -122.079595");
//	        json.append("}");
//	        json.append("}");
//	        json.append("},");
//	        
//	        json.append("\"travelMode\":\"");
//	        json.append(travelMode);
//	        json.append("\"");
//	        json.append("}");
//
//	        System.out.println(json);
//	        
//	        try ( OutputStream os = con.getOutputStream()) {
//                byte[] input = json.toString().getBytes(StandardCharsets.UTF_8);
//                os.write(input);
//            }
//	        
//	        // 응답
//	        int responseCode = con.getResponseCode();
//	        System.out.println("responseCode : "+ responseCode);
//	        
//	        InputStream inputStream;
//	        if (responseCode>=200 && responseCode<300) {
//	                inputStream = con.getInputStream();
//	        } else { inputStream =con.getErrorStream(); }
//	        
//	        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
//	        
//	        StringBuffer response = new StringBuffer();
//	        
//	        String line;
//	        while ((line = br.readLine()) != null) {
//	        	System.out.println("!"+line);
//	        	response.append(line);
//	        }
//	        
//	        System.out.println(response.toString());
//	        return response.toString();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		return "fail";
//	}
//	
//	// 게시글 일정 경로 생성
//	@GetMapping("/test2")
//	public String getRoute2() {
//		String travelMode = "DRIVE";
//		System.out.println(travelMode);
//		String url = "https://routes.googleapis.com/directions/v2:computeRoutes";
//		try {
//			URL apiUrl = new URL(url);
//			HttpURLConnection con = (HttpURLConnection) apiUrl.openConnection();
//			
//			// 헤더
//			con.setRequestMethod("POST");
//			con.setRequestProperty("Content-Type", "application/json");
//			con.setRequestProperty("X-Goog-Api-Key", GoogleRKey);
//			con.setRequestProperty("X-Goog-FieldMask", "routes.polyline.encodedPolyline");
//			con.setDoOutput(true);
//			
//			// 바디
//			StringBuffer json = new StringBuffer();
//			
//			json.append("{");
//
//			json.append("\"origin\":{");
//			json.append("\"placeId\":\"");
//			//json.append("ChIJWexGkLL3wokRRoOOt3yTiDw"); US
//			json.append("ChIJA3CU42aifDURaq-3csGXvuc"); // (2026년 상반기) 정부정책으로 인해 구글 길찾기 서비스는 해외에서만 가능합니다.
//			json.append("\"");
//			json.append("},");
//
//			json.append("\"destination\":{");
//			json.append("\"placeId\":\"");
//			//json.append("ChIJb2UK6Qr3wokRpeZmbezYfN4"); US
//			json.append("ChIJs6s6qbyZfDURbF3fMwEeFkA");
//			json.append("\"");
//			json.append("},");
//
//			json.append("\"travelMode\":\"");
//			json.append(travelMode);
//			json.append("\"");
//
//			json.append("}");
//			
//			System.out.println(json);
//			
//			try ( OutputStream os = con.getOutputStream()) {
//				byte[] input = json.toString().getBytes(StandardCharsets.UTF_8);
//				os.write(input);
//			}
//			
//			// 응답
//			int responseCode = con.getResponseCode();
//			System.out.println("responseCode : "+ responseCode);
//			
//			InputStream inputStream;
//			if (responseCode>=200 && responseCode<300) {
//				inputStream = con.getInputStream();
//			} else { inputStream =con.getErrorStream(); }
//			
//			BufferedReader br = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
//			
//			StringBuffer response = new StringBuffer();
//			
//			String line;
//			while ((line = br.readLine()) != null) {
//				System.out.println("!"+line);
//				response.append(line);
//			}
//			
//			System.out.println(response.toString());
//			return response.toString();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		return "fail";
//	}
	
	// 메인화면 게시글 지도 이미지
	@GetMapping(value="/getBoardImg", produces = MediaType.IMAGE_JPEG_VALUE)
	public byte[] getBoardImg(@RequestParam String center, @RequestParam String path) throws MalformedURLException, IOException {
		String url = "https://maps.googleapis.com/maps/api/staticmap?" +
				"center=" + URLEncoder.encode(center, StandardCharsets.UTF_8) +
				"&size=290x220&path=" + URLEncoder.encode(path, StandardCharsets.UTF_8) +
				"&key=" + GoogleRKey;
		//System.out.println(url);
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
		//System.out.println(url);
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
	public List<String> getRoute(@RequestBody Map<String,Object> mapReq) {
		String travelMode = (String)mapReq.get("travelMode");
		List<String> placeIds = (List<String>)mapReq.get("placeIds");
		//System.out.println(travelMode);
		//System.out.println(placeIds);
		return eSvc.getRoute(travelMode, placeIds);
	}
	
	// ai 견적 확인
	@PostMapping("/searchCost")
	public Map<String,Object> searchCost(int bno){
		return eSvc.searchCost(bno);
	}
	
	// ai 일정 추천
	@PostMapping("/searchAiRecommend")
	public List<Map<String,Object>> searchAiRecommend(@RequestBody Map<String, Object> mapReq) {
		// 게시글 번호로 일정 블록 정보 조회 
		List<Map<String, Object>> userBlocks = (List<Map<String, Object>>)mapReq.get("userBlocks");
		int bno = (Integer)mapReq.get("bno");
		String arrPlaceCity = (String)mapReq.get("arrPlaceCity");
		String date = (String)mapReq.get("date");
		String mapll = (String)mapReq.get("mapll");
		//System.out.println(userBlocks);
		
		return eSvc.searchAiRecommend(userBlocks, bno, arrPlaceCity, date, mapll);
	}
	
	// 이메일 보내기
	@PostMapping("/sendEmail")
	public String sendEmail(HttpSession session,@RequestBody Map<String, Object> mapReq) {
		String email = (String)mapReq.get("email");
		String nickName = (String)mapReq.get("nickName");
		String pageType = (String)mapReq.get("pageType");
		
		if("editInfo".equals(pageType)) {
			if(!mSvc.isExistEmail(email)) {
				try {
					String key = eSvc.sendEmail(email, nickName, pageType);
					session.setAttribute("key", key);
					return key;
				} catch (Exception e) {
					e.printStackTrace();
					return "fail";
				}
			} else {return "exist";}
			
		}else {
			if(mSvc.isExistEmail(email)) {
				try {
					String key = eSvc.sendEmail(email, nickName, pageType);
					session.setAttribute("key", key);
					return key;
				} catch (Exception e) {
					e.printStackTrace();
					return "fail";
				}
			} else {return "empty";}
		}
	}
	
	// 구글비전
	@PostMapping("/googleVision")
	public Map<String, Object> googleVision(@RequestParam("file") MultipartFile file) {
		Map<String, Object> query = eSvc.getVisionResult(file);
        return query;
    }
}
