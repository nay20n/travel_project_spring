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
        google.maps.importLibrary('places'),
        google.maps.importLibrary("geometry")
    ]);
    AdvancedMarkerElementObject = AdvancedMarkerElement;

    // Get the gmp-map element.
    const mapElement = document.querySelector('gmp-map');
	const autocomplete = document.getElementById('autocomplete');

    // Get the inner map.
    const innerMap = mapElement.innerMap;

    // Set map options.
    innerMap.setOptions({
        mapTypeControl: false,
		streetViewControl: false
    });
    
	
	autocomplete.addEventListener("gmp-select", async (event) => {
		//console.log(event);
		const prediction = event.prediction || event.placePrediction;
		const place = prediction.toPlace();
		console.log(prediction);
	   	//alert(prediction.placeId);
	
	    if (!place) return;  // 장소 정보가 없으면 리턴
	
	    // 최신 Places API는 필요한 데이터 필드를 명시적으로 요청(fetchFields)해야 합니다.
	    await place.fetchFields({
	      fields: ["displayName", "location", "formattedAddress"]
	    });
	
	    console.log("선택한 장소 이름:", place.displayName);
	    console.log("선택한 장소 주소:", place.formattedAddress);
	    console.log("선택한 장소 좌표:", place.location.lat(), place.location.lng());
	
	    // 검색된 장소로 지도 이동
	    if (place.location) {
			// innerMap을 통해 지도 중심 변경 및 줌인
			//mapElement.innerMap.panTo(place.location);
			//mapElement.innerMap.setZoom(15);
			console.log(place.location);
			const lat = place.location.lat();
			const lng = place.location.lng();
	      
			mapElement.innerMap.moveCamera({
				center: place.location,
				zoom: 15
			});
			
			// DB에 장소 추가
			
			// Add Marker. (AdvancedMarkerElement로 마커 생성)
			if(marker != undefined) {
				marker.map = null;
				marker = undefined;
			}
			marker = new AdvancedMarkerElement({
				position: { lat, lng },   // event.latLng 객체도 가능
			    map: mapElement.innerMap
			});
			marker.addListener("click", (event) => {
				// 맨 상단에 해당 장소 삽입
				marker.map = null;
				marker = undefined;
			});
	    } else {
	      alert("선택한 장소의 위치 정보가 없습니다.");
	    }
  	});

	// map 변수는 이미 생성된 지도 인스턴스라고 가정합니다.
	google.maps.event.addListener(mapElement.innerMap, 'click', function(event) {
	    console.log(event);
	    const latLng = event.latLng;
	    console.log(latLng);
	    
	    // 클릭한 지점의 위도와 경도 추출
	    const lat = event.latLng.lat();
	    const lng = event.latLng.lng();
	    
	    console.log(`클릭한 위치의 위도: ${lat}, 경도: ${lng}`);
	    
	    // 클릭한 위치에 마커 생성 등 추가 작업 수행
		// click! ---> 마커 추가가 아닌 걸로. / 마커를 클릭하면 ->마커 취소 / 마커 아닌 곳을 클릭하면? --> 그건 아직 모르겠음.
		  
	});
    
}
void init();



