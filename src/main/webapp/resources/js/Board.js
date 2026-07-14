// 댓글 페이지 처리 필드
let gPageLock = false; // 페이지 불러오는 중에는 작동하지 않도록 막아둠
let gPageNum = 1;

// 댓글 페이지 불러오기 함수
function newPage(pageNum, bno) {
	if(gPageLock) return;
	gPageLock = true;
	
	// 댓글 불러오기 비동기
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
		$("#commentList>*").remove();
		let arr = data.comments;
		let loginId = data.loginId;
		for(let i=0;i<arr.length;i++){
			let item = arr[i];
			
			const str1 = `
				<div class="comment">
					<div>
						<div>
			`;
			
			// 프로필 이미지
			let str2 = "";
			if(item.proFile==null){
				str2 = `<img src="../resources/img/기본 프로필.png"/>`;
			} else {
				str2 = `<img src="../resources/img/${item.proFile}" />`;
			}
			const str3 = `
							<div>${item.nickName}</div>
						</div>
			`;
			
			// 수정, 삭제 버튼
			let str4 = "";
			if(item.memberId==loginId) {
				str4 = `
							<div>
								<div class="moveDate">
									<svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="size-6">
										<path stroke-linecap="round" stroke-linejoin="round" d="m16.862 4.487 1.687-1.688a1.875 1.875 0 1 1 2.652 2.652L10.582 16.07a4.5 4.5 0 0 1-1.897 1.13L6 18l.8-2.685a4.5 4.5 0 0 1 1.13-1.897l8.932-8.931Zm0 0L19.5 7.125M18 14v4.75A2.25 2.25 0 0 1 15.75 21H5.25A2.25 2.25 0 0 1 3 18.75V8.25A2.25 2.25 0 0 1 5.25 6H10" />
									</svg>
									수정
								</div>
								<div class="moveDate" data-cno="${item.cno}">
									<svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="size-6">
										<path stroke-linecap="round" stroke-linejoin="round" d="m14.74 9-.346 9m-4.788 0L9.26 9m9.968-3.21c.342.052.682.107 1.022.166m-1.022-.165L18.16 19.673a2.25 2.25 0 0 1-2.244 2.077H8.084a2.25 2.25 0 0 1-2.244-2.077L4.772 5.79m14.456 0a48.108 48.108 0 0 0-3.478-.397m-12 .562c.34-.059.68-.114 1.022-.165m0 0a48.11 48.11 0 0 1 3.478-.397m7.5 0v-.916c0-1.18-.91-2.164-2.09-2.201a51.964 51.964 0 0 0-3.32 0c-1.18.037-2.09 1.022-2.09 2.201v.916m7.5 0a48.667 48.667 0 0 0-7.5 0" />
									</svg>
									삭제
								</div>
							</div>
						</div>
						<input data-cno="${item.cno}" type="text" value="${item.content}"/>
				`;
			}
			else {
				str4 = `
					</div>
					<div>${item.content}</div>				
				`
			}
			
			const str5 = `
					<div>${item.finalDate}</div>
				</div>
			`;
			
			$("#commentList").append(str1+str2+str3+str4+str5);
		}
		
		// 상단 페이지 수 수정
		$("#topPageNation>div>span:nth-child(1)").html(gPageNum);
		$("#topPageNation>div>span:nth-child(2)").html(data.lastPageNum);
		
		// 댓글 넘기기가 필요하지 않을 경우 그리지 않음
		$("#bottomPageNation>*").remove();
		if(data.lastPageNum==1) return;
		
		// 페이지 이동 버튼
		if(parseInt((gPageNum-1)/5)*5+1>5) $("#bottomPageNation").append(`<div id="beforePage" class="moveDate">이전</div>`);
		for(let i=parseInt((gPageNum-1)/5)*5+1;i<=parseInt((gPageNum-1)/5)*5+5;i++){
			if(i>data.lastPageNum) return;
			if(i==gPageNum) {
				$("#bottomPageNation").append(`<div class="fixDate">${i}</div>`);
			} else {
				$("#bottomPageNation").append(`<div class="moveDate nthPage">${i}</div>`);
			}
		}
		if(parseInt((gPageNum-1)/5)*5+5<data.lastPageNum+1) $("#bottomPageNation").append(`<div id="nextPage" class="moveDate">다음</div>`);
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
	// ************* 공통으로 쓸 필드 *************
	let bno = $(".fee").attr('data-bno');
	
	// ************ 첫 화면 불러올 때 초기화 ********
	// 댓글
	newPage(gPageNum,bno);
	// 예산 합
	getSum();
	// 찜한 게시글 하트
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
			"field" : field,
			"fee" : currentFee,
			"bno" : bno
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
	
	// *************** 예산 아래 버튼들 ****************
	// 게시글 복제
	$(document).on("click", ".other>div", function() {
		location.href="/TravelPlanner/newplan/start?bno=" + bno;
	});
	// 삭제
	$(document).on("click", ".my>div:nth-child(1)", function() {
		alert("정말 삭제하시겠습니까?");
		location.href="/TravelPlanner";
	});
	// 수정
	$(document).on("click", ".my>div:nth-child(2)", function() {
		location.href = $(location).attr('pathname') + "/week";
	});
	// AI 비용
	$(document).on("click", ".my>div:nth-child(3)", function() {
		alert("계산해드립니당");
	});
	// 캘린더 동기화
	$(document).on("click", ".my>div:nth-child(4)", function() {
		alert("동기화!");
	});
	
	// *************** 댓글 페이지네이션 ****************
	// 이전
	$(document).on("click", "#beforePage", function() {
		gPageNum = parseInt((gPageNum-1)/5)*5;
		newPage(gPageNum,bno);
	});
	// 다음
	$(document).on("click", "#nextPage", function() {
		gPageNum = parseInt((gPageNum-1)/5)*5+6;
		newPage(gPageNum,bno);
	});
	// 페이지 버튼 클릭 시 불러오기
	$(document).on("click", ".nthPage", function() {
		let page = Number($(this).html());
		gPageNum = page;
		newPage(gPageNum,bno);
	});
	
	// ************** 댓글 수정, 삭제 *******************
	// 포커스 발생 시 현재 댓글 저장
	$('.comment > input').on('focus', function () {
	    $(this).data('original-value', $(this).val());
	});
	// 엔터키로도 저장 작동
	$('.comment > input').on('keydown', function (e) {
	    if(e.keyCode == 13){
	        $(this).blur();
	    }
	});
	// 수정 버튼 클릭 시 포커스
	$(document).on("click", ".comment>div>div:nth-child(2)>div:nth-child(1)", function() {
	    $(this).parent().parent().parent().find("input").focus();
	});
	// 댓글 수정
	$(document).on("blur", ".comment > input", function() {
		const originalContent = String($(this).data('original-value'));
    	const currentContent = $(this).val();
    	const cno = $(this).data("cno");
    	
    	// 내용이 없거나 공백만 존재한다면 종료
		if(currentContent.trim()=="") {
			alert("빈 내용으로는 수정할 수 없습니다.");
			return;
		}
    	
		// 제목에 변경 사항이 없다면 종료
		if (originalContent === currentContent) return;
		
		const jsonData = {
			"content" : currentContent,
			"cno" : cno
		};
		const initData = {
			method: "post",
			headers: {
				"Content-Type": "application/json"
			},
			body: JSON.stringify(jsonData)
		};
		fetch("/TravelPlanner/updateComment", initData)
		.then(function(response) {
			return response.text();
		})
		.then(function(data) {
			console.log(data);
		})
		.catch(function(error) {
			alert("에러! : 댓글 저장에 문제가 발생했습니다. 다시 시도해주세요." + error);
		});
	});
	// 댓글 삭제
	$(document).on("click", ".comment>div>div:nth-child(2)>div:nth-child(2)", function() {
		if(confirm("정말 삭제 하시나요?")) {
			const cno = $(this).data("cno");
	   
			const jsonData = {
				"cno" : cno
			};
			const initData = {
				method: "post",
				headers: {
					"Content-Type": "application/json"
				},
				body: JSON.stringify(jsonData)
			};
			fetch("/TravelPlanner/deleteComment", initData)
			.then(function(response) {
				return response.text();
			})
			.then(function(data) {
				alert("삭제되었습니다.");
				// 페이지 다시 그리기
				newPage(gPageNum,bno);
			})
			.catch(function(error) {
				alert("에러! : 댓글 삭제에 문제가 발생했습니다. 다시 시도해주세요." + error);
			});
			}
	});
	// 댓글 등록
	$(document).on("click", "#commentInput>div", function() {
		let content = $(this).parent().find("textarea").val();
		const $btn = $(this);
		
		// 내용이 없거나 공백만 존재한다면 종료
		if(content.trim()=="") {
			$(this).parent().find("textarea").val("");
			return;
		}
		
		const jsonData = {
			"content" : content,
			"bno" : bno
		};
		const initData = {
			method: "post",
			headers: {
				"Content-Type": "application/json"
			},
			body: JSON.stringify(jsonData)
		};
		fetch("/TravelPlanner/insertComment", initData)
		.then(function(response) {
			return response.text();
		})
		.then(function(data) {
			console.log(data);
			alert("정상 등록");
			
			// 페이지 다시 그리기
			newPage(gPageNum,bno);
		})
		.catch(function(error) {
			alert("에러! : 댓글 저장에 문제가 발생했습니다. 다시 시도해주세요." + error);
		});
		
		// textarea 비우기
		$(this).parent().find("textarea").val("");
	});
	// ****************** 게시글 찜 ************************8
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
});