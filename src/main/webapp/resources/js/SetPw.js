$(function(){
	// 저장 버튼 엔터 처리 
	$("#pwInput > div > input").keypress(function(e){
		if(e.keyCode && e.keyCode == 13){
			$("#main > div:last-child > button").click();
		}
	});
	
	/*******저장버튼**********/
	$("#main > div:last-child > button").click(function() {
		let pw = $("#pwInput > div:nth-child(1) > input").val();
		let pwConfirm = $("#pwInput > div:nth-child(2) > input").val() 
		let isAllValid = true; 
		//alert(pw);
		
		// 영문자, 숫자, 특수문자가 각각 최소 1개 이상 포함된 8~20자 정규식
		let pwRegex = /^(?=.*[A-Za-z])(?=.*\d)(?=.*[@$!%*#?&])[A-Za-z\d@$!%*#?&]{8,20}$/;

		//둘다 채워져 있을 때 
		if(pw!="" && pwConfirm!=""){
			
			if(!pwRegex.test(pw)){// 영문자 어쩌구가 아닐 경우 
				$("#pwInput > div:nth-child(1) > div:nth-child(3)").text("비밀번호는 영문자, 숫자, 특수문자 모두 포함한 8~20자여야 합니다.");
				$("#pwInput > div:nth-child(1) > div:nth-child(3)").addClass("pwWarning");
				$("#pwInput > div:nth-child(1) > input").addClass("borderWarning");
				isAllValid = false;
			} else { // 잘 입력했을 경우 
				$("#pwInput > div:nth-child(1) > div:nth-child(3)").text("비밀번호 (영문자, 숫자 특수문자 포함 최소 8~20자)");
				$("#pwInput > div:nth-child(1) > div:nth-child(3)").removeClass("pwWarning");
				$("#pwInput > div:nth-child(1) > input").removeClass("borderWraning");
			}
			
			if(pw != pwConfirm){ //두개가 다른 내용일 때 
				$("#pwInput > div:nth-child(2) > div:nth-child(3)").addClass("show");
				$("#pwInput > div:nth-child(2) > input").addClass("borderWraning");
				isAllValid = false;
			} else { //두개가 같을 때
				$("#pwInput > div:nth-child(2) > div:nth-child(3)").removeClass("show");
				$("#pwInput > div:nth-child(2) > input").removeClass("borderWraning");
			}
			
			// 잘 입력했을 때 비동기 처리
			if(isAllValid){
			
				fetch("/TravelPlanner/modifyPw?pw="+pw, {method: "post"})
				.then(function(response){
					return response.text();
				})
				.then(function(data){
					//console.log(data);
					Toastify({
					  text: "비밀번호가 변경되었습니다.",
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
					//alert("비밀번호가 변경되었습니다.");
				})
				.catch(function(error){
					alert("에러! : " + error);
				});
				
			}
			
		} else { // 둘중 하나라도 비워져 있을 때 
			Toastify({
			  text: "정보를 입력하세요!",
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
			//alert("정보를 입력하세요!");
		}
	});
});