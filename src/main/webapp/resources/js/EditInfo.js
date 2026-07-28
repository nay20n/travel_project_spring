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
	$("#basicInfo > div:nth-child(2) > input").click(function() {
		
		
	});
	// 프로필 사진 바뀌면 
	$("#basicInfo > div:nth-child(2) > input").change(function(){
		if(this.files && this.files[0]){
			let imgUrl = URL.createObjectURL(this.files[0]);
	        // img 태그 src 변경
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
	
	//이메일 변경 버튼  
	$("#basicInfo > div:nth-child(2) > div:nth-child(5) > button:nth-child(4)").click(function(){
		
		let inputEmail = $("#basicInfo > div:nth-child(2) > div:nth-child(5) > input").val();
		
		if(inputEmail===email){ // 기존 이메일과 동일할 때 
			$("#basicInfo > div:nth-child(2) > div:nth-child(5) > div:nth-child(3)").show();
			$("#basicInfo > div:nth-child(2) > div:nth-child(5) > input").addClass("borderWraning");
		} else { // 기존 이메일이랑 다를 때
			$("#basicInfo > div:nth-child(2) > div:nth-child(5) > div:nth-child(3)").hide();
			$("#basicInfo > div:nth-child(2) > div:nth-child(5) > input").removeClass("borderWraning");
			$(".popupContainer").show(); // 인증 메일 보내는 팝업 띄우기
		}
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
	// 팝업창 닫기
	$(".popupContent > svg:nth-child(1)").click(function() {
		$(".popupContainer").hide();
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

		if(confirm("저장하겠습니까?")){
			let inputNickName = $("#basicInfo > div:nth-child(2) > div:nth-child(4) > input").val();
			let inputEmail = $("#basicInfo > div:nth-child(2) > div:nth-child(5) > input ").val();
			
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
				//console.log(data);
			})
			.catch(function(error){
				alert("에러! : " + error);
			});
			
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