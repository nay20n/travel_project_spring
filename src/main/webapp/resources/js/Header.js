$(function() {
	// *************헤더****************
	// 메인로고 누르면 메인화면으로
	$("#header>div:nth-child(1)").click(function() {
		location.href="/TravelPlanner";
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