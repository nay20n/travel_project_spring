// 캘린더 객체
let calendar;
let aiCalendar;

// 일정에 들어간 이벤트들
let eventList;
let aiEventList;
let aiBlocks;

// 장소 팝업 페이지 처리 필드
let popupPageNum = 1; // 댓글 페이지 
let reviewPageLock = false;
let scrollLock = false;

// 장소 검색 페이지 처리 필드
let placePageNum = 1; 
let placePageLock = false;
let placeScrollLock = false;
let mapping = "getSerchedPlace";

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

// 별점(1), 퍼센트(50%) 들어오면 그래프 그려주는 함수
// rate : 점수 1, 2, 3점 
// per : 1점의 개수 / 전체  
function setGraph(rate, per) {
	$(".graph").each(function(idx, item) {
		if(5-idx==Number(rate)) {
			let width = "width: " + per*100 + "%";
			$(this).attr("style",width);
		}
	});
}

// esc로 팝업창 닫기
window.addEventListener('keydown', (e) => { 
	if (e.key == 'Escape' || e.keyCode == 27) 
    	$(".popupContent>svg:nth-child(1)").trigger("click");
});

// json 배열이 들어오면 사이드바 그리는 함수
function addSideContent(data) {
	if(data.length<1) { // 그릴 것이 없다면 종료
		placePageNum = -1
		return; 
	}
	if(data.length<10) placePageNum = -1; // 불러올 게 더 없으므로 -1 설정
	
	for(let i=0;i<data.length;i++) {
		let place = data[i];  
		
		// 지도에 마커 추가
		//console.log(typeof place.lat);
		//console.log(typeof place.lng);
		//console.log(typeof place.placeId);
		drawMarker(place.lat, place.lng, place.placeId);
				
		let placeImage = `<img class="hide"/>`;
		if(place.image!=null)
			placeImage = `<img src="${place.image}" onerror="this.style.display='none';"/>`;
		
		let star = `
		<svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="size-6">
			<path stroke-linecap="round" stroke-linejoin="round" d="M11.48 3.499a.562.562 0 0 1 1.04 0l2.125 5.111a.563.563 0 0 0 .475.345l5.518.442c.499.04.701.663.321.988l-4.204 3.602a.563.563 0 0 0-.182.557l1.285 5.385a.562.562 0 0 1-.84.61l-4.725-2.885a.562.562 0 0 0-.586 0L6.982 20.54a.562.562 0 0 1-.84-.61l1.285-5.386a.562.562 0 0 0-.182-.557l-4.204-3.602a.562.562 0 0 1 .321-.988l5.518-.442a.563.563 0 0 0 .475-.345L11.48 3.5Z" />
		</svg>
		`;
		if(`${place.isLiked}`==1)
			star = `
				<svg  class="fillStar" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="size-6">
					<path stroke-linecap="round" stroke-linejoin="round" d="M11.48 3.499a.562.562 0 0 1 1.04 0l2.125 5.111a.563.563 0 0 0 .475.345l5.518.442c.499.04.701.663.321.988l-4.204 3.602a.563.563 0 0 0-.182.557l1.285 5.385a.562.562 0 0 1-.84.61l-4.725-2.885a.562.562 0 0 0-.586 0L6.982 20.54a.562.562 0 0 1-.84-.61l1.285-5.386a.562.562 0 0 0-.182-.557l-4.204-3.602a.562.562 0 0 1 .321-.988l5.518-.442a.563.563 0 0 0 .475-.345L11.48 3.5Z" />
				</svg>
			`;
			
		let placeHtml = `
			<div class="place" data-place-id="${place.placeId}" data-lat="${place.lat}" data-lat="${place.lng}">
	            <div>
	                ${placeImage}
	            </div>
				<div class="placeInfo">
					<div class="placeTitle">
						<div>
							<a>${place.name}</a><span>${place.category}</span>
						</div>
						<div>
							${star}
						</div>
					</div>
					<div>
						<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="currentColor" class="size-6">
							<path fill-rule="evenodd" d="M10.788 3.21c.448-1.077 1.976-1.077 2.424 0l2.082 5.006 5.404.434c1.164.093 1.636 1.545.749 2.305l-4.117 3.527 1.257 5.273c.271 1.136-.964 2.033-1.96 1.425L12 18.354 7.373 21.18c-.996.608-2.231-.29-1.96-1.425l1.257-5.273-4.117-3.527c-.887-.76-.415-2.212.749-2.305l5.404-.434 2.082-5.005Z" clip-rule="evenodd" />
						</svg>
						<span>${place.avgRating}</span><span>(${place.reviewCnt})</span>
					</div>
					<div>${place.address}</div>
				</div>
			</div>
		`;
		
		const $newPlace = $(placeHtml);
		$("#sidebar").append($newPlace);
		// 생성에 드래그 기능 넣기
		$newPlace.draggable({
			helper: function() { return $(this).clone().css({ "opacity": "0.3" }) },
			cursor: "move"
		});
	}
}

