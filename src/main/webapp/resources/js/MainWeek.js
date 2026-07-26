// 캘린더 객체
let calendar;
let aiCalendar;
// 일정에 들어간 이벤트들
let eventList;

// 지도
let routeLine=null; // 경로
let mapElement;
let encoding; // 구글맵의 geometryLib.encoding 클래스
let AdvancedMarkerElement;  // 구글맵의 마커 클래스
let PinElement; // 구글맵의 핀 클래스
let mapReady; // 준비 된 mapElement.innerMap
let markers = []; // 마커 배열
let myPlaceMarkers = []; // 내 일정에 있는 장소 마커 배열

// 이동 수단 필드
let travelModeArr = ["DRIVE","DRIVE","TRANSIT","WALK","BICYCLE"];
let travelModeIdx = 0;

// 지도에 경로 그리기
function drawRoute(encodedPolyline) {
    // 기존 경로가 있다면 제거
    if (routeLine!=null) { routeLine.setMap(null); }
    // encodedPolyline → 좌표 배열
    const path = encoding.decodePath(encodedPolyline);
    // 지도에 경로 그리기
    routeLine = new google.maps.Polyline({
        path: path,
        map: mapElement.innerMap,
        strokeColor: "#ff0000",
        strokeOpacity: 1,
        strokeWeight: 5
    });
}

// 지도에 마커 그리기
async function drawMarker(lat, lng, placeId) {
	// 지도가 준비될 때까지 기다림
    const map = await mapReady;
    let bno = $("#main").data("bno");
    
	let marker = new AdvancedMarkerElement({
		position: { lat, lng },   // event.latLng 객체도 가능
	    map: map,
	    zIndex: 1
	});
	
	marker.placeId = placeId;
	
    marker.addListener("gmp-click", function() {
    	// 정보창 팝업
		clickPlaceTitle(placeId);
		// 초기화 후 해당 장소로 검색
		$("#main>div:nth-child(1)>div:nth-child(2)>div").removeClass("isCheckedBtn");
		$(".place").remove();
		placePageNum = 1;
		
		mapping = "getSerchedPlace";
		placeNewPage(placeId, bno);
    });
    
	markers.push(marker);
}

// 지도에 내 일정 마커 그리기
async function drawMyMarker(lat, lng) {
	// 지도가 준비될 때까지 기다림
    const map = await mapReady;
    
    // 마커 디자인
    const myPin = new PinElement({
	    background: "#0000FF",
	    borderColor: "#FFFFFF",
	    glyphColor: "#FFFFFF"
	});
    
	let marker = new AdvancedMarkerElement({
		position: { lat, lng },
	    map: map,
	    content: myPin,
	    zIndex: 3
	});

    marker.addListener("gmp-click", function() {
        console.log("내 일정 마크이다.");
    });
    
	myPlaceMarkers.push(marker);
}

// 마커 삭제하기
function removeMarker(markerArr) {
	for (let i=0;i<markerArr.length;i++) {
		markerArr[i].setMap(null);
	}
	markerArr = [];
}

// 일정 삽입 함수
function setBlocks(calendar) {
	eventList = [];
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
		// 내 일정 마커를 지우고 다시 생성
		removeMarker(myPlaceMarkers);
		
		for(let i=0;i<data.length;i++) {
			let block = data[i];
			
			let id = block.blockIdx;
	        let title = block.name;
	        let start = block.startTime;
	        let end = block.endTime;
	        let colorCode = block.colorCode;
	        let placeId = block.placeId;
	        let lat = block.lat;
	        let lng = block.lng;
	        
	        // 만약 장소 데이터가 있다면 내 일정 마커를 추가
	        if(lat!=null)
	        	drawMyMarker(lat, lng);
	        
	        // 캘린더 데이터
	        eventList.push({
	            id: id,
	            title: title,
	            start: start,
	            end: end,
	            backgroundColor: colorCode,
	            color: '#000000',
	            body: placeId
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
		//console.log(eventList);
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
	console.log("장소 삽입");
	
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
		//console.log(data);
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
	  id: 'calendar',
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
	  id: 'aiCalendar',
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
		})
		.catch(function(error){
			alert("에러! : " + error);
		})
	  	$(".popupContainer").addClass("hide");
		$(".popupContainer>div:nth-child(2)").addClass("hide");
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
		setBlocks(calendar);
		$("#calendar").addClass("changeToDay");
		$(this).parent().find("span").removeClass("selectedView");
		$(this).addClass("selectedView");
		$("#goNext").addClass("hide");
		$("#makeAiBlock").removeClass("hide");
		$("#map").removeClass("hide");
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
		$("#toPlan").addClass("hide");
		$("#closeAi").addClass("hide");
		$("#map").addClass("hide");
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
		$("#toPlan").removeClass("hide");
		$("#closeAi").removeClass("hide");
	});
	// ai 추천 반영버튼
	$("#toPlan").click(function() {
		alert("반영하기!");
	});
	// ai 추천 닫기
	$("#closeAi").click(function() {
		$("#aiCalendar").addClass("hide");
		$("#toPlan").addClass("hide");
		$("#closeAi").addClass("hide");
	});
	// 내 일정 경로 표시(이동수단의 첫번째 버튼 클릭 시 경로 보여줌/그 외>이동수단만 변경)
	$(".popupContainer>div:nth-child(4)>span").click(function() {
		travelModeIdx = $(this).index();
		$(".popupContainer").addClass("hide");
		$(".popupContainer>div:nth-child(4)").addClass("hide");
		if($(".dayView").hasClass("selectedView")&&$(this).index()==0){
			//alert(calendar.getDate().toDate().toISOString());
			let date = calendar.getDate().toDate();
			let yyyy = date.getFullYear();
			let mm = String(date.getMonth() + 1).padStart(2, "0");	
			let dd = String(date.getDate()).padStart(2, "0");
			date = `${yyyy}-${mm}-${dd}`;
			//console.log(date);
			
			//해당 날짜에서 장소가 들어간 블럭만 추출
			let events = eventList
			.filter(event => {
		        const temp = event.start.slice(0, 10);
		        return (temp == date) && (event.body != null);
		    })
		    .sort((a, b) => {
		        return a.start - b.start
		    });
		    
		    // 장소가 들어간 일정이 하나거나 없다면 종료
		    if(events.length<2) return;
		    
		    // 장소가 들어있다면 경로 그리기
		    let placeIds = [];
		    for(let i=0;i<events.length;i++){
		    	let place = events[i];
		    	placeIds.push(place.body);
		    }
		    console.log(placeIds);
		    fetch("../../getRoute", {
			    method: "POST",
			    headers: {
			        "Content-Type": "application/json"
			    },
			    body: JSON.stringify({
			        placeIds: placeIds,
			        travelMode: travelModeArr[travelModeIdx]
			    })
			})
			.then(function(response) {
			    return response.json();
			})
			.then(function(data) {
        		if (!data.routes || data.routes.length == 0) {
        			Toastify({
					  text: "경로를 찾을 수 없습니다.",
					  duration: 3000,
					  newWindow: true,
					  close: true,
					  gravity: "top",
					  position: "center",
					  stopOnFocus: true,
					  style: {
					    background: "linear-gradient(to left, #E3D4FF, #925DE8)",
					  }
					}).showToast();
			        console.error("경로 데이터가 없습니다.", data);
			        return;
			    }
			    encodedPolyline = data.routes[0].polyline.encodedPolyline;
        		console.log(encodedPolyline);
        		drawRoute(encodedPolyline);
			})
			.catch(function(error) {
			    alert("에러! : " + error);
			});
		}
	});
});