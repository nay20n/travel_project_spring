// 캘린더 객체
let calendar;
let aiCalendar;

// 일정 삽입 함수
function setBlocks(calendar) {
	let eventList = [];
	let bno = $("#main").data("bno");
	
	const jsonData = {
		"bno" : bno
	};
	const initData = {
		method: "post",
		headers: {
			"Content-Type": "application/json"
		},
		body: JSON.stringify(jsonData)
	};
	fetch("../../getAllBlocks", initData)
	.then(function(response){
		return response.json();
	})
	.then(function(data){
		for(let i=0;i<data.length;i++) {
			let block = data[i];
			
			let id = block.blockIdx;
	        let title = block.name;
	        let start = block.startTime;
	        let end = block.endTime;
	        let colorCode = block.colorCode;
	        
	        // 캘린더 데이터
	        eventList.push({
	            id: id,
	            title: title,
	            start: start,
	            end: end,
	            backgroundColor: colorCode,
	            color: '#000000'
	        });
	        
	        // 캘린더 비우기
	        calendar.clear();
	        
	        // 데이터 삽입
	        if (eventList.length > 0) {
		        calendar.createEvents(eventList);
		    }
		}
	    setTimeout(function() {
		    console.log($(".toastui-calendar-event-time").length);
		
		    $(".toastui-calendar-event-time").droppable({
		        drop: handleDropEvent
		    });
		}, 100);
		console.log(eventList);
	})
	.catch(function(error){
		alert("에러! : " + error);
	})
}

// 드래그로 장소추가
function handleDropEvent(event, ui) {
	const $droppable = $(event.target);
	//alert(ui.draggable.data("place-id"));
	//alert($droppable.data("event-id"));
	
	const jsonData = {
		"placeId" : ui.draggable.data("place-id"),
		"blockIdx": $droppable.data("event-id")
	};
	const initData = {
		method: "post",
		headers: {
			"Content-Type": "application/json"
		},
		body: JSON.stringify(jsonData)
	};
	fetch("../../modifyBlockPlace", initData)
	.then(function(response){
		return response.text();
	})
	.then(function(data){
		console.log(data);
	  	setBlocks(calendar);
	})
	.catch(function(error){
		alert("에러! : " + error);
	})
}

$(function() {
	// **************캘린더********************
	const Calendar = tui.Calendar;
	calendar = new Calendar("#calendar", {
	  defaultView: 'week',
	  week: {
	    showMilestone: false,
        showTask: false,
        taskView: false,
        eventView: ['time'] 
	  }
	});
	aiCalendar = new Calendar("#aiCalendar", {
	  defaultView: 'day',
	  isReadOnly: true,
	  width: "1000px",
	  week: {
	    showMilestone: false,
        showTask: false,
        taskView: false,
        enableDblClick: false,
  		enableClick: false,
        eventView: ['time']
	  }
	});
	setTimeout(function() {
	    $("#aiCalendar").addClass("hide");
	}, 100);
	// 날짜 설정
	calendar.setDate($("#calendar").data("start-date"));
	aiCalendar.setDate($("#calendar").data("start-date"));
	// 기존 블럭 생성
	setBlocks(calendar);
	
	// 기본 날짜 이동
	$("#moveNext").click(function(){
		calendar.move(1);
		aiCalendar.move(1);
	});
	$("#today").click(function(){
		calendar.today();
		aiCalendar.today();
	});
	$("#movePast").click(function(){
		calendar.move(-1);
		aiCalendar.move(-1);
	});
	
	// 캘린더 내 드래그로 일정추가
	calendar.on('selectDateTime', (eventObj) => {
		console.log(eventObj);
		let bno = $("#main").data("bno");
	  	
	  	calendar.clearGridSelections();
		const jsonData = {
			"bno" : bno,
			"startTime": eventObj.start.toISOString(),
	    	"endTime": eventObj.end.toISOString()
		};
		const initData = {
			method: "post",
			headers: {
				"Content-Type": "application/json"
			},
			body: JSON.stringify(jsonData)
		};
		fetch("../../addBlock", initData)
		.then(function(response){
			return response.json();
		})
		.then(function(data){
			console.log(data);
		  	setBlocks(calendar);
		})
		.catch(function(error){
			alert("에러! : " + error);
		})
	});
	
	// 일정 삭제
	$(".popupContainer > div:nth-child(2) > div:nth-child(2) > div:nth-child(3)").click(function(){
		let blockIdx = $(this).data("blockIdx");
		console.log(blockIdx);
		
		const jsonData = {
			"blockIdx" : blockIdx
		};
		const initData = {
			method: "post",
			headers: {
				"Content-Type": "application/json"
			},
			body: JSON.stringify(jsonData)
		};
		fetch("../../deleteBlock", initData)
		.then(function(response){
			return response.text();
		})
		.then(function(data){
			console.log(data);
		  	setBlocks(calendar);
		  	$(".popupContainer").addClass("hide");
			$(".popupContainer>div:nth-child(4)").addClass("hide");
		})
		.catch(function(error){
			alert("에러! : " + error);
		})
	});
	
	// 일정 이동 및 시간 변경
	calendar.on('beforeUpdateEvent', (updateData) => {
		const { event, changes } = updateData;
		
		// 변화가 없다면 종료
		if(changes.start==null&&changes.end==null) return;
		console.log(event);
		console.log(changes);
		
		// 시간 세팅
		let startTime = event.start;
		let endTime = event.end;
		let blockIdx = event.id;
		if(changes.start!=null) startTime = changes.start;
		if(changes.end!=null) endTime = changes.end;
		
		const jsonData = {
			"blockIdx" : blockIdx,
			"startTime": startTime.toDate().toISOString(),
	    	"endTime": endTime.toDate().toISOString()
		};
		
		console.log(jsonData.startTime);
		const initData = {
			method: "post",
			headers: {
				"Content-Type": "application/json"
			},
			body: JSON.stringify(jsonData)
		};
		fetch("../../modifyBlockTime", initData)
		.then(function(response){
			return response.text();
		})
		.then(function(data){
			console.log(data);
		  	setBlocks(calendar);
		})
		.catch(function(error){
			alert("에러! : " + error);
		})
	});
    
	// 일 단위로 이동
	$(".changeView > span:nth-child(1)").click(function() {
		calendar.changeView('day');
		$("#calendar").addClass("changeToDay");
		$(this).parent().find("span").removeClass("selectedView");
		$(this).addClass("selectedView");
		$("#goNext").addClass("hide");
		$("#makeAiBlock").removeClass("hide");
	});
	
	// 주 단위로 이동
	$(".changeView > span:nth-child(2)").click(function() {
		calendar.changeView('week');
		$("#calendar").removeClass("changeToDay");
		$(this).parent().find("span").removeClass("selectedView");
		$(this).addClass("selectedView");
		$("#aiCalendar").addClass("hide");
		$("#goNext").removeClass("hide");
		$("#makeAiBlock").addClass("hide");
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
	// ai 추천
	$("#main > div:nth-child(2)>button:nth-child(4)").click(function() {
		$("#aiCalendar").removeClass("hide");
	});
});