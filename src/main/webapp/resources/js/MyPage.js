$(function() {
	/***************** 테이블 ******************/
	// 테이블 행 클릭시 게시글로 이동 
	$(".table table > tbody > tr > *:not(':last-child')").click(function() {
		let bno = $(this).parent().data("bno");
		//alert(bno);
		location.href="plan/" + bno;
	});
	// 찜하기 하트 클릭 
	$(".table svg").click(function() {
		let cntLike = Number($(this).parent().find("span").html()); // 찜 갯수 가져오기
		let bno = $(this).parent().parent().data("bno");
		//alert(bno);
		if($(this).hasClass("fillHeart")){ // 찜 삭제
			$(this).parent().find("span").html(cntLike - 1);
			fetch("deleteLikeBoard?bno="+bno, {method:"POST"})
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
			$(this).parent().find("span").html(cntLike + 1);
			fetch("insertLikeBoard?bno="+bno, {method:"POST"})
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
		$(this).toggleClass("fillHeart"); // css 꾸미기
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