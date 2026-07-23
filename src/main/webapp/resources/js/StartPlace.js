function getUrlParams() {     
	    var params = {};  
	    
	    window.location.search.replace(/[?&]+([^=&]+)=([^&]*)/gi, 
	    	function(str, key, value) { 
	        	params[key] = value; 
	        }
	    );     
	    
	    return params; 
}

let isActive = false;
let AdvancedMarkerElementObject;  // 전역변수. 마커.
let marker; 

$(function() {
	// <-이미지 클릭하면 전 페이지로
	$("#header > img").click(function() {
		history.back();
	});
	
	// 지도 클릭 시 버튼 활성화
	$("#searchPlace > img").click(function() {
		isActive = true;
		$("#nextBtn > div").addClass("coloredBtn");
	});
	
	// 다음 페이지 클릭하면 도착지 페이지로 이동
	$("#nextBtn > div").click(function(){
		
		// 도착 아이디 얻어오기
		let arrId = getUrlParams().arrId;
		// 출발 아이디 (일단 서울역)
		let startId = $(this).parent().attr("data-placeid");
		// bno 가져오기 
		let bno = getUrlParams().bno;
		
		if(isActive) // 활성화 됐을때만 이동 가능 
			location.href="date?arrId=" + arrId + "&startId=" + startId + "&bno=" + bno;
	});
});