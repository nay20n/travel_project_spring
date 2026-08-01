let mailVerify = false;

// 이메일 유효성 체크
const pattern = /^[A-Za-z0-9_\.\-]+@[A-Za-z0-9\-]+\.[A-za-z0-9\-]+/;
function emailValidChk(email) {
    if(pattern.test(email) === false) { return false; }
    else { return true; }
}

$(function() {
	let rttrMsg = $("#serverMsg").val();
	if(rttrMsg)
		alert(rttrMsg);

	let email = $("#basicInfo > div:nth-child(2) > div:nth-child(5) > input ").val();
	let nickName = $("#basicInfo > div:nth-child(2) > div:nth-child(4) > input").val();
	/************** 기본정보 ************/
	// 프로필 연필 수정 버튼 
	$("#basicInfo > div:nth-child(2) > svg").click(function() {
		$("#basicInfo > div:nth-child(2) > input").click(); // 파일 업로드 input 태그 클릭
		//alert("프로필 사진 수정");
	});
	// 프로필 사진 바뀌면 
	$("#basicInfo > div:nth-child(2) > input").change(function(){
		if(this.files && this.files[0]){
			let imgUrl = URL.createObjectURL(this.files[0]);
	        // img 태그 src 변경(사진 미리보기)
	        $("#basicInfo > div:nth-child(2) > img").attr("src", imgUrl);
	        
	        let files = this.files[0];
	        let formData = new FormData();
			formData.append("file", files);
			
			$.ajax({
				url: '/TravelPlanner/updateprofile',
				processData : false,
				contentType : false,
				data : formData,
				type : 'POST',
				success : function(result){
					console.log(result);
					Toastify({
					  text: "프로필 이미지가 업데이트 되었습니다.",
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
					//alert(result);
				},
	            error: function(r,s,e){
	                alert("업로드 실패!");
	                console.log(e);
	            }
			});
		}
	});
	
	//이메일 변경 버튼 (팝업열기)
	$("#basicInfo > div:nth-child(2) > div:nth-child(5) > button:nth-child(4)").click(function(){
		
		let inputEmail = $("#basicInfo > div:nth-child(2) > div:nth-child(5) > input").val();
		let nickName = $("#basicInfo > div:nth-child(2) > div > input ").val();
		$(".popupContent > div:nth-child(2) > div:nth-child(2) > span:nth-child(1)").text(inputEmail);
		
		if(inputEmail===email){ // 기존 이메일과 동일할 때 
			$("#basicInfo > div:nth-child(2) > div:nth-child(5) > div:nth-child(3)").text("기존 이메일과 동일합니다. 새 이메일 주소를 입력하세요.");
			$("#basicInfo > div:nth-child(2) > div:nth-child(5) > div:nth-child(3)").show();
			$("#basicInfo > div:nth-child(2) > div:nth-child(5) > input").addClass("borderWraning");
		} else if(!emailValidChk(inputEmail)) { // 이메일 유효성 체크
			$("#basicInfo > div:nth-child(2) > div:nth-child(5) > div:nth-child(3)").text("올바른 이메일을 입력하세요.");
			$("#basicInfo > div:nth-child(2) > div:nth-child(5) > div:nth-child(3)").show();
			$("#basicInfo > div:nth-child(2) > div:nth-child(5) > input").addClass("borderWraning");
		} else { // 기존 이메일이랑 다를 때
			$("#basicInfo > div:nth-child(2) > div:nth-child(5) > div:nth-child(3)").hide();
			$("#basicInfo > div:nth-child(2) > div:nth-child(5) > input").removeClass("borderWraning");
			
			//인증번호 메일 전송
			const jsonData = {
				"email" : inputEmail,
				"nickName" : nickName,
				"pageType" : "editInfo",
			};
			const initData = {
				method: "post",
				headers: { "Content-Type": "application/json" },
				body: JSON.stringify(jsonData)
			};
			
			fetch("/TravelPlanner/sendEmail", initData)
			.then(function(response){
				return response.text();
			})
			.then(function(data){
				//console.log(data);
				if(data === "exist") { // 이미 가입한 이메일이라면
					$("#basicInfo > div:nth-child(2) > div:nth-child(5) > div:nth-child(3)").text("이미 가입이 된 이메일 입니다. 다른 이메일을 입력하세요.");
					$("#basicInfo > div:nth-child(2) > div:nth-child(5) > div:nth-child(3)").show();
					$("#basicInfo > div:nth-child(2) > div:nth-child(5) > input").addClass("borderWraning");
				} else if(data == "fail") { // 전송을 실패했을 경우
					Toastify({
					  text: "메일 전송을 실패했습니다.",
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
				} else { //이메일을 전송했을 경우
					Toastify({
					  text: "메일이 전송되었습니다.",
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
					$(".popupContainer").show(); // 인증 메일 보내는 팝업 띄우기
					$(".popupContent > div:nth-child(2) > div:nth-child(4)").focus();
				}
				
			})
			.catch(function(error){
				alert("에러: " + error);
			});
		}
	});
	// 팝업창 닫기
	$(".popupContent > svg:nth-child(1)").click(function() {
		$(".popupContainer").hide();
	});
	// 팝업창 인증완료 버튼 
	$(".popupContent > div:nth-child(2) > div:nth-child(5)").click(function() {
		let inputKey = $(".popupContent > div:nth-child(2) > div:nth-child(4)").text();
		
		fetch("/TravelPlanner/checkAuthCode?key=" + inputKey, {method:'post'})
		.then(function(response){
			return response.json();
		})
		.then(function(data){
			console.log("인증",data);
			
			if(data){ // 인증번호가 맞다면
				mailVerify = true;
				Toastify({
					  text: "인증메일이 완료되었습니다.",
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
				$(".popupContent > div:nth-child(2) > div:nth-child(4)").removeClass("borderWraning");
				$(".popupContainer").hide();
				$("#basicInfo > div:nth-child(2) > div:nth-child(5) > div:nth-child(3)").text("이메일이 인증되었습니다.");
				$("#basicInfo > div:nth-child(2) > div:nth-child(5) > div:nth-child(3)").show();
				
			} else { // 인증번호가 일치하지 않으면
				Toastify({
					  text: "인증번호가 불일치 합니다.",
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
				$(".popupContent > div:nth-child(2) > div:nth-child(4)").addClass("borderWraning");
			}
			
		})
		.catch(function(error){
			alert("에러: " + error);
		});
		
		//alert(inputKey);
	});
	//팝업창 인증번호 재전송 버튼
	$(".popupContent > div:nth-child(2) > div:nth-child(6)").click(function() {
		let inputEmail = $("#basicInfo > div:nth-child(2) > div:nth-child(5) > input").val();
		let nickName = $("#basicInfo > div:nth-child(2) > div > input ").val();
		
		const jsonData = {
				"email" : inputEmail,
				"nickName" : nickName,
				"pageType" : "editInfo",
		};
		const initData = {
			method: "post",
			headers: { "Content-Type": "application/json" },
			body: JSON.stringify(jsonData)
		};
		
		fetch("/TravelPlanner/sendEmail", initData)
		.then(function(response){
			return response.text();
		})
		.then(function(data){
			//console.log(data);
			Toastify({
					  text: "인증메일이 재전송 되었습니다.",
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
			alert("에러: " + error);
		});
	});
	
	//비밀번호 변경 버튼 클릭 
	$("#basicInfo > div:nth-child(2) > div:nth-child(5) > button:nth-child(5)").click(function(){
		
		fetch("/TravelPlanner/createResetKey?email="+email, {method:"POST"})
		.then(function(response){
			return response.text();
		})
		.then(function(data){
			console.log("받은 키",data);
			location.href = "/TravelPlanner/setpw?key=" + encodeURIComponent(data);
		})
		.catch(function(error){
			alert("에러! : " + error);
		});
	});
	//취소 버튼 
	$("#basicInfo > div:nth-child(2) > div:nth-child(6) > button:nth-child(1)").click(function(){
		if(confirm("취소하겠습니까?")) {
			$("#basicInfo > div:nth-child(2) > div:nth-child(5) > input ").val(email);
			let inputNickName = $("#basicInfo > div:nth-child(2) > div:nth-child(4) > input").val(nickName);
		}
	});
	//저장버튼
	$("#basicInfo > div:nth-child(2) > div:nth-child(6) > button:nth-child(2)").click(function(){

		let inputNickName = $("#basicInfo > div:nth-child(2) > div:nth-child(4) > input").val();
		let inputEmail = $("#basicInfo > div:nth-child(2) > div:nth-child(5) > input ").val();
		
		if(email == inputEmail || mailVerify){ //원래의 이메일과 적은 이메일이 같거나, 인증이 되었다면
			if(confirm("저장하겠습니까?")){
				
				//메일 , 이름 저장
				const jsonData = {
					"nickName" : inputNickName,
					"email" : inputEmail,
				};
				const initData = {
					method: "post",
					headers: {
						"Content-Type": "application/json"
					},
					body: JSON.stringify(jsonData)
				};
				fetch("/TravelPlanner/updateInfo", initData)
				.then(function(response){
					return response.json();
				})
				.then(function(data){
					//console.log(data);
					Toastify({
						text: "저장되었습니다.",
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
					$("#basicInfo > div:nth-child(2) > div:nth-child(5) > div:nth-child(3)").hide();
				})
				.catch(function(error){
					alert("에러! : " + error);
				});
			} else {  //저장 안하겟다고 하면 
				Toastify({
						text: "저장이 취소 되었습니다.",
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
			}
			
		}  else { // 원래의 이메일과 적은 이메일이 같지 않고, 인증도 안되얶ㅆ다면 
				$("#basicInfo > div:nth-child(2) > div:nth-child(5) > div:nth-child(3)").text("이메일을 인증하세요!");
				$("#basicInfo > div:nth-child(2) > div:nth-child(5) > div:nth-child(3)").show();
				$("#basicInfo > div:nth-child(2) > div:nth-child(5) > input").addClass("borderWraning");
			}	
		//let profileImg = $("#basicInfo > div:nth-child(2) > img")
	});
	/***********계정연동*************/
	//연결해제 버튼 
	$("#connect > div:nth-child(2) > div > button").click(function() {
		
		if($(this).hasClass("connect")){ // 연결하기를 가지고 있는 클래스(즉, 연결해제 버튼)
			$(this).removeClass("connect");
			$(this).text("연결하기");
			$(this).parent().find("span").eq(1).remove();
		} else { //연결하기 버튼)
			$(this).addClass("connect");
			$(this).text("연결해제");
			$(this).after("<span>연결됨<span>");
		}
	});
});