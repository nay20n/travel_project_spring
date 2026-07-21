function getUrlParams() {     
    var params = {};  
    
    window.location.search.replace(/[?&]+([^=&]+)=([^&]*)/gi, 
    	function(str, key, value) { 
        	params[key] = value; 
        }
    );     
    
    return params; 
}
function clickInput() {
	$("#selectDate > input").click();
}
let isActive = false;
let startDate;
let endDate;

$(function(){

	// <-이미지 클릭하면 전 페이지로
	$("#header > img").click(function() {
		history.back();
	});
	
	// 데이터 피커 인풋
	$('#selectDate > input').daterangepicker({
		locale: {
			"separator": " ~ ",                     
			"format": 'YY년 MM월 DD일',     		
			"yearSuffix": "년",
			"daysOfWeek": ["일", "월", "화", "수", "목", "금", "토"],
			"monthNames": ["1월", "2월", "3월", "4월", "5월", "6월", "7월", "8월", "9월", "10월", "11월", "12월"],
		},
		"autoApply": true, 
		"showDropdowns": true, // 월/년 셀렉터 
		"minDate": new Date(),
		"maxYear": new Date().getFullYear() + 5,
		"inline": true,
		"linkedCalendars": false,
		autoUpdateInput: false
		
		}, function(start, end, label) {
			$('#selectDate > input').val(start.format('YY년 MM월 DD일') + ' ~ ' + end.format('YY년 MM월 DD일'));
			startDate = start.format('YY.MM.DD');
			endDate = end.format('YY.MM.DD');
			
			isActive = true;
			$("#nextBtn > div").addClass("coloredBtn");
		    //console.log("A new date selection was made: " + start.format('YYYY-MM-DD') + ' to ' + end.format('YYYY-MM-DD'));
	});
	// 어플라이 버튼을 클릭 시 인풋 클릭한걸로 치기 
	$('#selectDate > input').on('apply.daterangepicker', function(ev, picker) {
		$("#selectDate > input").trigger("click");
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
			//console.log(data);
			let bno = data;
			if(isActive)
				location.href="../plan/" + bno;
			
		})
		.catch(function(error){
			alert("에러! : " + error);
		});
		
	});
	
	
	
});


