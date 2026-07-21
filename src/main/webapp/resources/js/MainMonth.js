function clickInput() {
	$("#calender").click();
}

let plus=`<svg class="bs plus" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="currentColor" class="size-6">
			<path fill-rule="evenodd" d="M12 3.75a.75.75 0 0 1 .75.75v6.75h6.75a.75.75 0 0 1 0 1.5h-6.75v6.75a.75.75 0 0 1-1.5 0v-6.75H4.5a.75.75 0 0 1 0-1.5h6.75V4.5a.75.75 0 0 1 .75-.75Z" clip-rule="evenodd" />
		</svg>`;
let del= `<svg class="bs delete" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="currentColor" class="size-6">
			<path fill-rule="evenodd" d="M16.5 4.478v.227a48.816 48.816 0 0 1 3.878.512.75.75 0 1 1-.256 1.478l-.209-.035-1.005 13.07a3 3 0 0 1-2.991 2.77H8.084a3 3 0 0 1-2.991-2.77L4.087 6.66l-.209.035a.75.75 0 0 1-.256-1.478A48.567 48.567 0 0 1 7.5 4.705v-.227c0-1.564 1.213-2.9 2.816-2.951a52.662 52.662 0 0 1 3.369 0c1.603.051 2.815 1.387 2.815 2.951Zm-6.136-1.452a51.196 51.196 0 0 1 3.273 0C14.39 3.05 15 3.684 15 4.478v.113a49.488 49.488 0 0 0-6 0v-.113c0-.794.609-1.428 1.364-1.452Zm-.355 5.945a.75.75 0 1 0-1.5.058l.347 9a.75.75 0 1 0 1.499-.058l-.346-9Zm5.48.058a.75.75 0 1 0-1.498-.058l-.347 9a.75.75 0 0 0 1.5.058l.345-9Z" clip-rule="evenodd" />
		</svg>`;