// 사이드바 장소 불러오기 비동기
function placeNewPage(search, bno) {
	//console.log("placeNewPage 호출");
	const jsonData = {
		"pageNum" : placePageNum,
		"bno" : bno,
		"input" : search
	};
	//console.log(jsonData.input);
	const initData = {
		method: "post",
		headers: {
			"Content-Type": "application/json"
		},
		body: JSON.stringify(jsonData)
	};
	fetch("../../"+mapping, initData)
	.then(function(response) {
		return response.json();
	})
	.then(function(data) {
		//console.log(data);
		addSideContent(data);
	})
	.catch(function(error) {
		alert("에러! : " + error);
	});
	placePageLock = false;
	placeScrollLock = false;
	placePageNum++;
}

// 평균별점 들어오면 별점 그려주는 함수
function setAvgStar(rate) {
	let width = "width: " + (Number(rate)/5*100) + "%";
	$(".placeDetail>div:nth-child(5)>div:nth-child(2)>div:nth-child(2)>div").attr("style",width);
}

// 댓글 별점 그리는 함수
function setStar(rate, rnum) {
	for(let i=1; i<=rate; i++){
		$(`.popupPlace>div:nth-child(6)>div:nth-child(${rnum})>div:nth-child(2)>div:nth-child(1)>svg:nth-child(${i})`).addClass("fillStar");
	}
}

// 색 배열 들어오면 블럭 초기 색상 넣어주는 함수 + 임시 색 배열
let blockColorArr = ["#fff4e6","#b0e0e6","#9fb5c8","#f7d9c4","#e2cfd4"];
function setInitialColor(blockColorArr) {
	$(".blockColor").each(function(idx, item) {
		color = "color: " + blockColorArr[idx];
		$(this).attr("style",color);
	});
}

