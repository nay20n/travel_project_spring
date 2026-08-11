// 캘린더 객체
let calendar;
let aiCalendar;
// 일정에 들어간 이벤트들
let eventList;
let aiEventList;
let aiBlocks;

// 웹소켓
let webSocket=null;

// ai
let aiLock = false; // 실행 중이라면 막아줄 전역 변수

// 지도
let routeLine=[]; // 경로
let aiRouteLine=[]; // ai경로
let mapElement;
let encoding; // 구글맵의 geometryLib.encoding 클래스
let AdvancedMarkerElement;  // 구글맵의 마커 클래스
let PinElement; // 구글맵의 핀 클래스
let mapReady; // 준비 된 mapElement.innerMap
let markers = []; // 전체 마커 배열
let myPlaceMarkers = []; // 내 일정에 있는 장소 마커 배열
let aiPlaceMarkers = []; // ai에 있는 장소 마커 배열

// 이동 수단 필드
let travelModeArr = ["DRIVE","DRIVE","TRANSIT","WALK","BICYCLE"];
let travelModeIdx = 0;

// 지도에 경로 그리기
function drawRoute(encodedPolyline) {
    // encodedPolyline → 좌표 배열
    const path = encoding.decodePath(encodedPolyline);
    // 지도에 경로 그리기
    routeLine.push(new google.maps.Polyline({
        path: path,
        map: mapElement.innerMap,
        strokeColor: "#ff0000",
        strokeOpacity: 1,
        strokeWeight: 5
    }));
}

// 지도에 ai경로 그리기
function drawAiRoute(encodedPolyline) {
    // encodedPolyline → 좌표 배열
    const path = encoding.decodePath(encodedPolyline);
    // 지도에 경로 그리기
    aiRouteLine.push(new google.maps.Polyline({
        path: path,
        map: mapElement.innerMap,
        strokeColor: "#0000ff",
        strokeOpacity: 1,
        strokeWeight: 5
    }));
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
	//console("placeId", placeId);
	
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
	    background: "#ff0000",
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
		// 내 일정에 포함된 장소 보이기
    	$("#sidebar>div:nth-child(2)>div:nth-child(1)").trigger("click");
    });
    
	myPlaceMarkers.push(marker);
}

// 지도에 ai 마커 그리기
async function drawAiMarker(lat, lng, placeId) {
	// 지도가 준비될 때까지 기다림
    const map = await mapReady;
    let bno = $("#main").data("bno");
    
    // 마커 디자인
    const aiPin = new PinElement({
	    background: "#0000FF",
	    borderColor: "#FFFFFF",
	    glyphColor: "#FFFFFF"
	});
    
	let marker = new AdvancedMarkerElement({
		position: { lat, lng },
	    map: map,
	    content: aiPin,
	    zIndex: 3
	});

    marker.addListener("gmp-click", function() {
        // 정보창 팝업
		clickPlaceTitle(placeId);
		// 초기화 후 해당 장소로 검색
		$("#main>div:nth-child(1)>div:nth-child(2)>div").removeClass("isCheckedBtn");
		$(".place").remove();
		placePageNum = 1;
		
		mapping = "getSerchedPlace";
		//console.log(placeId);
		placeNewPage(placeId, bno);
    });
    
	aiPlaceMarkers.push(marker);
}

// 마커 삭제하기
function removeMarker(markerArr) {
	for (let i=0;i<markerArr.length;i++) {
		markerArr[i].setMap(null);
	}
	markerArr = [];
}

// 캘린더 타입과 현재 게시글의 블럭 list가 들어오면 캘린더에 담아주는 함수
function setBlocksToCalendar(calendar,data) {
	// 내 일정 마커를 지우고 다시 생성
	removeMarker(myPlaceMarkers);
	
	// 캘린더 비우기
    calendar.clear();
    eventList = [];
	
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
            calendarId: 'calendar',
            title: title,
            start: start,
            end: end,
            backgroundColor: colorCode,
            color: '#000000',
            body: placeId
        });
	}
    // 데이터 삽입
    if (eventList.length > 0) {
        calendar.createEvents(eventList);
    }
	// 장소가 들어갈 수 있도록 설정
    setTimeout(function() {
	    //console.log($(".toastui-calendar-event-time").length);
	
	    $(".toastui-calendar-event-time").droppable({
	        drop: handleDropEvent,
	        tolerance: 'pointer'
	    });
	}, 100);
	//console.log(eventList);
}

// 일정 삽입 함수
function setBlocks(calendar) {
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
		//console.log("일정삽입함수",data);
		setBlocksToCalendar(calendar,data);
	})
	.catch(function(error){
		alert("에러! : " + error);
	})
}

