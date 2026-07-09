$(function() {
	// *************헤더****************
	// 메인로고 누르면 메인화면으로
	$("#header>div:nth-child(1)").click(function() {
		location.href="/TravelPlanner";
	});
	// 로그인 팝업
	$("#header> div:nth-child(2)>div:nth-child(1)>div").click(function() {
		$(this).parent().find(".hide").addClass("hide");
		$(".popupContainer").attr("style","display: block");
		$(".loginpop").attr("style","display: block");
	});
});