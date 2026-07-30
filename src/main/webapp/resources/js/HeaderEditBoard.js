let gCreateKeyLock = false;

$(function() {
	// *************헤더****************
	// 메인로고 누르면 메인화면으로
	$("#header>div:nth-child(1)").click(function() {
		location.href="/TravelPlanner";
	});
	
	// 공유
	$(document).on("click", "#shareBtn", function() {
		if(gCreateKeyLock) return; // 이미 생성 중이라면 막기
		
		gCreateKeyLock = true;
		let bno = $(this).data("bno");
		
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
			
			gCreateKeyLock = false;
		})
		.catch(function(error) {
			alert("에러! : 공유링크 생성에 문제가 발생했습니다. 다시 시도해주세요." + error);
		});
		// 연결
		if(webSocket==null)
			webSocket = new WebSocket("ws://localhost:9090/TravelPlanner/broadcasting?key="+bno);
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