// 댓글 페이지 불러오는 함수 
function reviewNewPage(pageNum, placeId) {
	if(reviewPageLock) return;
	if(popupPageNum==-1) return;
	reviewPageLock = true;
	
	// 댓글 불러오기 비동기
	const jsonData = {
		"pageNum" : pageNum,
		"placeId" : placeId
	};
	const initData = {
		method: "post",
		headers: {
			"Content-Type": "application/json"
		},
		body: JSON.stringify(jsonData)
	};
	fetch("../../getReviews", initData)
	.then(function(response) {
		return response.json();
	})
	.then(function(data) {
		//console.log(data);
		let reviews = data.reviews;
		if(data.reviews.length<1) {
			reviewPageLock = false;
			popupPageNum = -1;
			return;
		}
		if(data.reviews.length<5) popupPageNum = -1;
		let loginId = data.loginId;
		
		if(reviews.length<1) return; // 페이지가 1보다작으면 return 
		if(reviews.length<5) gPageNum = -1; //불러올게 더 없다면 페이지 번호를 -1로 변경 
		
		for(let i=0; i<reviews.length; i++) {
			let rnum = Number(reviews[i].RNUM);
			
			let profile = `<img src="../../resources/img/기본 프로필.png"/>`;
			if(reviews[i].profileImg!=null)
				profile = `<img src="../../resources/upload/${reviews[i].profileImg}"/>`;
				
			let imgHtml = `<img class="hide"/>`;
			if(reviews[i].image!=null)
				imgHtml = `<img src="../../resources/upload/${reviews[i].image}" />`;
				
			let content = ``;
			if(reviews[i].content!=null)
				content = `<div>${reviews[i].content}</div>`
			
			let btnDelete = ``;
			if(reviews[i].memberId == loginId)
				btnDelete = `<div class="inputBdDiv">
							<svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="size-6">
								<path stroke-linecap="round" stroke-linejoin="round" d="m14.74 9-.346 9m-4.788 0L9.26 9m9.968-3.21c.342.052.682.107 1.022.166m-1.022-.165L18.16 19.673a2.25 2.25 0 0 1-2.244 2.077H8.084a2.25 2.25 0 0 1-2.244-2.077L4.772 5.79m14.456 0a48.108 48.108 0 0 0-3.478-.397m-12 .562c.34-.059.68-.114 1.022-.165m0 0a48.11 48.11 0 0 1 3.478-.397m7.5 0v-.916c0-1.18-.91-2.164-2.09-2.201a51.964 51.964 0 0 0-3.32 0c-1.18.037-2.09 1.022-2.09 2.201v.916m7.5 0a48.667 48.667 0 0 0-7.5 0" />
							</svg>
							삭제
						</div>`;
				
			let reviewHtml = `<div class="placeReview" data-reviewIdx = "${reviews[i].reviewIdx}">
							<div>
								<div>
									${profile}
									<div>${reviews[i].nickName}</div> 
								</div>
								<div>
									${btnDelete}
								</div>
							</div>
							<div>
								<div>
									<svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="size-6">
	  									<path stroke-linecap="round" stroke-linejoin="round" d="M11.48 3.499a.562.562 0 0 1 1.04 0l2.125 5.111a.563.563 0 0 0 .475.345l5.518.442c.499.04.701.663.321.988l-4.204 3.602a.563.563 0 0 0-.182.557l1.285 5.385a.562.562 0 0 1-.84.61l-4.725-2.885a.562.562 0 0 0-.586 0L6.982 20.54a.562.562 0 0 1-.84-.61l1.285-5.386a.562.562 0 0 0-.182-.557l-4.204-3.602a.562.562 0 0 1 .321-.988l5.518-.442a.563.563 0 0 0 .475-.345L11.48 3.5Z" />
									</svg>
									<svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="size-6">
	  									<path stroke-linecap="round" stroke-linejoin="round" d="M11.48 3.499a.562.562 0 0 1 1.04 0l2.125 5.111a.563.563 0 0 0 .475.345l5.518.442c.499.04.701.663.321.988l-4.204 3.602a.563.563 0 0 0-.182.557l1.285 5.385a.562.562 0 0 1-.84.61l-4.725-2.885a.562.562 0 0 0-.586 0L6.982 20.54a.562.562 0 0 1-.84-.61l1.285-5.386a.562.562 0 0 0-.182-.557l-4.204-3.602a.562.562 0 0 1 .321-.988l5.518-.442a.563.563 0 0 0 .475-.345L11.48 3.5Z" />
									</svg>
									<svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="size-6">
	  									<path stroke-linecap="round" stroke-linejoin="round" d="M11.48 3.499a.562.562 0 0 1 1.04 0l2.125 5.111a.563.563 0 0 0 .475.345l5.518.442c.499.04.701.663.321.988l-4.204 3.602a.563.563 0 0 0-.182.557l1.285 5.385a.562.562 0 0 1-.84.61l-4.725-2.885a.562.562 0 0 0-.586 0L6.982 20.54a.562.562 0 0 1-.84-.61l1.285-5.386a.562.562 0 0 0-.182-.557l-4.204-3.602a.562.562 0 0 1 .321-.988l5.518-.442a.563.563 0 0 0 .475-.345L11.48 3.5Z" />
									</svg>
									<svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="size-6"> 
										<path stroke-linecap="round" stroke-linejoin="round" d="M11.48 3.499a.562.562 0 0 1 1.04 0l2.125 5.111a.563.563 0 0 0 .475.345l5.518.442c.499.04.701.663.321.988l-4.204 3.602a.563.563 0 0 0-.182.557l1.285 5.385a.562.562 0 0 1-.84.61l-4.725-2.885a.562.562 0 0 0-.586 0L6.982 20.54a.562.562 0 0 1-.84-.61l1.285-5.386a.562.562 0 0 0-.182-.557l-4.204-3.602a.562.562 0 0 1 .321-.988l5.518-.442a.563.563 0 0 0 .475-.345L11.48 3.5Z" />
									</svg>
									<svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="size-6">
	  									<path stroke-linecap="round" stroke-linejoin="round" d="M11.48 3.499a.562.562 0 0 1 1.04 0l2.125 5.111a.563.563 0 0 0 .475.345l5.518.442c.499.04.701.663.321.988l-4.204 3.602a.563.563 0 0 0-.182.557l1.285 5.385a.562.562 0 0 1-.84.61l-4.725-2.885a.562.562 0 0 0-.586 0L6.982 20.54a.562.562 0 0 1-.84-.61l1.285-5.386a.562.562 0 0 0-.182-.557l-4.204-3.602a.562.562 0 0 1 .321-.988l5.518-.442a.563.563 0 0 0 .475-.345L11.48 3.5Z" />
									</svg>
								</div>
								<div>${reviews[i].finalDate}</div>
							</div>
							${imgHtml}
							${content}
						</div>`;
						
			$(".popupPlace>div:nth-child(6)").append(reviewHtml);
			setStar(reviews[i].rating,rnum);
		}
		scrollLock = false;
		reviewPageLock = false;
	})
	.catch(function(error) {
		alert("에러! : " + error);
	});
}

