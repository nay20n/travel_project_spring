$(function() {
	// *************헤더****************
	// 메인로고 누르면 메인화면으로
	$("#header>div:nth-child(1)").click(function() {
		location.href="/TravelPlanner";
	});
	// 로그아웃 알림
	$("#header> div:nth-child(2)>div:nth-child(2)>div").click(function() {
		alert("로그아웃 되었습니다!");
	});
	// 마이페이지 이동
	$("#header> div:nth-child(2)>div:nth-child(3)>div").click(function() {
		location.href="/TravelPlanner/mypage";
	});
});