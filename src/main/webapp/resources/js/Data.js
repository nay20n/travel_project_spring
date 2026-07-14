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
		
		// bno 가져오기
		let bno = getUrlParams().bno;
		// 도착 아이디 가져오기 
		let arrId = getUrlParams().arrId;
		let arrCity = "부산광역시";
		// 출발 아이디 가져오기 
		let startId = getUrlParams().startId;
		// 여행 기간 설정 
		let endDate = "2026-09-02";	
		let startDate = "2026-09-01";
		
		const jsonData = {
			arrId : arrId,
			startId : startId,
			endDate : endDate,
			startDate : startDate, 
			arrCity : arrCity,
			bno : bno
		};
		const initData = {
			method: "post",
			headers: { "Content-Type": "application/json" },
			body: JSON.stringify(jsonData)
		};
		
		fetch("../insertBoard", initData)
		.then(function(response){
			return response.json();
		})
		.then(function(data){
			console.log(data);
			
			let bno = data;
			// alert(bno);
			location.href="../plan/" + bno;
			
		})
		.catch(function(error){
			alert("에러! : " + error);
		});
		
	});
	
});