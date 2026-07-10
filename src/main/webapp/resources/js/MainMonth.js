$(function() {
	// **************캘린더********************
	// 일 단위로 이동
	$(".changeView > span:nth-child(1)").click(function() {
		location.href="day";
	});
	// 주 단위로 이동
	$(".changeView > span:nth-child(2)").click(function() {
		location.href="week";
	});
	// 일정확정하기
	$("#main>button").click(function() {
		let currentUrl = window.location.href;
		const url = new URL("./",currentUrl);
		let endIdx = url.pathname.length-1;
		
		location.href = url.pathname.substr(0, endIdx);
	});
});