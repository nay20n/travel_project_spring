// 게시글 페이지 처리 필드
let gPageLock = false; // 페이지 불러오는 중에는 작동하지 않도록 막아둠
let scrollLock = false;
let gPageNum = 1;
let boardListIdx = 1;

// 이메일 유효성 체크
const pattern = /^[A-Za-z0-9_\.\-]+@[A-Za-z0-9\-]+\.[A-za-z0-9\-]+/;
function emailValidChk(email) {
    if(pattern.test(email) === false) { return false; }
    else { return true; }
}

// 게시글 페이지 불러오기 함수
function newPage(pageNum,mapping,input) {
	if(gPageLock) return;
	gPageLock = true;
	
	// 댓글 불러오기 비동기
	const jsonData = {
		"pageNum" : pageNum,
		"input" : input
	};
	const initData = {
		method: "post",
		headers: {
			"Content-Type": "application/json"
		},
		body: JSON.stringify(jsonData)
	};
	fetch(mapping, initData)
	.then(function(response) {
		return response.json();
	})
	.then(function(data) {
		if(data.length<1) return;
		let str1;
		let str2;
		let dataIdx = 0;
		if(data.length<8) gPageNum = -1;
		for(let i=0;i<2;i++){ //2줄 생성
			str1 = `
				<div id="boardList${boardListIdx}">
				</div>
			`;
			$("#board").append(str1);
			// 4개씩 채우기
			for(let j=0;j<4;j++) {
				if(dataIdx>data.length-1) break;
				let board = data[dataIdx];
				//console.log(board);
				
				// 지도 중앙, 위치 쿼리 만들기
				let mapData = board.mapData;
				let sumLat = 0;
				let sumLng = 0;
				let path = "weight:5|color:0xE53935|";
				for(let k=0;k<mapData.length;k++) {
					sumLat+=mapData[k].lat;
					sumLng+=mapData[k].lng;
					path += mapData[k].lat+","+mapData[k].lng;
					if(k==mapData.length-1) break;
					path += "|";
				}
				let center = sumLat/mapData.length + "," + sumLng/mapData.length;
				//console.log(center);
				//console.log(path);
				const params = new URLSearchParams({
				    center: center,
				    path: path
				});
				//console.log(params.toString());
				
				str2 = `
					<div class="bs" data-bno="${board.bno}">
						<img src="getBoardImg?${params.toString()}">
						</img>
						<div>
							<span>${board.title}</span>
							<span>
								<svg data-bno="${board.bno}" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="size-6">
									<path stroke-linecap="round" stroke-linejoin="round" d="M21 8.25c0-2.485-2.099-4.5-4.688-4.5-1.935 0-3.597 1.126-4.312 2.733-.715-1.607-2.377-2.733-4.313-2.733C5.1 3.75 3 5.765 3 8.25c0 7.22 9 12 9 12s9-4.78 9-12Z" />
								</svg>
								${board.year}년 ${board.month}월
							</span>
						</div>
						<div>
							<span>
								<svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="size-6">
									<path stroke-linecap="round" stroke-linejoin="round" d="M12 6v6h4.5m4.5 0a9 9 0 1 1-18 0 9 9 0 0 1 18 0Z" />
								</svg>
								${board.elapsedTime}
							</span>
						</div>
					</div>
				`;
				$(`#boardList${boardListIdx}`).append(str2);
				dataIdx++;
			}
			boardListIdx++;
		}
		scrollLock = false;
		gPageLock = false;
		gPageNum++;
	})
	.catch(function(error) {
		alert("에러! : " + error);
	});
}

// 무한 스크롤로 게시글 불러오기
window.onscroll = function() {
	if(Math.round( $(window).scrollTop()) == $(document).height() - $(window).height()) {
		// 더 불러올 게시글이 없으면 종료
		if(gPageNum==-1) return;
		// 검색중이면 종료
		if(scrollLock) return;
		
		let input = $("#search>span").html();
		let mapping;
		
		if(input=="최신 여행 계획"){
			mapping = "getBoardsLastestOrder";
		} else {
			mapping = "getBoardsKeyOrder";
		}
		
		newPage(gPageNum,mapping,input);
	}
};

