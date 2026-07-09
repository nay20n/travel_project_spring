/* 
let nextPageNum = 1;
let ajaxLock = false;
function addMyBoardPage() {
	if(ajaxLock) return;
	
	gAjaxLock = true;
	
	$.ajax({
		type : "post",
		url: "",
		data: {"pageNum": gNextPageNumber++},
		success: function(arr){
			console.log(arr);
			for(let i=0; i < arr.length; i++) {
				const str = `
					<tr>
						<th><c:out value="${status.count}"/>.</th>
						<td>
							<c:choose>
								<c:when test="${myBoard.dDay>0}">D+${myBoard.dDay}</c:when>
								<c:when test="${myBoard.dDay<0}">D${myBoard.dDay}</c:when>
								<c:otherwise>D-Day</c:otherwise>
							</c:choose>
						</td>
						<td>${myBoard.title}</td>
						<td>${myBoard.nickName}</td>
						<td>
							<c:choose>
								<c:when test="${myBoard.isLiked==1}">
									<svg class = "fillHeart" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="size-6">
										<path stroke-linecap="round" stroke-linejoin="round" d="M21 8.25c0-2.485-2.099-4.5-4.688-4.5-1.935 0-3.597 1.126-4.312 2.733-.715-1.607-2.377-2.733-4.313-2.733C5.1 3.75 3 5.765 3 8.25c0 7.22 9 12 9 12s9-4.78 9-12Z"></path>
									</svg>
								</c:when>
								<c:otherwise>
									<svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="size-6">
										<path stroke-linecap="round" stroke-linejoin="round" d="M21 8.25c0-2.485-2.099-4.5-4.688-4.5-1.935 0-3.597 1.126-4.312 2.733-.715-1.607-2.377-2.733-4.313-2.733C5.1 3.75 3 5.765 3 8.25c0 7.22 9 12 9 12s9-4.78 9-12Z"></path>
									</svg>
								</c:otherwise>
							</c:choose>
							️<span>${myBoard.cntLike}</span>
						</td>
					</tr>
				`;
				$("#table_board").append(str); 
				}
				gAjaxLock = false;
			},
			error: function(r,s,e) {
				alert(`[에러] code: \${r.status}
					msg:\${r.responseTest}
					error:\${e}`);
			}
	});
}
*/

$(function() {

	$(window).on('beforeunload', function(event) {
		
	});

	/***************** 헤더 ************/
	// 트래플 플래너 로그 클릭
	$("#header > div:nth-child(1)").click(function() {
		location.href="MainPage.html";
	});
	//로그아웃 버튼 클릭 
	$("#header > div:nth-child(2) > div:nth-child(1)> a").click(function() {
		alert("로그아웃 되었습니다.");
		location.href="MainPage.html";
	});
	//마이페이지 버튼 클릭
	$("#header > div:nth-child(2) > div:nth-child(2) > a").click(function() {
		location.href="MyPage.html";
	});
	
	/***************** 테이블 ******************/
	// 테이블 행 클릭시 게시글로 이동 
	$(".table tbody tr").click(function() {
		let bno = $(this).data("bno");
		//alert(bno);
		location.href="plan/" + bno;
	});
	// 찜하기 하트 클릭 
	$(".table svg").click(function() {
		let cntLike = Number($(this).parent().find("span").html());
		$(this).toggleClass("fillHeart");
		if(!$(this).hasClass("fillHeart")){
			$(this).parent().find("span").html(cntLike - 1)
		} else {
			$(this).parent().find("span").html(cntLike + 1);
		}
	});
	$(".table").scroll(function(e){
		var containerScrollTop = $(this).scrollTop();
    	var containerHeight = $(this).height()
	    var contentHeight = $(this)[0].scrollHeight;
	    if(containerScrollTop + containerHeight >= contentHeight - 1) {
	    	alert("스크롤 끝까지 내림");
	    	addMyBoardPage();
		} 
	});
	
	/***************** 정보수정 팝업창 ************/
	// 정보수정 버튼 클릭
	$("#profile > div:nth-child(6)").click(function() { 
		$("#popupEditInfo").show();
	});
	// 정보창 닫기 버튼 클릭
	$("#popupHeader > svg").click(function() {
		$("#popupEditInfo").hide();
	});
	// 정보수정팝업 확인 버튼 클릭(정보수정창으로 이동)
	$("#popupPw > div > span").click(function() {
	
		if($("#popupPw > div > input").val()==""){ //비밀번호가 비어잇으면
			$("#popupContent > div:nth-child(3)").show();
			$("#popupPw > div > input").addClass("borderWraning");
		}else {
			$("#popupContent > div:nth-child(3)").hide();
			location.href="mypage/edit";
		}
	});
	// 비밀번호 재설정 링크 클릭 
	$("#popupContent > div:nth-child(4) > a").click(function() {
		location.href="forget";
	});
	// 구글 계정 인증 버튼 클릭
	$("#popupVerify > img").click(function() {
		alert("구글 인증하는 API 연결하기");
	});
	
});