// 드래그로 장소추가
function handleDropEvent(event, ui) {
	//alert("드래그감지");
	const $droppable = $(event.target);
	let bno = $("#main").data("bno");
	//alert(ui.draggable.data("place-id"));
	//alert($droppable.data("event-id"));
	//console.log("장소 삽입");
	
	const jsonData = {
		"placeId" : ui.draggable.data("place-id"),
		"blockIdx": $droppable.data("event-id"),
		"bno" : bno
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
		return response.json();
	})
	.then(function(data){
		//console.log("장소 드래그 후의 블럭들", data);
		Toastify({
		  text: "장소를 추가했습니다.",
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
		if(webSocket!=null)
			webSocket.send("week");
	  	setBlocksToCalendar(calendar, data);
	})
	.catch(function(error){
		alert("에러! : " + error);
	})
}

$(function() {
	// ************** 공동 필드 *****************
	let bno = $("#main").data("bno");
	let arrPlaceCity = $("#main").data("arr-place-city");
	let key = $("#main").attr("data-key");
	let mapll = $("#main").attr("data-mapll");
	
	
  
	
	// ai 추천 일정 받기
	function getAiResult() {
		// 로딩바 생성
		$('.loading_spinner').removeClass("hide");
		$(".popupContainer").removeClass("hide");
		
		aiLock = true;
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
	    
	    let userBlocks = events.map(event => ({
		    placeId: event.body,
		    placeName: event.title,
		    start: event.start,
		    end: event.end
		}));
	    
	    const jsonData = {
			"userBlocks" : userBlocks,
			"bno" : bno,
			"arrPlaceCity" : arrPlaceCity,
			"date" : date,
			"mapll" : mapll
		};
		const initData = {
			method: "post",
			headers: {
				"Content-Type": "application/json"
			},
			body: JSON.stringify(jsonData)
		};
		fetch("../../searchAiRecommend", initData)
		.then(function(response){
			return response.json();
		})
		.then(function(data){
			//console.log(data);
			
			$('.loading_spinner').addClass("hide");
			$(".popupContainer").addClass("hide");
			
			if(webSocket!=null)
				webSocket.send("aiUpdate");
			
			// ai 마커와 캘린더 데이터를 지우고 다시 생성
			removeMarker(aiPlaceMarkers);
			aiEventList = [];
			aiBlocks = [];
			
	        // 캘린더 비우기
	        aiCalendar.clear();
			
			for(let i=0;i<data.length;i++) {
				let block = data[i];
				aiBlocks.push(block);
				
				let id = block.idx;
		        let placeId = block.placeId;
		        let start = block.startTime;
		        let end = block.endTime;
		        let title = block.name;
		        let lat = block.lat;
		        let lng = block.lng;
		        
		        // 만약 장소 데이터가 있다면 ai 마커를 추가
		        if(lat!=null)
		        	drawAiMarker(lat, lng, placeId);
		        
		        // 캘린더 데이터
		        aiEventList.push({
		            id: id,
		            calendarId: 'aiCalendar',
		            title: title,
		            start: start,
		            end: end,
		            backgroundColor: '#EDE8F6',
		            color: '#000000',
		            body: placeId
		        });
		    }
	        
	        // 데이터 삽입
	        if (aiEventList.length > 0) {
		        aiCalendar.createEvents(aiEventList);
		    }
		    
		    // 보여주기
			$("#aiCalendar").removeClass("hide");
			$("#toPlan").removeClass("hide");
			$("#closeAi").removeClass("hide");
			
			// 장소가 들어간 일정이 하나거나 없다면 종료
		    if(aiEventList.length<2) return;
		    
		    // 장소가 들어있다면 경로 그리기
		    let placeIds = [];
		    for(let i=0;i<aiEventList.length;i++){
		    	let place = aiEventList[i];
		    	placeIds.push(place.body);
		    }
		    //console.log(placeIds);
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
				let rData = JSON.parse(data[0]).routes;
	    		if (!rData || rData.length == 0) {
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
			    
			    // 기존 경로 지우기
			    for(let i=0;i<aiRouteLine.length;i++){
			    	aiRouteLine[i].setMap(null);
			    }
			    aiRouteLine = [];
			    
			    for(let i=0;i<data.length;i++){
				    encodedPolyline = JSON.parse(data[i]).routes[0].polyline.encodedPolyline;
		    		//console.log(encodedPolyline);
		    		drawRoute(encodedPolyline);
			    }
			    setTimeout(function() {
		    		aiLock = false;
				}, 1000);
			})
			.catch(function(error) {
			    alert("에러! : " + error);
			});
		})
		.catch(function(error){
			alert("에러! : " + error);
		})
	}
	
	// ************* 웹소켓 ************************
	if(key) {
		webSocket = new WebSocket("ws://52.199.216.149:9090/TravelPlanner/broadcasting?key="+bno);
		
		// 공유
		webSocket.onmessage = function(e) {
			if(e.data=="week") {
				Toastify({
				  text: "수정사항이 반영되었습니다.",
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
				setBlocks(calendar);
				return;
			}
			if(e.data=="true") {
				Toastify({
				  text: "작업자 중 한명이 AI 추천을 사용하고 있습니다. 잠시만 기다려주세요.",
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
				setTimeout(function() {
		    		aiLock = false;
				}, 1000);
			}
			if(e.data=="false") {
				setTimeout(function() {
		    		aiLock = false;
				}, 1000);
				getAiResult();
			}
			if(e.data=="aiUpdate") {
				Toastify({
				  text: "AI 추천 결과가 도착했습니다.",
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
				fetch("../../getAiBlock", initData)
				.then(function(response){
					return response.json();
				})
				.then(function(data){
					//console.log(data);

					// ai 마커와 캘린더 데이터를 지우고 다시 생성
					removeMarker(aiPlaceMarkers);
					aiEventList = [];
					aiBlocks = [];
					
			        // 캘린더 비우기
			        aiCalendar.clear();
					
					for(let i=0;i<data.length;i++) {
						let block = data[i];
						aiBlocks.push(block);
						
						let id = block.idx;
				        let placeId = block.placeId;
				        let start = block.startTime;
				        let end = block.endTime;
				        let title = block.name;
				        let lat = block.lat;
				        let lng = block.lng;
				        
				        // 만약 장소 데이터가 있다면 ai 마커를 추가
				        if(lat!=null)
				        	drawAiMarker(lat, lng, placeId);
				        
				        // 캘린더 데이터
				        aiEventList.push({
				            id: id,
				            calendarId: 'aiCalendar',
				            title: title,
				            start: start,
				            end: end,
				            backgroundColor: '#EDE8F6',
				            color: '#000000',
				            body: placeId
				        });
				    }
			        
			        // 데이터 삽입
			        if (aiEventList.length > 0) {
				        aiCalendar.createEvents(aiEventList);
				    }
				    
				    // 보여주기
				    calendar.changeView('day');
					$("#calendar").addClass("changeToDay");
					$(this).parent().find("span").removeClass("selectedView");
					$(this).addClass("selectedView");
					$("#goNext").addClass("hide");
					$("#makeAiBlock").removeClass("hide");
					$("#map").removeClass("hide");
					$(".transportation").removeClass("hide");
					
					$("#aiCalendar").removeClass("hide");
					$("#toPlan").removeClass("hide");
					$("#closeAi").removeClass("hide");
					
					// 장소가 들어간 일정이 하나거나 없다면 종료
				    if(aiEventList.length<2) return;
				    
				    // 장소가 들어있다면 경로 그리기
				    let placeIds = [];
				    for(let i=0;i<aiEventList.length;i++){
				    	let place = aiEventList[i];
				    	placeIds.push(place.body);
				    }
				    //console.log(placeIds);
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
						let rData = JSON.parse(data[0]).routes;
			    		if (!rData || rData.length == 0) {
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
					    // 기존 경로 지우기
					    for(let i=0;i<aiRouteLine.length;i++){
					    	aiRouteLine[i].setMap(null);
					    }
					    aiRouteLine = [];
					    
					    for(let i=0;i<data.length;i++){
						    encodedPolyline = JSON.parse(data[i]).routes[0].polyline.encodedPolyline;
				    		//console.log(encodedPolyline);
				    		drawRoute(encodedPolyline);
					    }
			    		setTimeout(function() {
				    		aiLock = false;
						}, 1000);
					})
					.catch(function(error) {
					    alert("에러! : " + error);
					});
				})
				.catch(function(error){
					alert("에러! : " + error);
				})
				//console.log(e.data);
			}
		};
		webSocket.onopen = function(e) {
			Toastify({
			  text: "연결되었습니다.",
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
		};
		webSocket.onerror = function(e) {
			alert("에러!"); 
		};
	}
	
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
		//console.log(eventObj);
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
			if(webSocket!=null)
				webSocket.send("week");
				
			setBlocksToCalendar(calendar,data.blocks);
		})
		.catch(function(error){
			alert("에러! : " + error);
		})
	});
	
	// 일정 삭제
	$(".popupContainer > div:nth-child(2) > div:nth-child(2) > div:nth-child(3)").click(function(){
		if(confirm("일정을 삭제하시겠습니까?")){
			let blockIdx = $(this).data("blockIdx");
			//console.log(blockIdx);
			
			const jsonData = {
				"blockIdx" : blockIdx,
				"bno" : bno
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
				return response.json();
			})
			.then(function(data){
				//console.log("삭제 후의 블럭들",data);
				if(webSocket!=null)
					webSocket.send("week");
			  	setBlocksToCalendar(calendar,data);
			  	Toastify({
				  text: "삭제되었습니다.",
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
			})
			.catch(function(error){
				alert("에러! : " + error);
			})
		  	$(".popupContainer").addClass("hide");
			$(".popupContainer>div:nth-child(2)").addClass("hide");
		}
	});
	
	// 일정 이동 및 시간 변경
	calendar.on('beforeUpdateEvent', (updateData) => {
		const { event, changes } = updateData;
		
		// 변화가 없다면 종료
		if(changes.start==null&&changes.end==null) return;
		//console.log(event);
		//console.log(changes);
		
		// 시간 세팅
		let startTime = event.start;
		let endTime = event.end;
		let blockIdx = event.id;
		if(changes.start!=null) startTime = changes.start;
		if(changes.end!=null) endTime = changes.end;
		
		const jsonData = {
			"blockIdx" : blockIdx,
			"startTime": startTime.toDate().toISOString(),
	    	"endTime": endTime.toDate().toISOString(),
	    	"bno": bno
		};
		
		//console.log(jsonData.startTime);
		const initData = {
			method: "post",
			headers: {
				"Content-Type": "application/json"
			},
			body: JSON.stringify(jsonData)
		};
		fetch("../../modifyBlockTime", initData)
		.then(function(response){
			return response.json();
		})
		.then(function(data){
			//console.log("일정 블럭 옮긴 후의 블럭들",data);
			if(webSocket!=null)
				webSocket.send("week");
		  	setBlocksToCalendar(calendar,data);
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
		$(".transportation").removeClass("hide");
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
		$(".transportation").addClass("hide");
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
		// 버튼 결과를 아직 받지 못했다면 종료
		if(aiLock) return;
		
		// 연결되어 있다면 서버에 사용여부 확인
		if(webSocket!=null) {
			webSocket.send("ai");
			aiLock = true;
			return;
		}
		
		// 공동작업자 없음
		Toastify({
		  text: "AI 추천 일정을 확인합니다. 잠시만 기다려주세요.",
		  duration: 3000,
		  newWindow: true,
		  close: true,
		  gravity: "top",
		  position: "center",
		  stopOnFocus: true,
		  style: {
		    background: "linear-gradient(to left, #E8EEFF, #3D5AFE)",
		  }
		}).showToast();
		getAiResult();
	});
	// ai 추천 반영버튼
	$("#toPlan").click(function() {
		if(confirm("AI 일정을 반영하겠습니까?\n(원래의 계획은 사라집니다.)")){
			fetch("/TravelPlanner/reflectAIBlock?bno="+bno, {method:'post'})
			.then(function(response) {
				return response.json();
			})
			.then(function(data) {
				//console.log("ai 반영 후의 블럭들",data);
				
        		removeMarker(aiPlaceMarkers);
        		setBlocks(calendar);
        		//setBlocksToCalendar(calendar,data);
        		$("#closeAi").click();
        		
        		if(webSocket!=null)
					webSocket.send("week");
        		
        		Toastify({
				  text: "AI 추천 일정이 반영되었습니다.",
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
			})
			.catch(function(error) {
			    alert("에러! : " + error);
			});
		
		}
	
	});
	// ai 추천 닫기
	$("#closeAi").click(function() {
		$("#aiCalendar").addClass("hide");
		$("#toPlan").addClass("hide");
		$("#closeAi").addClass("hide");
	});
	// 내 일정 경로 표시(이동수단의 첫번째 버튼 클릭 시 경로 보여줌/그 외>이동수단만 변경)
	$(".popupContainer>div:nth-child(4)>span").click(function() {
		if($(this).index()!=0) travelModeIdx = $(this).index();
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
			
			// 해당 날짜에서 장소가 들어간 블럭만 추출
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
		    //console.log(placeIds);
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
				let rData = JSON.parse(data[0]).routes;
        		if (!rData || rData.length == 0) {
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
			    // 기존 경로 지우기
			    for(let i=0;i<routeLine.length;i++){
			    	routeLine[i].setMap(null);
			    }
			    routeLine = [];
			    for(let i=0;i<data.length;i++){
				    encodedPolyline = JSON.parse(data[i]).routes[0].polyline.encodedPolyline;
		    		//console.log(encodedPolyline);
		    		drawRoute(encodedPolyline);
			    }
			})
			.catch(function(error) {
			    alert("에러! : " + error);
			});
		}
	});
});