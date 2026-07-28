$(function (){
	/***************** 메인박스 ************/
	$("#main > button").click(function() {
		
		let email = $("#main").attr("data-email");
		let nickName = $("#main").attr("data-nick-name");
		let emailInput = $("#main > input").val();
		
		if(emailInput===email){  //이메일을 잘 입력했을 떄
			$("#main > div:nth-child(7)").removeClass("show");
			$("#main > input").removeClass("borderWraning");
			
			fetch("getEmail?email=" + emailInput + "&nickName=" + nickName, {method:"post"})
			.then(function(response){
				return response.text();
			})
			.then(function(data){
				console.log(data);
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