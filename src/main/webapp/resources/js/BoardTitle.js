$(function() {
	// 포커스 발생 시 현재 제목 값 저장
	$('.boardTitle').on('focus', function () {
	    $(this).data('original-value', $(this).val());
	});
	// 엔터키로도 저장 작동
	$('.boardTitle').on('keydown', function (e) {
	    if(e.keyCode == 13){
	        $(this).blur();
	    }
	});
	// 제목 저장 비동기
	$('.boardTitle').on('blur', function () {
		const originalTitle = String($(this).data('original-value'));
    	const currentTitle = $(this).val();
    	const bno = $(this).attr('data-bno');
    	
		// 제목에 변경 사항이 없다면 종료
		if (originalTitle === currentTitle) return;
		
		const jsonData = {
			title : currentTitle,
			bno : bno
		};
		const initData = {
			method: "post",
			headers: {
				"Content-Type": "application/json"
			},
			body: JSON.stringify(jsonData)
		};
		fetch("/TravelPlanner/updateBoardTitle", initData)
		.then(function(response) {
			return response.text();
		})
		.then(function(data) {
			console.log(data);
		})
		.catch(function(error) {
			alert("에러! : 제목 저장에 문제가 발생했습니다. 다시 시도해주세요." + error);
		});
	});
});