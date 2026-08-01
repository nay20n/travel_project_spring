
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

// 마커 생성 & 삭제로직 
function drawMarker(lat, lng) {
	
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
	
	// 마커를 클릭 했을때 (버튼 비활성화, 마커 삭제)	
	marker.addListener("gmp-click", (event) => {
		$("#nextBtn > div").removeClass("coloredBtn");
		isActive = false;
		marker.map = null;
		marker = undefined;
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
		const lat = Number($(this).attr("data-lat"));
		const lng = Number($(this).attr("data-lng"));
		moveCameraInTheMap(lat, lng);
		drawMarker(lat, lng);
		
		// Set the placeid of '#nextBtn'
		$("#nextBtn").attr("data-placeid", $(this).attr("data-placeid"));
		
		// Activate '다음' 버튼.
		$("#nextBtn > div").addClass("coloredBtn");
		isActive = true;
		
	});
	// 파일 업로드 클릭
	$("#aiPlace > div:nth-child(4)").click(function(){
		$("#aiPlace > input ").click();
	});
	
	//파일 업로드를 하면 
	$("#aiPlace > input").change(function(){
			
		if(this.files && this.files[0]){
	        let imgUrl = URL.createObjectURL(this.files[0]);
	        $("#aiPlace > img").attr("src", imgUrl);
			
		}
		
	});
	// 이미지 분석 버튼 클릭
	$("#aiPlace > div:nth-child(5)").click(function(){
		let fileInput = $("#aiPlace > input")[0];
    	let file = fileInput.files[0];
    	
		// 장소를 업로드 안했는데 분석 버튼을 클릭했을 경우 return 
		if( $("#aiPlace > img").attr("src")=='../resources/img/장소대체이미지.png'){
			Toastify({
				text: "장소를 업로드 하세요.",
				duration: 3000,
				newWindow: true,
				close: true,
				gravity: "top",
				position: "center",
				stopOnFocus: true,
				style: {
			    background: "linear-gradient(to left, #E3D4FF, #925DE8)",
			  }
			}).showToast();
			return;
		}
		
		// 파일 데이터를 담을 FormData 생성
	    let formData = new FormData();
	    formData.append("file", file); // 'file'이라는 이름으로 담음
		const initData = {
			method: "post",
			body: formData
		};
	    fetch("/TravelPlanner/googleVision",initData)
	    .then(function(response){
	        return response.json(); 
	    })
	    .then(function(data){
	       console.log("구글 비전 결과:", data);
	       
	       if(data.success === true || data.success === "true"){
	      		moveCameraInTheMap(data.lat, data.lng);	 	
	       		drawMarker(data.lat, data.lng);
	       } else if (data.error) { //에러 났을 때
				alert("error : " + data.error);
	       
	       } else {
	       		alert("사진에서 랜드마크를 찾을 수 없습니다.");
	       }
	       
	       //drawMarker(data.lat, data.lng);
	    })
	    .catch(function(error){
	        alert("에러: " + error);
	    });
	});
	
	
	// 다음 페이지 클릭하면 도착지 페이지로 이동
	$("#nextBtn > div").click(function(){
	
		let arrId = $(this).parent().attr("data-placeid");
		
		if(isActive) // 활성화 됐을때만 이동 가능 
			location.href="start?arrId=" + arrId;
	});
	
});