function drawDel() {
	//$('.daterangepicker td.active').html(del);
	$('.daterangepicker td.active').each(function(index) {
		let originalNum = $(this).text();
		$(this).attr("num", originalNum);
		$(this).html(del);
	});	
}
function drawPlus() {
	$(".daterangepicker td.active").each(function(index) {
		let title = $(this).attr('data-title'); 
		let row = Number(title.split('r')[1].split('c')[0]); // 행
		let col = Number(title.split('c')[1]);              // 열
		let target;	
		
		if(index == 0){
			if(col==0){
				target = "r" + (row-1) + "c" + (6);
			} else {
				target = "r" + (row) + "c" + (col-1);
			}
		} 
		else if(index == 1){
			if(col==6){
				target = "r" + (row+1) + "c" + (0);
			} else {
				target = "r" + (row) + "c" + (col+1);
			}
		}
		let originalNum = $(`.left td[data-title=${target}]`).text();
		//alert(originalNum);
		$(`.left td[data-title=${target}]`).attr("num", originalNum);
		$(`.left td[data-title=${target}]`).html(plus);
	});
}

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
	/*************달력*****************/
	let sDate = $('#daterange').attr('data-start'); 
    let eDate = $('#daterange').attr('data-end');  
	// 데이터 피컷 인풋 
	$("#calender").daterangepicker({
		locale: {
			"format": 'YYYY.MM.DD',
			"yearSuffix": "년",
			"daysOfWeek": ["일", "월", "화", "수", "목", "금", "토"],
			"monthNames": ["1월", "2월", "3월", "4월", "5월", "6월", "7월", "8월", "9월", "10월", "11월", "12월"],
		},
		"startDate" : sDate, 
		"endDate" : eDate,
		"inline": true,
		"linkedCalendars": false,
		"autoUpdateInput": false,
		"autoApply": true
		}, function(start, end, label) {
		console.log("A new date selection was made: " + start.format('YYYY-MM-DD') + ' to ' + end.format('YYYY-MM-DD'));
	});

	// 이전 달, 다음 달 클릭 시 -> (+),(쓰레기통) 다시 그림.
	$('#calender').on('showCalendar.daterangepicker', function (ev, picker) {
		//alert("show");
	    picker.container
	        .find('.prev, .next')
	        .off('click.custom')
	        .on('click.custom', function () {
				setTimeout(drawPlus, 100);
				setTimeout(drawDel, 100);
	        });
	});
	
	setTimeout(drawPlus, 100);
	setTimeout(drawDel, 100);
	
	//삭제 버튼 
	$(document).on("click", ".delete", function() {
		let title = $(this).parent().attr('data-title'); 
		let row = Number(title.split('r')[1].split('c')[0]); // 행
		let col = Number(title.split('c')[1]);              // 열
		let target;
		
		let index = $("td .delete").index(this);
		//alert(index);  // 시작=0, 끝=1
		
		if(index == 0){  // 시작날짜인 경우
			// 1. 왼쪽에 있는 숫자(날짜) 복원
			if(col==0){
				target = "r" + (row-1) + "c" + (6);
			} else {
				target = "r" + (row) + "c" + (col-1);
			}
			let originalNum = $(`.left td[data-title=${target}]`).attr("num");
			$(`.left td[data-title=${target}]`).text(originalNum);

			// 2. (+, 쓰레기통) 오른쪽으로 옮기기
			$(this).parent().removeClass("active"); 
			$(this).parent().removeClass("start-date");
			$(this).parent().html(plus);

			if(col==6){
				target = "r" + (row+1) + "c" + (0);
			} else {
				target = "r" + (row) + "c" + (col+1);
			}

			originalNum = $(`.left td[data-title=${target}]`).text();
			$(`.left td[data-title=${target}]`).attr("num", originalNum);
			$(`.left td[data-title=${target}]`).html(del);			
			$(`.left td[data-title=${target}]`).removeClass("in-range");
			$(`.left td[data-title=${target}]`).addClass("active start-date");
		}
		else if(index == 1){  // 끝날짜인 경우
			// 1. 오른쪽에 있는 숫자(날짜) 복원
			if(col==6){
				target = "r" + (row+1) + "c" + (0);
			} else {
				target = "r" + (row) + "c" + (col+1);
			}
			let originalNum = $(`.left td[data-title=${target}]`).attr("num");
			$(`.left td[data-title=${target}]`).text(originalNum);

			// 2. (+, 쓰레기통) 왼쪽으로 옮기기
			$(this).parent().removeClass("active"); 
			$(this).parent().removeClass("end-date");
			$(this).parent().removeClass("in-range");
			$(this).parent().html(plus);

			if(col==0){
				target = "r" + (row-1) + "c" + (6);
			} else {
				target = "r" + (row) + "c" + (col-1);
			}

			originalNum = $(`.left td[data-title=${target}]`).text();
			$(`.left td[data-title=${target}]`).attr("num", originalNum);
			$(`.left td[data-title=${target}]`).html(del);			
			$(`.left td[data-title=${target}]`).removeClass("in-range");
			$(`.left td[data-title=${target}]`).addClass("active end-date");
		}
		
	});
	$(document).on("click", ".plus", function() {
		let title = $(this).parent().attr('data-title'); 
		let row = Number(title.split('r')[1].split('c')[0]); // 행
		let col = Number(title.split('c')[1]);              // 열
		let target;
		
		let index = $("td .plus").index(this);
		//alert(index);  // 시작=0, 끝=1

		if(index == 0){  // 시작날짜인 경우
			// 1. (+)버튼 왼쪽으로 옮기기  
			if(col==0){
				target = "r" + (row-1) + "c" + (6);
			} else {
				target = "r" + (row) + "c" + (col-1);
			}
			let originalNum = $(`.left td[data-title=${target}]`).text();
			$(`.left td[data-title=${target}]`).attr("num", originalNum);
			$(`.left td[data-title=${target}]`).html(plus);
			$(".left .start-date").removeClass("start-date");
			$(`.left td.active:eq(${index})`).removeClass("active");
			
			// 2. (쓰레기통) 왼쪽으로 옮기기
			$(this).parent().addClass("active start-date");
			$(this).parent().html(del);
			if(col==6){
				target = "r" + (row+1) + "c" + (0);
			} else {
				target = "r" + (row) + "c" + (col+1);
			}
			originalNum = $(`.left td[data-title=${target}]`).attr("num");
			$(`.left td[data-title=${target}]`).html(originalNum);			
			$(`.left td[data-title=${target}]`).addClass("in-range");
		} 
		else if(index == 1){  // 끝날짜인 경우
			// 1. (+)버튼 오른쪽으로 옮기기  
			if(col==6){
				target = "r" + (row+1) + "c" + (0);
			} else {
				target = "r" + (row) + "c" + (col+1);
			}
			let originalNum = $(`.left td[data-title=${target}]`).text();
			$(`.left td[data-title=${target}]`).attr("num", originalNum);
			$(`.left td[data-title=${target}]`).html(plus);
			$(".left .end-date").removeClass("end-date");
			$(`.left td.active:eq(${index})`).removeClass("active");
			
			// 2. (쓰레기통) 오른쪽으로 옮기기
			$(this).parent().addClass("active end-date");
			$(this).parent().html(del);

			if(col==0){
				target = "r" + (row-1) + "c" + (6);
			} else {
				target = "r" + (row) + "c" + (col-1);
			}

			originalNum = $(`.left td[data-title=${target}]`).attr("num");
			$(`.left td[data-title=${target}]`).html(originalNum);			
			$(`.left td[data-title=${target}]`).addClass("in-range");
		}
		
	});
	
	$(document).on("click", ".calendar-table svg", function() {
		alert("");
		
	});
	
	// 일정확정하기
	$("#main>button").click(function() {
		let currentUrl = window.location.href;
		const url = new URL("./",currentUrl);
		let endIdx = url.pathname.length-1;
		
		location.href = url.pathname.substr(0, endIdx);
	});
});