// 이미지 슬라이더 처리 필드
let placeSlider = null;
// 장소 팝업 띄우기
function clickPlaceTitle(placeId) {
	fetch("../../getPlaceDetail?placeId="+placeId, {method:"POST"})
	.then(function(response){
		return response.json();
	})
	.then(function(data){
		//console.log(data.placeDetail);
		//console.log(data.reviews);
		let pDetail = data.placeDetail;
		let reviews = data.reviews;
		let loginId = data.loginId;
			
		if (placeSlider!=null) {
			placeSlider.destroySlider();
			placeSlider = null;
		}
	
		let img = pDetail.images;
		let imgHtml = ``;

		if (img != null && img.length > 0) {
			for(let i = 0; i < img.length; i++){
				imgHtml += `<div><img src="${img[i]}" onerror="this.style.display='none';"/></div>`;
			}
			$(".popupContainer > div:nth-child(1) > div > div:nth-child(1)").html(imgHtml);
		} else {
			$(".popupContainer > div:nth-child(1) > div > div:nth-child(1)").empty();
		}
			
		$(".popupPlace").attr("data-placeid", placeId);
		//$(".popupContainer> div:nth-child(1) > div > div:nth-child(1)").html(imgHtml);
		$(".popupPlace > div:nth-child(1) > div:nth-child(1) > a").text(pDetail.name); 
		let avgRating = Number(pDetail.avgRating);
		$(".popupPlace > div:nth-child(2) > span:nth-child(2)").text(Math.round(avgRating*10)/10);
		$(".popupPlace > div:nth-child(2) > span:nth-child(3)").text("("+pDetail.reviewCnt+")"); 
		$(".popupPlace > div:nth-child(3)").text(pDetail.category);
		if(pDetail.isLiked==1) $(".popupPlace > div:nth-child(1) > div:nth-child(2) > svg").addClass("fillStar");
		else $(".popupPlace > div:nth-child(1) > div:nth-child(2) > svg").removeClass("fillStar");
		
		$(".placeDetail > div:nth-child(1)>span").text(pDetail.address);
		
		if(pDetail.businessHours==null || pDetail.businessHours=="(null)"){
			$(".placeDetail > div:nth-child(2)").addClass("hide");
			$(".placeDetail > div:nth-child(3)").addClass("hide");
		} else {
			$(".placeDetail > div:nth-child(2)").removeClass("hide");
			$(".placeDetail > div:nth-child(3)").removeClass("hide");
			$(".placeDetail > div:nth-child(3) > div").html(pDetail.businessHours);
		}
		
		if(pDetail.websiteUrl==null){
			$(".placeDetail > div:nth-child(4)").addClass("hide");
		} else {
			$(".placeDetail > div:nth-child(4)").removeClass("hide");
			$(".placeDetail > div:nth-child(4) > div").text(pDetail.websiteUrl);
		}
		
		if(pDetail.reviewCnt==0){
			setGraph(5 ,0);
			setGraph(4 ,0);
			setGraph(3 ,0);
			setGraph(2 ,0);
			setGraph(1 ,0);
			setGraph(0 ,0);
		} else {
			setGraph(5 ,pDetail.rating5/pDetail.reviewCnt);
			setGraph(4 ,pDetail.rating4/pDetail.reviewCnt);
			setGraph(3 ,pDetail.rating3/pDetail.reviewCnt);
			setGraph(2 ,pDetail.rating2/pDetail.reviewCnt);
			setGraph(1 ,pDetail.rating1/pDetail.reviewCnt);
			setGraph(0 ,pDetail.rating0/pDetail.reviewCnt);
		}
		$(".placeDetail > div:nth-child(5) > div:nth-child(2) > div:nth-child(1)").text(Math.round(avgRating*10)/10);
		setAvgStar(pDetail.avgRating);
		$(".placeDetail > div:nth-child(5) > div:nth-child(2) > div:nth-child(3)").text("리뷰 " + pDetail.reviewCnt + "개");
		
		$(".popupContainer").removeClass("hide");
		$(".popupContainer>div:nth-child(1)").removeClass("hide");
		
		
		if (img != null && img.length > 0) {
			placeSlider = $(".popupContainer > div:nth-child(1) > div > div:nth-child(1)").bxSlider({
				pager: false,
				controls: true,      
				adaptiveHeight: true,
				touchEnabled: false
			});
		}

	})
	.catch(function(error){
		alert("에러! : " + error);
	});
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
	
	// 초기 설정
	// 블럭 색 가져오기
	fetch("../../getColors", {method:"POST"})
	.then(function(response){
		return response.json();
	})
	.then(function(data){
		//console.log(data);
		for(let i=0;i<data.length;i++){
			blockColorArr[i] = data[i].colorCode;
		}
		setInitialColor(blockColorArr);
	})
	.catch(function(error){
		alert("에러! : " + error);
	});
	$(".popupContent").draggable(); // 팝업 드래그 가능
	if(arrPlaceCity=="해외") {
		placeNewPage("", bno); // 장소 검색창 채우기
	} else {
		placeNewPage(arrPlaceCity, bno); // 장소 검색창 채우기
	}
	
	// ************ 장소 사이드바 ****************
	// 검색 엔터로 돋보기 클릭
	$("#main>div.bs>div.inputBdDiv>label>input").keypress(function(e) {
		if(e.keyCode == 13){
			$("#main > div:nth-child(1) > div:nth-child(1) > label > svg").trigger("click");
		}
	});
	// 장소 검색
	$("#main > div:nth-child(1) > div:nth-child(1) > label > svg").click(function() {
		if(placePageLock) return;
		placePageLock = true;
		
		// 초기화
		$("#main>div:nth-child(1)>div:nth-child(2)>div").removeClass("isCheckedBtn");
		$(".place").remove();
		placePageNum = 1;
		
		let search = $(this).parent().find("input").val();
		mapping = "getSerchedPlace";
		
		placeNewPage(search, bno);
	});
	// 내 일정 버튼 클릭 
	$("#main>div:nth-child(1)>div:nth-child(2)>div:nth-child(1)").click(function() {
		if(placePageLock) return;
		if($(this).hasClass("isCheckedBtn")) return;
		placePageLock = true;
		
		// 초기화
		$(this).addClass("isCheckedBtn");
		$("#main>div:nth-child(1)>div:nth-child(2)>div:nth-child(2)").removeClass("isCheckedBtn");
		$(".place").remove();
		placePageNum = 1;
		
		mapping = "getSelectedPlaces";
		
		placeNewPage("", bno);
	});
	// 찜한 장소 버튼 클릭 
	$("#main>div:nth-child(1)>div:nth-child(2)>div:nth-child(2)").click(function() {
		if(placePageLock) return;
		if($(this).hasClass("isCheckedBtn")) return;
		placePageLock = true;
		
		// 초기화
		$(this).addClass("isCheckedBtn");
		$("#main>div:nth-child(1)>div:nth-child(2)>div:nth-child(1)").removeClass("isCheckedBtn");
		$(".place").remove();
		placePageNum = 1;
		
		mapping = "getLikedPlaces";
		
		placeNewPage("", bno);
	});
	// 사이드바 스크롤
	$("#sidebar").scroll(function(e){
		if(placePageLock) return;
		if(placePageNum==-1) return;
		
		var scrollTop = $(this).scrollTop();
    	var innerHeight = $(this).innerHeight();
	    var scrollHeight = $(this).prop('scrollHeight');
	    if(scrollTop + innerHeight >= scrollHeight) {
	    	placeScrollLock = true;
	    	placeNewPage("", bno);
		} 
	});
	// 장소 옆 별 on off
	$(document).on("click", ".placeTitle>div>svg", function() {
		let placeId = $(this).parent().parent().parent().parent().data("placeid");
		//alert(placeId);
		if($(this).hasClass("fillStar")){ // 찜 삭제
			fetch("../../deleteLikedPlace?placeId="+placeId, {method:"POST"})
			.then(function(response){
				return response.json();
			})
			.then(function(data){
				//console.log(data);
			})
			.catch(function(error){
				alert("에러! : " + error);
			})
		} else { // 찜 더하기
			fetch("../../addLikedPlace?placeId="+placeId, {method:"POST"})
			.then(function(response){
				return response.json();
			})
			.then(function(data){
				//console.log(data);
			})
			.catch(function(error){
				alert("에러! : " + error);
			})
		} 
		$(this).toggleClass("fillStar"); // css 꾸미기 
	});
	
	
	// ********************* ai 추천 일정 받기 ********************
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
		    		drawAiRoute(encodedPolyline);
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
	
	// **************** 웹소켓 ************************
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
				    		drawAiRoute(encodedPolyline);
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
	// ******************************* ai 추천 ***************************
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
	
	// ************** 팝업 ******************
	// 이름 클릭 시 정보창 팝업
	$(document).on("click", ".placeTitle>div:nth-child(1)>a", function() {
		popupPageNum = 1; // 페이지 1로 초기화 
		let placeId = $(this).parent().parent().parent().parent().data("place-id");
		clickPlaceTitle(placeId); // 정보 가져오기 
		reviewNewPage(popupPageNum++,placeId); // 리뷰 가져오기 
	});
	// 팝업창 닫기
	$(document).on("click", ".popupContent>svg:nth-child(1)", function() {
		$('.popupContainer > div:first').scrollTop(0);
		$(".popupContainer").addClass("hide");
		$(".popupContent").addClass("hide");
		scrollLock = true;
		$(".popupPlace>div:nth-child(6)").empty(); // 원래 가져왔던 리뷰들 지우기
		scrollLock = false;
	});
	
	// ************** 장소 정보창 *************
	// 장소 옆 별 on off
	$(document).on("click", ".popupPlace>div:nth-child(1)>div:nth-child(2)>svg", function() {
		let placeId = $(".popupPlace").data("placeid");
		if($(this).hasClass("fillStar")){ // 찜 삭제
			fetch("../../deleteLikedPlace?placeId="+placeId, {method:"POST"})
			.then(function(response){
				return response.json();
			})
			.then(function(data){
				//console.log(data);
			})
			.catch(function(error){
				alert("에러! : " + error);
			});
		} else { // 찜 더하기
			fetch("../../addLikedPlace?placeId="+placeId, {method:"POST"})
			.then(function(response){
				return response.json();
			})
			.then(function(data){
				//console.log(data);
			})
			.catch(function(error){
				alert("에러! : " + error);
			});
		}
		$(".place[data-placeid="+placeId+"] > div >div:nth-child(1) > div:nth-child(2) > svg").toggleClass("fillStar");
		//$(".placeTitle > div > svg")
		$(this).toggleClass("fillStar");
	});
	// 영업시간 on off
	$(document).on("click", ".placeDetail > div:nth-child(2) > svg:NOT(:first-child)", function() {
		$(".placeDetail > div:nth-child(2) > svg:NOT(:first-child)").toggleClass("hide");
		$(".placeDetail > div:nth-child(3)").toggleClass("hide");
	});
	// 평점 별 on off
	$(document).on("click", ".reviewInput>div:nth-child(1)>svg", function() {
		if($(this).hasClass("fillStar")) {
			$(".reviewInput>div:nth-child(1)>svg").removeClass("fillStar");
		}else {
			let rate = $(this).index();
			$(".inputStar").each(function(idx, item) {
				if(idx>rate) return;
				$(this).addClass("fillStar");
			});
		}
	});
	// 댓글 사진 추가
	$(document).on("click", ".reviewInput>div:nth-child(3)>div:nth-child(1)", function() {
		$("#reviewImg").click();
	});
	// 댓글 데이터
	let formData = new FormData();
	$("#reviewImg").change(function(){
		if(this.files && this.files[0]){
			let imgUrl = URL.createObjectURL(this.files[0]);
	        let files = this.files[0];
			formData.set("file", files);
		}
	});
	
	// ******************* 장소 리뷰 스크롤 *****************************
	$(".popupContainer > div:nth-child(1)").scroll(function(e){
		if(popupPageNum==-1) return;
		var containerScrollTop = $(this).scrollTop();
    	var containerHeight = $(this).height()
	    var contentHeight = $(this)[0].scrollHeight;
	    if(containerScrollTop + containerHeight >= contentHeight - 1) {
	    	let placeId = $(this).find("div").find(".popupPlace").attr("data-placeid");
	    	scrollLock = true;
	    	reviewNewPage(popupPageNum++, placeId);
	    	scrollLock = false;
		} 
	});
	// 댓글 등록 엔터
	$(".reviewInput > div:nth-child(2)>textarea").on('keydown', function (e) {
		if(e.keyCode === 13){
			$(".reviewInput > div:nth-child(3) > div:nth-child(2)").click();
		}
	});
	// 댓글 등록
	$(".reviewInput>div:nth-child(3)>div:nth-child(2)").click(function() {
		let content	= $(this).parent().parent().find("#textarea").find("textarea").val();
		let placeId = $(".popupPlace").attr("data-placeid");
		let rating = 0;
		$(".reviewInput > div:nth-child(1) > svg").each(function(index, item) {
			if($(item).hasClass("fillStar"))
				rating++;
		});
		let imageFile = formData;
			
		if(!confirm("댓글을 등록하겠습니까?")){
			$(this).parent().parent().find("#textarea").find("textarea").val("");
			return;
		}
		//alert(placeId);
		
		
		formData.set("placeId", placeId);
		formData.set("content", content);
		formData.set("rating", rating);
		//const jsonData = {
		//	"placeId" : placeId,
		//	"content" : content,
		//	"rating" : rating,
		//	"image" : image
		//};
		const initData = {
			method: "POST",
			body: formData
		};
		fetch("../../addReview", initData)
		.then(function(response) {
			return response.text();
		})
		.then(function(data) {
			//console.log(data);
			//alert("정상 등록");
			popupPageNum = 1;
			$(".popupPlace>div:nth-child(6)").empty();
			reviewNewPage(popupPageNum, placeId);
			
			Toastify({
			  text: "댓글이 등록되었습니다.",
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
			alert("에러! : 댓글 저장에 문제가 발생했습니다. 다시 시도해주세요." + error);
		});
		
		// textarea 비우기
		$(this).parent().parent().find("#textarea").find("textarea").val("");
		// 평점 비우기
		$(".reviewInput>div:nth-child(1)>svg").removeClass("fillStar");
	});
	// 댓글 삭제
	$(document).on("click", ".placeReview > div:nth-child(1) > div:nth-child(2) > div:nth-child(1)", function() {
		let reviewIdx = $(this).parent().parent().parent().attr("data-reviewIdx");
		let placeId = $(".popupPlace").attr("data-placeid");
		
		if(confirm("정말로 삭제하겠습니까?")){
			fetch("../../deleteReview?reviewIdx=" + reviewIdx, {method: "post"})
			.then(function(response) {
				return response.text();
			})
			.then(function(data) {
				//console.log(data);
				$(".popupPlace>div:nth-child(6)").empty();
				popupPageNum=1;
				reviewNewPage(popupPageNum, placeId);
				Toastify({
				  text: "댓글을 삭제했습니다ㄴ.",
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
	
	// *************** 블럭 정보 팝업 *****************
	// 일정 클릭시 정보창 팝업
	$(document).on("click", ".toastui-calendar-event-time-content", function() {
		let blockIdx = $(this).parent().data("event-id");
		let calendarId = $(this).parent().data("calendar-id");
		//console.log(calendarId);
		//console.log(blockIdx);
		if(calendarId=="calendar") {
			//alert($(".popupContainer>div:nth-child(2)").length);
			$(".popupContainer>div:nth-child(2)").attr("data-block-index",blockIdx);
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
			fetch("../../getBlockDetail", initData)
			.then(function(response) {
				return response.json();
			})
			.then(function(data) {
				//console.log(data);
				// 일정 삭제를 위한 인덱스 넣기
				$(".popupContainer>div:nth-child(2)>div:nth-child(2)>div:nth-child(3)").data("blockIdx",data.blockIdx);
				// 블럭 색
				$(".popupContainer>div:nth-child(2)>div:nth-child(2)>div:nth-child(1)>span:nth-child(3)").css('color', blockColorArr[data.colorIdx]);
				if(data.name==null) {
					$(".popupContainer>div:nth-child(2)>div:nth-child(3)").addClass("hide");
					$(".popupContainer>div:nth-child(2)>div:nth-child(2)>div:nth-child(2)").addClass("hide");
					$(".popupContainer").removeClass("hide");
					$(".popupContainer>div:nth-child(2)").removeClass("hide");
					return;
				}
				let center = data.lat + "," + data.lng;
				let marker = "color:0x673AB7|" + center;
				const params = new URLSearchParams({
				    center: center,
				    marker: marker
				});
				$("#blockImg").css('background-image', `url(../../getBlockImg?${params.toString()})`);
				$(".popupContainer>div:nth-child(2)>div:nth-child(2)>div:nth-child(1)>span:nth-child(2)").html(data.startTime+"~"+data.endTime);
				if(data.checkedAi==1){
					$(".popupContainer>div:nth-child(2)>div:nth-child(2)>div:nth-child(2)>input").prop('checked', true);
				} else {
					$(".popupContainer>div:nth-child(2)>div:nth-child(2)>div:nth-child(2)>input").prop('checked', false);
				}
				$(".popupContainer>div:nth-child(2)>div:nth-child(3)>div:nth-child(2)>div:nth-child(1)").html(data.name);
				$(".popupContainer>div:nth-child(2)>div:nth-child(3)>div:nth-child(2)>div:nth-child(2)").html(data.category);
				$(".popupContainer>div:nth-child(2)>div:nth-child(3)>div:nth-child(2)>div:nth-child(3)").html(data.address);
				$(".popupContainer>div:nth-child(2)>div:nth-child(3)").removeClass("hide");
				$(".popupContainer>div:nth-child(2)>div:nth-child(2)>div:nth-child(2)").removeClass("hide");
				$(".popupContainer").removeClass("hide");
				$(".popupContainer>div:nth-child(2)").removeClass("hide");
			})
			.catch(function(error) {
				alert("에러! : " + error);
			});
		} else {
			let block;
			for(let i=0;i<aiBlocks.length;i++){
				block = aiBlocks[i];
				if(block.idx==blockIdx) break;
			}
			let center = block.lat + "," + block.lng;
			let marker = "color:0x673AB7|" + center;
			const params = new URLSearchParams({
			    center: center,
			    marker: marker
			});
			$("#aiBlockImg").css('background-image', `url(../../getBlockImg?${params.toString()})`);
			$(".popupContainer>div:nth-child(5)>div:nth-child(2)>div:nth-child(1)>span:nth-child(2)").html(block.startTime.substring(11,16)+"~"+block.endTime.substring(11,16));
			$(".popupContainer>div:nth-child(5)>div:nth-child(3)>div:nth-child(2)>div:nth-child(1)").html(block.name);
			$(".popupContainer>div:nth-child(5)>div:nth-child(3)>div:nth-child(2)>div:nth-child(2)").html(block.category);
			$(".popupContainer>div:nth-child(5)>div:nth-child(3)>div:nth-child(2)>div:nth-child(3)").html(block.address);
			$(".popupContainer>div:nth-child(5)>div:nth-child(3)").removeClass("hide");
			$(".popupContainer>div:nth-child(5)>div:nth-child(2)>div:nth-child(2)").removeClass("hide");
			$(".popupContainer").removeClass("hide");
			$(".popupContainer>div:nth-child(5)").removeClass("hide");
		}
	});
	// 정보창에서 AI 반영 클릭 시 
	$(".popupContainer>div:nth-child(2)>div:nth-child(2)>div:nth-child(2)>input").on("change", function () {
		//alert("변경!");
		let blockIdx = $(".popupContainer>div:nth-child(2)").attr("data-block-index");
		//alert(blockIdx);
		const jsonData = {
			"blockIdx" : blockIdx,
			"checkedAi" : $(this).is(":checked") ? true : false
		};
		const initData = {
			method: "post",
			headers: {
				"Content-Type": "application/json"
			},
			body: JSON.stringify(jsonData)
		};
		fetch("../../checkedAi", initData)
		.then(function(response) {
			return response.text();
		})
		.then(function(data) {
			//console.log(data);
		})
		.catch(function(error) {
			alert("에러! : " + error);
		});
	});
	// 블럭 색 바꾸기 창 띄우기
	$(document).on("click", ".setBlockColor", function() {
		$(".popupContainer>div:nth-child(3)").removeClass("hide");
	});
	// 블럭 색 지정하기
	$(document).on("click", ".blockColor", function() {
		let colorIdx = $(this).index();
		let blockIdx = $(".popupContainer>div:nth-child(2)").attr("data-block-index");
		
		//alert(colorIdx);
		//alert(blockIdx);
		
		const jsonData = {
			"blockIdx" : blockIdx,
			"colorIdx" : colorIdx,
			"bno" : bno
		};
		const initData = {
			method: "post",
			headers: {
				"Content-Type": "application/json"
			},
			body: JSON.stringify(jsonData)
		};
		fetch("../../modifyBlockColor", initData)
		.then(function(response) {
			return response.json();
		})
		.then(function(data) {
			//console.log("블럭 색 전환 후의 블럭들",data);
			let color = "color: " + blockColorArr[colorIdx];
			$(".setBlockColor").attr("style",color);
			$(".popupContainer>div:nth-child(3)").addClass("hide");
			setBlocksToCalendar(calendar, data);
		})
		.catch(function(error) {
			alert("에러! : " + error);
		});
	});
	
	// *************** 교통 팝업 *****************
	// (임시) 캘린더 클릭 시 팝업
	$(document).on("click", ".transportation", function() {
		$(".popupContainer").removeClass("hide");
		$(".popupContainer>div:nth-child(4)").removeClass("hide");
	});
});