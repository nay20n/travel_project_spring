// 장소 팝업 페이지 처리 필드
let popupPageNum = 1; // 댓글 페이지 
let reviewPageLock = false;
let scrollLock = false;

// 장소 검색 페이지 처리 필드
let placePageNum = 1; 
let placePageLock = false;
let placeScrollLock = false;
let mapping = "getSerchedPlace";

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

// json 배열이 들어오면 사이드바 그리는 함수
function addSideContent(data) {
	if(data.length<1) { // 그릴 것이 없다면 종료
		placePageNum = -1
		return; 
	}
	if(data.length<10) placePageNum = -1; // 불러올 게 더 없으므로 -1 설정
	
	for(let i=0;i<data.length;i++) {
		let place = data[i];  

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
			<div class="place" data-placeId="${place.placeId}">
	            <div>
	                <img src="../../resources/img/장소예시이미지.png"/>
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
					
		$("#sidebar").append(placeHtml);
	}
}
// 사이드바 장소 불러오기 비동기
function placeNewPage(search, bno) {
	const jsonData = {
		"pageNum" : placePageNum,
		"bno" : bno,
		"input" : search
	};
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
		console.log(data);
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
		console.log(data);
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
				profile = `<img src="../../resources/img/${reviews[i].profileImg}"/>`;
				
			let imgHtml = `<img class="hide"/>`;
			if(reviews[i].image!=null)
				imgHtml = `<img src="../../resources/img/${reviews[i].image}" />`;
				
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

// 장소 팝업 띄우기
function clickPlaceTitle(placeId) {
	fetch("../../getPlaceDetail?placeId="+placeId, {method:"POST"})
	.then(function(response){
		return response.json();
	})
	.then(function(data){
		console.log(data.placeDetail);
		console.log(data.reviews);
		let pDetail = data.placeDetail;
		let reviews = data.reviews;
		let loginId = data.loginId;
			
		$(".popupPlace").attr("data-placeid", placeId);
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
			$(".placeDetail > div:nth-child(3) > div").text(pDetail.businessHours);
		}
		
		if(pDetail.websiteUrl==null){
			$(".placeDetail > div:nth-child(4)").addClass("hide");
		} else {
			$(".placeDetail > div:nth-child(4)").removeClass("hide");
			$(".placeDetail > div:nth-child(4)>div").text(pDetail.websiteUrl);
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
	})
	.catch(function(error){
		alert("에러! : " + error);
	});
}

$(function() {
	// 초기 설정
	setInitialColor(blockColorArr); // 블럭 색
	$(".popupContent").draggable(); // 팝업 드래그 가능
	let bno = $("#main").data("bno");
	let arrPlaceCity = $("#title>div:last-child>div:last-child>div:last-child>div:last-child").html();
	placeNewPage(arrPlaceCity, bno); // 장소 검색창 채우기
	
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
				console.log(data);
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
				console.log(data);
			})
			.catch(function(error){
				alert("에러! : " + error);
			})
		} 
		$(this).toggleClass("fillStar"); // css 꾸미기 
	});
	
	// ************** 팝업 ******************
	// 이름 클릭 시 정보창 팝업
	$(document).on("click", ".placeTitle>div:nth-child(1)>a", function() {
		popupPageNum = 1; // 페이지 1로 초기화 
		let placeId = $(this).parent().parent().parent().parent().data("placeid");
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
				console.log(data);
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
				console.log(data);
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
		alert("사진 삽입!");
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
		let image;
			
		if(!confirm("댓글을 등록하겠습니까?")){
			$(this).parent().parent().find("#textarea").find("textarea").val("");
			return;
		}
		
		const jsonData = {
			"placeId" : placeId,
			"content" : content,
			"rating" : rating,
			"image" : image
		};
		const initData = {
			method: "post",
			headers: {
				"Content-Type": "application/json"
			},
			body: JSON.stringify(jsonData)
		};
		fetch("../../addReview", initData)
		.then(function(response) {
			return response.text();
		})
		.then(function(data) {
			console.log(data);
			//alert("정상 등록");
			popupPageNum = 1;
			$(".popupPlace>div:nth-child(6)").empty();
			reviewNewPage(popupPageNum, placeId);
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
				console.log(data);
				$(".popupPlace>div:nth-child(6)").empty();
				popupPageNum=1;
				reviewNewPage(popupPageNum, placeId);
			})
			.catch(function(error) {
				alert("에러! : " + error);
			});
		}
		
	});
	// *************** 블럭 정보 팝업 *****************
	// (임시) 캘린더 있는 곳 클릭 시 팝업
	$(document).on("click", ".calendar", function() {
		$(".popupContainer").removeClass("hide");
		$(".popupContainer>div:nth-child(2)").removeClass("hide");
	});
	// 블럭 색 바꾸기 창 띄우기
	$(document).on("click", ".setBlockColor", function() {
		$(".popupContainer>div:nth-child(3)").toggleClass("hide");
	});
	// 블럭 색 지정하기
	$(document).on("click", ".blockColor", function() {
		let colorIdx = $(this).index();
		let color = "color: " + blockColorArr[colorIdx];
		$(".setBlockColor").attr("style",color);
		$(".popupContainer>div:nth-child(3)").addClass("hide");
	});
	// *************** 교통 팝업 *****************
	// (임시) 캘린더 클릭 시 팝업
	$(document).on(".calendar", function() {
		$(".popupContainer").removeClass("hide");
		$(".popupContainer>div:nth-child(4)").removeClass("hide");
	});
	// 교통 클릭 시 닫힘
	$(document).on("click", ".popupContainer>div:nth-child(4)>span", function() {
		$(".popupContainer").addClass("hide");
		$(".popupContainer>div:nth-child(4)").addClass("hide");
	});
});