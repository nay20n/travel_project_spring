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
	        
	        // 데이터 삽입
	        if (eventList.length > 0) {
		        calendar.createEvents(eventList);
		    }
		}
		console.log(eventList);
	})
	.catch(function(error){
		alert("에러! : " + error);
	})
}

$(function() {

	// **************캘린더********************
	const Calendar = tui.Calendar;
	const calendar = new Calendar("#calendar", {
	  defaultView: 'week',
	  week: {
	    showMilestone: false,
        showTask: false,
        taskView: false,
        eventView: ['time'] 
	  }
	});
	// 기존 블럭 생성
	setBlocks(calendar);
	
	// 드래그로 일정추가
	calendar.on('selectDateTime', (eventObj) => {
		console.log(eventObj);
		let bno = $("#main").data("bno");
	  	const eventData = {
	    	start: eventObj.start,
	    	end: eventObj.end
	  	};
	  	
		const jsonData = {
			"bno" : bno,
			"startTime": eventObj.start,
	    	"endTime": eventObj.end
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
		  	calendar.createEvents([eventData]);
		  	calendar.clearGridSelections();
		})
		.catch(function(error){
			alert("에러! : " + error);
		})
	});
	
	// 일정 이동(화면)
	calendar.on('beforeUpdateEvent', (updateData) => {
		const { event, changes } = updateData;
		calendar.updateEvent(event.id, event.calendarId, changes);
	});
    
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