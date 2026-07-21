let gCreateKeyLock = false;

$(function() {
	// *************헤더****************
	// 메인로고 누르면 메인화면으로
	$("#header>div:nth-child(1)").click(function() {
		location.href="/TravelPlanner";
	});
	
	// 공유
	$(document).on("click", "#header> div:nth-child(2)>div:nth-child(1)>div", function() {
		if(gCreateKeyLock) return; // 이미 생성 중이라면 막기
		
		gCreateKeyLock = true;
		let bno = $(this).data("bno");
		
		const originalFee = String($(this).data('original-value'));
    	const currentFee = $(this).val();
    	const field = $(this).attr('data-field');
    	
		// 제목에 변경 사항이 없다면 종료
		if (originalFee === currentFee) return;
		
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
		fetch("/TravelPlanner/createShareKey", initData)
		.then(function(response) {
			return response.text();
		})
		.then(function(data) {
			var dummy = document.createElement("input");
		    var text = $(location).attr('href') + "?key=" + data;
		    
		    document.body.appendChild(dummy);
		    dummy.value = text;
		    dummy.select();
		    document.execCommand("copy");
		    document.body.removeChild(dummy);
		    
			gCreateKeyLock = false;
			Toastify({
			  text: "공유 링크가 복사되었습니다!",
			  duration: 3000,
			  newWindow: true,
			  close: true,
			  gravity: "top",
			  position: "center",
			  stopOnFocus: true, // Prevents dismissing of toast on hover
			  style: {
			    background: "linear-gradient(to left, #E3D4FF, #925DE8)",
			  }
			}).showToast();
		})
		.catch(function(error) {
			alert("에러! : 제목 저장에 문제가 발생했습니다. 다시 시도해주세요." + error);
		});
	});
	// 로그아웃
	$("#header> div:nth-child(2)>div:nth-child(2)>div").click(function() {
		location.href="/TravelPlanner/logout";
	});
	// 마이페이지 이동
	$("#profileImg").click(function() {
		location.href="/TravelPlanner/mypage";
	});
});