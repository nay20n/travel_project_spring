'use strict';
/*
 * @license
 * Copyright 2025 Google LLC. All Rights Reserved.
 * SPDX-License-Identifier: Apache-2.0
 */

async function init() {
    // Request the needed libraries.
    let [{ AdvancedMarkerElement }] = await Promise.all([
        google.maps.importLibrary('marker'),
        google.maps.importLibrary('maps'),
        google.maps.importLibrary('places')
    ]);
    AdvancedMarkerElementObject = AdvancedMarkerElement;

    const mapElement = document.querySelector('gmp-map');
	const autocomplete = document.getElementById('autocomplete');

    // Get the inner map.
    const innerMap = mapElement.innerMap;

    // Set map options.
    innerMap.setOptions({
        mapTypeControl: false,
		streetViewControl: false
    });
    
	// 장소를 선택했을때 
	autocomplete.addEventListener("gmp-select", async (event) => {
		const prediction = event.prediction || event.placePrediction;
		const place = prediction.toPlace();
		//console.log(place);
	
	    if (!place) return;  // 장소 정보가 없으면 리턴
	
	    await place.fetchFields({
	      fields: [
	      	"id",
	      	"displayName", 
	      	"formattedAddress",
	      	"primaryTypeDisplayName",
			"location",            // 위도, 경도
			"regularOpeningHours", // 영업시간
			"websiteURI",          // 웹사이트 URL
			"photos",              //  장소 사진 목록
	      	]
	    });
	    let businessHoursList = place.regularOpeningHours ? place.regularOpeningHours.weekdayDescriptions : [];
		let businessHours = businessHoursList.join('<br/>'); 
		//console.log(businessHours);
	    let photoList = place.photos ? place.photos.slice(0, 5).map(photo => photo.getURI())  : [];
	    let photos = photoList.join(' ');
	    //console.log(photos);
	    
	    // 데이터 추출 및 매핑
		const jsonData = {
			"placeId": place.id,
			"name": place.displayName,
			"address": place.formattedAddress,
			"category":  place.primaryTypeDisplayName,
			"lat": place.location.lat(),
			"lng": place.location.lng(),
			"businessHours": businessHours,
			"websiteUrl": place.websiteURI || null,
			"photos": photos
		};
		const initData = {
			method: "post",
			headers: {
				"Content-Type": "application/json"
			},
			body: JSON.stringify(jsonData)
		};
		fetch("../addPlace", initData)
		.then(function(response){
			return response.text();
		})
		.then(function(data){
			console.log(data);
		})
		.catch(function(error){
			alert("에러! : " + error);
		})
	
		console.log("전체 장소 데이터:", jsonData);
		
	    // 검색된 장소로 지도 이동 및 마커 표시
	    if (place.location) {
			//console.log(place.location);
			// 지도 이동 
			const lat = place.location.lat();
			const lng = place.location.lng();
			mapElement.innerMap.moveCamera({
				center: place.location,
				zoom: 15
			});
			
			// 마커 그리기 전 선택 된 것들 삭제
			$(".coloredPlace").removeClass("coloredPlace");
			if(marker != undefined) {
				marker.map = null;
				marker = undefined;
			}
			// 마커 그리기
			marker = new AdvancedMarkerElement({
				position: { lat, lng },   // event.latLng 객체도 가능
			    map: mapElement.innerMap,
			});
						
			// placeId 먹이'
			$("#nextBtn").attr("data-placeid", place.id);
				
			// Activate '다음' 버튼.
			$("#nextBtn > div").addClass("coloredBtn");
			isActive = true;
			
			// 마커를 클릭 했을때 (버튼 비활성화, 마커 삭제)
			marker.addListener("gmp-click", (event) => {
				$("#nextBtn > div").removeClass("coloredBtn");
				isActive = false;
				marker.map = null;
				marker = undefined;
			});
		
	    } else {
	      alert("선택한 장소의 위치 정보가 없습니다.");
	    }
  	});
}
void init();



