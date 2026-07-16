$(function() {
	// **************캘린더********************
	// 일 단위로 이동
	$(".changeView > span:nth-child(1)").click(function() {
		location.href="day";
	});
	// 주 단위로 이동
	$(".changeView > span:nth-child(2)").click(function() {
		location.href="week";
	});
	
	// 데이터 피컷 인풋 
	$('#calender').daterangepicker({
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
		    //console.log("A new date selection was made: " + start.format('YYYY-MM-DD') + ' to ' + end.format('YYYY-MM-DD'));
		});
		$('#selectDate > input').on('apply.daterangepicker', function(ev, picker) {
			$("#selectDate > input").trigger("click");
	});
	
	
	
	// 일정확정하기
	$("#main>button").click(function() {
		let currentUrl = window.location.href;
		const url = new URL("./",currentUrl);
		let endIdx = url.pathname.length-1;
		
		location.href = url.pathname.substr(0, endIdx);
	});
});