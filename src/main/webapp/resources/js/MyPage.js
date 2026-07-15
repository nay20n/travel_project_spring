let gPageMy = 1;
let gPageLike = 1;
let gPageCom = 1;

function newPageMyBoard(pageNum, lastNum) {
	
	fetch("getMyBoard?pageNum=" + pageNum, {method: "post"})
	.then(function(response) {
		return response.json();
	})
	.then(function(data) {
		console.log(data);
		
		for(let i=0; i<data.length; i++) {
		
			let dDay = `D-Day`;
			if(data[i].dDay>0){
				dDay = `D+${data[i].dDay}`;
			} else if(data[i].dDay<0) {
				dDay = `D${data[i].dDay}`;
			} 	
			
			let isLiked = ``;
			if(data[i].isLiked==1){
				isLiked = `<svg class = "fillHeart" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="size-6">
								<path stroke-linecap="round" stroke-linejoin="round" d="M21 8.25c0-2.485-2.099-4.5-4.688-4.5-1.935 0-3.597 1.126-4.312 2.733-.715-1.607-2.377-2.733-4.313-2.733C5.1 3.75 3 5.765 3 8.25c0 7.22 9 12 9 12s9-4.78 9-12Z"></path>
						   </svg>`;
			} else {
				isLiked = `<svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="size-6">
								<path stroke-linecap="round" stroke-linejoin="round" d="M21 8.25c0-2.485-2.099-4.5-4.688-4.5-1.935 0-3.597 1.126-4.312 2.733-.715-1.607-2.377-2.733-4.313-2.733C5.1 3.75 3 5.765 3 8.25c0 7.22 9 12 9 12s9-4.78 9-12Z"></path>
						   </svg>`;
			}
			
			let html = ` <tr data-bno="${data[i].bno}">
							<th>${++lastNum}.</th>
							<td>${dDay}</td>
							<td>${data[i].title}</td>
							<td>${data[i].nickName}</td>
							<td> 
								${isLiked}
								️<span>${data[i].cntLike}</span>
							</td>
					  </tr>`;
			$("body > div:nth-child(3) .table tbody").append(html);
		}
	})
	.catch(function(error) {
		alert("에러! : " + error);
	});
}
function newPageLikedAndCommentBoard(pageNum, mapping, nthChild, lastNum) {
	fetch(mapping + "?pageNum=" + pageNum, {method: "post"})
	.then(function(response) {
		return response.json();
	})
	.then(function(data) {
		console.log(data);
		
		for(let i=0; i<data.length; i++) {
			let isLiked = ``;
			if(data[i].isLiked==1){
				isLiked = `<svg class = "fillHeart" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="size-6">
								<path stroke-linecap="round" stroke-linejoin="round" d="M21 8.25c0-2.485-2.099-4.5-4.688-4.5-1.935 0-3.597 1.126-4.312 2.733-.715-1.607-2.377-2.733-4.313-2.733C5.1 3.75 3 5.765 3 8.25c0 7.22 9 12 9 12s9-4.78 9-12Z"></path>
						   </svg>`;
			} else {
				isLiked = `<svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="size-6">
								<path stroke-linecap="round" stroke-linejoin="round" d="M21 8.25c0-2.485-2.099-4.5-4.688-4.5-1.935 0-3.597 1.126-4.312 2.733-.715-1.607-2.377-2.733-4.313-2.733C5.1 3.75 3 5.765 3 8.25c0 7.22 9 12 9 12s9-4.78 9-12Z"></path>
						   </svg>`;
			}
			let html = ` <tr data-bno="${data[i].bno}">
							<th>${++lastNum}.</th>
							<td>${data[i].title}</td>
							<td>${data[i].nickName}</td>
							<td> 
								${isLiked}
								️<span>${data[i].cntLike}</span>
							</td>
						</tr>`;
			$(`body > div:nth-child(${nthChild}) .table tbody`).append(html);
		}
	})
	.catch(function(error) {
		alert("에러! : " + error);
	});
}

$(function() {
	/***************** 테이블 ******************/
	// 테이블 행 클릭시 게시글로 이동 
	$(document).on("click", ".table table > tbody > tr > *:not(':last-child')", function() {
		let bno = $(this).parent().data("bno");
		//alert(bno);
		location.href="plan/" + bno;
	});
	
	// 찜하기 하트 클릭 
	$(document).on("click", ".table svg", function() {
		let cntLike = Number($(this).parent().find("span").html()); // 찜 갯수 가져오기
		let cntLikeTotal = Number($("#profile > div:nth-child(4) > div:nth-child(3)").html());
		let bno = $(this).parent().parent().data("bno");
		//alert(bno);
		if($(this).hasClass("fillHeart")){ // 찜 삭제
			$(this).parent().find("span").html(cntLike - 1);
			$("#profile > div:nth-child(4) > div:nth-child(3)").html(cntLikeTotal-1);
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
			$("#profile > div:nth-child(4) > div:nth-child(3)").html(cntLikeTotal+1);
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
	// 내 게시글 스크롤
	$("body > div:nth-child(3) .table").scroll(function(e){
		var containerScrollTop = $(this).scrollTop();
    	var containerHeight = $(this).height()
	    var contentHeight = $(this)[0].scrollHeight;
	    if(containerScrollTop + containerHeight >= contentHeight - 1) {
	    	let lastNum = Number($(this).find('tr:last th:first').text());
	    	if (lastNum % 10 ==0)
				newPageMyBoard(++gPageMy, lastNum);
		} 
	});
	// 내가 찜한 게시글 스크롤
	$("body > div:nth-child(4) .table").scroll(function(e){
		var containerScrollTop = $(this).scrollTop();
    	var containerHeight = $(this).height()
	    var contentHeight = $(this)[0].scrollHeight;
	    if(containerScrollTop + containerHeight >= contentHeight - 1) {
	    	let lastNum = Number($(this).find('tr:last th:first').text());
	    	if (lastNum % 10 ==0)
				newPageLikedAndCommentBoard(++gPageLike, "getLikedBoard",4, lastNum);
		} 
	});
	// 내가 댓글을 단 게시글 스크롤
	$("body > div:nth-child(5) .table").scroll(function(e){
		var containerScrollTop = $(this).scrollTop();
    	var containerHeight = $(this).height()
	    var contentHeight = $(this)[0].scrollHeight;
	    if(containerScrollTop + containerHeight >= contentHeight - 1) {
	    	//alert("스크롤 끝까지 내림");
	    	let lastNum = Number($(this).find('tr:last th:first').text());
	    	if (lastNum % 10 ==0)
				newPageLikedAndCommentBoard(++gPageCom, "getCommentBoard",5,lastNum);
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