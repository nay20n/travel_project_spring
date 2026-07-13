// 댓글 페이지 처리 함수
let gPageLock = false;
let gPageNum = 1;
function newPage(pageNum, bno) {
	if(gPageLock) return;
	gPageLock = true;
	
	const jsonData = {
		"pageNum" : pageNum,
		"bno" : bno
	};
	const initData = {
		method: "post",
		headers: {
			"Content-Type": "application/json"
		},
		body: JSON.stringify(jsonData)
	};
	fetch("/TravelPlanner/getBoardComment", initData)
	.then(function(response) {
		return response.json();
	})
	.then(function(data) {
		console.log(data);
		$("#topPageNation>div>span:nth-child(1)").html(gPageNum);
		$("#topPageNation>div>span:nth-child(2)").html(data.lastPageNum);
		let arr = data.comments;
		for(let i=0;i<arr.length;i++){
			let item = arr[i];
			const str1 = `
				<div class="comment">
					<div>
						<div>
			`;
			let str2 = "";
			if(item.proFile==null){
				str2 = `<img src="../resources/img/기본 프로필.png"/>`;
			} else {
				str2 = `<img src="../resources/img/${item.proFile}" />`;
			}
			const str3 = `
							<div>${item.nickName}</div>
						</div>
						<div>
							<div class="moveDate" data-memberId="${item.memberId}">
								<svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="size-6">
									<path stroke-linecap="round" stroke-linejoin="round" d="m16.862 4.487 1.687-1.688a1.875 1.875 0 1 1 2.652 2.652L10.582 16.07a4.5 4.5 0 0 1-1.897 1.13L6 18l.8-2.685a4.5 4.5 0 0 1 1.13-1.897l8.932-8.931Zm0 0L19.5 7.125M18 14v4.75A2.25 2.25 0 0 1 15.75 21H5.25A2.25 2.25 0 0 1 3 18.75V8.25A2.25 2.25 0 0 1 5.25 6H10" />
								</svg>
								수정
							</div>
							<div class="moveDate" data-memberId="${item.memberId}">
								<svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="size-6">
									<path stroke-linecap="round" stroke-linejoin="round" d="m14.74 9-.346 9m-4.788 0L9.26 9m9.968-3.21c.342.052.682.107 1.022.166m-1.022-.165L18.16 19.673a2.25 2.25 0 0 1-2.244 2.077H8.084a2.25 2.25 0 0 1-2.244-2.077L4.772 5.79m14.456 0a48.108 48.108 0 0 0-3.478-.397m-12 .562c.34-.059.68-.114 1.022-.165m0 0a48.11 48.11 0 0 1 3.478-.397m7.5 0v-.916c0-1.18-.91-2.164-2.09-2.201a51.964 51.964 0 0 0-3.32 0c-1.18.037-2.09 1.022-2.09 2.201v.916m7.5 0a48.667 48.667 0 0 0-7.5 0" />
								</svg>
								삭제
							</div>
						</div>
					</div>
					<div>${item.content}</div>
					<div>${item.finalDate}</div>
				</div>
			`;
			$("#commentList").append(str1+str2+str3);
		}
	})
	.catch(function(error) {
		alert("에러! : " + error);
	});
	gPageLock = false;
}

// 예산 금액 합 갱신 함수
function getSum() {
	let sum = 0;
	$(".fee").each(function(idx,item) {
		let fee = Number($(this).val());
		sum += fee;
	});
	$("#content1>div:nth-child(2)>div:nth-child(2)>div:nth-child(7)>span:nth-child(2)").html(sum+"원");
}


$(function() {
	let bno = $(".fee").attr('data-bno');
	// 댓글 호출
	newPage(gPageNum,bno);
	// 예산 합 초기화
	getSum();
	// 찜한 게시글 하트 채워두기
	if($("#content3").hasClass("1")) $(".heart").addClass("fillHeart");
	
	// ************* 예산 사용자 입력 ****************
	// 포커스 발생 시 현재 비용 값 저장
	$('.fee').on('focus', function () {
	    $(this).data('original-value', $(this).val());
	});
	// 엔터키로도 저장 작동
	$('.fee').on('keydown', function (e) {
	    if(e.keyCode == 13){
	        $(this).blur();
	    }
	});
	// 숫자 외 입력 방지
	$('.fee').on('input', function () {
        $(this).val($(this).val().replace(/[^-0-9]/g, ''));
	});
	// 예산 저장 비동기
	$('.fee').on('blur', function () {
		const originalFee = String($(this).data('original-value'));
    	const currentFee = $(this).val();
    	const field = $(this).attr('data-field');
    	const bno = $(this).attr('data-bno');
    	
		// 제목에 변경 사항이 없다면 종료
		if (originalFee === currentFee) return;
		
		const jsonData = {
			field : field,
			fee : currentFee,
			bno : bno
		};
		const initData = {
			method: "post",
			headers: {
				"Content-Type": "application/json"
			},
			body: JSON.stringify(jsonData)
		};
		fetch("/TravelPlanner/updateBoardFee", initData)
		.then(function(response) {
			return response.text();
		})
		.then(function(data) {
			console.log(data);
		})
		.catch(function(error) {
			alert("에러! : 제목 저장에 문제가 발생했습니다. 다시 시도해주세요." + error);
		});
		// 예산 합 초기화
		getSum();
	});
	// *************** 예산 아래 버튼 ****************
	// 게시글 복제
	$(".other>div").on('click', function () {
		alert("게시글이 복제되었습니다!");
		location.href="/TravelPlanner/newplan/start";
	});
	// 삭제
	$(".my>div:nth-child(1)").on('click', function () {
		alert("정말 삭제하시겠습니까?");
		location.href="/TravelPlanner";
	});
	// 수정
	$(".my>div:nth-child(2)").on('click', function () {
		location.href = $(location).attr('pathname') + "/week";
	});
	// AI 비용
	$(".my>div:nth-child(3)").on('click', function () {
		alert("계산해드립니당");
	});
	// 캘린더 동기화
	$(".my>div:nth-child(4)").on('click', function () {
		alert("동기화!");
	});
	// *******************댓글*********************
	// 댓글 페이지네이션
	$("#beforePage").on('click', function () {
		// 페이지 넘기는 div 수정
	});
	$("#nextPage").on('click', function () {
		// 페이지 넘기는 div 수정
	});
	$(".nthPage").on('click', function () {
		let page = Number($(this).html());
		if(page!=gPageNum) {
			newPage(page);
			gPageNum = page;
		}
	});
	// 찜 색칠
	$("#content3 > div:nth-child(1) > div > div:nth-child(1)").on('click', function () {
		let bno = $(this).data("bno");
		let cntLike = Number($(this).find("span").html());
		//alert(bno);
		
		if($(this).find("svg").hasClass("fillHeart")){ // 찜 삭제
			$(this).find("span").html(cntLike - 1);
			fetch("/TravelPlanner/deleteLikeBoard?bno="+bno, {method:"POST"})
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
			$(this).find("span").html(cntLike + 1);
			fetch("/TravelPlanner/insertLikeBoard?bno="+bno, {method:"POST"})
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
		
		$(this).find("svg").toggleClass("fillHeart");
	});
	// 댓글 수정
	$(".comment>div>div:nth-child(2)>div:nth-child(1)").on('click', function () {
		alert("수정 버튼!");
	});
	// 댓글 삭제
	$(".comment>div>div:nth-child(2)>div:nth-child(2)").on('click', function () {
		alert("정말 삭제 하시나요?");
	});
	// 댓글 등록
	$("#commentInput>div").on('click', function () {
		alert("등록~");
	});
});