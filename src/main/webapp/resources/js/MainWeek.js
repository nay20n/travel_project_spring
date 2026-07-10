$(function() {
	// ************왼쪽 장소들 ****************
	// 내 일정 클릭 
	
	$("#main > div:nth-child(1) > div:nth-child(2) > div").click(function(){
		fetch("../../MainWeekGetSelectedPlaces", initData)
		.then(function(response){
			return response.json();
		})
		.then(function(data){
			console.log(data);
			
			
		})
		.catch(function(error){
			alert("에러! : " + error);
		})
	});


	// **************캘린더********************
	// 일 단위로 이동
	$(".changeView > span:nth-child(1)").click(function() {
		location.href="day";
	});
	// 월 단위로 이동
	$(".changeView > span:nth-child(3)").click(function() {
		location.href="month";
	});
	// 일정확정하기
	$("#main > div:nth-child(2)>button:nth-child(3)").click(function() {
		let currentUrl = window.location.href;
		const url = new URL("./",currentUrl);
		let endIdx = url.pathname.length-1;
		
		location.href = url.pathname.substr(0, endIdx);
	});	
});