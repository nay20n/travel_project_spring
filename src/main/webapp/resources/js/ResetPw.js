$(function (){
	/***************** 메인박스 ************/
	$("#main > button").click(function() {
		
		let email = $("#main").attr("data-email");
		let nickName = "사용자";
		let emailInput = $("#main > input").val();
		
		if(emailInput===email){ //이메일을 잘 입력했을 떄
			$("#main > div:nth-child(7)").removeClass("show");
			$("#main > input").removeClass("borderWraning");
			
			const jsonData = {
				"email" : emailInput,
				"nickName" : nickName,
				"pageType" : "resetPw", 
			};
			const initData = {
				method: "post",
				headers: { "Content-Type": "application/json" },
				body: JSON.stringify(jsonData)
			};
			
			fetch("sendEmail", initData)
			.then(function(response){
				return response.text();
			})
			.then(function(data){
				//console.log(data);(data);
				if(data==="empty"){
					Toastify({
					  text: "메일이 존재하지 않습니다.",
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
				} else if(data=="fail") {
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
				} else {
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
				}
			})
			.catch(function(error){
				alert("에러: " + error);
			});
			
			
			alert("비밀번호 재설정 메일을 보냈습니다.");
		}
		else{ //이메일을 잘못 입력 못햇을 떄 
			$("#main > div:nth-child(7)").addClass("show");
			$("#main > input").addClass("borderWraning");
		}
		
	});
});