
let isActive = false;

let marker;  // 전역변수. 지도에서 클릭해서 선택한 위치(위도/경도).
let AdvancedMarkerElementObject;  // 전역변수. 마커.

// 지도 이동 (파라미터 - 위도&경도)
function moveCameraInTheMap(lat, lng) {
    const mapElement = document.querySelector('gmp-map');

	mapElement.innerMap.moveCamera({
		center: {lat, lng},
		zoom: 11
	});
}

$(function(){
	// <-이미지 클릭하면 전 페이지로
	$("#header > img").click(function() {
		history.back();
	});
	
	// 도시들 span 클릭 시
	$("#recomendPlace > div:nth-child(2) > span").click(function() {
		// 선택한 span이라면 선택 취소 
		if($(this).hasClass("coloredPlace")){ 
			$(this).removeClass("coloredPlace");
			$("#nextBtn > div").removeClass("coloredBtn");
			isActive = false;
			// Remove a marker
			if(marker != undefined) {
				marker.map = null;
				marker = undefined;
			}
			return;
		}
		$(this).parent().find("span").removeClass("coloredPlace");
		$(this).addClass("coloredPlace");
		
		// // 지도 이동 
		const lat = Number($(this).attr("DATA-LAT"));
		const lng = Number($(this).attr("DATA-LNG"));
		moveCameraInTheMap(lat, lng);
		
		// 마커 그리기 전 선택 된 것들 삭제
		if(marker != undefined) {
			marker.map = null;
			marker = undefined;
		}
		
		// 마커 그리기
	    const mapElement = document.querySelector('gmp-map');
		marker = new AdvancedMarkerElementObject({
			position: { lat, lng },   // event.latLng 객체도 가능
		    map: mapElement.innerMap,
		});	
		
		// Set the placeid of '#nextBtn'
		$("#nextBtn").attr("data-placeid", $(this).attr("data-placeid"));
		
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
	});
	
	
	// 다음 페이지 클릭하면 도착지 페이지로 이동
	$("#nextBtn > div").click(function(){
	
		let arrId = $(this).parent().attr("data-placeid");
		
		if(isActive) // 활성화 됐을때만 이동 가능 
			location.href="start?arrId=" + arrId;
	});
	
});