$(function() {
	newPage(gPageNum,"getBoardsLastestOrder","input");
	
	// ************ 메인 *******************
	// 직접 여행 만들기 (로그인 되었을 경우)
	$("#main>div:nth-child(5)").click(function() {
		location.href="newplan/arr";
	});
	
	// ************ 게시글 검색 *******************
	// 엔터로도 작동
	$("#search>div>input").on('keydown', function (e) {
		if(e.keyCode == 13){
			$("#search>div>svg").trigger("click");
		}
	});
	// 검색 비동기
	$("#search>div>svg").click(function() {
		scrollLock = true;
		let search = $(this).parent().find("input").val();
		
		// 검색 키워드로 옆 글씨 수정
		$("#search>span").html(search);
		
		// 초기화
		$("#board>*").remove(); //게시글 리스트 초기화
		gPageNum = 1; //페이지 초기화
		
		newPage(gPageNum,"getBoardsKeyOrder",search);
	});
	
	// ************** 게시글 이동 *****************
	$(document).on("click", "#board>div>div>img:nth-child(1)", function() {
		let bno = $(this).parent().attr("data-bno");
		location.href="plan/" + bno;
	});
	
	// ****************** 게시글 찜 ************************
	$(document).on("click", "#board>div>div>div:nth-child(2)>span>svg", function() {
		let bno = $(this).data("bno");
		//alert(bno);
		
		if($(this).hasClass("fillHeart")){ // 찜 삭제
			fetch("deleteLikeBoard?bno="+bno, {method:"POST"})
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
			fetch("insertLikeBoard?bno="+bno, {method:"POST"})
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
		
		$(this).toggleClass("fillHeart");
	});

	// ************** 팝업 ******************
	// 팝업창 닫기
	$(".popupContainer>div>svg:nth-child(1)").click(function() {
		$(".popupContainer").attr("style","display: none");
		$(".popupContent").attr("style","display: none");
	});
	// 비밀번호 재설정
	$(".popupContainer>div:nth-child(1)>div:nth-child(3)>div:nth-child(4)>span:nth-child(2)").click(function() {
		let inputMail = $(this).parent().find(".userEmail").val();
		if(inputMail=="") {
			Toastify({
			  text: "이메일을 입력해주세요.",
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
			return;
		}
		fetch("isExistMail?email=" + inputMail, {method: "post"})
		.then(function(response) {
			return response.text();
		})
		.then(function(data) {
			if(data=="empty") {
				Toastify({
				  text: "이메일을 다시 확인해주세요.",
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
				return;
			}
			if(data=="true") {
				location.href="forget?email="+inputMail;
			}
		})
		.catch(function(error) {
			alert("에러! : " + error);
		});
	});
	// 로그인
	$(".userPw, .userEmail").keypress(function(e) {
		if(e.keyCode == 13){
			$(".login").trigger("click");
		}
	});
	$(".login").click(function() {
		let inputId = $(this).parent().find(".userEmail").val();
		let inputPw = $(this).parent().find(".userPw").val();
		
		let form = document.createElement('form');
	    form.setAttribute('method', 'post');
	    form.setAttribute('action', "login");
	    document.charset = "UTF-8";
	
		let hiddenField1 = document.createElement('input');
		hiddenField1.setAttribute('type', 'hidden');
		hiddenField1.setAttribute('name', "inputId");
		hiddenField1.setAttribute('value', inputId);
		
		let hiddenField2 = document.createElement('input');
		hiddenField2.setAttribute('type', 'hidden');
		hiddenField2.setAttribute('name', "inputPw");
		hiddenField2.setAttribute('value', inputPw);
		
		form.appendChild(hiddenField1);
		form.appendChild(hiddenField2);
	
	    document.body.appendChild(form);
	    form.submit();
	});
	//카카오로 로그인
	$(".popupContainer>div:nth-child(1)>div:nth-child(4)>div:nth-child(1)").click(function() {
		location.href="kakaologin?mapping=login";
	});
	//네이버로 로그인
	$(".popupContainer>div:nth-child(1)>div:nth-child(4)>div:nth-child(2)").click(function() {
		location.href="naverlogin?mapping=login";
	});
	// 이메일로 가입하기
	$(".popupContainer>div:nth-child(1)>div:nth-child(4)>div:nth-child(3)").click(function() {
		$(".popupContainer>div:nth-child(1)").hide();
		$(".popupContainer>div:nth-child(2)").show();
	});
	// 이메일로 가입하기-인증번호 받기
	$("body>div.popupContainer>div:nth-child(2)>div>div.inputBdDiv>input").keypress(function(e) {
		if(e.keyCode == 13){
			$(".popupContainer > div:nth-child(2) > div:nth-child(2) > div:nth-child(4)").trigger("click");
		}
	});
	$(".sendMail").click(function() {
		if($(this).parent().find(".checkBox").is(":checked")) {
			// 이메일 전송 비동기 및 임시 가입
			let email = $(".popupContainer>div:nth-child(2)>div>.inputBdDiv>input[type=email]").val();
			if(email=="") {
				Toastify({
				  text: "내용을 입력해주세요.",
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
				return;
			}
			if(!emailValidChk(email)) {
				Toastify({
				  text: "올바른 이메일을 입력해주세요.",
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
				return;
			}
			//console.log(email);
			fetch("join?email=" + email, {method: "post"})
			.then(function(response) {
				return response.text();
			})
			.then(function(data) {
				//console.log(data);
				if(data=="duplicate") {
					Toastify({
					  text: "가입되어 있는 이메일입니다.",
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
					return;
				}
				if(data=="fail") {
					Toastify({
					  text: "이메일 발송에 문제가 발생했습니다. 조금 뒤 다시 시도해주세요.",
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
					return;
				}
				Toastify({
				  text: "인증 메일을 전송했습니다.",
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
				$(".popupContainer>div:nth-child(3)>div>div:nth-child(2)>span:nth-child(1)").html(email);
				$(".popupContainer>div:nth-child(2)").hide();
				$(".popupContainer>div:nth-child(3)").show();
			})
			.catch(function(error) {
				alert("에러! : " + error);
			});
		}else {
			Toastify({
			  text: "약관에 동의하지 않으면 가입할 수 없습니다.",
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
			return;		
		}
	});
	// 이메일로 가입하기-뒤로가기
	$(".popupContainer > div:nth-child(2) > div:nth-child(2) > div:nth-child(5)").click(function() {
		$(".popupContainer>div:nth-child(2)").hide();
		$(".popupContainer>div:nth-child(1)").show();
	});
	// 이메일로 가입하기-인증번호 받기-인증완료
	$(".popupContainer > div:nth-child(3) > div > div:nth-child(5)").click(function() {
		if(!$(this).parent().find(".checkBox").is(":checked")) {
			Toastify({
			  text: "약관에 동의하지 않으면 가입할 수 없습니다.",
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
			return;
		}
		let inputKey = $(".popupContainer >div:nth-child(3)>div>div:nth-child(4)").html();
		location.href="joinCode?inputKey=" + inputKey;
	});
});