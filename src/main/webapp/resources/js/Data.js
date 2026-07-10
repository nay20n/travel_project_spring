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

$(function(){
	// <-이미지 클릭하면 전 페이지로
	$("#header > img").click(function() {
		history.back();
	});
	
	$("#selectDate > img").click(function() {
		isActive = true;
		$("#nextBtn > div").addClass("coloredBtn");
	});
	
	// 다음 페이지 클릭하면 도착지 페이지로 이동
	$("#nextBtn > div").click(function(){
	
		// 도착 아이디 가져오기 
		let arrId = getUrlParams().arrId;
		// 출발 아이디 가져오기 
		let startId = getUrlParams().startId;
		// 여행 기간 설정 
		//let endDate = 	
		//let staertDate = 	
		
		location.href="../plan/1";
	});
	
});