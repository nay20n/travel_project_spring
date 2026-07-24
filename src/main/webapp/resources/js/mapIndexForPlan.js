'use strict';
/*
 * @license
 * Copyright 2025 Google LLC. All Rights Reserved.
 * SPDX-License-Identifier: Apache-2.0
 */

async function init() {
    const [
        markerLib,
        mapsLib,
        placesLib,
        geometryLib
    ] = await Promise.all([
        google.maps.importLibrary('marker'),
        google.maps.importLibrary('maps'),
        google.maps.importLibrary('places'),
        google.maps.importLibrary('geometry')
    ]);
    encoding = geometryLib.encoding;
    AdvancedMarkerElement = markerLib.AdvancedMarkerElement;
    PinElement = markerLib.PinElement;

    // Get the gmp-map element.
    mapElement = document.querySelector('gmp-map');
	const autocomplete = document.getElementById('autocomplete');

    // Get the inner map.
    const innerMap = mapElement.innerMap;

    // Set map options.
    innerMap.setOptions({
        mapTypeControl: false,
		streetViewControl: false
    });
    
	// 장소를 선택했을때 DB에 장소 저장
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
		fetch("../../addPlace", initData)
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
		
	    // 검색된 장소로 지도 이동
	    if (place.location) {
			//console.log(place.location);
			// 지도 이동 
			const lat = place.location.lat();
			const lng = place.location.lng();
			mapElement.innerMap.moveCamera({
				center: place.location,
				zoom: 15
			});
	    } else {
	      alert("선택한 장소의 위치 정보가 없습니다.");
	    }
	    
	    // 지도에 마커 추가
	    drawMarker(place.location.lat(), place.location.lng(), place.id);
  	});
  	
  	return mapElement.innerMap;
}

mapReady = init();



