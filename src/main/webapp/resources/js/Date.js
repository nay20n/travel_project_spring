// 날씨 세팅
const weatherIcon = {
    0: "☀️",
    1: "🌤️",
    2: "⛅",
    3: "☁️",
    45: "🌫️",
    48: "🌫️",

    51: "🌧️", 53: "🌧️", 55: "🌧️",
    56: "🌧️", 57: "🌧️",
    61: "🌧️", 63: "🌧️", 65: "🌧️",
    66: "🌧️", 67: "🌧️",
    80: "🌧️", 81: "🌧️", 82: "🌧️",

    71: "❄️", 73: "❄️", 75: "❄️",
    77: "❄️", 85: "❄️", 86: "❄️",
    
    95: "⛈️", 96: "⛈️", 99: "⛈️",
    
    100: "⛓️‍💥"
};
let weeklyWeather = [100,100,100,100,100,100,100];

// 날씨 이모지 넣어주는 함수
function setWeather(startr,startc){
	for(let i=0;i<7;i++){
		if(startc==7) {
			startc=0;
			startr++;
		}
		let target = "r" + startr + "c" + startc;
		//console.log(target);
		let html = $(`td[data-title=${target}]`).html();
		if(html.includes("br")) return;
		$(`td[data-title=${target}]`).html(weatherIcon[weeklyWeather[i]]+"<br/>"+$(`td[data-title=${target}]`).html());
		startc++;
	}
}

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
	
	// 오늘 날짜 기본 설정
	let startrc;
	
	// 날씨 정보 가져오기
	const lat = $("#banner").data("lat");
	const lng = $("#banner").data("lng");
	
	const params = new URLSearchParams({
	    "latitude": lat,
	    "longitude": lng,
	    "timezone": "Asia/Seoul",
	    "daily": "weather_code"
	});
	
	console.log("https://api.open-meteo.com/v1/forecast?"+params.toString());
	
	fetch("https://api.open-meteo.com/v1/forecast?"+params.toString())
	.then(function(response){
		return response.json();
	})
	.then(function(data){
		console.log(data);
		console.log(data.daily.weather_code);
		weeklyWeather = data.daily.weather_code;
		startrc = $(".today").data("title");
		//console.log(startrc);
		//let startr = Number(startrc.substring(1,2));
		//let startc = Number(startrc.substring(3));
		//console.log(startr+startc);
		//setTimeout(() => setWeather(startr,startc), 100);
	})
	.catch(function(error){
		alert("에러! : " + error);
	});
	
	// 데이트 피커 인풋
	$('#selectDate > input').daterangepicker({
		locale: {
			"separator": " ~ ",                     
			"format": 'YY년 MM월 DD일',     		
			"yearSuffix": "년",
			"daysOfWeek": ["일", "월", "화", "수", "목", "금", "토"],
			"monthNames": ["1월", "2월", "3월", "4월", "5월", "6월", "7월", "8월", "9월", "10월", "11월", "12월"],
			"showMonthAfterYear": true //년도 먼저 나오고, 뒤에 월 표시
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
			
	});
	
	// 어플라이 버튼을 클릭 시 인풋 클릭한걸로 치기 
	$('#selectDate > input').on('apply.daterangepicker', function(ev, picker) {
		$("#selectDate > input").trigger("click");
		//$('#selectDate > input').val(startDate + ' ~ ' + endDate);
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
			return response.text();
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


